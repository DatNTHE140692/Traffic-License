package vn.edu.fpt.traffic_license.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.constants.SearchOperation;
import vn.edu.fpt.traffic_license.entities.*;
import vn.edu.fpt.traffic_license.mapper.UserMapper;
import vn.edu.fpt.traffic_license.model.user.UserDetailsImpl;
import vn.edu.fpt.traffic_license.repository.*;
import vn.edu.fpt.traffic_license.repository.specification.GenericSpecification;
import vn.edu.fpt.traffic_license.repository.specification.SearchCriteria;
import vn.edu.fpt.traffic_license.request.UserRequest;
import vn.edu.fpt.traffic_license.response.GeneralResponse;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.response.UserResponse;
import vn.edu.fpt.traffic_license.utils.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final RoleRepository roleRepository;
    private final WardRepository wardRepository;
    private final ResponseFactory responseFactory;
    private final CompanyRepository companyRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user, userRoleRepository, roleRepository);
    }

    public ResponseEntity<Object> isUserExisted(String phoneNumber) {
        User user = userRepository.findByUsername(phoneNumber);
        return responseFactory.success(user != null);
    }

    public ResponseEntity<Object> getUsers(Pageable pageable,
                                           String name,
                                           String username,
                                           String identificationNo,
                                           String address,
                                           Long wardId,
                                           Long provinceId,
                                           Long cityId,
                                           Boolean granted) {
        GenericSpecification<User> genericSpecification = new GenericSpecification<>();

        if (StringUtils.isNotBlank(name)) {
            genericSpecification.add(new SearchCriteria("fullName", name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(username)) {
            genericSpecification.add(new SearchCriteria("username", username, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(identificationNo)) {
            genericSpecification.add(new SearchCriteria("identificationNumber", identificationNo, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(address)) {
            genericSpecification.add(new SearchCriteria("address", address, SearchOperation.MATCH));
        }

        if (wardId != null) {
            genericSpecification.add(new SearchCriteria("wardId", wardId, SearchOperation.EQUAL));
        }

        if (provinceId != null) {
            genericSpecification.add(new SearchCriteria("provinceId", provinceId, SearchOperation.EQUAL));
        }

        if (cityId != null) {
            genericSpecification.add(new SearchCriteria("cityId", cityId, SearchOperation.EQUAL));
        }

        if (granted != null) {
            genericSpecification.add(new SearchCriteria("granted", granted, SearchOperation.EQUAL));
        }
        Page<User> userPage = userRepository.findAll(genericSpecification, pageable);
        List<UserResponse> userDtos = userPage.getContent()
                .stream()
                .map(user -> UserMapper.toDto(user, null, null, null, null))
                .collect(Collectors.toList());
        GeneralResponse.PaginationMetadata paginationMetadata = new GeneralResponse.PaginationMetadata(
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber()
        );
        return responseFactory.success(GeneralResponse.paginated(paginationMetadata, userDtos));
    }

    public ResponseEntity<Object> getUserInfo(UserRequest userRequest) {
        User user = userRepository.findByUsernameAndIdentificationNumber(userRequest.getUsername(), userRequest.getIdentificationNumber());
        if (user == null) {
            return responseFactory.fail("Không tìm thấy người dùng.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        Optional<City> cityOptional = cityRepository.findById(user.getCityId());
        if (!cityOptional.isPresent()) {
            return responseFactory.fail("Tỉnh/Thành phố không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        City city = cityOptional.get();

        Optional<Province> provinceOptional = provinceRepository.findById(user.getProvinceId());
        if (!provinceOptional.isPresent()) {
            return responseFactory.fail("Quận/Huyện không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Province province = provinceOptional.get();

        Optional<Ward> wardOptional = wardRepository.findById(user.getWardId());
        if (!wardOptional.isPresent()) {
            return responseFactory.fail("Xã/Phường không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Ward ward = wardOptional.get();

        Optional<Company> companyOptional = companyRepository.findById(user.getCompanyId());
        if (!companyOptional.isPresent()) {
            return responseFactory.fail("Công ty phụ thuộc không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Company company = companyOptional.get();

        UserResponse userResponse = UserResponse.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getUsername())
                .identificationNumber(user.getIdentificationNumber())
                .noOfVaccinated(user.getNoOfVaccinated())
                .address(user.getAddress())
                .ward(ward)
                .province(province)
                .city(city)
                .company(company)
                .granted(user.getGranted() && company.getActive())
                .build();
        return responseFactory.success(userResponse);
    }

    public ResponseEntity<Object> updateUserInfo(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findById(userRequest.getId());
        if (!userOptional.isPresent()) {
            return responseFactory.fail("Người dùng không tồn tại.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        User user  = userOptional.get();

        if (StringUtils.isBlank(userRequest.getFullName())) {
            return responseFactory.fail("Tên không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (StringUtils.isBlank(user.getUsername())) {
            return responseFactory.fail("Tên đăng nhập không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        if (StringUtils.isBlank(userRequest.getAddress())) {
            return responseFactory.fail("Địa chỉ không được để trống.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }

        Optional<City> cityOptional = cityRepository.findById(user.getCityId());
        if (!cityOptional.isPresent()) {
            return responseFactory.fail("Tỉnh/Thành phố không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        City city = cityOptional.get();

        Optional<Province> provinceOptional = provinceRepository.findById(user.getProvinceId());
        if (!provinceOptional.isPresent()) {
            return responseFactory.fail("Quận/Huyện không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Province province = provinceOptional.get();

        Optional<Ward> wardOptional = wardRepository.findById(user.getWardId());
        if (!wardOptional.isPresent()) {
            return responseFactory.fail("Xã/Phường không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Ward ward = wardOptional.get();

        Optional<Company> companyOptional = companyRepository.findById(user.getCompanyId());
        if (!companyOptional.isPresent()) {
            return responseFactory.fail("Công ty phụ thuộc không hợp lệ.", ResponseStatusCodeConst.DATA_NOT_FOUND_ERROR);
        }
        Company company = companyOptional.get();

        if (!user.getFullName().equals(userRequest.getFullName().trim())) {
            user.setFullName(userRequest.getFullName().trim());
        }

        if (!user.getUsername().equals(userRequest.getUsername().trim())) {
            user.setUsername(userRequest.getUsername().trim());
        }

        if (!user.getIdentificationNumber().equals(userRequest.getIdentificationNumber().trim())) {
            user.setIdentificationNumber(userRequest.getIdentificationNumber().trim());
        }

        if (!user.getAddress().equals(userRequest.getAddress().trim())) {
            user.setAddress(userRequest.getAddress().trim());
        }

        if (!user.getWardId().equals(userRequest.getWardId())) {
            user.setWardId(ward.getId());
        }

        if (!user.getProvinceId().equals(userRequest.getProvinceId())) {
            user.setProvinceId(province.getId());
        }

        if (!user.getCityId().equals(userRequest.getCityId())) {
            user.setCityId(city.getId());
        }

        if (!user.getCompanyId().equals(userRequest.getCompanyId())) {
            user.setCompanyId(company.getId());
        }

        if (!user.getNoOfVaccinated().equals(userRequest.getNoOfVaccinated())) {
            user.setNoOfVaccinated(userRequest.getNoOfVaccinated());
        }

        if (!user.getGranted().equals(userRequest.getGranted())) {
            user.setGranted(userRequest.getGranted() && company.getActive());
        }

        userRepository.save(user);
        return responseFactory.success("Thành công");
    }

}
