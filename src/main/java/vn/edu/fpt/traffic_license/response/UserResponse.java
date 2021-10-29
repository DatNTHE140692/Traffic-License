package vn.edu.fpt.traffic_license.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import vn.edu.fpt.traffic_license.entities.City;
import vn.edu.fpt.traffic_license.entities.Company;
import vn.edu.fpt.traffic_license.entities.Province;
import vn.edu.fpt.traffic_license.entities.Ward;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String fullName;
    private String phoneNumber;
    private String identificationNumber;
    private Ward ward;
    private Province province;
    private City city;
    private String address;
    private Long noOfVaccinated;
    private Company company;
    private Boolean granted;

}
