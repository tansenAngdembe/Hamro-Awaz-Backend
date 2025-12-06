package com.tansen.common.dto;

public class SearchFieldParam extends AbstractFieldParam{
    private String fieldCondition;
    private String fieldValue;
    public SearchFieldParam() {
        this.fieldKey = null;
        this.fieldValue = null;
        this.fieldCondition = null;
        this.fieldOperator = null;
    }
}
