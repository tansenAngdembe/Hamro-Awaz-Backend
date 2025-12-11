package com.tansen.admin.accessgroup.mapper;

import com.tansen.admin.accessgroup.dto.AccessGroupResponse;
import com.tansen.admin.accessgroup.dto.AdminRolesResponse;
import com.tansen.admin.accessgroup.dto.CreateAdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.ListAccessGroupResponse;
import com.tansen.common.constant.StatusConstant;
import com.tansen.entity.AccessGroup;
import com.tansen.entity.AccessGroupRoleMap;
import com.tansen.entity.AdminRole;
import com.tansen.repository.AccessGroupRepository;
import com.tansen.repository.AccessGroupRoleMapRepository;
import com.tansen.repository.AdminRolesRepository;
import com.tansen.repository.StatusRepository;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class AccessGroupMapper {

    private static final Logger LOG = LoggerFactory.getLogger(AccessGroupMapper.class);

    // Inject repositories
    @Autowired
    protected AccessGroupRepository accessGroupRepository;

    @Autowired
    protected AccessGroupRoleMapRepository accessGroupRoleMapRepository;

    @Autowired
    protected StatusRepository statusRepository;

    @Autowired
    protected AdminRolesRepository adminRolesRepository;

    // Map entity list to DTO list
    public abstract List<ListAccessGroupResponse> mapToAccessGroupList(List<AccessGroup> accessGroupList);

    // Map single AdminRole to AdminRolesResponse DTO
    public abstract AdminRolesResponse mapToAdminRolesResponse(AdminRole adminRole);

    // Helper to map list of AdminRole
    public List<AdminRolesResponse> getAdminRolesList(List<AdminRole> adminRoleList) {
        return adminRoleList.stream()
                .map(this::mapToAdminRolesResponse)
                .collect(Collectors.toList());
    }

    // Map single AccessGroup entity to DTO
    public abstract AccessGroupResponse mapToAccessGroupDto(AccessGroup accessGroup);

    // Map CreateAdminAccessGroupRequest to AccessGroup entity and save
    public AccessGroup mapToAccessGroupEntity(CreateAdminAccessGroupRequest request) {
        AccessGroup accessGroup = new AccessGroup();
        accessGroup.setName(request.getName());
        accessGroup.setDescription(request.getDescription());
        accessGroup.setRemarks(request.getRemarks());
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        accessGroup.setCreatedAt(LocalDateTime.now());
        accessGroup.setUpdatedAt(LocalDateTime.now());
        accessGroup.setSuperAdminGroup(false);

        AccessGroup savedAccessGroup = accessGroupRepository.save(accessGroup);
        List<AccessGroupRoleMap> accessGroupRoleMaps = new ArrayList<>();

        for (String roleName : request.getRoleNames()) {
            AdminRole adminRole = adminRolesRepository.findByName(roleName);
            if (adminRole != null) {
                AccessGroupRoleMap map = new AccessGroupRoleMap();
                map.setAdminRole(adminRole);
                map.setAccessGroup(savedAccessGroup);
                map.setIsActive(true);
                accessGroupRoleMaps.add(map);
            } else {
                LOG.warn("Role not found for name: {}", roleName);
            }
        }

        if (!accessGroupRoleMaps.isEmpty()) {
            accessGroupRoleMapRepository.saveAll(accessGroupRoleMaps);
        }

        return savedAccessGroup;
    }

    // Update AccessGroup roles
    public void updateAccessGroupRoles(AccessGroup accessGroup, List<String> roleNames) {
        List<AccessGroupRoleMap> existingRoleMappings = accessGroupRoleMapRepository.findByAccessGroup(accessGroup);
        List<String> newRoleNames = new ArrayList<>(roleNames);

        for (AccessGroupRoleMap existing : existingRoleMappings) {
            AdminRole role = existing.getAdminRole();
            if (!newRoleNames.contains(role.getName())) {
                existing.setIsActive(false);
                accessGroupRoleMapRepository.save(existing);
            } else {
                newRoleNames.remove(role.getName());
            }
        }

        for (String roleName : newRoleNames) {
            AdminRole role = adminRolesRepository.findByName(roleName);
            if (role != null) {
                AccessGroupRoleMap map = new AccessGroupRoleMap();
                map.setAdminRole(role);
                map.setAccessGroup(accessGroup);
                map.setIsActive(true);
                accessGroupRoleMapRepository.save(map);
            } else {
                LOG.warn("Role not found for name: {}", roleName);
            }
        }
    }

    // Soft delete AccessGroup
    public AccessGroup deleteAccessGroup(AccessGroup accessGroup) {
        accessGroup.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
        accessGroup.setUpdatedAt(LocalDateTime.now());
        return accessGroup;
    }
}
