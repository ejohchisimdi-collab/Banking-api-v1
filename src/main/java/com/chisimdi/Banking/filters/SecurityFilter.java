package com.chisimdi.Banking.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityFilter {
    @Autowired
public JwtsAuthFilter jwtsAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.csrf(csrf->csrf.disable()).
                authorizeHttpRequests(auth->
                        auth.requestMatchers("/users/login","/users/register","/swagger-ui/**","/v3/**","/swagger-ui.html")
                                .permitAll().anyRequest().authenticated()).addFilterBefore(jwtsAuthFilter, UsernamePasswordAuthenticationFilter.class);
        ;
        return httpSecurity.build();
    }
}
