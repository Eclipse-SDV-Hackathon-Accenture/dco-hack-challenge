package com.tsystems.dco.release.mapper;

import com.tsystems.dco.release.domain.QualityGateWithApproversList;
import com.tsystems.dco.release.entity.QualityGateWithApproversListEntity;
import com.tsystems.dco.release.entity.ReleaseEntity;
import com.tsystems.dco.release.model.CreateReleaseRequest;
import com.tsystems.dco.release.model.DefinitionResponse;
import com.tsystems.dco.release.model.Release;
import com.tsystems.dco.release.model.ReleaseApprovers;
import com.tsystems.dco.tm.integration.workflowregistry.domain.WorkflowDefinitionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Map release beans.
 */
@Mapper
public interface ReleaseMapper {
  ReleaseMapper INSTANCE = Mappers.getMapper(ReleaseMapper.class);
  Release toModel(ReleaseEntity entity);
  ReleaseEntity toEntity(CreateReleaseRequest createReleaseRequest);
  ReleaseEntity toEntity(Release release);
  QualityGateWithApproversListEntity toEntity(QualityGateWithApproversList gateWithApproversList);
  ReleaseApprovers toModel(QualityGateWithApproversListEntity qualityGateWithApproversListEntity);
  DefinitionResponse toEntity(WorkflowDefinitionRequest workflowDefinitionResponse);
  List<DefinitionResponse> toEntity(List<WorkflowDefinitionRequest> workflowDefinitionResponse);
}
