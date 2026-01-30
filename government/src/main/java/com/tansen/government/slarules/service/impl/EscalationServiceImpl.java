package com.tansen.government.slarules.service.impl;

import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.government.municipality.dto.AssignToListResponse;
import com.tansen.government.slarules.dto.CreateEscalationRequest;
import com.tansen.government.slarules.dto.ListEscalationResponse;
import com.tansen.government.slarules.mapper.EscalationMapper;
import com.tansen.government.slarules.service.EscalationService;
import com.tansen.repository.*;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import com.tansen.repository.searchrepo.EscalationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class EscalationServiceImpl implements EscalationService {
    private static final Logger LOG = LoggerFactory.getLogger(EscalationServiceImpl.class);

    private final AuthorityUserRepository authorityUserRepository;
    private final SearchResponse searchResponse;
    private final StatusRepository statusRepository;

    private final AuthorityUserSearchRepository authorityUserSearchRepository;
    private final EscalationMapper escalationMapper;
    private final EscalationRepository escalationRepository;
    private final CategoryRepository categoryRepository;
    private final EscalationSearchRepository escalationSearchRepository;

    public EscalationServiceImpl(AuthorityUserRepository authorityUserRepository, SearchResponse searchResponse, StatusRepository statusRepository, AuthorityUserSearchRepository authorityUserSearchRepository, EscalationMapper escalationMapper, EscalationRepository escalationRepository, CategoryRepository categoryRepository, EscalationSearchRepository escalationSearchRepository) {
        this.authorityUserRepository = authorityUserRepository;
        this.searchResponse = searchResponse;
        this.statusRepository = statusRepository;
        this.authorityUserSearchRepository = authorityUserSearchRepository;
        this.escalationMapper = escalationMapper;
        this.escalationRepository = escalationRepository;
        this.categoryRepository = categoryRepository;
        this.escalationSearchRepository = escalationSearchRepository;
    }

    @Override
    public ApiResponse<?> listEscalation(SearchParam searchParam, Principal loggedInAdmin) {

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

        SearchResponseWithMapperBuilder<Escalation, ListEscalationResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<Escalation, ListEscalationResponse>builder()
                        .count(param -> escalationSearchRepository.count(param, municipalityId))
                        .searchData(param -> escalationSearchRepository.getAll(param, municipalityId))
                        .mapperFunction(this.escalationMapper::listEscalationResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListEscalationResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        LOG.info("Escalation listed successfully");
        return ResponseUtil.getSuccessfulApiResponse(response, "Escalation listed successfully");
    }

    @Override
    public ApiResponse<?> createEscalation(CreateEscalationRequest createEscalationRequest, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        AuthorityUser authorityUser = authorityUserOpt.get();
        Municipality municipalityId = authorityUser.getMunicipality();
        LOG.info("Looking for category '{}' in municipality ID {}",
                createEscalationRequest.getCategoryName(),
                municipalityId.getId());

        Category category = categoryRepository
                .findByCategoryNameAndMunicipalityId(
                        createEscalationRequest.getCategoryName(),
                        municipalityId.getId()
                )
                .orElseThrow(() -> new RuntimeException(
                        "Category not found: " +createEscalationRequest.getCategoryName() +
                                " for municipality " + municipalityId.getId()                ));
        Escalation escalation = escalationMapper.createEscalationMap(createEscalationRequest,municipalityId,category);
        escalationRepository.save(escalation);

        return ResponseUtil.getSuccessfulApiResponse("Escalation created successfully");
    }

}
