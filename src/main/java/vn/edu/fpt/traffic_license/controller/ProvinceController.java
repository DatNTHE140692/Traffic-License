package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.repository.ProvinceRepository;
import vn.edu.fpt.traffic_license.response.ResponseFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/province")
public class ProvinceController {

    private final ProvinceRepository provinceRepository;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<Object> getProvincesOfCity(@RequestParam("city-id") Long cityId) {
        try {
            return responseFactory.success(provinceRepository.findByCityId(cityId));
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
