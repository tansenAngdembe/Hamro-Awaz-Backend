package com.tansen.admin.government.mapper;

import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.Municipality;
import com.tansen.repository.DistrictRepository;
import com.tansen.repository.LocalLevelRepository;
import com.tansen.repository.ProvinceRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MunicipalityMapper {
    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityMapper.class);
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final LocalLevelRepository localLevelRepository;
    private final UploadFileService uploadFileService;

    public MunicipalityMapper(ProvinceRepository provinceRepository, DistrictRepository districtRepository, LocalLevelRepository localLevelRepository, UploadFileService uploadFileService) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.localLevelRepository = localLevelRepository;
        this.uploadFileService = uploadFileService;
    }


    public Municipality mapToMunicipality(CreateMunicipalityRequest createMunicipalityRequest, MultipartFile documentFile)throws IOException {
        Municipality  municipality = new Municipality();
        municipality.setGovernmentName(createMunicipalityRequest.getGovernmentName());
        municipality.setEmail(createMunicipalityRequest.getEmail());
        municipality.setCode(createMunicipalityRequest.getCode());
        municipality.setDescription(createMunicipalityRequest.getDescription());
        municipality.setUniqueId(UuidUtil.generateUuid());
        municipality.setProvince(provinceRepository.findById(Long.valueOf(createMunicipalityRequest.getProvinceId())).orElseThrow(
                ()-> new RuntimeException("Province now found with id {}" + createMunicipalityRequest.getProvinceId())));
        municipality.setDistrict(districtRepository.findById(Long.valueOf(createMunicipalityRequest.getDistrictId())).orElseThrow(
                ()-> new RuntimeException("District not found with id {}" +  createMunicipalityRequest.getDistrictId())));
        municipality.setLocalLevel(localLevelRepository.findById(Long.valueOf(createMunicipalityRequest.getLocalLevelId())).orElseThrow(
                ()-> new RuntimeException("Locallevel repository not  found with id {}" + createMunicipalityRequest.getLocalLevelId())));
        municipality.setWardNumber(createMunicipalityRequest.getWardNumber());
        municipality.setLatitude(createMunicipalityRequest.getLatitude());
        municipality.setLongitude(createMunicipalityRequest.getLongitude());
        municipality.setAddress(createMunicipalityRequest.getAddress());
        if(documentFile != null) {
            municipality.setDocumentUrl(uploadFileService.uploadFile(documentFile, FilePathConstant.BASE_PATH,FilePathConstant.MUNICIPALITY,true));
        }
        return municipality;

    }
}
