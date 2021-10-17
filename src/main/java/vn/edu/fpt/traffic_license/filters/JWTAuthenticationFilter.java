package vn.edu.fpt.traffic_license.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.fpt.traffic_license.config.jwt.JWTConfig;
import vn.edu.fpt.traffic_license.service.UserServices;
import vn.edu.fpt.traffic_license.utils.StringUtils;
import vn.edu.fpt.traffic_license.utils.jwt.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig;
    private final JWTUtils jwtUtils;
    private final UserServices userServices;

    @Autowired
    public JWTAuthenticationFilter(JWTConfig jwtConfig,
                                   JWTUtils jwtUtils,
                                   UserServices userServices) {
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
        this.userServices = userServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } catch (Exception ex) {
            log.error("Authentication Failed, {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}
