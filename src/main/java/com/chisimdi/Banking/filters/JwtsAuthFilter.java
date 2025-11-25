package com.chisimdi.Banking.filters;

import com.chisimdi.Banking.services.JwtsUtilService;
import com.chisimdi.Banking.utils.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtsAuthFilter extends OncePerRequestFilter {
    @Autowired
    JwtsUtilService jwtsUtilService;
    private final static Logger log= LoggerFactory.getLogger(JwtsAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Extracting authorization header");
        String authorization=request.getHeader("Authorization");

        if(authorization!=null&&authorization.startsWith("Bearer ")){
            log.info("Extracting token from filter ");
            String token=authorization.substring(7);
            log.info("Checking if token is valid");
            if(jwtsUtilService.isTokenValid(token)) {
                log.info("Extracting token information");
                String userName = jwtsUtilService.extractUserName(token);
                String role = jwtsUtilService.extractRoles(token);
                int userId = jwtsUtilService.extractUserId(token);
                CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(userId, userName, role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserPrincipal, null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                SecurityContext securityContext = new SecurityContextImpl();
                securityContext.setAuthentication(auth);
                SecurityContextHolder.setContext(securityContext);

            };

    }
        filterChain.doFilter(request,response);
}
}
