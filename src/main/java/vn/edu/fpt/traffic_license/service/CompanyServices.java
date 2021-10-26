package vn.edu.fpt.traffic_license.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.entities.City;
import vn.edu.fpt.traffic_license.entities.Company;
import vn.edu.fpt.traffic_license.entities.Ward;
import vn.edu.fpt.traffic_license.entities.Province;
import vn.edu.fpt.traffic_license.repository.CityRepository;
import vn.edu.fpt.traffic_license.repository.CompanyRepository;
import vn.edu.fpt.traffic_license.repository.WardRepository;
import vn.edu.fpt.traffic_license.repository.ProvinceRepository;
import vn.edu.fpt.traffic_license.request.CompanyRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.utils.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServices {

    private final CompanyRepository companyRepository;
    private final ResponseFactory responseFactory;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;

    public ResponseEntity<Object> create(CompanyRequest companyRequest) {
        Company company = companyRepository.findByLicenseNo(companyRequest.getLicenseNo());
        if (company != null) {
            return responseFactory.fail("Công ty đã tồn tại.", ResponseStatusCodeConst.DUPLICATE_ERROR);
        }

        Optional<Ward> wardOptional = wardRepository.findById(companyRequest.getWardId());
        if (!wardOptional.isPresent()) {
            return responseFactory.fail("Phường/Xã không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Ward ward = wardOptional.get();

        Optional<Province> provinceOptional = provinceRepository.findById(companyRequest.getProvinceId());
        if (!provinceOptional.isPresent()) {
            return responseFactory.fail("Quận/Huyện không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Province province = provinceOptional.get();

        Optional<City> cityOptional = cityRepository.findById(companyRequest.getCityId());
        if (!cityOptional.isPresent()) {
            return responseFactory.fail("Tỉnh/Thành phố không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        City city = cityOptional.get();

        company = Company.builder()
                .name(companyRequest.getName())
                .wardId(ward.getId())
                .provinceId(province.getId())
                .cityId(city.getId())
                .active(companyRequest.isActive())
                .build();
        companyRepository.save(company);
        return responseFactory.success("Thành công");
    }

    public ResponseEntity<Object> update(CompanyRequest companyRequest) {
        Optional<Company> companyOptional = companyRepository.findById(companyRequest.getId());
        if (!companyOptional.isPresent()) {
            return responseFactory.fail("Công ty không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (StringUtils.isBlank(companyRequest.getName())) {
            return responseFactory.fail("Tên công ty không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (StringUtils.isBlank(companyRequest.getLicenseNo())) {
            return responseFactory.fail("Giấy phép kinh doanh không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (companyRequest.getWardId() == null) {
            return responseFactory.fail("Phường/Xã không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (companyRequest.getProvinceId() == null) {
            return responseFactory.fail("Quận/Huyện không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (companyRequest.getCityId() == null) {
            return responseFactory.fail("Tỉnh/Thành phố không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        Company company = companyOptional.get();
        if (company.getName().equals(companyRequest.getName().trim())) {
            company.setName(companyRequest.getName().trim());
        }

        if (company.getLicenseNo().equals(companyRequest.getLicenseNo().trim())) {
            company.setLicenseNo(companyRequest.getLicenseNo().trim());
        }

        if (company.getWardId().longValue() != companyRequest.getWardId()) {
            company.setWardId(companyRequest.getWardId());
        }

        if (company.getProvinceId().longValue() != companyRequest.getProvinceId()) {
            company.setProvinceId(companyRequest.getProvinceId());
        }

        if (company.getCityId().longValue() != companyRequest.getCityId()) {
            company.setCityId(companyRequest.getCityId());
        }

        if (!company.getActive().equals(companyRequest.isActive())) {
            company.setActive(companyRequest.isActive());
        }

        companyRepository.save(company);

        return responseFactory.success("Thành công");
    }

}
