package com.tansen.admin.administrativelevel.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListLocalLevelResponse extends ModelBase {
    private Long id;
    private String localLevel;
    private Integer localLevelCode;
    private Integer totalWards;
}
