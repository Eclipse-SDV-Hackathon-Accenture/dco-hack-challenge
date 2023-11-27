package com.tsystems.dco.tm.integration.tminterface.taskacitivity;

import com.tsystems.dco.util.EmailService;
import com.tsystems.tm.sdk.activity.Activity;
import com.tsystems.tm.sdk.activity.ActivityExecutionContext;
import com.tsystems.tm.sdk.activity.ActivityInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * implements all acitivites related to Quality gate
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class ActivitiesForQualityGateImpl implements ActivitiesForQualityGate {
  @Autowired
  private EmailService emailService;

  /**
   * @param qualityGate
   * @param approver
   * @return
   */
  @SneakyThrows
  @Override
  public String qualityGates(int qualityGate, String approver) {
    ActivityExecutionContext ctx = Activity.getExecutionContext();
    ActivityInfo info = ctx.getInfo();
    log.info("Execute activity {} of namespace {} in workflow with id {}",
      info.getActivityType(), info.getActivityNamespace(), info.getWorkflowId());
    log.info("quality gate " + qualityGate + " is approved by " + approver);
    return "quality gate " + qualityGate + " is approved by " + approver;
  }

  /**
   * @param approver
   * @return
   */
  @Override
  public String notifyApprover(String approver, String metaTrack, String track) {
    emailService.sendMail(approver, metaTrack, track);
    log.info("Email Sent Successfully");
    return "Email Sent Successfully";
  }

  /**
   * @param releaseId
   * @return
   */
  @Override
  public String waitForApprover(String releaseId) {
    System.out.println("releaseId is : " + releaseId + "release gate is approved");
    return "finished";
  }

}
