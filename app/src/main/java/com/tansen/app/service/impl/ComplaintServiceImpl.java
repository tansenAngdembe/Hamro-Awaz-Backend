package com.tansen.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.app.constant.RedisConstant;
import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.NearByComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.app.dto.response.ListComplainsResponse;
import com.tansen.app.dto.response.ListNearByComplainsResponse;
import com.tansen.app.dto.response.NearByComplainsResponse;
import com.tansen.app.mapper.ComplaintMapper;
import com.tansen.app.service.ComplaintService;
import com.tansen.app.util.redisutil.RedisHelper;
import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.repository.*;
import com.tansen.repository.searchrepo.MyComplaintSearchRepository;
import com.tansen.repository.searchrepo.NearByComplaintSearchRepository;
import com.tansen.repository.searchrepo.impl.MyComplaintSearchRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.tansen.app.constant.AppConstant.DEFAULT_RADIUS_KM;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdministrativeUnitRepository municipalityRepository;
    private final ComplainStatusRepository complainStatusRepository;
    private final ComplaintMapper complaintMapper;
    private final ComplaintRepository complaintRepository;
    private final ComplaintCoordinatesRepository complaintCoordinatesRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final NearByComplaintSearchRepository nearByComplaintSearchRepository;
    private final SearchResponse searchResponse;
    private final EscalationRepository escalationRepository;
    private final MyComplaintSearchRepository myComplaintSearchRepository;

    public ComplaintServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, AdministrativeUnitRepository municipalityRepository, ComplainStatusRepository complainStatusRepository, ComplaintMapper complaintMapper, ComplaintRepository complaintRepository, ComplaintCoordinatesRepository complaintCoordinatesRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, NearByComplaintSearchRepository nearByComplaintSearchRepository, SearchResponse searchResponse, EscalationRepository escalationRepository, MyComplaintSearchRepositoryImpl myComplaintSearchRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.municipalityRepository = municipalityRepository;
        this.complainStatusRepository = complainStatusRepository;
        this.complaintMapper = complaintMapper;
        this.complaintRepository = complaintRepository;
        this.complaintCoordinatesRepository = complaintCoordinatesRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.nearByComplaintSearchRepository = nearByComplaintSearchRepository;
        this.searchResponse = searchResponse;
        this.escalationRepository = escalationRepository;
        this.myComplaintSearchRepository = myComplaintSearchRepository;
    }
    @Override
    public ApiResponse<?> listNearByComplains(
            NearByComplaintRequest nearByComplaintRequest
    ) throws JsonProcessingException {

            Double effectiveRadius = (nearByComplaintRequest.getRadiusKm() == null ||nearByComplaintRequest.getRadiusKm() <= 0)
                ? DEFAULT_RADIUS_KM
                : nearByComplaintRequest.getRadiusKm();

        // REDIS CACHE KEY
        String cacheKey = RedisHelper.buildNearComplaintCacheKey(
                nearByComplaintRequest.getLatitude(),
                nearByComplaintRequest.getLongitude(),
                effectiveRadius
        );

        String cachedJson =
                (String) redisTemplate.opsForValue().get(cacheKey);
//
//        if (cachedJson != null) {
//            List<ListNearByComplainsResponse> cached =
//                    objectMapper.readValue(
//                            cachedJson,
//                            new TypeReference<PageableResponse<ListNearByComplainsResponse>>() {}
//                    );
//
//            LOG.info("Complaints fetched from redis for key {}", cacheKey);
//
//            return ResponseUtil.getSuccessfulApiResponse(
//                    cached, "Complaints listed successfully");
//        }

        // Validate request
        if (nearByComplaintRequest.getLatitude() == null ||
                nearByComplaintRequest.getLongitude() == null) {
            return ResponseUtil.getFailureResponse("Latitude and longitude are required");
        }


// Fetch data directly
        List<Complaint> complaints = nearByComplaintSearchRepository.findNearby(
                nearByComplaintRequest.getLatitude(),
                nearByComplaintRequest.getLongitude(),
                effectiveRadius,
                nearByComplaintRequest.getStatusId(),     // null if not provided
                nearByComplaintRequest.getCategoryId()
        );

        if (complaints == null || complaints.isEmpty()) {
            return ResponseUtil.getSuccessfulApiResponse("Complaints not found");
        }

        Long count = nearByComplaintSearchRepository.countNearby(
                nearByComplaintRequest.getLatitude(),
                nearByComplaintRequest.getLongitude(),
                effectiveRadius,
                nearByComplaintRequest.getStatusId(),     // null if not provided
                nearByComplaintRequest.getCategoryId()
        );

// Map to response
        List<ListNearByComplainsResponse> mappedResponse =
                complaintMapper.listNearByComplainsResponses(complaints);

        NearByComplainsResponse mappedComplaintResponse = NearByComplainsResponse.builder()
                .listNearByComplainsResponse(mappedResponse)
                        .count(count)
                                .build();



        // CACHE RESULT
        redisTemplate.opsForValue().set(
                cacheKey,
                objectMapper.writeValueAsString(mappedComplaintResponse),
                Duration.ofMinutes(2)
        );

        LOG.info("Complaints fetched from DB & cached for key {}", cacheKey);
        return ResponseUtil.getSuccessfulApiResponse(mappedComplaintResponse, "Complaint listed near by" );

    }



    @Override
    public ApiResponse<?> createComplaint(CreateComplaintRequest createComplaint,  Principal loggedUser, HttpServletRequest httpServletRequest, MultipartFile phots) throws IOException {
        User user = userRepository.findByEmail(loggedUser.getName());
        if(!user.getIsUserVerified()){
            LOG.error("User is not verified");
            return ResponseUtil.getFailureResponse("To confirm your complaint please upload documentation.");
        }
        try {
            if(RedisHelper.isCooldownActive(user,redisTemplate)){
                Long remainingSeconds = redisTemplate.getExpire(RedisConstant.COMPLAINT_COOLDOWN + user.getId());
                LOG.info("redis key for cooldown" );
                return ResponseUtil.getFailureResponse(
                        "Please wait " + remainingSeconds + " seconds before submitting another complaint."
                );
            }
            if(RedisHelper.isComplaintLimitExceeded(user, redisTemplate)){
                return  ResponseUtil.getFailureResponse("You can only register 4 complaints per day.");
            }
        } catch (Exception e) {
            LOG.error("Redis unavailable, skipping cooldown");
        }



        Category category = categoryRepository.findByUniqueId(createComplaint.getCategoryId());


        ComplaintStatus complaintStatus = complainStatusRepository.findByName(ComplaintStatusConstant.NEW.getName());
        if (complaintStatus == null) {
            LOG.info("ComplaintStatus Not Found");
            return ResponseUtil.getFailureResponse("ComplaintStatus Not Found");
        }

        Complaint complaint = complaintMapper.mapToComplaint(createComplaint, complaintStatus, category, user, phots);
        Escalation escalation = resolveEscalation(complaint.getMunicipality(), complaint.getCategory());
        complaint.setEscalation(escalation); // null-safe: sets null if no rule found
        complaintRepository.save(complaint);

        ComplaintCoordinates complaintCoordinates = new ComplaintCoordinates();
        complaintCoordinates.setLongitude(createComplaint.getComplaintCoordinates().getLongitude());
        complaintCoordinates.setLatitude(createComplaint.getComplaintCoordinates().getLatitude());
        complaintCoordinates.setComplaint(complaint);

        complaintCoordinatesRepository.save(complaintCoordinates);


        return ResponseUtil.getSuccessfulApiResponse("Complaint register successfully");
    }

    @Override
    public ApiResponse<?> listMyComplaints(SearchParam searchParam, Principal loggedInUser) {

        // 1. Find logged-in user
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(loggedInUser.getName()));

        if (userOpt.isEmpty()) {
            LOG.error("Failed to find user by email {}", loggedInUser.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        User user = userOpt.get();
        Long userId = user.getId();

        SearchResponseWithMapperBuilder<Complaint, ListComplainsResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<Complaint, ListComplainsResponse>builder()
                        .count(param -> myComplaintSearchRepository.count(param, userId))
                        .searchData(param -> myComplaintSearchRepository.getAll(param, userId))
                        .mapperFunction(complaintMapper::listMyComplainsResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListComplainsResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        if (response == null) {
            return ResponseUtil.getFailureResponse("No complaints found.");
        }

        return ResponseUtil.getSuccessfulApiResponse(response, "My complaints listed successfully.");
    }



    @Override
    public  ApiResponse<?> updateComplaint(UpdateComplaintRequest updateComplaintRequest, MultipartFile photos,  Principal loggedUser, HttpServletRequest httpServletRequest )throws IOException{
      User user = userRepository.findByEmail(loggedUser.getName());
      Complaint complaint = complaintRepository.findByUniqueId(updateComplaintRequest.getComplaintUniqueId());
      if(complaint == null){
          LOG.info("Complaint Not Found with ID {}", updateComplaintRequest.getComplaintUniqueId());
          return ResponseUtil.getFailureResponse("Complaint Not Found");
      }
      if(!complaint.getReportedBy().getId().equals(user.getId())){
          return ResponseUtil.getFailureResponse("You are not allowed to update this complaint");
      }

      if (updateComplaintRequest.getCategoryId() != null) {
          Category category = categoryRepository
                  .findByUniqueId(updateComplaintRequest.getCategoryId());

          if (category == null) {
              return ResponseUtil.getFailureResponse("Category Not Found");
          }
          complaint.setCategory(category);
      }

      if (updateComplaintRequest.getMunicipality() != null) {
          Optional<AdministrativeUnit> municipality = municipalityRepository
                  .findByUniqueId(updateComplaintRequest.getMunicipality());

          if (municipality.isEmpty()) {
              return ResponseUtil.getFailureResponse("Municipality Not Found");
          }
          complaint.setMunicipality(municipality.get());
      }
      Complaint update = complaintMapper.mapToUpdateComplaint(updateComplaintRequest,photos,complaint);
      complaintRepository.save(update);
      return ResponseUtil.getSuccessfulApiResponse("Complaint update successfully");

  }

    private Escalation resolveEscalation(AdministrativeUnit municipality, Category category) {
        // 1. Try exact match first (category + municipality)
        return escalationRepository
                .findByMunicipalityAndCategoryAndActiveTrue(municipality, category)
                // 2. Fallback to municipality-wide rule
                .or(() -> escalationRepository
                        .findByMunicipalityAndCategoryIsNullAndActiveTrue(municipality))
                // 3. No rule found — complaint proceeds without escalation
                .orElse(null);
    }


}
