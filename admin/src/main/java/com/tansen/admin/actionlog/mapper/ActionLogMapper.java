package com.tansen.admin.actionlog.mapper;


import com.tansen.admin.actionlog.constant.ActionTypeConstant;
import com.tansen.admin.actionlog.constant.TargetTypeConstant;
import com.tansen.admin.actionlog.dto.ActionLogModel;
import com.tansen.admin.actionlog.service.ActionLogService;
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

    public void changePassword(Long adminId, Principal principal, HttpServletRequest request) {
        ActionLogModel passwordChange = ActionLogModel.builder()
                .remarks("Password changed successfully")
                .actionType(ActionTypeConstant.CHANGE_PASSWORD)
                .targetType(TargetTypeConstant.ADMIN)
                .targetId(adminId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordChange, principal);

    }

    public void resetPassword(Long adminId, String remark, Principal principal, HttpServletRequest request) {
        ActionLogModel passwordReset = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.RESET_PASSWORD)
                .targetType(TargetTypeConstant.ADMIN)
                .targetId(adminId)
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

    public void createMunicipality(Long vendorId, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel createVendor = ActionLogModel.builder()
                .remarks("Municipality created successfully")
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(createVendor, loggedInUser);
    }

    public void updateMunicipality(Long vendorId, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel createVendor = ActionLogModel.builder()
                .remarks("Municipality Updated successfully")
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(createVendor, loggedInUser);
    }

    public void createAuthorityUser(Long userId, String remark, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel resetUserPassword = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(resetUserPassword, loggedInUser);
    }

    public void editMunicipalityUser(Long userId, String remark, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel editVendorUser = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(editVendorUser, loggedInUser);
    }

    public void blockMunicipality(Long municipalityId, String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel blockMunicipality = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.BLOCK)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(municipalityId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(blockMunicipality, loggedInUser);
    }

    public void unblockMunicipality(Long vendorId, String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel municipality = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.UNBLOCK)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(municipality, loggedInUser);
    }

    public void deleteMunicipality(Long vendorId, String remarks, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel municipality = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(vendorId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(municipality, loggedInUser);
    }

    public void blockVendorUser(Long userId, String remark, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel blockVendorUser = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.BLOCK)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(blockVendorUser, loggedInUser);
    }

    public void unblockVendorUser(Long userId, String remark, Principal loggedInUser, HttpServletRequest request) {
        ActionLogModel unblockVendorUser = ActionLogModel.builder()
                .remarks(remark)
                .actionType(ActionTypeConstant.UNBLOCK)
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .targetId(userId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(unblockVendorUser, loggedInUser);
    }

    public void createAdvertisement(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel passwordChange = ActionLogModel.builder()
                .remarks("Advertisement Created Successful.")
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.ADVERTISEMENT)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordChange, principal);
    }

    public void deleteAdvertisement(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel passwordChange = ActionLogModel.builder()
                .remarks("Advertisement Deleted Successful.")
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.ADVERTISEMENT)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordChange, principal);
    }

    public void createAdminAccessGroup(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("AccessGroup Created Successful.")
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void updateAdminAccessGroup(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("AccessGroup Updated Successful.")
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void deleteAdminAccessGroup(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("AccessGroup Deleted Successful.")
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.ACCESS_GROUP)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void increaseCommissionLog(String remarks, Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel increaseCommissionLogMapper = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.ADMIN)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(increaseCommissionLogMapper, principal);
    }

    public void blockPrivacyPolicy(Long id, String remarks, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.BLOCK)
                .targetType(TargetTypeConstant.PRIVACY_POLICY)
                .targetId(id)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void createPrivacyPolicy(Long id, String remarks, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.PRIVACY_POLICY)
                .targetId(id)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void unblockPrivacyPolicy(Long id, String remarks, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.UNBLOCK)
                .targetType(TargetTypeConstant.PRIVACY_POLICY)
                .targetId(id)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }

    public void deletePrivacyPolicy(Long id, String remarks, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.PRIVACY_POLICY)
                .targetId(id)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }
    public void createFaq(Long id, String remarks, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks(remarks)
                .actionType(ActionTypeConstant.CREATE)
                .targetType(TargetTypeConstant.FAQ)
                .targetId(id)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }
    public void updateFaq(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("AccessGroup Updated Successful.")
                .actionType(ActionTypeConstant.UPDATE)
                .targetType(TargetTypeConstant.FAQ)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }
    public void deleteFaq(Long adId, Principal principal, HttpServletRequest request) {
        ActionLogModel actionLogModel = ActionLogModel.builder()
                .remarks("AccessGroup Deleted Successful.")
                .actionType(ActionTypeConstant.DELETE)
                .targetType(TargetTypeConstant.FAQ)
                .targetId(adId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(actionLogModel, principal);
    }
}

