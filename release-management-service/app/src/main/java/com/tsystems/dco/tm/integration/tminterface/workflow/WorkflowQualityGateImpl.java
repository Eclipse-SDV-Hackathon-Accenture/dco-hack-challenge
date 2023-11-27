package com.tsystems.dco.tm.integration.tminterface.workflow;

import com.tsystems.dco.tm.integration.tminterface.domain.QualityGateRequest;
import com.tsystems.dco.tm.integration.tminterface.taskacitivity.ActivitiesForQualityGate;
import com.tsystems.tm.sdk.activity.ActivityOptions;
import com.tsystems.tm.sdk.common.RetryOptions;
import com.tsystems.tm.sdk.workflow.Workflow;
import com.tsystems.tm.sdk.workflow.WorkflowInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j

public class WorkflowQualityGateImpl implements WorkflowQualityGate {

  private final ActivitiesForQualityGate activities = Workflow.newActivityStub(
    ActivitiesForQualityGate.class,
    ActivityOptions.newBuilder()
      //set to when you want to time out
      .setStartToCloseTimeout(Duration.ofMinutes(4320))
      .setRetryOptions(RetryOptions.newBuilder()
        .setMaximumAttempts(0)
        .setInitialInterval(Duration.ofSeconds(5))
        .setBackoffCoefficient(1)
        .build())
      .build());
  public boolean isQualityGateApproved = false;  //change QualityGateRequest accordingly
  private String releaseIdFromTopic;

  @Override
  public String execute(QualityGateRequest request) {
    WorkflowInfo workflowInfo = Workflow.getInfo();
    log.info("Execute workflow {} of namespace {} with id {}",
      workflowInfo.getWorkflowType(), workflowInfo.getNamespace(), workflowInfo.getWorkflowId());
    for (int i = 0; i < request.getQualityGates().size(); i++) {
      //get the approval n do other activities
      activities.qualityGates(request.getQualityGates().get(i), request.getApprovers().get(i));
      //notify approver with mail
      activities.notifyApprover(request.getApprovers().get(i), request.getMetaTrack(), request.getTrack());
      //wait for approval
      this.releaseIdFromTopic = request.getMetaTrack();
      Workflow.await(() -> isQualityGateApproved);
    }
    return "All " + request.getQualityGates().size() + " quality gates approved";
  }

  @Override
  public void updateApproval(String FromTopic) {

    activities.waitForApprover(FromTopic);
    if (FromTopic.equals(this.releaseIdFromTopic)) {
      isQualityGateApproved = true;
    }
  }
  @Override
  public String getWorkflowId() {
    return Workflow.getInfo().getWorkflowId();
  }
}

