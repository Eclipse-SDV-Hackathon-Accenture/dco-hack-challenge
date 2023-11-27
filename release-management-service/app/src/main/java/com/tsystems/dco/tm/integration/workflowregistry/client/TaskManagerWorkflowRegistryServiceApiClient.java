package com.tsystems.dco.tm.integration.workflowregistry.client;

import com.tsystems.dco.release.integration.track.client.TrackFeignClientConfig;
import com.tsystems.dco.tm.integration.workflowregistry.domain.WorkflowDefinitionRequest;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(
  name = "TaskManagerWorkflowRegistryFeignClientConfig",
  url = "${workflow-registry.url}",
  configuration = TrackFeignClientConfig.class)

public interface TaskManagerWorkflowRegistryServiceApiClient {
  @PostMapping("/api/v1/workflow-definition")
  ResponseEntity<String> createWorkflowRegistry(@RequestBody WorkflowDefinitionRequest workflowDefinition);

  @GetMapping("/api/v1/workflow-definition/DCO/{id}")
  ResponseEntity<WorkflowDefinitionRequest> getWorkflowDefinitionById(@PathVariable("id") String id);

  @GetMapping("/api/v1/workflow-definition")
  ResponseEntity<List<WorkflowDefinitionRequest>> getAllWorkflowDefinition();

}

