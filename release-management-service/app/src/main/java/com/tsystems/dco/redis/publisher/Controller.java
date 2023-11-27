package com.tsystems.dco.redis.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsystems.dco.redis.entity.RedisTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Controller {

  private final Publisher publisher;

  @PostMapping("/publisher")
  public void publish(@RequestBody RedisTransport redisTransport) throws JsonProcessingException {
    log.info(">> Publishing : {}" + redisTransport);
    publisher.publisher(redisTransport);
  }
}
