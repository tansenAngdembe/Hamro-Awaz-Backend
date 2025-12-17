package com.tansen.admin.administrativelevel.service;


import com.tansen.admin.administrativelevel.dto.ListDistrictRequest;
import com.tansen.admin.administrativelevel.dto.ListLocalLevelRequest;
import com.tansen.admin.administrativelevel.dto.ListWardRequest;
import com.tansen.common.dto.ApiResponse;

public interface AdministrativeLevelService {
    ApiResponse<?> listProvinces();
    ApiResponse<?> listDistricts(ListDistrictRequest request);
    ApiResponse<?> listLocalLevels(ListLocalLevelRequest request);
    ApiResponse<?> listWards(ListWardRequest request);
}
