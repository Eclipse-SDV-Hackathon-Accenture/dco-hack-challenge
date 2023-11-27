package com.tsystems.dco.release.repository;

import com.tsystems.dco.release.integration.track.domain.TrackCampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackCampaignRepository extends JpaRepository<TrackCampaignEntity, String> {
}
