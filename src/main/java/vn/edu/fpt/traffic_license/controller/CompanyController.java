package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.repository.specification.PagingOptionDto;
import vn.edu.fpt.traffic_license.request.CompanyRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.CompanyServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management/company")
public class CompanyController {

    private final CompanyServices companyServices;
    private final ResponseFactory responseFactory;

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

    @PutMapping
    public ResponseEntity<Object> create(@RequestBody CompanyRequest companyRequest) {
        try {
            return companyServices.create(companyRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping
    public ResponseEntity<Object> update(@RequestBody CompanyRequest companyRequest) {
        try {
            return companyServices.update(companyRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
