package vn.edu.fpt.traffic_license.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {

    private String publicKey;
    private String privateKey;
    private Long expiresIn;
    private String tokenPrefix;

}
