package com.tsystems.dco.tm.integration.tminterface.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tsystems.dco.release.model.CreateQualityGateWithApprovers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkFlowRequest {
  private String workflowType;
  private String taskQueue;
  private String namespace;
  private CreateQualityGateWithApprovers payload;
}
