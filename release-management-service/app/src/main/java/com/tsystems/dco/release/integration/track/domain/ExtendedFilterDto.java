package com.tsystems.dco.release.integration.track.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class ExtendedFilterDto {

  private String key;
  private String value;
  private String operation;
  private String type;
  private String scope;
  private String name;
}
