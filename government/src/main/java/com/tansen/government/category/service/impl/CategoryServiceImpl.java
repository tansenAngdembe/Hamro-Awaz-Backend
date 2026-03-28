package com.tansen.government.category.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Category;
import com.tansen.entity.Complaint;
import com.tansen.entity.Municipality;
import com.tansen.government.category.dto.CreateCategoryRequest;
import com.tansen.government.category.dto.ListCategoryResponse;
import com.tansen.government.category.mapper.CategoryMapper;
import com.tansen.government.category.service.CategoryService;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import com.tansen.government.complaints.service.impl.ComplaintServiceImpl;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.CategoryRepository;
import com.tansen.repository.searchrepo.CategorySearchRepository;
import com.tansen.repository.searchrepo.impl.CategorySearchRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final AuthorityUserRepository authorityUserRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SearchResponse searchResponse;
    private final CategorySearchRepository categorySearchRepository;
    private final ObjectMapper objectMapper;

    public CategoryServiceImpl(AuthorityUserRepository authorityUserRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper, SearchResponse searchResponse, CategorySearchRepository categorySearchRepository, ObjectMapper objectMapper) {
        this.authorityUserRepository = authorityUserRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.searchResponse = searchResponse;
        this.categorySearchRepository = categorySearchRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiResponse<?> listCategory(SearchParam searchParam, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        Municipality municipality = authorityUser.getMunicipality();

        if (municipality == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any municipality.");
        }

        Long municipalityId = municipality.getId();
        SearchResponseWithMapperBuilder<Category, ListCategoryResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<Category, ListCategoryResponse>builder()
                        .count(param -> categorySearchRepository.count(param, municipalityId))
                        .searchData(param -> categorySearchRepository.getAll(param, municipalityId))
                        .mapperFunction(this.categoryMapper::listCategoryResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListCategoryResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        LOG.info("Category listed successfully");
        return ResponseUtil.getSuccessfulApiResponse(response, "Category listed successfully");

    }

    @Override
    public ApiResponse<?> createCategory(CreateCategoryRequest request, Principal loggedInAdmin) {

          Municipality municipalityId = getLoggedInMunicipality(loggedInAdmin);

        Optional<Category> existingCategory = categoryRepository.findByCategoryNameAndMunicipalityId(request.getCategoryName(), municipalityId.getId());
        if (existingCategory.isPresent()) {
            return ResponseUtil.getFailureResponse(
                    "Category with this name already exists in your municipality."
            );
        }
        categoryRepository.save(categoryMapper.createCategory(request, municipalityId));
        return ResponseUtil.getSuccessfulApiResponse("Category created.");


    }


    public Municipality getLoggedInMunicipality(Principal principal) {

        AuthorityUser authorityUser = authorityUserRepository
                .findByEmail(principal.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged in user not found")
                );

        Municipality municipality = authorityUser.getMunicipality();

        if (municipality == null) {
            throw new IllegalStateException(
                    "Authority user is not assigned to any municipality"
            );
        }

        return municipality;
    }
}
