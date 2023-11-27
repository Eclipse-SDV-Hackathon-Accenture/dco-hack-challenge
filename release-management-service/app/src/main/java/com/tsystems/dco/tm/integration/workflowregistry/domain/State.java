package com.tsystems.dco.tm.integration.workflowregistry.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = "actions", allowGetters = true)
public class State {

  private String name;
  private String type;
  private List<Action> actions = new ArrayList<Action>();
  private boolean end;

}

