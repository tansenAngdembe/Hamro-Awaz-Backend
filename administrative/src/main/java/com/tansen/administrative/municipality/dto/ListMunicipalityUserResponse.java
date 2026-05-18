package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListMunicipalityUserResponse extends ModelBase {
    private String name;
    private String email;
    private String phoneNumber;
    private String uniqueId;
    private LocalDateTime createdAt;
    private MunicipalityAccessGroupDto authorityAccessGroup;
    private StatusDto status;
}
