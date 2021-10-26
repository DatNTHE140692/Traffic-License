package vn.edu.fpt.traffic_license.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("identification_number")
    private String identificationNumber;

    @JsonProperty("company_id")
    private Long companyId;

    @JsonProperty("address")
    private String address;

    @JsonProperty("ward_id")
    private Long wardId;

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("city_id")
    private Long cityId;

}
