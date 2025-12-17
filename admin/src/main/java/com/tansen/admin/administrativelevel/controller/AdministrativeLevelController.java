package com.tansen.admin.administrativelevel.controller;

import com.tansen.admin.administrativelevel.dto.ListDistrictRequest;
import com.tansen.admin.administrativelevel.dto.ListLocalLevelRequest;
import com.tansen.admin.administrativelevel.dto.ListWardRequest;
import com.tansen.admin.administrativelevel.service.AdministrativeLevelService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class AdministrativeLevelController {
    private final AdministrativeLevelService administrativeLevelService;

    public AdministrativeLevelController(AdministrativeLevelService administrativeLevelService) {
        this.administrativeLevelService = administrativeLevelService;
    }

    @GetMapping(ApiConstant.LIST+ApiConstant.SLASH+ApiConstant.PROVINCE)
    public ApiResponse<?> listProvinces() {
        return administrativeLevelService.listProvinces();
    }

    @PostMapping(ApiConstant.LIST+ApiConstant.SLASH+ApiConstant.DISTRICT)
    public ApiResponse<?> listDistricts(@RequestBody @Valid ListDistrictRequest request) {
        return administrativeLevelService.listDistricts(request);
    }

    @PostMapping(ApiConstant.LIST+ApiConstant.SLASH+ApiConstant.LOCAL_LEVEL)
    public ApiResponse<?> listLocalLevels(@RequestBody @Valid ListLocalLevelRequest request) {
        return administrativeLevelService.listLocalLevels(request);
    }

    @PostMapping(ApiConstant.LIST+ApiConstant.SLASH+ApiConstant.WARDS)
    public ApiResponse<?> listWards(@RequestBody @Valid ListWardRequest request) {
        return administrativeLevelService.listWards(request);
    }
}
