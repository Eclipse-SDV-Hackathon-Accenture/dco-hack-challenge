package com.tsystems.dco.release.entity;

import com.tsystems.dco.release.integration.track.domain.TrackCampaignEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * ReleaseEntity
 */
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners({
  AuditingEntityListener.class
})
@Table(name = "release")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReleaseEntity {

  @Id
  @Column
  private String releaseId;
  @Column
  private String metaTrack;
  @Column
  private Boolean isHardwareChangesAllowed;
  @Column
  @ElementCollection
  private List<String> brands;
  @Column
  @ElementCollection
  private List<String> models;
  @Column
  @ElementCollection
  private List<String> countries;
  @Column
  private String releaseDate;
  @Column
  private String createdDate;
  @OneToMany(targetEntity = FunctionEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "release_id", referencedColumnName = "releaseId")
  private Set<FunctionEntity> functions;
  @OneToOne(mappedBy = "releaseEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private TrackCampaignEntity trackCampaign;
  @Column
  private String releaseStatus;
}
