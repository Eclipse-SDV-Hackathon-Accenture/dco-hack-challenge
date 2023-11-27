package com.tsystems.dco.release.controller;

import com.tsystems.dco.release.api.ReleaseApi;
import com.tsystems.dco.release.model.*;
import com.tsystems.dco.release.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReleaseController implements ReleaseApi {

  private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);
  private final ReleaseService releaseService;

  @Override
  public ResponseEntity<String> createQualityGateWithApprovers(CreateQualityGateWithApprovers createQualityGateWithApprovers) {
    logger.info("Create gate with approvers");
    return ResponseEntity
      .status(HttpStatus.OK).body(releaseService.createQualityGateWithApprovers(createQualityGateWithApprovers));
  }

  @Override
  public ResponseEntity<Release> createRelease(CreateReleaseRequest createReleaseRequest) {
    logger.info("creating release");
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(releaseService.createRelease(createReleaseRequest));
  }

  @Override
  public ResponseEntity<String> createWorkflowDefinition(WorkflowDefinition workflowDefinition) {
    logger.info("create dummy Workflow Definition");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.createWorkflowDefinition(workflowDefinition));
  }

  @Override
  public ResponseEntity<List<DefinitionResponse>> getAllWorkflowDefinition() {
    logger.info("Get all defined Workflow Definition");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getAllWorkflowDefinition());
  }


  @Override
  public ResponseEntity<BrandModelCountry> getBrandModelCountryList() {
    logger.info("fetching Brand Model Country Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getBrandModelCountryList());
  }

  @Override
  public ResponseEntity<List<FunctionDataSet>> getFunctionDataSet() {
    logger.info("fetching function data Set Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getFunctionDataSet());
  }

  @Override
  public ResponseEntity<List<String>> getMetaTrack() {
    logger.info("fetching Meta track Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getMetaTrack());
  }

  @Override
  public ResponseEntity<ReleaseApprovers> getQualitygateAndApproversStatus(String id) {
    logger.info("fetching Quality gate And Approvers Status Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getQualitygateAndApproversStatus(id));
  }

  @Override
  public ResponseEntity<Release> getReleaseById(String id) {
    logger.info("fetching release by id Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getReleaseById(id));
  }

  @Override
  public ResponseEntity<Release> deleteReleaseById(String id) {
    logger.info("deleting release by id Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.deleteReleaseById(id));
  }

  /**
   * @param id The Workflow Definition id (required)
   * @return
   */
  @Override
  public ResponseEntity<DefinitionResponse> getWorkflowDefinitionById(String id) {
    logger.info("Get defined Workflow Definition");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getWorkflowDefinitionById(id));
  }

  @Override
  public ResponseEntity<List<Release>> listTrackRelease() {
    logger.info("fetching release Track list Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.listTrackRelease());
  }

  @Override
  public ResponseEntity<String> updateQualityGateWithApprovers(UpdateApproverStatus updateApproverStatus) {
    logger.info("updating gate with approvers");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.updateQualityGateWithApprovers(updateApproverStatus));
  }
}
