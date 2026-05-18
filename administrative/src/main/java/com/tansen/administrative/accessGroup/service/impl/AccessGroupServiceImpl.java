package com.tansen.administrative.accessGroup.service.impl;

import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.*;
import com.tansen.administrative.accessGroup.dto.*;
import com.tansen.administrative.accessGroup.dto.request.CreateMunicipalityAccessGroupRequest;
import com.tansen.administrative.accessGroup.mapper.AccessGroupMapper;
import com.tansen.administrative.accessGroup.service.AccessGroupService;
import com.tansen.administrative.actionlog.mapper.ActionLogMapper;
import com.tansen.administrative.municipality.dto.ListMunicipalityAccessGroupResponse;
import com.tansen.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccessGroupServiceImpl implements AccessGroupService {
    private static final Logger LOG = LoggerFactory.getLogger(AccessGroupServiceImpl.class);
    private final AuthorityAccessGroupRepository authorityAccessGroupRepository;
    private final AccessGroupMapper accessGroupMapper;
    private final AuthorityAccessGroupRoleMapRepository authorityAccessGroupRoleMapRepository;
    private final StatusRepository statusRepository;
    private final AuthorityUserRoleRepository authorityUserRoleRepository;
    private final ActionLogMapper actionLogMapper;
    private final AuthorityUserRepository authorityUserRepository;

    public AccessGroupServiceImpl(AuthorityAccessGroupRepository authorityAccessGroupRepository, AccessGroupMapper accessGroupMapper, AuthorityAccessGroupRoleMapRepository vendorAccessGroupRoleMapRepository, StatusRepository statusRepository, AuthorityUserRoleRepository vendorUserRoleRepository, ActionLogMapper actionLogMapper, AuthorityUserRepository vendorUserRepository) {
        this.authorityAccessGroupRepository = authorityAccessGroupRepository;
        this.accessGroupMapper = accessGroupMapper;
        this.authorityAccessGroupRoleMapRepository = vendorAccessGroupRoleMapRepository;
        this.statusRepository = statusRepository;
        this.authorityUserRoleRepository = vendorUserRoleRepository;
        this.actionLogMapper = actionLogMapper;
        this.authorityUserRepository = vendorUserRepository;
    }

    @Override
    public ApiResponse<?> listVendorAccessGroups(Principal loggedInUser) {
        AdministrativeUnit municipality = authorityUserRepository.findByEmail(loggedInUser.getName()).orElseThrow().getMunicipality();
        List<AuthorityAccessGroup> vendorAccessGroupsList = authorityAccessGroupRepository.findByMunicipalityOrderByCreatedAtDesc(municipality);
        List<ListMunicipalityAccessGroupResponse> vendorAccessGroupResponse = accessGroupMapper.vendorAccessGroups(vendorAccessGroupsList);
        LOG.info("Listed vendor access groups");
        return ResponseUtil.getSuccessfulApiResponse(vendorAccessGroupResponse, "Listed vendor access groups");
    }


    @Override
    public ApiResponse<?> createAccessGroup(CreateMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest){
        Optional<AuthorityAccessGroup> byName = authorityAccessGroupRepository.findByName(request.getName());
        if (byName.isPresent()) {
            LOG.error("Failed to create Access Group. Access Group already exists with name: {}!", request.getName());
            return ResponseUtil.getFailureResponse("Access Group already exists!");
        }
        Optional<AuthorityUser> byEmail = authorityUserRepository.findByEmail(loggedInUser.getName());
        if (byEmail.isEmpty()) {
            return ResponseUtil.getFailureResponse("Logged In User does not exist");
        }
        AdministrativeUnit municipality = byEmail.get().getMunicipality();
        AuthorityAccessGroup accessGroup = accessGroupMapper.mapToAccessGroupEntity(request, authorityAccessGroupRepository, authorityAccessGroupRoleMapRepository, statusRepository, authorityUserRoleRepository, municipality);
        actionLogMapper.createAdminAccessGroup(accessGroup.getId(), loggedInUser, httpServletRequest);
        LOG.info("Access Group Created Successfully with name {}", accessGroup.getName());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Created successfully.");
    }

    @Override
    public ApiResponse<?> editAccessGroup(EditMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        AuthorityAccessGroup existingAccessGroup = authorityAccessGroupRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        if (!Objects.equals(existingAccessGroup.getMunicipality(), authorityUserRepository.findByEmail(loggedInUser.getName()).orElseThrow(() -> new RuntimeException("Logged In User does not exist")).getMunicipality())) {
            LOG.error("Failed to edit Access Group. Access Group does not exist with vendor: {}!", existingAccessGroup.getMunicipality());
            return ResponseUtil.getFailureResponse("Access Group does not exist");
        }
        existingAccessGroup.setName(request.getName());
        existingAccessGroup.setDescription(request.getDescription());
        existingAccessGroup.setRemarks(request.getRemarks());
        existingAccessGroup.setUpdatedAt(LocalDateTime.now());

        authorityAccessGroupRepository.save(existingAccessGroup);
        accessGroupMapper.updateAccessGroupRoles(existingAccessGroup, request.getRoleNames(), authorityUserRoleRepository, authorityAccessGroupRoleMapRepository);

        actionLogMapper.updateAccessGroup(existingAccessGroup.getId(), loggedInUser, httpServletRequest);
        LOG.info("Access Group Updated Successfully with ID {}", existingAccessGroup.getId());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Updated successfully.");
    }

    @Override
    public ApiResponse<?> viewAccessGroup(MunicipalityAccessGroupRequest request, Principal loggedInUser) {
        AuthorityAccessGroup accessGroup = authorityAccessGroupRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        if (!Objects.equals(accessGroup.getMunicipality(), authorityUserRepository.findByEmail(loggedInUser.getName()).orElseThrow().getMunicipality())) {
            return ResponseUtil.getFailureResponse("Access Group does Not Found");
        }
        MunicipalityAccessGroupResponse accessGroupDto = accessGroupMapper.mapToVendorAccessGroupDto(accessGroup);
        return ResponseUtil.getSuccessfulApiResponse(accessGroupDto, "Access Group View successfully.");
    }

    @Override
    public ApiResponse<?> deleteAccessGroup(MunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        AuthorityAccessGroup accessGroup = authorityAccessGroupRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("AccessGroup not found with ID: " + request.getId()));
        if (!Objects.equals(authorityUserRepository.findByEmail(loggedInUser.getName()).orElseThrow().getMunicipality(), accessGroup.getMunicipality())) {
            LOG.error("Failed to delete access group. Access Group does not exist with vendor: {}!", accessGroup.getMunicipality());
            return ResponseUtil.getFailureResponse("Access Group does Not Found");
        }

        AuthorityAccessGroup accessGroupUpdated = accessGroupMapper.deleteAccessGroup(accessGroup, statusRepository);
        authorityAccessGroupRepository.save(accessGroupUpdated);
        actionLogMapper.deleteAdminAccessGroup(accessGroup.getId(), loggedInUser, httpServletRequest);
        LOG.info("Access Group Deleted Successfully with ID {} by user {}", accessGroup.getId(), loggedInUser.getName());
        return ResponseUtil.getSuccessfulApiResponse("Access Group Deleted successfully.");
    }

    @Override
    public ApiResponse<?> listActive(Principal loggedInUser) {
        AdministrativeUnit vendor = authorityUserRepository.findByEmail(loggedInUser.getName()).orElseThrow(() -> new RuntimeException("Logged In User does not exist")).getMunicipality();
        Status status = statusRepository.findByName(StatusConstant.ACTIVE.getName());
        List<AuthorityAccessGroup> accessGroupList = authorityAccessGroupRepository.findByStatusAndMunicipality(status, vendor);
        if (accessGroupList.isEmpty()) {
            return ResponseUtil.getFailureResponse("No access groups found");
        } else {
            List<ListAccessGroupResponse> accessGroupResponseList = accessGroupMapper.mapToAccessGroupList(accessGroupList);
            return ResponseUtil.getSuccessfulApiResponse(accessGroupResponseList, "Access Group Fetched Successfully");
        }
    }

    @Override
    public ApiResponse<?> listAdminRoles() {
        List<AuthorityUserRole> adminRoleList = authorityUserRoleRepository.findAll();
        List<MunicipalityRolesResponse> adminRolesListResponse = accessGroupMapper.getAdminRolesList(adminRoleList);
        return ResponseUtil.getSuccessfulApiResponse(adminRolesListResponse, "Admin Roles Fetched Successfully");
    }
}
