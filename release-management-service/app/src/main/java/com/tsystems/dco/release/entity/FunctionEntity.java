package com.tsystems.dco.release.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.UUID;

/**
 * FunctionEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "function_data")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class FunctionEntity {

  @Id
  @GeneratedValue
  private UUID functionId;
  @Column
  private String name;
  @OneToMany(targetEntity = EcuDataEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "function_id", referencedColumnName = "functionId")
  private Set<EcuDataEntity> ecuDatas;

}
