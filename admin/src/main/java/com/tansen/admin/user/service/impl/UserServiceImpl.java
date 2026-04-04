package com.tansen.admin.user.service.impl;


import com.tansen.admin.user.dto.ListUserResponse;
import com.tansen.admin.user.mapper.UserMapper;
import com.tansen.admin.user.service.UserService;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.Admin;
import com.tansen.entity.User;
import com.tansen.repository.searchrepo.UserSearchRepository;
import com.tansen.repository.searchrepo.impl.UserSearchRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserSearchRepository userSearchRepository;
    private final UserMapper userMapper;
    private final SearchResponse searchResponse;


    public UserServiceImpl(UserSearchRepository userSearchRepositoryImpl, UserMapper userMapper, SearchResponse searchResponse) {
        this.userSearchRepository = userSearchRepositoryImpl;
        this.userMapper = userMapper;
        this.searchResponse = searchResponse;
    }

    @Override
    public ApiResponse<?> listAllUsers(SearchParam searchParam){
        SearchResponseWithMapperBuilder<User, ListUserResponse> responseBuilder = SearchResponseWithMapperBuilder.<User, ListUserResponse>builder()
                .count(userSearchRepository::count).searchData(userSearchRepository::getAll)
                .mapperFunction(this.userMapper::listAllUsers).searchParam(searchParam).build();
        PageableResponse<ListUserResponse> response = searchResponse.getSearchResponse(responseBuilder);
        LOG.info("User listed successfully");
        return ResponseUtil.getSuccessfulApiResponseWithData(response, "User listed successfully");
    }
}
