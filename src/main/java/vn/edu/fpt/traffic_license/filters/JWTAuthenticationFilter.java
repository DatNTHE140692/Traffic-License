package vn.edu.fpt.traffic_license.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.fpt.traffic_license.config.jwt.JWTConfig;
import vn.edu.fpt.traffic_license.constants.ResponseStatusCodeConst;
import vn.edu.fpt.traffic_license.response.ResponseFactory;
import vn.edu.fpt.traffic_license.service.UserServices;
import vn.edu.fpt.traffic_license.utils.StringUtils;
import vn.edu.fpt.traffic_license.utils.jwt.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig;
    private final JWTUtils jwtUtils;
    private final UserServices userServices;
    private final ResponseFactory responseFactory;

    @Autowired
    public JWTAuthenticationFilter(JWTConfig jwtConfig,
                                   JWTUtils jwtUtils,
                                   UserServices userServices,
                                   ResponseFactory responseFactory) {
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
        this.userServices = userServices;
        this.responseFactory = responseFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isBlank(authorization) || !authorization.startsWith(jwtConfig.getTokenPrefix())) {
                returnError(response, ResponseStatusCodeConst.FORBIDDEN, null);
                return;
            }
            String token = authorization.substring(7);
            String username = jwtUtils.extractUsername(token);
            if (jwtUtils.isTokenValid(token) && StringUtils.isNotBlank(username)) {
                UserDetails userDetails = userServices.loadUserByUsername(username);
                if (userDetails == null) {
                    returnError(response, ResponseStatusCodeConst.FORBIDDEN, null);
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Authentication Failed, {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public void returnError(HttpServletResponse response, ResponseStatusCodeConst statusCodeConst, String errorDetail) throws IOException {
        ResponseEntity<Object> responseEntity = responseFactory.fail(errorDetail, statusCodeConst);
        response.setStatus(statusCodeConst.getHttpCode());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseEntity.getBody());
    }

}
