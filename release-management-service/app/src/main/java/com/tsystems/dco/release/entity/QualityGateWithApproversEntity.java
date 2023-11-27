package com.tsystems.dco.release.entity;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.tsystems.dco.release.model.GateStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gate_approver")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class QualityGateWithApproversEntity {

  @Id
  @GeneratedValue
  UUID id;
  @Column
  Integer qualityGate;
  @Column
  @ElementCollection
  List<String> tracks;
  @Column
  @ElementCollection
  List<UUID> trackIds;
  @Column
  @ElementCollection
  List<String> approvers;
  @Column
  Boolean isPassed;
  @Column
  GateStatus status = GateStatus.PENDING;
  @Column
  String approvedDate;
}
