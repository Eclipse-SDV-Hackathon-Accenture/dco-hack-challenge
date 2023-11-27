package com.tsystems.dco.release.integration.track.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentStatusData {

  private UUID componentId;
  private String status;
}


