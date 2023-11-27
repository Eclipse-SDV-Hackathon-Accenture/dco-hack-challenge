package com.tsystems.dco.tm.integration.tminterface.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityGateRequest {
  private String metaTrack;
  private String track;
  private List<Integer> qualityGates;
  private List<String> approvers;
}

