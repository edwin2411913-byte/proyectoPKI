package com.pki.pkitest.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CiferUtils {

    public final KeyStore ks;

    public CiferUtils(@Qualifier("hsmKeyStore") KeyStore keyStore) {
        this.ks = keyStore;
    }

    public String ciferAES(String plainTex, String ksAlias){
        SecretKey key;
        
        try{
            key = (SecretKey)ks.getKey(ksAlias, null);

            byte[] iv = new byte[12]; 
            new SecureRandom().nextBytes(iv);

            // 2. Configurar GCM con un tag de autenticación de 128 bits (16 bytes)
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", ks.getProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] cipherText = cipher.doFinal(plainTex.getBytes());
            // Concatenar IV + Texto Cifrado para poder descifrar después
            byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
        }
        catch(Exception e){
            throw new RuntimeException("error"+ e.getMessage());
        }
    }

    public String decrypAES(String encryptedData, String ksAlias){
        try{
            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            // 1. Extraer el IV (12 bytes para GCM)
            byte[] iv = Arrays.copyOfRange(decoded, 0, 12);
            
            // 2. El resto es Ciphertext + Tag (el Tag suelen ser los últimos 16 bytes)
            byte[] cipherTextWithTag = Arrays.copyOfRange(decoded, 12, decoded.length);

            System.out.println("IV (Hex): " + bytesToHex(iv));

            // 2. Obtener la llave del HSM
            SecretKey key = (SecretKey) ks.getKey(ksAlias, null);

            // 3. Configurar GCM con el IV recuperado
            // 128 es el tamaño estándar del tag de autenticación en bits
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);

            // 4. Inicializar el Cipher en modo DECRYPT
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", ks.getProvider());
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // 5. Descifrar
            byte[] plainTextBytes = cipher.doFinal(cipherTextWithTag);
            String valorReal = new String(plainTextBytes, StandardCharsets.UTF_8);
            System.out.println("Texto descifrado: " + valorReal);
            return valorReal;
        }
        catch(Exception e){
            throw new RuntimeException("Eror" + e.getMessage());
        }
    }
    // Utilería para visualizar el IV
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
