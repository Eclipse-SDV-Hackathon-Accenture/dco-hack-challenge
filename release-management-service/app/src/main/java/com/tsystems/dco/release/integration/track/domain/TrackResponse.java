package com.tsystems.dco.release.integration.track.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * TrackResponse
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class TrackResponse {

  private UUID id;
  private String name;
  private String trackType;
  private String state;
  private String duration;
  private String description;
  private Integer count;
  private List<ComponentResponse> components;
  private List<VehicleResponse> vehicles;

}
