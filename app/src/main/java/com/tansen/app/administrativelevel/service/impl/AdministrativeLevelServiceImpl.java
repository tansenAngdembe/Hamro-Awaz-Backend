package com.tansen.app.administrativelevel.service.impl;

import com.tansen.app.administrativelevel.dto.*;
import com.tansen.app.administrativelevel.service.AdministrativeLevelService;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.District;
import com.tansen.entity.LocalLevel;
import com.tansen.entity.Province;
import com.tansen.repository.DistrictRepository;
import com.tansen.repository.LocalLevelRepository;
import com.tansen.repository.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class AdministrativeLevelServiceImpl implements AdministrativeLevelService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final LocalLevelRepository localLevelRepository;

    public AdministrativeLevelServiceImpl(ProvinceRepository provinceRepository, DistrictRepository districtRepository, LocalLevelRepository localLevelRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.localLevelRepository = localLevelRepository;
    }

    @Override
    public ApiResponse<?> listProvinces() {
        List<Province> provinceList = provinceRepository.findAll();
        return ResponseUtil.getSuccessfulApiResponse(provinceList,"Province Listed Successfully" );
    }

    @Override
    public ApiResponse<?> listDistricts(ListDistrictRequest request) {
        List<District> districtList = districtRepository.findDistrictsByProvinceId(request.getProvinceId());
        List<ListDistrictResponse> response = districtList.stream().map(district -> {
            ListDistrictResponse dto = new ListDistrictResponse();
            dto.setId(district.getId());
            dto.setDistrictName(district.getDistrictName());
            return dto;
        }).toList();
        return ResponseUtil.getSuccessfulApiResponse(response,"District Listed Successfully");
    }

    @Override
    public ApiResponse<?> listLocalLevels(ListLocalLevelRequest request) {
        List<LocalLevel> localLevelList = localLevelRepository.findLocalLevelsByDistrictId(request.getDistrictId());
        List<ListLocalLevelResponse> response = localLevelList.stream().map(localLevel -> {
            ListLocalLevelResponse dto = new ListLocalLevelResponse();
            dto.setId(localLevel.getId());
            dto.setLocalLevel(localLevel.getLocalLevel());
            dto.setLocalLevelCode(localLevel.getLocalLevelCode());
            dto.setTotalWards(localLevel.getTotalWards());
            return dto;
        }).toList();
        return ResponseUtil.getSuccessfulApiResponse(response,"Local Levels Listed Successfully");
    }

    @Override
    public ApiResponse<?> listWards(ListWardRequest request) {
        Optional<LocalLevel> localLevel = localLevelRepository.findById(request.getLocalLevelId());
        if(localLevel.isEmpty()){
            return ResponseUtil.getSuccessfulApiResponse("Local Level Not Found");
        }
        Integer totalWards = localLevel.get().getTotalWards();
        List<ListWardResponse> wardList = IntStream.range(1, totalWards+1).mapToObj(i -> {
            ListWardResponse response = new ListWardResponse();
            response.setWard(i);
            return response;
        }).toList();
        return ResponseUtil.getSuccessfulApiResponse(wardList,"Wards Listed Successfully");
    }
}
