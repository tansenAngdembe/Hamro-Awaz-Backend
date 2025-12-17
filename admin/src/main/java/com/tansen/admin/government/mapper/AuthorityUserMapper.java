package com.tansen.admin.government.mapper;

import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.common.constant.StatusConstant;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Municipality;
import com.tansen.repository.AuthorityAccessGroupRepository;
import com.tansen.repository.StatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AuthorityUserMapper {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityUserMapper.class);
    @Autowired
    private  AuthorityAccessGroupRepository authorityAccessGroupRepository;
    @Autowired
    private  StatusRepository statusRepository;


    public AuthorityUser mapToAuthorityUser(CreateMunicipalityRequest authorityUserRequest, Municipality municipality) {
        AuthorityUser authorityUser = new AuthorityUser();
        authorityUser.setName(authorityUserRequest.getAuthorityAdminFullName());
        authorityUser.setEmail(authorityUserRequest.getAuthorityAdminEmail());
        authorityUser.setPhoneNumber(authorityUserRequest.getAuthorityAdminPhoneNumber());
        authorityUser.setAddress(authorityUserRequest.getAuthorityAdminAddress());
        authorityUser.setAuthorityAccessGroup(authorityAccessGroupRepository.findById(1L).orElseThrow(
                ()-> new RuntimeException("Default Access Group Not Found")
        ));
        authorityUser.setMunicipality(municipality);
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        authorityUser.setCreatedAt(LocalDateTime.now());
        authorityUser.setAuthorityAdmin(true);
        return authorityUser;
    }
}
