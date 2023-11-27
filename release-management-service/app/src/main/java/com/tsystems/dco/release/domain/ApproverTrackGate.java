package com.tsystems.dco.release.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ApproverTrackGate {

  private Integer qualityGate;
  private List<String> tracks;
  private String approver;

}
