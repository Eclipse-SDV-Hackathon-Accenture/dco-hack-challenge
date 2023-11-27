package com.tsystems.dco.release.integration.track.domain;

import com.tsystems.dco.release.entity.ReleaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Entity
public class TrackCampaignEntity {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Type(type = "pg-uuid")
  private UUID id;
  @Column
  @ElementCollection
  private List<String> vin;
  @Column
  private String version;
  @Column
  private String name;
  @Column
  private String description;
  @Column
  private String endDate;
  @OneToOne
  @JoinColumn(name = "release_id", referencedColumnName = "releaseId")
  private ReleaseEntity releaseEntity;
}
