package com.tansen.government.municipality.mapper;

import com.tansen.common.constant.ActionTypeConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.model.ActionLogModel;
import com.tansen.common.service.MailService;
import com.tansen.common.utility.IpUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.entity.Municipality;
import com.tansen.government.actionlog.constant.TargetTypeConstant;
import com.tansen.government.actionlog.service.ActionLogService;
import com.tansen.government.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.government.municipality.ViewAuthorityProfileRequest;
import com.tansen.government.municipality.dto.*;
import com.tansen.repository.AuthorityAccessGroupRepository;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.ForgotPasswordOtpRepository;
import com.tansen.repository.StatusRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MunicipalityUserMapper {
    @Autowired
    protected AuthorityAccessGroupRepository authorityAccessGroupRepository;
    @Autowired
    private StatusRepository statusRepository;
//    @Autowired
//    private ActionLogRepository actionLogRepository;
    @Autowired
    private ActionLogService actionLogService;
    @Autowired
    private AuthorityUserEmailLogMapper authorityUserEmailLogMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityUserRepository authorityUserRepository;
    @Autowired
    private ForgotPasswordOtpRepository forgotPasswordOtpRepository;

    public abstract ListMunicipalityUserResponse entityToResponse(AuthorityUser authorityUser);
    public List<ListMunicipalityUserResponse> listAllVendors(List<AuthorityUser> vendorUsers) {
        return vendorUsers.stream().map(this::entityToResponse).collect(Collectors.toList());
    }



    public AuthorityUser createVendor(CreateMunicipalityUserRequest createMunicipalityUserRequest, Municipality vendorAdmin, HttpServletRequest request, Principal loggedInVendor) {
        AuthorityUser authorityUser = new AuthorityUser();
        authorityUser.setName(createMunicipalityUserRequest.getFullName());
        authorityUser.setEmail(createMunicipalityUserRequest.getEmail());
        authorityUser.setPhoneNumber(createMunicipalityUserRequest.getMobileNumber());
        authorityUser.setAddress(createMunicipalityUserRequest.getAddress());
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        authorityUser.setAuthorityAccessGroup(authorityAccessGroupRepository.findByName(createMunicipalityUserRequest.getVendorAccessGroup().getName()).orElseThrow());
        authorityUser.setIsActive(false);
        authorityUser.setAuthorityAdmin(false);
        authorityUser.setMunicipality(vendorAdmin);
        authorityUser.setUniqueId(UuidUtil.generateUuid());

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks("CREATED AUTHORITY USER");
        actionLogModel.setTargetType(TargetTypeConstant.MUNICIPALITY);
        actionLogModel.setActionType(ActionTypeConstant.CREATE);
        actionLogModel.setTargetId(vendorAdmin.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel,loggedInVendor);
        return authorityUser;
    }



    public AuthorityUser updateVendorUser(AuthorityUser existingAdmin, UpdateMunicipalityUserRequest updateVendorUserRequest, HttpServletRequest request, Principal loggedInAdmin) {

        existingAdmin.setName(updateVendorUserRequest.getFullName());
        existingAdmin.setEmail(updateVendorUserRequest.getEmail());
        existingAdmin.setPhoneNumber(updateVendorUserRequest.getMobileNumber());
        existingAdmin.setAddress(updateVendorUserRequest.getAddress());
        existingAdmin.setAuthorityAccessGroup(authorityAccessGroupRepository.findByName(updateVendorUserRequest.getVendorAccessGroup().getName()).orElseThrow());

        ActionLogModel  actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(updateVendorUserRequest.getRemarks());
        actionLogModel.setTargetType(TargetTypeConstant.MUNICIPALITY);
        actionLogModel.setActionType(ActionTypeConstant.UPDATE);
        actionLogModel.setTargetId(existingAdmin.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel,loggedInAdmin);
        return existingAdmin;
    }

    public AuthorityUser blockVendorUser(AuthorityUser existingAdmin, BlockMunicipalityUserRequest updateVendorUserRequest, HttpServletRequest request, Principal loggedInAdmin) {
       existingAdmin.setStatus(statusRepository.findByName(StatusConstant.BLOCKED.getName()));
       existingAdmin.setIsActive(false);

        ActionLogModel  actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(updateVendorUserRequest.getRemarks());
        actionLogModel.setTargetType(TargetTypeConstant.MUNICIPALITY);
        actionLogModel.setActionType(ActionTypeConstant.BLOCK);
        actionLogModel.setTargetId(existingAdmin.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel,loggedInAdmin);
       return existingAdmin;
    }

    public AuthorityUser setPassword(AuthorityUser users, SetPasswordRequest setPasswordRequest) {
        users.setPassword(passwordEncoder.encode(setPasswordRequest.getPassword()));
        users.setPasswordChangeDate(LocalDateTime.now());
        users.setIsActive(true);
        users.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        return users;
    }

    public void changePassword(Long adminId, Principal principal, HttpServletRequest request){
        ActionLogModel passwordChange = ActionLogModel.builder()
                .remarks("Password changed successfully")
                .targetType(TargetTypeConstant.MUNICIPALITY)
                .actionType(ActionTypeConstant.CHANGE_PASSWORD)
                .targetId(adminId)
                .ipAddress(request.getRemoteAddr())
                .build();
        actionLogService.insertActionLog(passwordChange, principal);

    }

    public void forgotPasswordChange(AuthorityUser vendorUsers, ForgotPasswordOtp validUsrOtp, SetForgotPasswordRequest forgotPasswordRequest){
        vendorUsers.setPasswordChangeDate(LocalDateTime.now());
//        vendorUsers.setUpdatedAt(LocalDateTime.now());
        authorityUserRepository.save(vendorUsers);

        if (validUsrOtp.getIsValid()) {
            validUsrOtp.setIsValid(false);
            forgotPasswordOtpRepository.save(validUsrOtp);
        }
    }

    public AuthorityUser sendPasswordResetLink(AuthorityUser existingUser, SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request,Principal loggedInUser) {
        ActionLogModel  actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(sendPasswordResetLinkRequest.getRemarks());
        actionLogModel.setTargetType(TargetTypeConstant.MUNICIPALITY);
        actionLogModel.setActionType(ActionTypeConstant.SEND_ADMIN_PASSWORD_RESET_LINK);
        actionLogModel.setTargetId(existingUser.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel,loggedInUser);
        return   existingUser;

    }
    public abstract ViewAuthorityProfileRequest viewProfile(AuthorityUser vendorUsers);

    public AuthorityUser editProfile(AuthorityUser VendorUsers, EditProfileRequest editProfileRequest, Principal principal, HttpServletRequest httpServletRequest) {
        VendorUsers.setName(editProfileRequest.getName());
        VendorUsers.setPhoneNumber(editProfileRequest.getMobileNumber());
        VendorUsers.setAddress(editProfileRequest.getAddress());

        ActionLogModel editProfileLog = new ActionLogModel();
        editProfileLog.setRemarks("EDIT PROFILE");
        editProfileLog.setTargetType(TargetTypeConstant.MUNICIPALITY);
        editProfileLog.setActionType(ActionTypeConstant.UPDATE);
        editProfileLog.setIpAddress(IpUtil.getClientIp(httpServletRequest));
        editProfileLog.setTargetId(VendorUsers.getId());
        actionLogService.insertActionLog(editProfileLog,principal);
        return VendorUsers;
    }
    public AuthorityUser deleteVendorUser(AuthorityUser vendorUser,DeleteMunicipalityUserRequest deleteVendorUserRequest, HttpServletRequest request, Principal loggedInVendor) {
        vendorUser.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));

        ActionLogModel  actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(deleteVendorUserRequest.getRemarks());
        actionLogModel.setTargetType(TargetTypeConstant.MUNICIPALITY);
        actionLogModel.setActionType(ActionTypeConstant.DELETE);
        actionLogModel.setTargetId(vendorUser.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel,loggedInVendor);
        return vendorUser;
    }

    public AuthorityUser unblockVendorUser(AuthorityUser authorityUser, UnblockMunicipalityUserRequest unBlockVendorUserRequest, Principal loggedInVendor, HttpServletRequest request) {
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        authorityUser.setIsActive(true);
        AuthorityUser vendorUsers1 = authorityUserRepository.save(authorityUser);

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(unBlockVendorUserRequest.getRemarks());
        actionLogModel.setActionType(ActionTypeConstant.UNBLOCK);
        actionLogModel.setTargetType(TargetTypeConstant.USER);
        actionLogModel.setTargetId(vendorUsers1.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel, loggedInVendor);
        return vendorUsers1;
    }
    public abstract ViewUserDetailResponse entityToViewDetails(AuthorityUser  vendorUsers);


}
