package com.tansen.app.dto.response;

import com.cosmotech.app.dto.model.BookingStatusDto;
import com.cosmotech.app.dto.model.ServiceTimeSlotModel;
import com.cosmotech.common.dto.ModelBase;
import com.cosmotech.common.dto.model.ServiceLineModel;
import com.cosmotech.common.dto.model.UserModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookedServiceResponse extends ModelBase {
    private String uniqueId;
    private UserModel bookedBy;
    private BookingStatusDto status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalDate bookingDate;
    private ServiceLineModel serviceLine;
    private ServiceTimeSlotModel timeSlot;
}
