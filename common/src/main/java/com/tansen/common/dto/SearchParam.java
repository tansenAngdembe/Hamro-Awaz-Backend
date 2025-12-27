package com.tansen.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SearchParam extends ModelBase{
    @Schema(description = "The row number to start fetching data from", defaultValue = "0")
    private Integer firstRow;
    @Schema(description = "The number of rows to fetch per page", defaultValue = "10")
    private Integer pageSize;

    private String sortField;
    private String sortOrder;

    private List<SearchFieldParam> searchFieldParams;
    private Map<String, Object> param = new HashMap<>();


    public Integer getFirstRow() {
        if (firstRow == null) {
            return 0;
        }
        return firstRow;
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            return 10;
        }
        return pageSize;
    }
    public String getSortField() {
        return (sortOrder == null || sortOrder.isBlank())
                ? "createdAt" : sortField;
    }
    public String getSortOrder() {
        return (sortOrder == null || sortOrder.isBlank())
                ? "asc": sortOrder;
    }

}

