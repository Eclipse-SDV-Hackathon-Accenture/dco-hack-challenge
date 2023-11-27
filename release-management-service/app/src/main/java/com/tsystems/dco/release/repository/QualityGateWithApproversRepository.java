package com.tsystems.dco.release.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tsystems.dco.release.entity.QualityGateWithApproversListEntity;

/**
 * QualityGateWithApproversRepository
 *
 */
@Repository
public interface QualityGateWithApproversRepository extends JpaRepository<QualityGateWithApproversListEntity, String> {

}
