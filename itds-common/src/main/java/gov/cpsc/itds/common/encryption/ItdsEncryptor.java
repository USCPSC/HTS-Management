package gov.cpsc.itds.common.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItdsEncryptor {

  private static final Logger logger = LoggerFactory.getLogger(ItdsEncryptor.class);
  private SecretKeySpec key;

  public ItdsEncryptor(String secret, String salt, int iterationCount) throws InvalidKeySpecException, NoSuchAlgorithmException {
    logger.debug("in constructor");
    key = createSecretKey(secret.toCharArray(),salt.getBytes(),iterationCount);
    logger.debug("done with constructor");
  }

  SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount) throws NoSuchAlgorithmException, InvalidKeySpecException {
    logger.debug("in createSecretKey");
    if (password==null || password.length==0 || salt==null || salt.length==0 || iterationCount<1){
      logger.error("invalid parameters passed to createSecretKey");
      throw new InvalidKeySpecException();
    }
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, 256);
    SecretKey keyTmp = keyFactory.generateSecret(keySpec);
    logger.debug("done with createSecretKey");
    return new SecretKeySpec(keyTmp.getEncoded(), "AES");
  }

  public String encrypt(String password) throws GeneralSecurityException, UnsupportedEncodingException {
    if (password==null){
      return null;
    }
    Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    pbeCipher.init(Cipher.ENCRYPT_MODE, key);
    AlgorithmParameters parameters = pbeCipher.getParameters();
    IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
    byte[] cryptoText = pbeCipher.doFinal(password.getBytes("UTF-8"));
    byte[] iv = ivParameterSpec.getIV();
    return base64Encode(iv) + ":" + base64Encode(cryptoText);
  }

  public String decrypt(String encryptedPassword) throws GeneralSecurityException, IOException {
    if (encryptedPassword==null){
      return null;
    }
    String iv = encryptedPassword.split(":")[0];
    String property = encryptedPassword.split(":")[1];
    Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
    return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
  }

  String base64Encode(byte[] bytes) {
    if (bytes==null || bytes.length==0){
      return null;
    }
    return Base64.getEncoder().encodeToString(bytes);
  }

  byte[] base64Decode(String property) throws IOException {
    if (property==null || property.equals("")){
      return null;
    }
    return Base64.getDecoder().decode(property);
  }
}
