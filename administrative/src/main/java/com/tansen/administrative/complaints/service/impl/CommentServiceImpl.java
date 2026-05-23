package com.tansen.administrative.complaints.service.impl;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Comment;
import com.tansen.administrative.complaints.dto.response.ListCommentResponse;
import com.tansen.administrative.complaints.mapper.CommentMapper;
import com.tansen.administrative.complaints.service.CommentService;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final AuthorityUserRepository authorityUserRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(AuthorityUserRepository authorityUserRepository, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.authorityUserRepository = authorityUserRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public ApiResponse<?> listAllComment(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin){
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());
        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        AdministrativeUnit administrativeUnit = authorityUser.getMunicipality();

        if (administrativeUnit == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any administrativeUnit.");
        }
        Long municipalityId = administrativeUnit.getId();
        List<Comment> comments = commentRepository.findAllByComplaintUniqueIdAndMunicipality(complaintUniqueIdDto.getComplaintUniqueId(), municipalityId);

        List<ListCommentResponse> responses =    commentMapper.listAllComment(comments);
        return ResponseUtil.getSuccessfulApiResponse(responses,"Comments listed.");
    }

}
