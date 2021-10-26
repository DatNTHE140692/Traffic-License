package vn.edu.fpt.traffic_license.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String fullName;
    private String phoneNumber;
    private String identificationNumber;
    private String ward;
    private String province;
    private String city;
    private String company;
    private Boolean granted;

}
