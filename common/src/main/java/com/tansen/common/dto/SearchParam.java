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
public class SearchParam extends ModelBase {

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
        return (sortField == null || sortField.isBlank())  // ✅ fixed: was checking sortOrder
                ? "createdAt" : sortField;
    }

    public String getSortOrder() {
        return (sortOrder == null || sortOrder.isBlank())
                ? "asc" : sortOrder;
    }


    // SearchParam.java
    public String toCacheKey() {
        StringBuilder key = new StringBuilder();

        key.append("firstRow=").append(getFirstRow());
        key.append("_pageSize=").append(getPageSize());
        key.append("_sortField=").append(getSortField());
        key.append("_sortOrder=").append(getSortOrder());

        if (searchFieldParams != null && !searchFieldParams.isEmpty()) {
            searchFieldParams.stream()
                    .sorted(Comparator.comparing(SearchFieldParam::getFieldKey))
                    .forEach(f -> key.append("_")
                            .append(f.getFieldKey() != null ? f.getFieldKey() : "")
                            .append(f.getFieldOperator() != null ? f.getFieldOperator() : "")
                            .append("=")
                            .append(f.getFieldValue() != null ? f.getFieldValue() : ""));
        }

        if (param != null && !param.isEmpty()) {
            param.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> key.append("_")
                            .append(e.getKey())
                            .append("=")
                            .append(e.getValue() != null ? e.getValue() : ""));
        }

        return key.toString();
    }

    /**
     * Generates a wildcard pattern to match ALL cache keys for a given namespace.
     * Used for bulk invalidation on mutations (resolve, reject, close, etc.).
     *
     * Example: "complaints:42:*"
     *
     * @param namespace prefix like "complaints:{municipalityId}"
     * @return Redis-compatible wildcard pattern string
     */
    public static String toCacheKeyPattern(String namespace) {
        return namespace + ":*";
    }
}

