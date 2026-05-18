package com.tansen.app.dto.response;

import com.tansen.app.dto.model.CategoryDto;
import com.tansen.app.dto.model.ComplaintStatusDto;
import com.tansen.common.dto.ModelBase;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Category;
import com.tansen.entity.ComplaintStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListComplainsResponse extends ModelBase {
    private String uniqueId;
    private String complaintTitle;
    private String complaintDescription;
    private CategoryDto category;
    private ComplaintStatusDto status;
    private LocalDateTime createdDate;
    private String photoUrl;

}
