package vn.edu.fpt.traffic_license.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("identification_number")
    private String identificationNumber;

    @JsonProperty("ward_id")
    private Long wardId;

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("city_id")
    private Long cityId;

    @JsonProperty("company_id")
    private Long companyId;

}
