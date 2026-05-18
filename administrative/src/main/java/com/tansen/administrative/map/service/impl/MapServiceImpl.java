package com.tansen.administrative.map.service.impl;

import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.ComplaintCoordinates;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.administrative.complaints.mapper.ComplaintMapper;
import com.tansen.administrative.map.dto.response.ListMapResponse;
import com.tansen.administrative.map.mapper.MapMapper;
import com.tansen.administrative.map.service.MapService;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.ComplainStatusRepository;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.StatusRepository;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import com.tansen.repository.searchrepo.ComplaintSearchRepository;
import com.tansen.repository.searchrepo.MapSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class MapServiceImpl implements MapService {
    private static final Logger LOG = LoggerFactory.getLogger(MapServiceImpl.class);

    private final ComplaintRepository complaintRepository;
    private final AuthorityUserRepository authorityUserRepository;
    private final SearchResponse searchResponse;
    private final StatusRepository statusRepository;
    private final ComplainStatusRepository complainStatusRepository;
    private final MapSearchRepository mapSearchRepository;
    private final MapMapper mapMapper;

    private final AuthorityUserSearchRepository authorityUserSearchRepository;

    public MapServiceImpl(ComplaintRepository complaintRepository, AuthorityUserRepository authorityUserRepository, ComplaintSearchRepository complaintSearchRepository, ComplaintMapper complaintMapper, SearchResponse searchResponse, StatusRepository statusRepository, ComplainStatusRepository complainStatusRepository, MapSearchRepository mapSearchRepository, MapMapper mapMapper, AuthorityUserSearchRepository authorityUserSearchRepository) {
        this.complaintRepository = complaintRepository;
        this.authorityUserRepository = authorityUserRepository;
        this.searchResponse = searchResponse;
        this.statusRepository = statusRepository;
        this.complainStatusRepository = complainStatusRepository;
        this.mapSearchRepository = mapSearchRepository;
        this.mapMapper = mapMapper;
        this.authorityUserSearchRepository = authorityUserSearchRepository;
    }

    @Override
    public ApiResponse<?> listComplaintCoordinates(SearchParam searchParam, Principal loggedInAdmin) {

        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit municipality = authorityUser.getMunicipality();

        if (municipality == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any municipality.");
        }

        Long municipalityId = municipality.getId();

        SearchResponseWithMapperBuilder<ComplaintCoordinates, ListMapResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<ComplaintCoordinates, ListMapResponse>builder()
                        .count(param -> mapSearchRepository.count(param, municipalityId))
                        .searchData(param -> mapSearchRepository.getAll(param, municipalityId))
                        .mapperFunction(this.mapMapper::listComplainsResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListMapResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        LOG.info("Map coordinates listed successfully");
        return ResponseUtil.getSuccessfulApiResponse(response, "Complaints listed successfully");
    }




}
