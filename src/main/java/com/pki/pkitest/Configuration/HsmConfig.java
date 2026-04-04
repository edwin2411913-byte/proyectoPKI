package com.pki.pkitest.Configuration;

import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HsmConfig {

    @Value("${hsm.pkcs11.config-path}")
        private String configHSM;

    @Value("${hsm.pkcs11.User0}")
        private String slot0;

    public KeyStore ks;

    
    @Bean
    public Provider hsmProvider() {
        try{
            Provider p = Security.getProvider("SunPKCS11");
            p = p.configure(configHSM);
            System.out.println("HSM Inizializado");
            return p;
        }catch (Exception e) {
            throw new BeanCreationException("Falla: No se pudo configurar el proveedor PKCS11 ", e);
        }

    }

    @Bean(name = "hsmKeyStore")
    public KeyStore loadKeySotre(Provider hsmProvider){
        try{
            ks = KeyStore.getInstance("PKCS11", hsmProvider);
            ks.load(null, slot0.toCharArray());
            System.out.println("Key Store cargado ...");
            return ks;
        }
        catch (Exception e){
            throw new BeanCreationException("Falla: No se pudo cargar el KeyStore ", e);
        }
    }

}
