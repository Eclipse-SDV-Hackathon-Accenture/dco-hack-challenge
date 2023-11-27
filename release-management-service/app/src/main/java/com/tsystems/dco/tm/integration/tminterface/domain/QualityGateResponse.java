package com.tsystems.dco.tm.integration.tminterface.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class QualityGateResponse {
  String qualityGateStatus;
}
