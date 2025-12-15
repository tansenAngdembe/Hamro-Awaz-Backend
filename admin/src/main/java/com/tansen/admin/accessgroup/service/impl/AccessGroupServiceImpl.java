package com.tansen.admin.accessgroup.service.impl;


import com.tansen.admin.accessgroup.dto.*;
import com.tansen.admin.accessgroup.mapper.AccessGroupMapper;
import com.tansen.admin.accessgroup.service.AccessGroupService;
import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.AccessGroup;
import com.tansen.entity.AccessGroupRoleMap;
import com.tansen.entity.AdminRole;
import com.tansen.entity.Status;
import com.tansen.repository.AccessGroupRepository;
import com.tansen.repository.AccessGroupRoleMapRepository;
import com.tansen.repository.AdminRolesRepository;
import com.tansen.repository.StatusRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AccessGroupServiceImpl implements AccessGroupService {
    private static final Logger LOG = LoggerFactory.getLogger(AccessGroupServiceImpl.class);
    private final AccessGroupRepository accessGroupRepository;
    private final AccessGroupRoleMapRepository accessGroupRoleMapRepository;
    private final AccessGroupMapper accessGroupMapper;
    private final AdminRolesRepository adminRolesRepository;
    private final StatusRepository statusRepository;
//    private final VendorAccessGroupRepository vendorAccessGroupRepository;
    private final ActionLogMapper actionLogMapper;

    public AccessGroupServiceImpl(StatusRepository statusRepository, AccessGroupRepository accessGroupRepository, AccessGroupMapper accessGroupMapper, AdminRolesRepository adminRolesRepository, AccessGroupRoleMapRepository accessGroupRoleMapRepository, ActionLogMapper actionLogMapper) {
        this.statusRepository = statusRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.accessGroupMapper = accessGroupMapper;
//        this.vendorAccessGroupRepository = vendorAccessGroupRepository;
        this.adminRolesRepository = adminRolesRepository;
        this.accessGroupRoleMapRepository = accessGroupRoleMapRepository;
        this.actionLogMapper = actionLogMapper;
    }

    @Override
    public ApiResponse<?> createAdminAccessGroup(CreateAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        Optional<AccessGroup> byName = accessGroupRepository.findByName(request.getName());
        if (byName.isPresent()) {
            LOG.error("Failed to Create. Access group with name {} already exists", request.getName());
            return ResponseUtil.getFailureResponse("Access group already exists with this name.");
        }
        AccessGroup accessGroup = accessGroupMapper.mapToAccessGroupEntity(request,
                statusRepository
        );
        AccessGroup savedAccessGroup = accessGroupRepository.save(accessGroup);
        List<AccessGroupRoleMap> accessGroupRoleMaps = new ArrayList<>();

        for (String roleName : request.getRoleNames()) {
            AdminRole adminRole = adminRolesRepository.findByName(roleName);
            if (adminRole != null) {
                AccessGroupRoleMap accessGroupRoleMap = new AccessGroupRoleMap();
                accessGroupRoleMap.setAdminRole(adminRole);
                accessGroupRoleMap.setAccessGroup(savedAccessGroup);
                accessGroupRoleMap.setIsActive(true);
                accessGroupRoleMaps.add(accessGroupRoleMap);
            } else {
                LOG.warn("Role not found for name: {}", roleName);
            }
        }
        if (!accessGroupRoleMaps.isEmpty()) {
            accessGroupRoleMapRepository.saveAll(accessGroupRoleMaps);
        }
        actionLogMapper.createAdminAccessGroup(savedAccessGroup.getId(), loggedInUser,  httpServletRequest);
        LOG.info("Access Group Created Successfully with name {}", accessGroup.getName());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Created successfully.");
    }


    @Override
    public ApiResponse<?> editAdminAccessGroup(EditAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        AccessGroup existingAccessGroup = accessGroupRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        existingAccessGroup.setName(request.getName());
        existingAccessGroup.setDescription(request.getDescription());
        existingAccessGroup.setRemarks(request.getRemarks());
        existingAccessGroup.setUpdatedAt(LocalDateTime.now());

        accessGroupRepository.save(existingAccessGroup);

        List<AccessGroupRoleMap> existingRoleMappings = accessGroupRoleMapRepository.findByAccessGroup(existingAccessGroup);
        List<String> newRoleNames = new ArrayList<>(request.getRoleNames());

        for (AccessGroupRoleMap existingRoleMapping : existingRoleMappings) {
            AdminRole adminRole = existingRoleMapping.getAdminRole();
            if (!newRoleNames.contains(adminRole.getName())) {
                existingRoleMapping.setIsActive(false);
                accessGroupRoleMapRepository.save(existingRoleMapping);
            } else {
                newRoleNames.remove(adminRole.getName());
            }
        }

        for (String roleName : newRoleNames) {
            AdminRole adminRole = adminRolesRepository.findByName(roleName);
            if (adminRole != null) {
                AccessGroupRoleMap newAccessGroupRoleMap = new AccessGroupRoleMap();
                newAccessGroupRoleMap.setAdminRole(adminRole);
                newAccessGroupRoleMap.setAccessGroup(existingAccessGroup);
                newAccessGroupRoleMap.setIsActive(true);
                accessGroupRoleMapRepository.save(newAccessGroupRoleMap);
            } else {
                LOG.warn("Failed to update Admin Access Group. Admin Role not found for name: {}", roleName);
            }
        }

//        accessGroupMapper.updateAccessGroupRoles(existingAccessGroup, request.getRoleNames(), adminRolesRepository, accessGroupRoleMapRepository);

        actionLogMapper.updateAdminAccessGroup(existingAccessGroup.getId(), loggedInUser, httpServletRequest);
        LOG.info("Access Group Updated Successfully with ID {}", existingAccessGroup.getId());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Updated successfully.");
    }

    @Override
    public ApiResponse<?> viewAdminAccessGroup(AdminAccessGroupRequest request) {
        AccessGroup accessGroup = accessGroupRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        AccessGroupResponse accessGroupDto = accessGroupMapper.mapToAccessGroupDto(accessGroup);
        return ResponseUtil.getSuccessfulApiResponse(accessGroupDto, "Access Group View successfully.");
    }

    @Override
    public ApiResponse<?> deleteAdminAccessGroup(AdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        AccessGroup accessGroup = accessGroupRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        AccessGroup accessGroupUpdated = accessGroupMapper.deleteAccessGroup(accessGroup, statusRepository);
        accessGroupRepository.save(accessGroupUpdated);
        actionLogMapper.deleteAdminAccessGroup(accessGroup.getId(), loggedInUser,  httpServletRequest);
        LOG.info("Access Group Deleted Successfully with ID {} by user {}", accessGroup.getId(), loggedInUser.getName());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Deleted successfully.");
    }


    @Override
    public ApiResponse<?> listActive() {
        Status status = statusRepository.findByName(StatusConstant.ACTIVE.getName());
        List<AccessGroup> accessGroupList = accessGroupRepository.findByStatus(status);
        if(accessGroupList.isEmpty()) {
            return ResponseUtil.getFailureResponse("No access groups found");
        }
        else{
            List<ListAccessGroupResponse> accessGroupResponseList = accessGroupMapper.mapToAccessGroupList(accessGroupList);
            return ResponseUtil.getSuccessfulApiResponse(accessGroupResponseList,"Access Group Fetched Successfully");
        }
    }



    @Override
    public ApiResponse<?> listAdminRoles() {
        List<AdminRole> adminRoleList = adminRolesRepository.findAll();
        List<AdminRolesResponse> adminRolesListResponse = accessGroupMapper.getAdminRolesList(adminRoleList);
        return ResponseUtil.getSuccessfulApiResponse(adminRolesListResponse,"Admin Roles Fetched Successfully");
    }
}
