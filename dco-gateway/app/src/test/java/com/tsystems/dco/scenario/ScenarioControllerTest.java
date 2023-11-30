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

package com.tsystems.dco.scenario;

import com.tsystems.dco.scenario.model.ScenarioPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
class ScenarioControllerTest {

  @Autowired
  private GraphQlTester tester;

  @MockBean
  private ScenarioRestClient client;

  @Test
  void scenarioReadByQuery() {
    var tracks = """
      query GET_SCENARIO {
              scenarioReadByQuery(search: null, query: null, page: 0, size: 2, sort: null){
                content {
                  id
                  name
                  file {
                    id
                  }
                }
                total
              }
            }
      """;
    when(client.scenarioReadByQuery(any(), any(), anyInt(), anyInt(), any())).thenReturn(new ScenarioPage());
    tester.document(tracks).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void deleteScenarioById() {
    var tracks = """
      mutation DELETE_SCENARIO {
        deleteScenarioById(id: "9711f69b-8210-42d0-a6e6-5397d4bd5d99")
      }
      """;
    tester.document(tracks).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void searchScenarioByPattern() {
    var tracks = """
      query SEARCH_SCENARIO {
      	searchScenarioByPattern(scenarioPattern: "Scenario", page:0, size:11){
          content{
            id
            name
            file {
              id
            }
          }
          total
        }
      }
      """;
    when(client.searchScenarioByPattern(any(), anyInt(), anyInt())).thenReturn(new ScenarioPage());
    tester.document(tracks).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }
}
