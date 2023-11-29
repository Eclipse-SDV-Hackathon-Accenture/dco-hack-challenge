/*
 *   ========================================================================
 *  SDV Developer Console
 *
 *   Copyright (C) 2022 - 2023 T-Systems International GmbH
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   SPDX-License-Identifier: Apache-2.0
 *
 *   ========================================================================
 */

package com.tsystems.dco.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.exception.BaseException;
import graphql.schema.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.ExecutionGraphQlRequest;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.Assert;
import org.springframework.util.IdGenerator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Configuration
public class GraphQLConfiguration {

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurerUpload() {

    GraphQLScalarType uploadScalar = GraphQLScalarType.newScalar()
      .name("Upload")
      .coercing(new UploadCoercing())
      .build();

    return wiringBuilder -> wiringBuilder.scalar(uploadScalar);
  }

  @Bean
  @Order(1)
  public RouterFunction<ServerResponse> graphQlMultipartRouterFunction(
    GraphQlProperties properties,
    WebGraphQlHandler webGraphQlHandler,
    ObjectMapper objectMapper
  ) {
    String path = properties.getPath();
    var builder = RouterFunctions.route();
    var graphqlMultipartHandler = new GraphqlMultipartHandler(webGraphQlHandler, objectMapper);
    builder = builder.POST(path, RequestPredicates.contentType(MULTIPART_FORM_DATA)
      .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)), graphqlMultipartHandler::handleRequest);
    return builder.build();
  }
}

class UploadCoercing implements Coercing<MultipartFile, MultipartFile> {

  @Override
  public MultipartFile serialize(Object dataFetcherResult) throws CoercingSerializeException {
    throw new CoercingSerializeException("Upload is an input-only type");
  }

  @Override
  public MultipartFile parseValue(Object input) throws CoercingParseValueException {
    if (input instanceof MultipartFile) {
      return (MultipartFile) input;
    }
    throw new CoercingParseValueException(
      String.format("Expected a 'MultipartFile' like object but was '%s'.", input != null ? input.getClass() : null)
    );
  }

  @Override
  public MultipartFile parseLiteral(Object input) throws CoercingParseLiteralException {
    throw new CoercingParseLiteralException("Parsing literal of 'MultipartFile' is not supported");
  }
}

class GraphqlMultipartHandler {

  private final WebGraphQlHandler graphQlHandler;

  private final ObjectMapper objectMapper;

  public GraphqlMultipartHandler(WebGraphQlHandler graphQlHandler, ObjectMapper objectMapper) {
    Assert.notNull(graphQlHandler, "WebGraphQlHandler is required");
    Assert.notNull(objectMapper, "ObjectMapper is required");
    this.graphQlHandler = graphQlHandler;
    this.objectMapper = objectMapper;
  }

  protected static final List<MediaType> SUPPORTED_RESPONSE_MEDIA_TYPES =
    Arrays.asList(MediaType.APPLICATION_GRAPHQL, MediaType.APPLICATION_JSON);

  private static final Log logger = LogFactory.getLog(GraphqlMultipartHandler.class);
  private static final String VARIABLES = "variables";

  private final IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  public ServerResponse handleRequest(ServerRequest serverRequest) {
    Optional<String> operation = serverRequest.param("operations");
    Optional<String> mapParam = serverRequest.param("map");
    Map<String, Object> inputQuery = readJson(operation, new TypeReference<>() {
    });
    final Map<String, Object> queryVariables;
    if (inputQuery.containsKey(VARIABLES)) {
      queryVariables = (Map<String, Object>) inputQuery.get(VARIABLES);
    } else {
      queryVariables = new HashMap<>();
    }

    Map<String, MultipartFile> fileParams = readMultipartBody(serverRequest);
    Map<String, List<String>> fileMapInput = readJson(mapParam, new TypeReference<>() {
    });
    fileMapInput.forEach((String fileKey, List<String> objectPaths) -> {
      MultipartFile file = fileParams.get(fileKey);
      if (file != null) {
        objectPaths.forEach((String objectPath) ->
          MultipartVariableMapper.mapVariable(
            objectPath,
            queryVariables,
            file
          )
        );
      }
    });

    String query = (String) inputQuery.get("query");
    String opName = (String) inputQuery.get("operationName");

    WebGraphQlRequest graphQlRequest = new MultipartGraphQlRequest(
      query,
      opName,
      queryVariables,
      serverRequest.uri(), serverRequest.headers().asHttpHeaders(),
      this.idGenerator.generateId().toString(), LocaleContextHolder.getLocale());

    if (logger.isDebugEnabled()) {
      logger.debug("Executing: " + graphQlRequest);
    }

    Mono<ServerResponse> responseMono = this.graphQlHandler.handleRequest(graphQlRequest)
      .map(response -> {
        if (logger.isDebugEnabled()) {
          logger.debug("Execution complete");
        }
        ServerResponse.BodyBuilder builder = ServerResponse.ok();
        builder.headers(headers -> headers.putAll(response.getResponseHeaders()));
        builder.contentType(selectResponseMediaType(serverRequest));
        return builder.body(response.toMap());
      });

    return ServerResponse.async(responseMono);
  }

  private <T> T readJson(Optional<String> string, TypeReference<T> t) {
    Map<String, Object> map = new HashMap<>();
    if (string.isPresent()) {
      try {
        return objectMapper.readValue(string.get(), t);
      } catch (JsonProcessingException e) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
      }
    }
    return (T) map;
  }

  private static Map<String, MultipartFile> readMultipartBody(ServerRequest request) {
    try {
      var abstractMultipartHttpServletRequest = (AbstractMultipartHttpServletRequest) request.servletRequest();
      return abstractMultipartHttpServletRequest.getFileMap();
    } catch (RuntimeException ex) {
      throw new ServerWebInputException("Error while reading request parts", null, ex);
    }
  }

  private static MediaType selectResponseMediaType(ServerRequest serverRequest) {
    for (MediaType accepted : serverRequest.headers().accept()) {
      if (SUPPORTED_RESPONSE_MEDIA_TYPES.contains(accepted)) {
        return accepted;
      }
    }
    return MediaType.APPLICATION_JSON;
  }

}

class MultipartVariableMapper {

  private static final Pattern PERIOD = Pattern.compile("\\.");
  private static final String VARIABLES = "variables";

  private static final Mapper<Map<String, Object>> MAP_MAPPER =
    new Mapper<Map<String, Object>>() {
      @Override
      public Object set(Map<String, Object> location, String target, MultipartFile value) {
        return location.put(target, value);
      }

      @Override
      public Object recurse(Map<String, Object> location, String target) {
        return location.get(target);
      }
    };
  private static final Mapper<List<Object>> LIST_MAPPER =
    new Mapper<List<Object>>() {
      @Override
      public Object set(List<Object> location, String target, MultipartFile value) {
        return location.set(Integer.parseInt(target), value);
      }

      @Override
      public Object recurse(List<Object> location, String target) {
        return location.get(Integer.parseInt(target));
      }
    };

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static void mapVariable(String objectPath, Map<String, Object> variables, MultipartFile part) {
    String[] segments = PERIOD.split(objectPath);

    if (segments.length < 2) {
      throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "object-path in map must have at least two segments");
    } else if (!VARIABLES.equals(segments[0])) {
      throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "can only map into variables");
    }

    Object currentLocation = variables;
    for (var i = 1; i < segments.length; i++) {
      String segmentName = segments[i];
      Mapper mapper = determineMapper(currentLocation, objectPath, segmentName);

      if (i == segments.length - 1) {
        if (null != mapper.set(currentLocation, segmentName, part)) {
          throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "expected null value when mapping " + objectPath);
        }
      } else {
        currentLocation = mapper.recurse(currentLocation, segmentName);
        if (null == currentLocation) {
          throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "found null intermediate value when trying to map " + objectPath);
        }
      }
    }
  }

  private static Mapper<?> determineMapper(
    Object currentLocation, String objectPath, String segmentName) {
    if (currentLocation instanceof Map) {
      return MAP_MAPPER;
    } else if (currentLocation instanceof List) {
      return LIST_MAPPER;
    }

    throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "expected a map or list at " + segmentName + " when trying to map " + objectPath);
  }

  interface Mapper<T> {

    Object set(T location, String target, MultipartFile value);

    Object recurse(T location, String target);
  }
}

class MultipartGraphQlRequest extends WebGraphQlRequest implements ExecutionGraphQlRequest {

  private final String document;
  private final String operationName;
  private final Map<String, Object> variables;


  public MultipartGraphQlRequest(
    String query,
    String operationName,
    Map<String, Object> variables,
    URI uri, HttpHeaders headers,
    String id, @Nullable Locale locale) {

    super(uri, headers, fakeBody(query), id, locale);

    this.document = query;
    this.operationName = operationName;
    this.variables = variables;
  }

  private static Map<String, Object> fakeBody(String query) {
    Map<String, Object> fakeBody = new HashMap<>();
    fakeBody.put("query", query);
    return fakeBody;
  }

  @Override
  public String getDocument() {
    return document;
  }

  @Override
  public String getOperationName() {
    return operationName;
  }

  @Override
  public Map<String, Object> getVariables() {
    return variables;
  }
  
}
