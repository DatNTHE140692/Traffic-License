package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.UserServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserServices userServices;
    private final ResponseFactory responseFactory;

    @GetMapping("/is-user-existed")
    public ResponseEntity<Object> isUserExisted(@RequestParam(name = "username") String username) {
        try {
            return userServices.isUserExisted(username);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
