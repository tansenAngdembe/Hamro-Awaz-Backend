package com.tansen.app.service.impl;

import com.tansen.app.dto.request.UploadDocumentRequest;
import com.tansen.app.mapper.UserDocumentMapper;
import com.tansen.app.service.UserDocumentService;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.service.UploadFileService;
import com.tansen.entity.*;
import com.tansen.entity.enums.DocumentVerificationStatus;
import com.tansen.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDocumentServiceImpl implements UserDocumentService {


    private static final Logger LOG = LoggerFactory.getLogger(UserDocumentServiceImpl.class);

    private final UserDocumentsRepository userDocumentsRepository;
    private final AdministrativeUnitRepository municipalityRepository;
    private final UserRepository userRepository;
    private final UserDocumentMapper userDocumentMapper;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    private final UploadFileService uploadFileService;

    public UserDocumentServiceImpl(UserDocumentsRepository userDocumentsRepository, AdministrativeUnitRepository municipalityRepository, UserRepository userRepository, UserDocumentMapper userDocumentMapper, ProvinceRepository provinceRepository, DistrictRepository districtRepository, UploadFileService uploadFileService) {
        this.userDocumentsRepository = userDocumentsRepository;
        this.municipalityRepository = municipalityRepository;
        this.userRepository = userRepository;
        this.userDocumentMapper = userDocumentMapper;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.uploadFileService = uploadFileService;
    }

    @Transactional
    @Override
    public ApiResponse<?> uploadDocument(
            UploadDocumentRequest uploadDocumentRequest,
            MultipartFile citizenshipFront,
            MultipartFile citizenshipBack,
            Principal principal,
            HttpServletRequest httpServletRequest) {

        try {

            // 1️⃣ Get Logged-in User
            User user = userRepository.findByEmail(principal.getName());
            if (user == null) {
                return ResponseUtil.getFailureResponse("User not found");
            }

            Province province = provinceRepository
                    .findById(uploadDocumentRequest.getProvinceUniqueId())
                    .orElseThrow(() -> new RuntimeException("Province not found"));

            // 2️⃣ Fetch Location Data
            AdministrativeUnit administrativeUnit = municipalityRepository
                    .findByProvince_Id(uploadDocumentRequest.getProvinceUniqueId())
                    .orElseThrow(() ->
                            new RuntimeException("No administrative unit account found for this province"));

            District district = districtRepository
                    .findById(uploadDocumentRequest.getDistrictUniqueId())
                    .orElseThrow(() -> new RuntimeException("District not found"));

            // 3️⃣ Check if document already exists
            Optional<UserDocuments> existingDoc = userDocumentsRepository.findByUser(user);

            UserDocuments userDocuments;

            if (existingDoc.isPresent()) {
                // 🔥 UPDATE EXISTING DOCUMENT
                userDocuments = existingDoc.get();

                // Update fields
                userDocuments.setNationalIdentityNumber(
                        uploadDocumentRequest.getNationalIdentityNumber());
                userDocuments.setProvince(province);
                if(administrativeUnit != null) {
                    userDocuments.setMunicipality(administrativeUnit);
                }else{
                    userDocuments.setMunicipality(null);
                    return ResponseUtil.getFailureResponse("No administrative unit account found");
                }
                userDocuments.setDistrict(district);
                userDocuments.setUpdatedAt(LocalDateTime.now());
                userDocuments.setVerificationStatus(DocumentVerificationStatus.PENDING);


                // Update files only if provided
                if (citizenshipFront != null && !citizenshipFront.isEmpty()) {
                    userDocuments.setCitizenshipCardFront(
                            uploadFileService.uploadFile(
                                    citizenshipFront,
                                    FilePathConstant.BASE_PATH,
                                    FilePathConstant.CITIZENSHIPCARDFRONT,
                                    true));
                }

                if (citizenshipBack != null && !citizenshipBack.isEmpty()) {
                    userDocuments.setCitizenshipCardBack(
                            uploadFileService.uploadFile(
                                    citizenshipBack,
                                    FilePathConstant.BASE_PATH,
                                    FilePathConstant.CITIZENSHIPCARDBACK,
                                    true));
                }

            } else {

                if (citizenshipFront == null || citizenshipFront.isEmpty()
                        || citizenshipBack == null || citizenshipBack.isEmpty()) {

                    return ResponseUtil.getFailureResponse("Documents are required");
                }

                userDocuments = userDocumentMapper.mapToUserDocuments(
                        uploadDocumentRequest, citizenshipFront, citizenshipBack);

                userDocuments.setUser(user);
                userDocuments.setProvince(province);
                if(administrativeUnit != null) {
                    userDocuments.setMunicipality(administrativeUnit);
                }else{
                    userDocuments.setMunicipality(null);
                    return ResponseUtil.getFailureResponse("No administrative unit account found");
                }                userDocuments.setDistrict(district);
                userDocuments.setVerificationStatus(DocumentVerificationStatus.PENDING);
            }

            userDocumentsRepository.save(userDocuments);

            user.setMunicipality(administrativeUnit);
            userRepository.save(user);

            return ResponseUtil.getSuccessfulApiResponse("Document uploaded successfully. Wait for verification.");

        } catch (Exception e) {
            return ResponseUtil.getFailureResponse("Upload failed: " + e.getMessage());
        }
    }

//    @Transactional
//    @Override
//    public ApiResponse<?> resubmitDocument(
//            UploadDocumentRequest uploadDocumentRequest,
//            MultipartFile citizenshipFront,
//            MultipartFile citizenshipBack,
//            Principal principal) {
//
//        try {
//
//            //  Get Logged-in User
//            User user = userRepository.findByEmail(principal.getName());
//            if (user == null) {
//                return ResponseUtil.getFailureResponse("User not found");
//            }
//
//            // Find Existing Document
//            UserDocuments userDocuments = userDocumentsRepository
//                    .findByUser(user)
//                    .orElseThrow(() ->
//                            new RuntimeException("No document found to resubmit"));
//
//            // Validate Files (Required for Resubmit)
//            if (citizenshipFront == null || citizenshipFront.isEmpty()
//                    || citizenshipBack == null || citizenshipBack.isEmpty()) {
//
//                return ResponseUtil.getFailureResponse(
//                        "Both citizenship front and back are required for resubmission");
//            }
//
//            // Replace Files (Optional: delete old files if stored physically)
//            String newFront = uploadFileService.uploadFile(
//                    citizenshipFront,
//                    FilePathConstant.BASE_PATH,
//                    FilePathConstant.CITIZENSHIPCARDFRONT,
//                    true);
//
//            String newBack = uploadFileService.uploadFile(
//                    citizenshipBack,
//                    FilePathConstant.BASE_PATH,
//                    FilePathConstant.CITIZENSHIPCARDBACK,
//                    true);
//
//            userDocuments.setCitizenshipCardFront(newFront);
//            userDocuments.setCitizenshipCardBack(newBack);
//
//            //  Update Basic Fields
//            userDocuments.setNationalIdentityNumber(
//                    uploadDocumentRequest.getNationalIdentityNumber());
//
//            userDocuments.setUpdatedAt(LocalDateTime.now());
//
//            // Reset Verification Status
//            userDocuments.setVerificationStatus(DocumentVerificationStatus.PENDING);
//
//            // Optional: Clear rejection reason
////            userDocuments.setRejectionReason(null);
//
//            userDocumentsRepository.save(userDocuments);
//
//            return ResponseUtil.getSuccessfulApiResponse(
//                    "Document resubmitted successfully");
//
//        } catch (Exception e) {
//            return ResponseUtil.getFailureResponse(
//                    "Resubmission failed: " + e.getMessage());
//        }
//    }


}
