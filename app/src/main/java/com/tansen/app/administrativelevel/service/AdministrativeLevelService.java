package com.tansen.app.administrativelevel.service;


import com.tansen.app.administrativelevel.dto.ListDistrictRequest;
import com.tansen.app.administrativelevel.dto.ListLocalLevelRequest;
import com.tansen.app.administrativelevel.dto.ListWardRequest;
import com.tansen.common.dto.ApiResponse;

public interface AdministrativeLevelService {
    ApiResponse<?> listProvinces();
    ApiResponse<?> listDistricts(ListDistrictRequest request);
    ApiResponse<?> listLocalLevels(ListLocalLevelRequest request);
    ApiResponse<?> listWards(ListWardRequest request);
}
