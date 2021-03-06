package vn.edu.fpt.traffic_license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.request.UserRegistrationRequest;
import vn.edu.fpt.traffic_license.model.user.LoginRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.AuthServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServices authServices;
    private final ResponseFactory responseFactory;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            return authServices.signIn(loginRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        try {
            return authServices.register(userRegistrationRequest);
        } catch (Exception ex) {
            return responseFactory.fail(String.format("Server error, %s", ex.getMessage()), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
