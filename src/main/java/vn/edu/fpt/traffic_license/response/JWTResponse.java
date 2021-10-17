package vn.edu.fpt.traffic_license.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import vn.edu.fpt.traffic_license.model.user.Role;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private Set<Role> roles;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    public String tokenType;

}
