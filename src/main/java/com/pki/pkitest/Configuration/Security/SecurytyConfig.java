package com.pki.pkitest.Configuration.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
           
           .authorizeHttpRequests(aut -> aut

                .requestMatchers(HttpMethod.POST,"/ciferAES").hasRole("AES_USER")
                .requestMatchers(HttpMethod.POST,"/deciferAES").hasRole("AES_USER")
                .requestMatchers(HttpMethod.POST,"/issueCertificate").hasAnyRole("KRD_USER","KDH_USER", "MTLS_USER")
                .requestMatchers(HttpMethod.POST,"/getJWS").permitAll()
                .requestMatchers(HttpMethod.GET, "/TestApikey").permitAll()
                .requestMatchers(HttpMethod.GET,"/**").denyAll()
                .requestMatchers(HttpMethod.DELETE, "/**").denyAll()
                .requestMatchers(HttpMethod.PUT, "/**").denyAll()
                .requestMatchers(HttpMethod.HEAD, "/**").denyAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").denyAll()
                .requestMatchers(HttpMethod.PATCH, "/**").denyAll()
                .requestMatchers(HttpMethod.TRACE, "/**").denyAll()
                .anyRequest()
                .authenticated())

           .csrf(csrf -> csrf.disable())
           .cors(Customizer.withDefaults())
           .headers(headers -> headers.cacheControl(cacheControlConfig->{})) 
           .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .httpBasic(Customizer.withDefaults())
           .addFilterBefore(new JwtTokenFilter(certificateUtils), UsernamePasswordAuthenticationFilter.class);

           return http.build();


        }catch(Exception ex){
            throw new RuntimeException("Eroor en la configuracion inical de seguridad" + ex.getMessage());
        }
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
