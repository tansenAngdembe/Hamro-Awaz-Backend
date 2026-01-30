package com.tansen.app.service.impl;

import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.app.mapper.ComplaintMapper;
import com.tansen.app.service.ComplaintService;
import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.*;
import com.tansen.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.security.Principal;
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

    public ComplaintServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, MunicipalityRepository municipalityRepository, ComplainStatusRepository complainStatusRepository, ComplaintMapper complaintMapper, ComplaintRepository complaintRepository, ComplaintCoordinatesRepository complaintCoordinatesRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.municipalityRepository = municipalityRepository;
        this.complainStatusRepository = complainStatusRepository;
        this.complaintMapper = complaintMapper;
        this.complaintRepository = complaintRepository;
        this.complaintCoordinatesRepository = complaintCoordinatesRepository;
    }

    @Override
    public ApiResponse<?> createComplaint(CreateComplaintRequest createComplaint, MultipartFile photos,  Principal loggedUser, HttpServletRequest httpServletRequest) throws IOException {
        User user = userRepository.findByEmail(loggedUser.getName());
        if(!user.getIsUserVerified()){
            LOG.error("User is not verified");
            return ResponseUtil.getFailureResponse("To confirm your complaint please upload documentation.");
        }

        Category category = categoryRepository.findByUniqueId(createComplaint.getCategoryId());
        Optional<Municipality> municipality = municipalityRepository.findByUniqueId(createComplaint.getMunicipality());

        if (municipality.isEmpty()) {
            LOG.info("Municipality Not Found");
            return ResponseUtil.getFailureResponse("Municipality Not Found");
        }
        ComplaintStatus complaintStatus = complainStatusRepository.findByName(ComplaintStatusConstant.NEW.getName());
        if (complaintStatus == null) {
            LOG.info("ComplaintStatus Not Found");
            return ResponseUtil.getFailureResponse("ComplaintStatus Not Found");
        }

        Complaint complaint = complaintMapper.mapToComplaint(createComplaint, photos, complaintStatus, category, municipality.get(), user);
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
