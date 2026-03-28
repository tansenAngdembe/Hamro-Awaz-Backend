package com.tansen.app.comment.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.app.comment.dto.CreateCommentRequest;
import com.tansen.app.comment.dto.DeleteCommentRequest;
import com.tansen.app.comment.dto.ListCommentResponse;
import com.tansen.app.comment.dto.UpdateCommentRequest;
import com.tansen.app.comment.mapper.CommentMapper;
import com.tansen.app.comment.service.CommentService;
import com.tansen.app.constant.RedisConstant;
import com.tansen.app.util.redisutil.RedisHelper;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.Comment;
import com.tansen.entity.Complaint;
import com.tansen.entity.User;
import com.tansen.repository.CommentRepository;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final CommentRepository commentRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CommentMapper commentMapper;
    private final ObjectMapper objectMapper;

    public CommentServiceImpl(UserRepository userRepository, ComplaintRepository complaintRepository, CommentRepository commentRepository, RedisTemplate<String, Object> redisTemplate, CommentMapper commentMapper, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.commentRepository = commentRepository;
        this.redisTemplate = redisTemplate;
        this.commentMapper = commentMapper;
        this.objectMapper = objectMapper;
    }
    // Public api
    @Override
    public ApiResponse<?>  getCommentBy(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedUser, HttpServletRequest httpServletRequest) throws IOException {
        String complaintCacheKey = RedisHelper.buildCommentCacheKey(complaintUniqueIdDto.getUniqueId());
        String cachedJson = (String)  redisTemplate.opsForValue().get(complaintCacheKey);
        if (cachedJson != null) {
            List<ListCommentResponse> cached = objectMapper.readValue(
                    complaintCacheKey,
                    new TypeReference<List<ListCommentResponse>>() {}
            );
            LOG.info("Comments fetched from redis for key {}", complaintCacheKey);
            return ResponseUtil.getSuccessfulApiResponse(cached,"Comments listed.");
        }

        List<Comment> comments = commentRepository.findByComplaintUniqueIdAndIsDeleteFalse(complaintUniqueIdDto.getUniqueId());
        if (comments == null || comments.isEmpty()) {
            LOG.error("Comment not found with id {}", complaintUniqueIdDto.getUniqueId());
            return ResponseUtil.getFailureResponse("No Comments on this Complaint.");
        }
        List<ListCommentResponse> responses = commentMapper.listAllComment(comments);
        redisTemplate.opsForValue().set(complaintCacheKey, objectMapper.writeValueAsString(responses), Duration.ofMinutes(1));
        LOG.info("Comments fetched from DB & cache for key {}",complaintCacheKey );
        return ResponseUtil.getSuccessfulApiResponse(responses, "Comments listed.");

    }

    // private logged api
    @Override
    public ApiResponse<?> createComment(CreateCommentRequest createComment, Principal loggedUser, HttpServletRequest httpServletRequest
    ) throws IOException {
        //  Fetch user
        User user = userRepository.findByEmail(loggedUser.getName());
        if (user == null) {
            LOG.error("Logged in user not found: {}", loggedUser.getName());
            return ResponseUtil.getFailureResponse("User not found.");
        }

        //  Verification check
        if (!Boolean.TRUE.equals(user.getIsUserVerified())) {
            LOG.error("User is not verified");
            return ResponseUtil.getFailureResponse(
                    "To confirm your complaint please upload documentation."
            );
        }

        // Validate request
        if (createComment.getMessage() == null || createComment.getMessage().isBlank()) {
            return ResponseUtil.getFailureResponse("Comment message cannot be empty.");
        }

        if (createComment.getComplaintUniqueId() == null) {
            return ResponseUtil.getFailureResponse("Complaint id is required.");
        }

        String complaintId = createComment.getComplaintUniqueId();


        // Fetch complaint
        Complaint complaint = complaintRepository.findByUniqueId(complaintId);
        if (complaint == null) {
            LOG.error("Complaint not found with id {}", complaintId);
            return ResponseUtil.getFailureResponse("Complaint not found.");
        }

        Comment comment = commentMapper.entityToCreateComment(createComment,complaint,user,httpServletRequest);
        commentRepository.save(comment);
        // Invalidate cache for this complaint
        String cacheKey = RedisHelper.buildCommentCacheKey(complaintId);
        redisTemplate.delete(cacheKey);

        LOG.info("Comment created & cache cleared for complaint {}", complaintId);
        return ResponseUtil.getSuccessfulApiResponse("Comment added successfully.");
    }


    @Override
    public ApiResponse<?> updateComment(UpdateCommentRequest updateCommentRequest, Principal loggedUser){
        //  Fetch user
        User user = userRepository.findByEmail(loggedUser.getName());
        if (user == null) {
            LOG.error("Logged in user not found: {}", loggedUser.getName());
            return ResponseUtil.getFailureResponse("User not found.");
        }

        // Validate request
        if (updateCommentRequest.getMessage() == null || updateCommentRequest.getMessage().isBlank()) {
            return ResponseUtil.getFailureResponse("Comment message cannot be empty.");
        }

        if (updateCommentRequest.getComplaintUniqueId() == null) {
            return ResponseUtil.getFailureResponse("Complaint id is required.");
        }

        Long municipalityId = user.getMunicipality().getId();
        Optional<Comment> indComment = commentRepository.findCommentByUniqueIds(
                updateCommentRequest.getCommentUniqueId(),
                updateCommentRequest.getComplaintUniqueId(),
                user.getUniqueId(),
                municipalityId
                );
        if(indComment.isPresent()) {
            commentRepository.save(commentMapper.updateComment(indComment.get(),updateCommentRequest));
            String cacheKey = RedisHelper.buildCommentCacheKey(updateCommentRequest.getCommentUniqueId());
            redisTemplate.delete(cacheKey);
            return ResponseUtil.getSuccessfulApiResponse("Comment updated successfully.");
        }else{
            return ResponseUtil.getFailureResponse("Cannot update comment.");
        }


    }

    @Override
    public ApiResponse<?> deleteComment(DeleteCommentRequest deleteCommentRequest, Principal loggedUser){
        //  Fetch user
        User user = userRepository.findByEmail(loggedUser.getName());
        if (user == null) {
            LOG.error("Logged in user not found: {}", loggedUser.getName());
            return ResponseUtil.getFailureResponse("User not found.");
        }
        Long municipalityId = user.getMunicipality().getId();
        Optional<Comment> delComment = commentRepository.findCommentByUniqueIds(
                deleteCommentRequest.getCommentUniqueId(),
                deleteCommentRequest.getComplaintUniqueId(),
                user.getUniqueId(),
                municipalityId
        );
        if(delComment.isPresent()) {
            delComment.get().setIsDelete(true);
            commentRepository.save(delComment.get());
            String cacheKey = RedisHelper.buildCommentCacheKey(deleteCommentRequest.getCommentUniqueId());
            redisTemplate.delete(cacheKey);
            return ResponseUtil.getSuccessfulApiResponse("Comment deleted successfully.");
        }else {
           return ResponseUtil.getFailureResponse("Cannot delete comment.");
        }

    }

}





