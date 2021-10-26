package vn.edu.fpt.traffic_license.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.entities.Company;
import vn.edu.fpt.traffic_license.entities.Role;
import vn.edu.fpt.traffic_license.entities.User;
import vn.edu.fpt.traffic_license.entities.UserRole;
import vn.edu.fpt.traffic_license.model.user.LoginRequest;
import vn.edu.fpt.traffic_license.model.user.UserDetailsImpl;
import vn.edu.fpt.traffic_license.repository.CompanyRepository;
import vn.edu.fpt.traffic_license.repository.RoleRepository;
import vn.edu.fpt.traffic_license.repository.UserRepository;
import vn.edu.fpt.traffic_license.repository.UserRoleRepository;
import vn.edu.fpt.traffic_license.request.UserRegistrationRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.response.UserInfo;
import vn.edu.fpt.traffic_license.utils.Utils;
import vn.edu.fpt.traffic_license.utils.jwt.JWTUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final ResponseFactory responseFactory;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    private static final String TOKEN_TYPE = "Bearer";

    public ResponseEntity<Object> signIn(LoginRequest loginRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Set<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        Set<Role> roles = Collections.emptySet();
        if (Utils.isNotEmpty(userRoles)) {
            roles = roleRepository.findByIdIn(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
        }
        UserInfo response = UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roles(Utils.isEmpty(roles) ? Collections.emptySet() : roles.stream().map(Role::getCode).collect(Collectors.toSet()))
                .accessToken(jwtUtils.generateToken(userDetails))
                .tokenType(TOKEN_TYPE)
                .build();
        return responseFactory.success(response);
    }

    public ResponseEntity<Object> register(UserRegistrationRequest userRegistrationRequest) {
        User user = userRepository.findByUsername(userRegistrationRequest.getUsername());
        if (user != null) {
            return responseFactory.fail("Username đã tồn tại.", ResponseStatusCodeConst.DUPLICATE_ERROR);
        }

        user = userRepository.findByIdentificationNumber(userRegistrationRequest.getIdentificationNumber());
        if (user != null) {
            return responseFactory.fail("Số CMTND đã tồn tại.", ResponseStatusCodeConst.DUPLICATE_ERROR);
        }

        Optional<Company> companyOptional = companyRepository.findById(userRegistrationRequest.getCompanyId());
        if (!companyOptional.isPresent()) {
            return responseFactory.fail("Công ty không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        Company company = companyOptional.get();
        user = User.builder()
                .fullName(userRegistrationRequest.getFullName())
                .username(userRegistrationRequest.getUsername())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .identificationNumber(userRegistrationRequest.getIdentificationNumber())
                .companyId(company.getId())
                .enabled(true)
                .locked(false)
                .granted(false)
                .build();
        user = userRepository.save(user);
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(3L)
                .build();
        userRoleRepository.save(userRole);
        return responseFactory.success("Thành công");
    }

}
