package com.tansen.government.accessGroup.mapper;

import com.tansen.common.constant.StatusConstant;
import com.tansen.entity.AuthorityAccessGroup;
import com.tansen.entity.AuthorityAccessGroupRoleMap;
import com.tansen.entity.AuthorityUserRole;
import com.tansen.entity.Municipality;
import com.tansen.government.accessGroup.dto.*;
import com.tansen.government.accessGroup.dto.request.CreateMunicipalityAccessGroupRequest;
import com.tansen.government.municipality.dto.ListMunicipalityAccessGroupResponse;
import com.tansen.repository.AuthorityAccessGroupRepository;
import com.tansen.repository.AuthorityAccessGroupRoleMapRepository;
import com.tansen.repository.AuthorityUserRoleRepository;
import com.tansen.repository.StatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AccessGroupMapper {

    public abstract ListMunicipalityAccessGroupResponse entityToResponse(AuthorityAccessGroup authorityAccessGroup);
    public List<ListMunicipalityAccessGroupResponse> vendorAccessGroups(List<AuthorityAccessGroup> authorityAccessGroups) {
        return authorityAccessGroups.stream().map(this::entityToResponse).collect(Collectors.toList());
    }


    public AuthorityAccessGroup mapToAccessGroupEntity(CreateMunicipalityAccessGroupRequest request,
                                                       AuthorityAccessGroupRepository accessGroupRepository,
                                                       AuthorityAccessGroupRoleMapRepository accessGroupRoleMapRepository,
                                                       StatusRepository statusRepository,
                                                       AuthorityUserRoleRepository vendorUserRoleRepository,
                                                       Municipality municipality) {
        AuthorityAccessGroup accessGroup = new AuthorityAccessGroup();
        accessGroup.setName(request.getName());
        accessGroup.setDescription(request.getDescription());
        accessGroup.setRemarks(request.getRemarks());
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        accessGroup.setCreatedAt(LocalDateTime.now());
        accessGroup.setUpdatedAt(LocalDateTime.now());
        accessGroup.setAuthorityAdminGroup(false);
        accessGroup.setMunicipality(municipality);

        AuthorityAccessGroup savedAccessGroup = accessGroupRepository.save(accessGroup);
        List<AuthorityAccessGroupRoleMap> accessGroupRoleMaps = new ArrayList<>();

        for (String roleName : request.getRoleNames()) {
            AuthorityUserRole adminRole = vendorUserRoleRepository.findByName(roleName);
            if (adminRole != null) {
                AuthorityAccessGroupRoleMap accessGroupRoleMap = new AuthorityAccessGroupRoleMap();
                accessGroupRoleMap.setAuthorityUserRole(adminRole);
                accessGroupRoleMap.setAuthorityAccessGroup(savedAccessGroup);
                accessGroupRoleMap.setIsActive(true);
                accessGroupRoleMaps.add(accessGroupRoleMap);
            } else {
                System.out.println("Role not found: " + roleName);
            }
        }
        if (!accessGroupRoleMaps.isEmpty()) {
            accessGroupRoleMapRepository.saveAll(accessGroupRoleMaps);
        }
        return savedAccessGroup;
    }

    public void updateAccessGroupRoles(AuthorityAccessGroup accessGroup, List<String> roleNames, AuthorityUserRoleRepository vendorUserRoleRepository, AuthorityAccessGroupRoleMapRepository vendorAccessGroupRoleMapRepository) {
        List<AuthorityAccessGroupRoleMap> existingRoleMappings = vendorAccessGroupRoleMapRepository.findByAuthorityAccessGroup(accessGroup);
        List<String> newRoleNames = new ArrayList<>(roleNames);

        for (AuthorityAccessGroupRoleMap existingRoleMapping : existingRoleMappings) {
            AuthorityUserRole adminRole = existingRoleMapping.getAuthorityUserRole();
            if (!newRoleNames.contains(adminRole.getName())) {
                existingRoleMapping.setIsActive(false);
                vendorAccessGroupRoleMapRepository.save(existingRoleMapping);
            } else {
                newRoleNames.remove(adminRole.getName());
            }
        }

        for (String roleName : newRoleNames) {
            AuthorityUserRole role = vendorUserRoleRepository.findByName(roleName);
            if (role != null) {
                AuthorityAccessGroupRoleMap newAccessGroupRoleMap = new AuthorityAccessGroupRoleMap();
                newAccessGroupRoleMap.setAuthorityUserRole(role);
                newAccessGroupRoleMap.setAuthorityAccessGroup(accessGroup);
                newAccessGroupRoleMap.setIsActive(true);
                vendorAccessGroupRoleMapRepository.save(newAccessGroupRoleMap);
            } else {
                System.out.println("Failed to update Vendor Access Group. Role not found for name " + roleName);
            }
        }
    }

    public abstract AccessGroupResponse mapToAccessGroupDto(AuthorityAccessGroup accessGroup);
    public abstract MunicipalityAccessGroupResponse mapToVendorAccessGroupDto(AuthorityAccessGroup accessGroup);


    public AuthorityAccessGroup deleteAccessGroup(AuthorityAccessGroup accessGroup, StatusRepository statusRepository) {
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
        accessGroup.setUpdatedAt(LocalDateTime.now());
        return accessGroup;
    }

    public abstract List<ListAccessGroupResponse> mapToAccessGroupList(List<AuthorityAccessGroup> accessGroupList);

    public abstract MunicipalityRolesResponse mapToAdminRolesResponse(AuthorityUserRole vendorUserRole);

    public List<MunicipalityRolesResponse> getAdminRolesList(List<AuthorityUserRole> adminRoleList) {
        return adminRoleList.stream().map(this::mapToAdminRolesResponse).collect(Collectors.toList());
    }
}
