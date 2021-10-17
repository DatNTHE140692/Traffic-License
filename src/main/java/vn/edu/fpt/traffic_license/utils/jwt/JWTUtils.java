package vn.edu.fpt.traffic_license.utils.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.edu.fpt.traffic_license.config.jwt.JWTConfig;
import vn.edu.fpt.traffic_license.utils.DateUtils;

import java.util.Date;

@Slf4j
@Component
public class JWTUtils {

    private final JWTConfig jwtConfig;

    @Autowired
    public JWTUtils(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserDetails userDetails) {
        Date now = DateUtils.now();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiresIn());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.RS512, jwtConfig.getPrivateKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getPrivateKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getPrivateKey())
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

}
