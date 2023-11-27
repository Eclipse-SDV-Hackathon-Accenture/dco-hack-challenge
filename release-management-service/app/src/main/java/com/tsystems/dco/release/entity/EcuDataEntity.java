package com.tsystems.dco.release.entity;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

/**
 * FunctionEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ecu_data")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@TypeDef(name = "jsonb", typeClass = JsonNodeBinaryType.class)
public class EcuDataEntity {

  @Id
  @GeneratedValue
  UUID id;
  @Column
  String ecu;
  @Column
  String hardwareVersion;
  @Column
  UUID componentId;
  @Column
  String componentName;
  @Column
  String componentVersion;
  @Column
  String status;
  @Column
  private String lastChange;
  @Column
  private String actualBatteryCapacity;
}
