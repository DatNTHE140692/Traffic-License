package vn.edu.fpt.traffic_license.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.entities.User;
import vn.edu.fpt.traffic_license.model.user.UserDetailsImpl;
import vn.edu.fpt.traffic_license.repository.RoleRepository;
import vn.edu.fpt.traffic_license.repository.UserRepository;
import vn.edu.fpt.traffic_license.repository.UserRoleRepository;
import vn.edu.fpt.traffic_license.request.UserRequest;
import vn.edu.fpt.traffic_license.response.ResponseFactory;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final UserRepository userRepository;
    private final ResponseFactory responseFactory;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user, userRoleRepository, roleRepository);
    }

    public ResponseEntity<Object> getUserInfo(UserRequest userRequest) {
//        User user = userRepository.findByUsernameAndIdentificationNumber(userRequest.getUsername(), userRequest.getIdentificationNumber());
//        if (user == null) {
//            return responseFactory.fail("Không tìm thấy người dùng.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
//        }
//        String reqName = StringUtils.removeAccent(userRequest.getFullName()).replaceAll("\\s+", " ");
//        String resName = StringUtils.removeAccent(user.getFullName()).replaceAll("\\s+", " ");
//        if (reqName.equals(resName)) {
//            Company resCompany = user.getCompany();
//            Long companyId = userRequest.getCompanyId();
//            if (resCompany != null && resCompany.getActive() && resCompany.getId().longValue() == companyId) {
//                return responseFactory.success(user.getGranted());
//            }
//            return responseFactory.fail("Người dùng không hợp lệ.", ResponseStatusCodeConst.VALIDATION_ERROR);
//        }
        return responseFactory.fail("Người dùng không hợp lệ.", ResponseStatusCodeConst.VALIDATION_ERROR);
    }

    public ResponseEntity<Object> updateUserInfo(UserRequest userRequest) {
        return null;
    }

}
