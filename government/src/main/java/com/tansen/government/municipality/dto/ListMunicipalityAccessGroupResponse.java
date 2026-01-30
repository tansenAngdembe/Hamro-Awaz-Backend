package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMunicipalityAccessGroupResponse extends ModelBase {
   private Integer id;
   private String name;
   private String description;
   private StatusDto status;

}
