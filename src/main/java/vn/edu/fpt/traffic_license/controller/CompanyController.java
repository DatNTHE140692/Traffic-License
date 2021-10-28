package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.repository.specification.PagingOptionDto;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.CompanyServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyServices companyServices;
    private final ResponseFactory responseFactory;

    @GetMapping("/get-companies")
    public ResponseEntity<Object> getCompaniesByLocation(@RequestParam("ward_id") Long wardId,
                                                         @RequestParam("province_id") Long provinceId,
                                                         @RequestParam("city_id") Long cityId) {
        try {
            return companyServices.getCompaniesByLocation(wardId, provinceId, cityId);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getCompanies(PagingOptionDto pagingOptionDto,
                                               @RequestParam(name = "name", required = false) String name,
                                               @RequestParam(name = "license_no", required = false) String licenseNo,
                                               @RequestParam(name = "address", required = false) String address,
                                               @RequestParam(name = "ward_id", required = false) Long wardId,
                                               @RequestParam(name = "province_id", required = false) Long provinceId,
                                               @RequestParam(name = "city_id", required = false) Long cityId,
                                               @RequestParam(name = "active", required = false) Boolean active) {
        try {
            Pageable pageable = pagingOptionDto.createPageable(pagingOptionDto.getPage(), pagingOptionDto.getLimit(), null);
            return companyServices.getCompanies(
                    pageable,
                    name,
                    licenseNo,
                    address,
                    wardId,
                    provinceId,
                    cityId,
                    active
            );
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }
}
