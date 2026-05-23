package com.tansen.app.category.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.app.category.dto.ListCategoryResponse;
import com.tansen.app.category.mapper.CategoryMapper;
import com.tansen.app.category.service.CategoryService;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Category;
import com.tansen.entity.User;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.CategoryRepository;
import com.tansen.repository.UserRepository;
import com.tansen.repository.searchrepo.CategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ApiResponse<?> listCategory(Principal loggedInAdmin) {  // ← removed SearchParam
        Optional<User> authorityUserOpt =
                Optional.ofNullable(userRepository.findByEmail(loggedInAdmin.getName()));

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        User user = authorityUserOpt.get();
        AdministrativeUnit municipality = user.getMunicipality();

        if (municipality == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any municipality.");
        }

        // Fetch only categories matching user's municipality
        List<Category> categories = categoryRepository
                .findAllByMunicipality(municipality);

        List<ListCategoryResponse> response = categoryMapper
                .listCategoryResponses(categories);

        LOG.info("Category listed successfully for municipality: {}", municipality.getId());
        return ResponseUtil.getSuccessfulApiResponse(response, "Category listed successfully");
    }


}
