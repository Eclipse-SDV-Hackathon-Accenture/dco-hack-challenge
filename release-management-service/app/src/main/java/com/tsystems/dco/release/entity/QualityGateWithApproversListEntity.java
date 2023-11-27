package com.tsystems.dco.release.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QualityGateWithApproversListEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "release_gate_approver")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class QualityGateWithApproversListEntity {

  @Id
  @Column
  String releaseId;
  @OneToMany(targetEntity = QualityGateWithApproversEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "qg_approval_id", referencedColumnName = "releaseId")
  List<QualityGateWithApproversEntity> gatesWithApprovers;
}
