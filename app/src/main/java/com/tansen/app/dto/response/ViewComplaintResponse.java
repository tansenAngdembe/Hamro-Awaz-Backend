package com.tansen.app.dto.response;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import com.tansen.common.dto.response.CategoryResponse;
import com.tansen.common.dto.response.MunicipalityDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewComplaintResponse extends ModelBase {
    private String complaintTitle;
    private String complaintDescription;

    private CategoryResponse category;
    private MunicipalityDto municipality;

    private StatusDto status;
    private String photoUrl;

    private Boolean active;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime resolvedAt;

    private Long totalVotes;
    private Long upVotes;
    private Long downVotes;
}
