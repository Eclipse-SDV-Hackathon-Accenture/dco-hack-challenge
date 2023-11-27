package com.tsystems.dco.release.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class QualityGateWithApprovers {

  private Integer qualityGate;
  private List<String> tracks;
  private List<UUID> trackIds;
  private List<String> approvers;
  Boolean isPassed;
  String status;
  String approvedDate;
}
