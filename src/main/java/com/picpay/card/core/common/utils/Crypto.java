package com.picpay.card.core.common.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@UtilityClass
public class Crypto {
    public static String encrypt(String plainText) {
        try (FileInputStream fis = new FileInputStream("key.pub")) {

            Key publicKey = getPublicKey(fis.readAllBytes());

            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] plainTextBytes = plainText.getBytes();
            byte[] encriptedTextBytes = encryptCipher.doFinal(plainTextBytes);

            return Base64.getMimeEncoder().encodeToString(encriptedTextBytes);
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try (FileInputStream fis = new FileInputStream("key.priv")) {

            Key privateKey = getPrivateKey(fis.readAllBytes());

            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encriptedTextBase64Bytes = encryptedText.getBytes();
            byte[] encriptedTextBytes = Base64.getMimeDecoder().decode(encriptedTextBase64Bytes);
            byte[] plainTextBytes = decryptCipher.doFinal(encriptedTextBytes);

            return new String(plainTextBytes);
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privKeyBytes = Base64.getMimeDecoder().decode(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    private static PublicKey getPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytesPubKeyStr = Base64.getMimeDecoder().decode(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytesPubKeyStr);
        return keyFactory.generatePublic(publicKeySpec);
    }

}
