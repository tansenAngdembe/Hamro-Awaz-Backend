package com.tansen.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.app.constant.RedisConstant;
import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.NearByComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.app.dto.response.ListNearByComplainsResponse;
import com.tansen.app.mapper.ComplaintMapper;
import com.tansen.app.service.ComplaintService;
import com.tansen.app.util.redisutil.RedisHelper;
import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.repository.*;
import com.tansen.repository.searchrepo.NearByComplaintSearchRepository;
import com.tansen.repository.searchrepo.impl.NearByComplaintSearchRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.Duration;
import java.util.Optional;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ComplainStatusRepository complainStatusRepository;
    private final ComplaintMapper complaintMapper;
    private final ComplaintRepository complaintRepository;
    private final ComplaintCoordinatesRepository complaintCoordinatesRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final NearByComplaintSearchRepository nearByComplaintSearchRepository;
    private final SearchResponse searchResponse;

    public ComplaintServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, MunicipalityRepository municipalityRepository, ComplainStatusRepository complainStatusRepository, ComplaintMapper complaintMapper, ComplaintRepository complaintRepository, ComplaintCoordinatesRepository complaintCoordinatesRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, NearByComplaintSearchRepository nearByComplaintSearchRepository, SearchResponse searchResponse) {
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
    }
    @Override
    public ApiResponse<?> listNearByComplains(
            SearchParam searchParam,
            NearByComplaintRequest nearByComplaintRequest
    ) throws JsonProcessingException {

            double effectiveRadius = (nearByComplaintRequest.getRadiusKm() == null ||nearByComplaintRequest.getRadiusKm() <= 0)
                ? 12.0
                : nearByComplaintRequest.getRadiusKm();

        // REDIS CACHE KEY
        String cacheKey = RedisHelper.buildNearComplaintCacheKey(
                searchParam,
                nearByComplaintRequest.getLatitude(),
                nearByComplaintRequest.getLongitude(),
                effectiveRadius
        );

        String cachedJson =
                (String) redisTemplate.opsForValue().get(cacheKey);

        if (cachedJson != null) {
            PageableResponse<ListNearByComplainsResponse> cached =
                    objectMapper.readValue(
                            cachedJson,
                            new TypeReference<PageableResponse<ListNearByComplainsResponse>>() {}
                    );

            LOG.info("Complaints fetched from redis for key {}", cacheKey);

            return ResponseUtil.getSuccessfulApiResponse(
                    cached, "Complaints listed successfully");
        }

        SearchResponseWithMapperBuilder<Complaint, ListNearByComplainsResponse> responseBuilder;
            responseBuilder =
                    SearchResponseWithMapperBuilder
                            .<Complaint, ListNearByComplainsResponse>builder()
                            .count(param ->
                                    nearByComplaintSearchRepository.countNearby(
                                            param,
                                            nearByComplaintRequest.getLatitude(),
                                            nearByComplaintRequest.getLongitude(),
                                            effectiveRadius
                                    )
                            )
                            .searchData(param ->
                                    nearByComplaintSearchRepository.findNearby(
                                            param,
                                            nearByComplaintRequest.getLatitude(),
                                            nearByComplaintRequest.getLongitude(),
                                            effectiveRadius
                                    )
                            )
                            .mapperFunction(this.complaintMapper::listNearByComplainsResponses)
                            .searchParam(searchParam)
                            .build();
        PageableResponse<ListNearByComplainsResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        // CACHE RESULT
        redisTemplate.opsForValue().set(
                cacheKey,
                objectMapper.writeValueAsString(response),
                Duration.ofMinutes(2)
        );

        LOG.info("Complaints fetched from DB & cached for key {}", cacheKey);

        return ResponseUtil.getSuccessfulApiResponse(
                response, "Complaints listed successfully");
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
        Optional<Municipality> municipality = municipalityRepository.findByUniqueId(createComplaint.getMunicipalityUniqueId());
        LOG.info("Municipality UniqueId from request: {}", createComplaint.getMunicipalityUniqueId());

        if (municipality.isEmpty()) {
            LOG.info("Municipality Not Found");
            return ResponseUtil.getFailureResponse("Municipality Not Found");
        }
        ComplaintStatus complaintStatus = complainStatusRepository.findByName(ComplaintStatusConstant.NEW.getName());
        if (complaintStatus == null) {
            LOG.info("ComplaintStatus Not Found");
            return ResponseUtil.getFailureResponse("ComplaintStatus Not Found");
        }

        Complaint complaint = complaintMapper.mapToComplaint(createComplaint, complaintStatus, category, municipality.get(), user, phots);
        complaintRepository.save(complaint);

        ComplaintCoordinates complaintCoordinates = new ComplaintCoordinates();
        complaintCoordinates.setLongitude(createComplaint.getComplaintCoordinates().getLongitude());
        complaintCoordinates.setLatitude(createComplaint.getComplaintCoordinates().getLatitude());
        complaintCoordinates.setComplaint(complaint);

        complaintCoordinatesRepository.save(complaintCoordinates);


        return ResponseUtil.getSuccessfulApiResponse("Complaint register successfully");
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
          Optional<Municipality> municipality = municipalityRepository
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

}
