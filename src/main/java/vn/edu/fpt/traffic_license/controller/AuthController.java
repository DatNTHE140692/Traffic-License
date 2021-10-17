package vn.edu.fpt.traffic_license.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.model.user.LoginRequest;
import vn.edu.fpt.traffic_license.entities.User;
import vn.edu.fpt.traffic_license.model.user.UserDetailsImpl;
import vn.edu.fpt.traffic_license.response.JWTResponse;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.utils.jwt.JWTUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtils jwtUtils;
    private final ResponseFactory responseFactory;
    private final AuthenticationManager authenticationManager;

    private static final String TOKEN_TYPE = "Bearer";

    @Autowired
    public AuthController(JWTUtils jwtUtils,
                          ResponseFactory responseFactory,
                          AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.responseFactory = responseFactory;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            JWTResponse response = JWTResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .roles(user.getRoles())
                    .accessToken(jwtUtils.generateToken(userDetails))
                    .tokenType(TOKEN_TYPE)
                    .build();
            return responseFactory.success(response);
        } catch (Exception ex) {
            return responseFactory.fail(ex.getMessage(), ResponseStatusCodeConst.INTERNAL_SERVER_ERROR);
        }
    }

}
