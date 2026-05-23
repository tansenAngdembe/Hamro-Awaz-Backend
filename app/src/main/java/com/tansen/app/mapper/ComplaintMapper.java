package com.tansen.app.mapper;

import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.app.dto.response.ListComplainsResponse;
import com.tansen.app.dto.response.ListNearByComplainsResponse;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.service.UploadFileService;
import com.tansen.entity.*;
import com.tansen.entity.enums.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ComplaintMapper {
    @Autowired
    private UploadFileService uploadFileService;

    public abstract ListNearByComplainsResponse entityToResponse(Complaint complaint);
    public List<ListNearByComplainsResponse> listNearByComplainsResponses(List<Complaint> complaintList) {
        return complaintList.stream().map(this::entityToResponse).collect(Collectors.toList());
    }



    public Complaint mapToComplaint(CreateComplaintRequest createComplaint, ComplaintStatus status, Category category, User user, MultipartFile photos) throws IOException {
        Complaint newComplaint = new Complaint();
        newComplaint.setUniqueId(UUID.randomUUID().toString());

        newComplaint.setComplaintTitle(createComplaint.getComplaintTitle());
        newComplaint.setComplaintDescription(createComplaint.getComplaintDescription());
        newComplaint.setMunicipality(user.getMunicipality());
        newComplaint.setCategory(category);
        newComplaint.setStatus(status);

        newComplaint.setReportedBy(user);
        newComplaint.setAssignedTo(null);

        newComplaint.setPriority(Priority.MEDIUM); // default priority

        if (photos != null && !photos.isEmpty()) {
            newComplaint.setPhotoUrl(
                    uploadFileService.uploadFile(photos, FilePathConstant.BASE_PATH, FilePathConstant.COMPLAINT, true)
            );
        }

        LocalDateTime now = LocalDateTime.now();
        newComplaint.setCreatedDate(now);
        newComplaint.setUpdatedDate(null);
        newComplaint.setResolvedAt(null);

        newComplaint.setActive(true);
        return newComplaint;
    }

    public Complaint mapToUpdateComplaint(UpdateComplaintRequest updateComplaintRequest,MultipartFile photos,Complaint complaint) throws   IOException {

        complaint.setComplaintTitle(updateComplaintRequest.getComplaintTitle());
        complaint.setComplaintDescription(updateComplaintRequest.getComplaintDescription());
        complaint.setUpdatedDate(LocalDateTime.now());

        if(updateComplaintRequest.getPhotoUrl() != null){
            if (photos != null && !photos.isEmpty()) {
                complaint.setPhotoUrl(
                        uploadFileService.uploadFile(photos, FilePathConstant.BASE_PATH, FilePathConstant.COMPLAINT, true)
                );
            }
        }
        return  complaint;


    }

    public abstract ListComplainsResponse myComplaintResponse(Complaint complaint);
    public List<ListComplainsResponse> listMyComplainsResponses(List<Complaint> complaintList) {
        return complaintList.stream().map(this::myComplaintResponse).collect(Collectors.toList());
    }
}
