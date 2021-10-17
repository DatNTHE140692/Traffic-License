package vn.edu.fpt.traffic_license.utils.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.edu.fpt.traffic_license.config.jwt.JWTConfig;
import vn.edu.fpt.traffic_license.utils.DateUtils;
import vn.edu.fpt.traffic_license.utils.SignUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

@Slf4j
@Component
public class JWTUtils {

    private final JWTConfig jwtConfig;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS512;

    @Autowired
    public JWTUtils(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserDetails userDetails) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Date now = DateUtils.now();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiresIn());
        PrivateKey privateKey = SignUtils.convertStrToPrivateKey(jwtConfig.getPrivateKey());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signatureAlgorithm, privateKey)
                .compact();
    }

    public String extractUsername(String token) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PrivateKey privateKey = SignUtils.convertStrToPrivateKey(jwtConfig.getPrivateKey());
        return Jwts.parser()
                .setSigningKey(privateKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean isTokenValid(String token) {
        try {
            PrivateKey privateKey = SignUtils.convertStrToPrivateKey(jwtConfig.getPrivateKey());
            Jwts.parser()
                    .setSigningKey(privateKey)
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
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
