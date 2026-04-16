package com.tansen.app.vote.controller;

import com.tansen.app.vote.dto.CastVoteRequest;
import com.tansen.app.vote.dto.ComplaintIdRequest;
import com.tansen.app.vote.service.VoteService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.USER_API)
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(ApiConstant.VOTE + ApiConstant.SLASH + ApiConstant.CAST)
    public ApiResponse<?> castVote(
            @RequestBody @Valid CastVoteRequest  complaintIdRequest,
            Principal loggedInUser) {
        return voteService.castVote(complaintIdRequest, loggedInUser);
    }

    @PostMapping(ApiConstant.VOTE + ApiConstant.SLASH + ApiConstant.REMOVE)
    public ApiResponse<?> removeVote(
            @RequestBody @Valid ComplaintIdRequest removeComplaintRequest,
            Principal loggedInUser) {
        return voteService.removeVote(removeComplaintRequest, loggedInUser);
    }

    @PostMapping(ApiConstant.VOTE + ApiConstant.SLASH + ApiConstant.STATUS)
    public ApiResponse<?> getVoteStatus(
            @RequestBody @Valid ComplaintIdRequest voteStatus,
            Principal loggedInUser) {
        return voteService.getVoteStatus(voteStatus, loggedInUser);
    }


}