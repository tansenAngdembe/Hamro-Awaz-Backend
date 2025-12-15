package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.SearchParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMunicipalityUserRequest extends ModelBase {
    private String municipalityUniqueId;
    private SearchParam searchParam;
}