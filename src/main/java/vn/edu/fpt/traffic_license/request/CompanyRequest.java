package vn.edu.fpt.traffic_license.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("license_no")
    private String licenseNo;

    @JsonProperty("address")
    private String address;

    @JsonProperty("ward_id")
    private Long wardId;

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("city_id")
    private Long cityId;

    @JsonProperty("active")
    private boolean active;

}
