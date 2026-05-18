package com.tansen.app.dto.response;

import com.tansen.app.dto.UserDto;
import com.tansen.app.dto.model.ComplaintStatusDto;
import com.tansen.common.dto.ModelBase;
import com.tansen.entity.ComplaintStatus;
import com.tansen.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListNearByComplainsResponse extends ModelBase{
    private String uniqueId;
    private String complaintTitle;
    private String complaintDescription;
    private ComplaintStatusDto status;
    private UserDto reportedBy;
    private LocalDateTime createdDate;

}
