package vn.com.lol.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import vn.com.lol.common.enums.EncryptAlgorithmType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptUtil {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(EncryptAlgorithmType.RSA.name());
        keyGen.initialize(4096);
        return keyGen.generateKeyPair();
    }

    public static String getPublicKey(KeyPair keyGen) {
        return Base64.getEncoder()
                .encodeToString(keyGen
                        .getPublic()
                        .getEncoded()
                );
    }


    public static String getPrivateKey(KeyPair keyGen) {
        return Base64.getEncoder()
                .encodeToString(keyGen
                        .getPrivate()
                        .getEncoded()
                );
    }

    public static PublicKey retrievePublicKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPublicKey = Base64.getDecoder().decode(keyString);
        return KeyFactory.getInstance(EncryptAlgorithmType.RSA.name())
                .generatePublic(new X509EncodedKeySpec(decodedPublicKey));
    }

    public static PrivateKey retrievePrivateKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public String encryptData(Object encryptData, String publicKeyString) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

        PublicKey publicKey = retrievePublicKey(publicKeyString);

        Cipher cipher = Cipher.getInstance(EncryptAlgorithmType.RSA.name());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataSerialization = ObjectUtil.serializeObject(encryptData);
        byte[] encryptedData = cipher.doFinal(dataSerialization);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public <T> T decryptData(PrivateKey privateKey, String encryptString, Class<T> clazz) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException {

        Cipher cipher = Cipher.getInstance(EncryptAlgorithmType.RSA.name());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedData =  Base64.getDecoder().decode(encryptString);

        byte[] decryptedData = cipher.doFinal(encryptedData);

        return ObjectUtil.deserializeObject(decryptedData, clazz);
    }
}
