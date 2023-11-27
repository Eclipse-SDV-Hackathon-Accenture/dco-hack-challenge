package com.tsystems.dco.tm.integration.workflowregistry.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Definition {
  private String id;
  private String name;
  private String version;
  private String specVersion;
  private String start;
  private List<State> states = new ArrayList<State>();
  private List<Function> functions = new ArrayList<Function>();
}
