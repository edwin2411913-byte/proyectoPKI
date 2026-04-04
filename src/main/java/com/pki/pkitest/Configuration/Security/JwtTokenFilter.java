package com.pki.pkitest.Configuration.Security;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        RSAPublicKey publicKey = (RSAPublicKey)certificateUtils.getCaCert("CA_JWS").getPublicKey();

        JWSVerifier verifier = new RSASSAVerifier(publicKey);

        if (signedJWT.verify(verifier)) {
            
                
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Object payloadObject = claims.getClaim("Payloud");
                request.setAttribute("payload_jws", payloadObject);
                String username = claims.getSubject();

                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                
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
