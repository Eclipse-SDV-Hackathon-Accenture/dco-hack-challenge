package com.tsystems.dco.release.integration.track.client;

import com.tsystems.dco.release.integration.track.domain.ComponentStatusData;
import com.tsystems.dco.release.integration.track.domain.TrackCampaignEntity;
import com.tsystems.dco.release.integration.track.domain.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tsystems.dco.config.FeignClientConfiguration;
import java.util.UUID;

@FeignClient(
  name = "track",
  url = "${track-management.url}",
  configuration = FeignClientConfiguration.class
)
public interface TrackApiClient {

  @GetMapping("/api/track/{id}")
  ResponseEntity<TrackResponse> findTrackById(@PathVariable("id") UUID id);

  @GetMapping("/api/track/search")
  ResponseEntity<Object> searchTrackByMatchingPattern(@RequestParam("trackPattern") String trackPattern, @RequestParam("page") int page, @RequestParam("size") int size);

  @PostMapping("/api/track/campaign")
  ResponseEntity<Long> createCampaign(@RequestBody TrackCampaignEntity trackCampaignEntity);

  @PostMapping(value = "/api/component/update", produces = {"application/json"}, consumes = {"application/json"})
  ResponseEntity<String> updateSoftwareComponentStatus(@RequestBody ComponentStatusData componentStatusData);

}


