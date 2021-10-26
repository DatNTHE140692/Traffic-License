package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.repository.specification.PagingOptionDto;
import vn.edu.fpt.traffic_license.request.UserRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.UserServices;

@RestController
@RequestMapping("/management/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<Object> getUsers(PagingOptionDto pagingOptionDto,
                                           @RequestParam(required = false, name = "name") String name,
                                           @RequestParam(required = false, name = "username") String username,
                                           @RequestParam(required = false, name = "identification_no") String identificationNo,
                                           @RequestParam(required = false, name = "address") String address,
                                           @RequestParam(required = false, name = "ward_id") Long wardId,
                                           @RequestParam(required = false, name = "province_id") Long provinceId,
                                           @RequestParam(required = false, name = "city_id") Long cityId,
                                           @RequestParam(required = false, name = "granted") Boolean granted) {
        try {
            Pageable pageable = pagingOptionDto.createPageable(pagingOptionDto.getPage(), pagingOptionDto.getLimit(), null);
            return userServices.getUsers(
                    pageable,
                    name,
                    username,
                    identificationNo,
                    address,
                    wardId,
                    provinceId,
                    cityId,
                    granted
            );
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Object> getUserInfo(@RequestBody UserRequest userRequest) {
        try {
            return userServices.getUserInfo(userRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping
    public ResponseEntity<Object> updateInfo(@RequestBody UserRequest userRequest) {
        try {
            return userServices.updateUserInfo(userRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
