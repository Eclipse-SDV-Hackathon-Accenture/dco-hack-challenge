package com.tsystems.dco.tm.integration.tminterface.client;

import com.tsystems.dco.tm.integration.tminterface.domain.WorkFlowRequest;
import com.tsystems.dco.tm.integration.workflowregistry.client.TaskManagerWorkflowRegistryFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  name = "TaskManagerService",
  url = "${task-management-api.url}",
  configuration = TaskManagerWorkflowRegistryFeignClientConfig.class)

public interface TaskManagerServiceApiClient {
  @PostMapping("/api/v1/workflow/start")
  ResponseEntity<String> createWorkflow(@RequestBody WorkFlowRequest request);
}
