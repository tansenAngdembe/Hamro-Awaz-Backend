package com.tansen.administrative.user.service.impl;

import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.User;
import com.tansen.administrative.user.dto.ListUserResponse;
import com.tansen.administrative.user.mapper.UserMapper;
import com.tansen.administrative.user.service.UserService;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.searchrepo.UserSearchTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserSearchTestRepository userSearchRepository;
    private final UserMapper userMapper;
    private final SearchResponse searchResponse;
    private final AuthorityUserRepository authorityUserRepository;

    public UserServiceImpl(UserSearchTestRepository userSearchRepository,
                           UserMapper userMapper,
                           SearchResponse searchResponse, AuthorityUserRepository authorityUserRepository) {
        this.userSearchRepository = userSearchRepository;
        this.userMapper = userMapper;
        this.searchResponse = searchResponse;
        this.authorityUserRepository = authorityUserRepository;
    }

    @Override
    public ApiResponse<?> listAllUsers(SearchParam searchParam, Principal principal) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(principal.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", principal.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        // Resolve the currently authenticated AuthorityUser
        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit municipality = authorityUser.getMunicipality();

        SearchResponseWithMapperBuilder<User, ListUserResponse> responseBuilder =
                SearchResponseWithMapperBuilder.<User, ListUserResponse>builder()
                        .count(sp -> userSearchRepository.count(sp, municipality))
                        .searchData(sp -> userSearchRepository.getAll(sp, municipality))
                        .mapperFunction(this.userMapper::listAllUsers)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListUserResponse> response = searchResponse.getSearchResponse(responseBuilder);
        LOG.info("Users listed successfully for municipality: {}",
                municipality != null ? municipality.getId() : "ALL");

        return ResponseUtil.getSuccessfulApiResponseWithData(response, "User listed successfully");
    }

//    private AuthorityUser getAuthenticatedAuthorityUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !(authentication.getPrincipal() instanceof AuthorityUser)) {
//            throw new UnauthorizedException("No authenticated authority user found");
//        }
//        return (AuthorityUser) authentication.getPrincipal();
//    }
}