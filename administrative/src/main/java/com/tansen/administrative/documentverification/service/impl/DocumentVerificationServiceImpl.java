package com.tansen.administrative.documentverification.service.impl;

import com.tansen.administrative.documentverification.dto.RejectUserDocumentRequest;
import com.tansen.administrative.documentverification.dto.VerifyUserDocumentRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.User;
import com.tansen.entity.UserDocuments;
import com.tansen.administrative.documentverification.dto.UserDocumentResponseDto;
import com.tansen.administrative.documentverification.dto.ViewUserDocumentUniqueId;
import com.tansen.administrative.documentverification.mapper.UserDocumentMapper;
import com.tansen.administrative.documentverification.service.DocumentVerificationService;
import com.tansen.entity.enums.DocumentVerificationStatus;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.UserDocumentsRepository;
import com.tansen.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {


    private final AuthorityUserRepository authorityUserRepository;
    private final UserDocumentsRepository userDocumentsRepository;
    private final UserDocumentMapper userDocumentMapper;
    private final UserRepository userRepository;

    public DocumentVerificationServiceImpl(AuthorityUserRepository authorityUserRepository, UserDocumentsRepository userDocumentsRepository, UserDocumentMapper userDocumentMapper, UserRepository userRepository) {
        this.authorityUserRepository = authorityUserRepository;
        this.userDocumentsRepository = userDocumentsRepository;
        this.userDocumentMapper = userDocumentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<?> viewUserDocument(ViewUserDocumentUniqueId request,
                                           Principal loggedInAdmin){

        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("Logged in Admin Not Found");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit adminMunicipality = authorityUser.getMunicipality();

        // 1. find user first
        Optional<User> userOpt = userRepository
                .findByUniqueId(request.getUniqueId());

        if (userOpt.isEmpty()){
            return ResponseUtil.getFailureResponse("User not found");
        }

        User user = userOpt.get();

        // 2. municipality check
        if (!user.getMunicipality().getId()
                .equals(adminMunicipality.getId())) {

            return ResponseUtil.getFailureResponse(
                    "You are not allowed to view this user document");
        }

        // 3. find document by user
        Optional<UserDocuments> userDocumentsOpt =
                userDocumentsRepository.findByUser(user);

        if (userDocumentsOpt.isEmpty()){
            return ResponseUtil.getFailureResponse("User document not found");
        }

        UserDocuments userDocuments = userDocumentsOpt.get();

        // 4. map response
        UserDocumentResponseDto response =
                userDocumentMapper.entityToUserDocument(userDocuments);

        return ResponseUtil.getSuccessfulApiResponse(
                response,"User Document Viewed");
    }


    @Override
    public ApiResponse<?> verifyUserDocument(VerifyUserDocumentRequest request,
                                             Principal loggedInAdmin) {
        // 1. Validate admin
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("Logged in Admin Not Found");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit adminMunicipality = authorityUser.getMunicipality();

        // 2. Find user
        Optional<User> userOpt = userRepository.findByUniqueId(request.getUniqueId());

        if (userOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("User not found");
        }

        User user = userOpt.get();

        // 3. Municipality check
        if (!user.getMunicipality().getId().equals(adminMunicipality.getId())) {
            return ResponseUtil.getFailureResponse(
                    "You are not allowed to verify this user document");
        }

        // 4. Find document
        Optional<UserDocuments> userDocumentsOpt =
                userDocumentsRepository.findByUser(user);

        if (userDocumentsOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("User document not found");
        }

        UserDocuments userDocuments = userDocumentsOpt.get();

        // 5. Guard: already verified
        if (Boolean.TRUE.equals(userDocuments.getIsDocumentVerified())) {
            return ResponseUtil.getFailureResponse("User document is already verified");
        }

        // 6. Update document
        userDocuments.setIsDocumentVerified(true);
        userDocuments.setVerificationStatus(DocumentVerificationStatus.VERIFIED);
        userDocuments.setRejectionCategory(null);
        userDocuments.setRejectionReason(null);
        userDocuments.setUpdatedAt(LocalDateTime.now());
        userDocumentsRepository.save(userDocuments);

        // 7. Mark user as verified
        user.setIsUserVerified(true);
        userRepository.save(user);

        return ResponseUtil.getSuccessfulApiResponse(null, "User document verified successfully");
    }

    @Override
    public ApiResponse<?> rejectUserDocument(RejectUserDocumentRequest request,
                                             Principal loggedInAdmin) {

        // 1. Validate admin
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("Logged in Admin Not Found");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit adminMunicipality = authorityUser.getMunicipality();

        // 2. Find user
        Optional<User> userOpt = userRepository.findByUniqueId(request.getUniqueId());

        if (userOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("User not found");
        }

        User user = userOpt.get();

        // 3. Municipality check
        if (!user.getMunicipality().getId().equals(adminMunicipality.getId())) {
            return ResponseUtil.getFailureResponse(
                    "You are not allowed to reject this user document");
        }

        // 4. Find document
        Optional<UserDocuments> userDocumentsOpt =
                userDocumentsRepository.findByUser(user);

        if (userDocumentsOpt.isEmpty()) {
            return ResponseUtil.getFailureResponse("User document not found");
        }

        UserDocuments userDocuments = userDocumentsOpt.get();

        // 5. Guard: already rejected with same reason (optional but clean)
        if (DocumentVerificationStatus.REJECTED.equals(userDocuments.getVerificationStatus())
                && request.getRejectionCategory().equals(userDocuments.getRejectionCategory())) {
            return ResponseUtil.getFailureResponse("User document is already rejected with the same category");
        }

        // 6. Update document
        userDocuments.setIsDocumentVerified(false);
        userDocuments.setVerificationStatus(DocumentVerificationStatus.REJECTED);
        userDocuments.setRejectionCategory(request.getRejectionCategory());
        userDocuments.setRejectionReason(request.getRejectionReason());
        userDocuments.setUpdatedAt(LocalDateTime.now());
        userDocumentsRepository.save(userDocuments);

        // 7. Ensure user is marked unverified
        user.setIsUserVerified(false);
        userRepository.save(user);

        return ResponseUtil.getSuccessfulApiResponse(null, "User document rejected successfully");
    }

 }
