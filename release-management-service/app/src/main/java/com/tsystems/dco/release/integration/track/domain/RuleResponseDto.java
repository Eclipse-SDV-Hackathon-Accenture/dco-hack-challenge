package com.tsystems.dco.release.integration.track.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * RuleResponseDto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class RuleResponseDto {

  private String id;
  private Integer sequence;
  private String description;
  private String type;
  private Map<String, Object> filter;
  private Set<ExtendedFilterDto> filterExtended;
  private Map<String, Object> action;
  private String priority;
  private Map<String, Object> options;

}
