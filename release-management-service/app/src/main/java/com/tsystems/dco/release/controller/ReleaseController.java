package com.tsystems.dco.release.controller;

import com.tsystems.dco.release.model.CreateReleaseRequest;
import com.tsystems.dco.release.model.Release;
import com.tsystems.rm.api.ReleaseApi;
import com.tsystems.rm.api.*;
import com.tsystems.dco.release.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReleaseController implements ReleaseApi {

  private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);
  private final ReleaseService releaseService;



  @GetMapping("/{releaseId}")
  public Boolean getImagesByReleaseId(@PathVariable String releaseId,
                                      @RequestParam(name = "status", required = false) Boolean status) throws IOException {
    return releaseService.updateStatus(releaseId, status);
  }

  @Override
  public ResponseEntity<Release> createRelease(CreateReleaseRequest createReleaseRequest) {
    logger.info("creating release");
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(releaseService.createRelease(createReleaseRequest));
  }



  @Override
  public ResponseEntity<Release> getReleaseById(String id) {
    logger.info("fetching release by id Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.getReleaseById(id));
  }


  @Override
  public ResponseEntity<List<Release>> listTrackRelease() {
    logger.info("fetching release Track list Response");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(releaseService.listTrackRelease());
  }

}
