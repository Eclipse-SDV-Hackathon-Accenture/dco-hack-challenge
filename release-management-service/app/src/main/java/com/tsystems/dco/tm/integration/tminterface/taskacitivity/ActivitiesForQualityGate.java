package com.tsystems.dco.tm.integration.tminterface.taskacitivity;

import com.tsystems.tm.sdk.activity.ActivityInterface;
import com.tsystems.tm.sdk.activity.ActivityMethod;

/**
 * Activities For QualityGate
 */
@ActivityInterface

public interface ActivitiesForQualityGate {
  /* add more activity methods in case needed*/
  @ActivityMethod(name = "release-management-service.qualityGates")
  String qualityGates(int qualityGate, String approver);

  @ActivityMethod(name = "release-management-service.notifyApprover")
  String notifyApprover(String approver, String metaTrack, String track);

  @ActivityMethod(name = "release-management-service.waitForApprover")
  String waitForApprover(String releaseId);

}
