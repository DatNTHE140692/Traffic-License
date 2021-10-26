package vn.edu.fpt.traffic_license.mapper;

import vn.edu.fpt.traffic_license.entities.*;
import vn.edu.fpt.traffic_license.response.UserResponse;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toDto(User user, Ward ward, Province province, City city, Company company) {
        String address = user.getAddress();
        if (ward == null || province == null || city == null) address = "";

        return UserResponse.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getUsername())
                .identificationNumber(user.getIdentificationNumber())
                .address(address)
                .ward(ward != null ? ward.getName() : "")
                .province(province != null ? province.getName() : "")
                .city(city != null ? city.getName() : "")
                .company(company)
                .noOfVaccinated(user.getNoOfVaccinated())
                .granted(user.getGranted())
                .build();
    }

}
