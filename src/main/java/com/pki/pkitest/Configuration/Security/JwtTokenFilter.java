package com.pki.pkitest.Configuration.Security;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pki.pkitest.Utils.CertificateUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final CertificateUtils certificateUtils;

    public JwtTokenFilter(CertificateUtils certificateUtils) {
        this.certificateUtils = certificateUtils;
    }

    @Override
    protected void doFilterInternal (HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
    
    String header = request.getHeader("authorization");

    if(header != null && header.startsWith("Bearer")){
        String token = header.substring(7);
        try {
        SignedJWT signedJWT = SignedJWT.parse(token);
        RSAPublicKey publicKey = (RSAPublicKey)certificateUtils.getCaCert("CA_JWT").getPublicKey();

        JWSVerifier verifier = new RSASSAVerifier(publicKey);

        if (signedJWT.verify(verifier)) {
            
                
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                List<String> roles = claims.getStringListClaim("authorities");
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) {
                authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    } 

                Object payloadObject = claims.getClaim("Payload");
                request.setAttribute("payload_jws", payloadObject);
                request.setAttribute("userID", UUID.fromString(claims.getStringClaim("userID")));
                String username = claims.getSubject();

                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            // Error de parseo o firma no válida
            System.err.println("ERROR CRÍTICO EN FILTRO: " + e.getMessage());
            e.printStackTrace();
        }

        
    }filterChain.doFilter(request, response);
    
    }
}
