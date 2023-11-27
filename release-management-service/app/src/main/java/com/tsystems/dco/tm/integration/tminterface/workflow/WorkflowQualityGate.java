package com.tsystems.dco.tm.integration.tminterface.workflow;

import com.tsystems.dco.tm.integration.tminterface.domain.QualityGateRequest;
import com.tsystems.tm.sdk.workflow.QueryMethod;
import com.tsystems.tm.sdk.workflow.SignalMethod;
import com.tsystems.tm.sdk.workflow.WorkflowInterface;
import com.tsystems.tm.sdk.workflow.WorkflowMethod;


@WorkflowInterface
public interface WorkflowQualityGate {

  @WorkflowMethod(name = "quality-gate-execution")
  String execute(QualityGateRequest request);

  @SignalMethod
  void updateApproval(String releaseIdFromTopic);
  @QueryMethod
  String getWorkflowId();
}
