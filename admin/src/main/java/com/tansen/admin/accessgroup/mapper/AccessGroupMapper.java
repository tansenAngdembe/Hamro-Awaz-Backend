package com.tansen.admin.accessgroup.mapper;

import com.tansen.admin.accessgroup.dto.AccessGroupResponse;
import com.tansen.admin.accessgroup.dto.AdminRolesResponse;
import com.tansen.admin.accessgroup.dto.CreateAdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.ListAccessGroupResponse;
import com.tansen.common.constant.StatusConstant;
import com.tansen.entity.AccessGroup;
import com.tansen.entity.AdminRole;
import com.tansen.repository.StatusRepository;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AccessGroupMapper {
    private static final Logger LOG = LoggerFactory.getLogger(AccessGroupMapper.class);

    public abstract ListAccessGroupResponse toDto(AccessGroup  accessGroup);
    public abstract List<ListAccessGroupResponse> mapToAccessGroupList(List<AccessGroup> accessGroupList);

    public abstract AdminRolesResponse mapToAdminRolesResponse(AdminRole adminRole);

    public List<AdminRolesResponse> getAdminRolesList(List<AdminRole> adminRoleList) {
        return adminRoleList.stream().map(this::mapToAdminRolesResponse).collect(Collectors.toList());
    }

    public abstract AccessGroupResponse mapToAccessGroupDto(AccessGroup accessGroup);

    public AccessGroup mapToAccessGroupEntity(CreateAdminAccessGroupRequest request,
                                              StatusRepository statusRepository
                                            ) {
        AccessGroup accessGroup = new AccessGroup();
        accessGroup.setName(request.getName());
        accessGroup.setDescription(request.getDescription());
        accessGroup.setRemarks(request.getRemarks());
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        accessGroup.setCreatedAt(LocalDateTime.now());
        accessGroup.setUpdatedAt(LocalDateTime.now());
        accessGroup.setSuperAdminGroup(false);

        return accessGroup;
    }

/*
    public void updateAccessGroupRoles(AccessGroup accessGroup,
                                       List<String> roleNames,
                                       AdminRolesRepository adminRolesRepository,
                                       AccessGroupRoleMapRepository accessGroupRoleMapRepository
    ) {
        List<AccessGroupRoleMap> existingRoleMappings = accessGroupRoleMapRepository.findByAccessGroup(accessGroup);
        List<String> newRoleNames = new ArrayList<>(roleNames);

//        for (AccessGroupRoleMap existingRoleMapping : existingRoleMappings) {
//            AdminRole adminRole = existingRoleMapping.getAdminRole();
//            if (!newRoleNames.contains(adminRole.getName())) {
//                existingRoleMapping.setIsActive(false);
//                accessGroupRoleMapRepository.save(existingRoleMapping);
//            } else {
//                newRoleNames.remove(adminRole.getName());
//            }
//        }

        for (String roleName : newRoleNames) {
            AdminRole adminRole = adminRolesRepository.findByName(roleName);
            if (adminRole != null) {
                AccessGroupRoleMap newAccessGroupRoleMap = new AccessGroupRoleMap();
                newAccessGroupRoleMap.setAdminRole(adminRole);
                newAccessGroupRoleMap.setAccessGroup(accessGroup);
                newAccessGroupRoleMap.setIsActive(true);
                accessGroupRoleMapRepository.save(newAccessGroupRoleMap);
            } else {
                LOG.warn("Failed to update Admin Access Group. Admin Role not found for name: {}", roleName);
            }
        }
    }
*/
    public AccessGroup deleteAccessGroup(AccessGroup accessGroup, StatusRepository statusRepository) {
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
        accessGroup.setUpdatedAt(LocalDateTime.now());
        return accessGroup;
    }


}
