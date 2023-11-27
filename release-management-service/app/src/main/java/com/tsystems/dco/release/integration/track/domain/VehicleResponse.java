package com.tsystems.dco.release.integration.track.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VehicleResponse
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class VehicleResponse {

  private String vin;
  private String owner;
  private String ecomDate;
  private String country;
  private String model;
  private String brand;
  private String region;
  private String instantiatedAt;
  private String createdAt;
  private String updatedAt;
  private String status;

}
