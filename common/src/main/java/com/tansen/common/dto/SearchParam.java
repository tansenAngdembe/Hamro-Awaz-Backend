package com.tansen.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
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
    public String toCacheKey() {

        StringBuilder key = new StringBuilder();

        // Pagination
        key.append("firstRow=").append(getFirstRow());
        key.append("_pageSize=").append(getPageSize());

        // Sorting
        key.append("_sortField=").append(getSortField());
        key.append("_sortOrder=").append(getSortOrder());

        // Search field filters
        if (searchFieldParams != null && !searchFieldParams.isEmpty()) {
            searchFieldParams.stream()
                    .sorted(Comparator.comparing(SearchFieldParam::getFieldKey))
                    .forEach(f -> {
                        key.append("_")
                                .append(f.getFieldKey())
                                .append(f.getFieldOperator() != null
                                        ? f.getFieldOperator() : "")
                                .append("=")
                                .append(f.getFieldValue());
                    });
        }

        // Extra params map
        if (param != null && !param.isEmpty()) {
            param.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> {
                        key.append("_")
                                .append(e.getKey())
                                .append("=")
                                .append(e.getValue());
                    });
        }

        return key.toString();
    }


}

