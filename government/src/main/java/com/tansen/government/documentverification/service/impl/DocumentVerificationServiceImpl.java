package com.tansen.government.documentverification.service.impl;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.dto.SearchParam;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Municipality;
import com.tansen.entity.User;
import com.tansen.entity.UserDocuments;
import com.tansen.government.documentverification.dto.UserDocumentResponseDto;
import com.tansen.government.documentverification.dto.ViewUserDocumentUniqueId;
import com.tansen.government.documentverification.mapper.UserDocumentMapper;
import com.tansen.government.documentverification.service.DocumentVerificationService;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.UserDocumentsRepository;
import com.tansen.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        Municipality adminMunicipality = authorityUser.getMunicipality();

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

 }
