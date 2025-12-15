package com.tansen.common.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalLevelResponse {
    private Long id;
    private String localLevel;
    private Integer localLevelCode;
    private Integer totalWards;
}
