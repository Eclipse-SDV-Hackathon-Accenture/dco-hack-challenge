package com.tsystems.dco.release.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tsystems.dco.release.entity.ReleaseEntity;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, String> {

}
