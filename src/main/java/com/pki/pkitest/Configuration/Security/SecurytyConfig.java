package com.pki.pkitest.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pki.pkitest.Utils.CertificateUtils;

@Configuration
@EnableWebSecurity
public class SecurytyConfig {
    @Autowired
    private CertificateUtils certificateUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        try{
            http
           .csrf(csrf -> csrf.disable())
           .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           )
           .authorizeHttpRequests(aut -> aut
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                
           ).addFilterBefore(new JwtTokenFilter(certificateUtils), UsernamePasswordAuthenticationFilter.class);

           return http.build();


        }catch(Exception ex){
            throw new RuntimeException("Eroor en la configuracion inical de seguridad" + ex.getMessage());
        }
    }
}
