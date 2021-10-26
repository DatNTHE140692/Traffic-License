package vn.edu.fpt.traffic_license.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig;
    private final JWTUtils jwtUtils;
    private final UserServices userServices;
    private final ResponseFactory responseFactory;

    private static final List<String> filterURI = Arrays.asList("/management");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (needAuthorize(uri)) {
            try {
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (StringUtils.isNotBlank(authorization) && authorization.startsWith(jwtConfig.getTokenPrefix())) {
                    String token = authorization.substring(7);
                    String username = jwtUtils.extractUsername(token);
                    if (jwtUtils.isTokenValid(token) && StringUtils.isNotBlank(username)) {
                        UserDetails userDetails = userServices.loadUserByUsername(username);
                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            returnError(response, ResponseStatusCodeConst.FORBIDDEN, null);
                            return;
                        }
                    }
                } else {
                    returnError(response, ResponseStatusCodeConst.FORBIDDEN, null);
                    return;
                }
            } catch (Exception ex) {
                log.error("Authentication Failed, {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean needAuthorize(String reqUri) {
        return filterURI.stream().anyMatch(reqUri::startsWith);
    }

    private void returnError(HttpServletResponse response, ResponseStatusCodeConst statusCodeConst, String errorDetail) throws IOException {
        ResponseEntity<Object> responseEntity = responseFactory.fail(errorDetail, statusCodeConst);
        response.setStatus(statusCodeConst.getHttpCode());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseEntity.getBody());
    }

}
