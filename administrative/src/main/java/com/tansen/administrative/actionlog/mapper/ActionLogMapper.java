package com.tansen.administrative.actionlog.mapper;

import com.tansen.common.constant.ActionTypeConstant;
import com.tansen.common.constant.TargetTypeConstant;
import com.tansen.common.dto.model.ActionLogModel;
import com.tansen.administrative.actionlog.service.ActionLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ActionLogMapper {

    @Autowired
    private ActionLogService actionLogService;

    public void changePassword(Long vendorId, Principal principal, HttpServletRequest request){
        ActionLogModel passwordChange = ActionLogModel.builder()
                .remarks("Password changed successfully")
                .actionType(ActionTypeConstant.CHANGE_PASSWORD)
                .targetType(TargetTypeConstant.ADMIN)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordChange, principal);

    }

    public void resetPassword(Long vendorId, String remark, Principal principal, HttpServletRequest request) {
        ActionLogModel passwordReset = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.RESET_PASSWORD)
                .targetType(TargetTypeConstant.ADMIN)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordReset, principal);
    }

    public void editUserProfile(Long userId, String remark, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel editProfile = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.USER)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(editProfile, loggedInUser);
    }

    public void resetUserPassword(Long userId, String remark, Principal principal, HttpServletRequest request) {
        ActionLogModel resetUserPassword = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.RESET_PASSWORD)
                .targetType(TargetTypeConstant.USER)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(resetUserPassword, principal);
    }

    public void createVendor(Long vendorId,Principal loggedInUser,HttpServletRequest request) {
        ActionLogModel createVendor = ActionLogModel.builder()
                .remarks("Vendor created successfully")
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(createVendor, loggedInUser);
    }

    public void updateVendor(Long vendorId,Principal loggedInUser,HttpServletRequest request) {
        ActionLogModel createVendor = ActionLogModel.builder()
                .remarks("Vendor created successfully")
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(createVendor, loggedInUser);
    }

    public void createVendorUser(Long userId, String remark, Principal loggedInUser, HttpServletRequest request){
        ActionLogModel resetUserPassword = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(resetUserPassword, loggedInUser);
    }

    public void blockVendor(Long vendorId,String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel blockVendor = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.BLOCK)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(blockVendor, loggedInUser);
    }
    public void unblockVendor(Long vendorId,String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel blockVendor = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.UNBLOCK)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(blockVendor, loggedInUser);
    }
    public void deleteVendor(Long vendorId,String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel blockVendor = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(blockVendor, loggedInUser);
    }

    public void createServiceLine(Long serviceLineId, String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel createServiceLine = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(serviceLineId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(createServiceLine, loggedInUser);
    }

    public void updateServiceLine(Long serviceLineId, String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel updateServiceLine = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(serviceLineId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(updateServiceLine, loggedInUser);
    }

    public void createAdminAccessGroup(Long id, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("CREATE ACCESS GROUP")
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(id)
                .ipAddress(httpServletRequest.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, loggedInUser);
    }

    public void updateAccessGroup(Long id, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("UPDATE ACCESS GROUP")
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(id)
                .ipAddress(httpServletRequest.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, loggedInUser);
    }

    public void deleteAdminAccessGroup(Long id, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("DELETE ACCESS GROUP")
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(id)
                .ipAddress(httpServletRequest.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, loggedInUser);
    }

    public void closedComplaintMapper(Long id, Principal loggedInUser, HttpServletRequest httpServletRequest, String complaintRemarks) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(complaintRemarks)
//                .actionType(ActionTypeConstant.CLOSED)// should be add on database as enum "CLOSED"
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.AUTHORITY)
                .targetId(id)
                .ipAddress(httpServletRequest.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, loggedInUser);
    }
}

