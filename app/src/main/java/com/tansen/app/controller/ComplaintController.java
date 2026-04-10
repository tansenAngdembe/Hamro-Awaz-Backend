package com.tansen.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.NearByComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.app.service.ComplaintService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RequestMapping(path = ApiConstant.USER_API + ApiConstant.SLASH +  ApiConstant.COMPLAINT)
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping(value = ApiConstant.CREATE)
    public ApiResponse<?> createComplaint(
            @Valid @RequestPart("data") CreateComplaintRequest createComplaint,
//            @RequestPart(value = "photos", required = false) MultipartFile photos,
            Principal loggedUser,
            HttpServletRequest httpServletRequest) throws IOException {

        return complaintService.createComplaint(createComplaint, loggedUser, httpServletRequest);
    }
//    @PostMapping(ApiConstant.CREATE)
//    public ApiResponse<?> createComplaint(
//            @Valid @RequestPart("data") String createComplaintJson,
//            @RequestPart(value = "photos", required = false) MultipartFile photos,
//            Principal loggedUser,
//            HttpServletRequest httpServletRequest) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        CreateComplaintRequest createComplaint =
//                mapper.readValue(createComplaintJson, CreateComplaintRequest.class);
//
//        return complaintService.createComplaint(createComplaint, photos, loggedUser, httpServletRequest);
//    }
    @PostMapping(ApiConstant.UPDATE)
   public  ApiResponse<?> updateComplaint(@RequestBody UpdateComplaintRequest updateComplaintRequest, MultipartFile photos, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException{
       return  complaintService.updateComplaint(updateComplaintRequest,photos,loggedUser,httpServletRequest);
   }
   @PostMapping(ApiConstant.LIST + ApiConstant.SLASH + ApiConstant.NEARBY)
    public ApiResponse<?> listNearByComplains(
           SearchParam searchParam,
           @RequestBody NearByComplaintRequest nearByComplaintRequest
   ) throws JsonProcessingException{
        return complaintService.listNearByComplains(searchParam,nearByComplaintRequest);
   }

}
