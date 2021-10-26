package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.repository.WardRepository;
import vn.edu.fpt.traffic_license.response.ResponseFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ward")
public class WardController {

    private final WardRepository wardRepository;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<Object> getWardsOfProvince(@RequestParam("province-id") Long provinceId) {
        try {
            return responseFactory.success(wardRepository.findByProvinceId(provinceId));
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
