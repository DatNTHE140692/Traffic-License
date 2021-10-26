package vn.edu.fpt.traffic_license.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private Set<String> roles;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    public String tokenType;

}
