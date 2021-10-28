package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.request.CompanyRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.CompanyServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management/company")
public class CompanyManagementController {

    private final CompanyServices companyServices;
    private final ResponseFactory responseFactory;

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
