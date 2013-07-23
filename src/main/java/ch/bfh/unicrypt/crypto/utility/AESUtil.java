package ch.bfh.unicrypt.crypto.utility;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides basic abilities for AES operations. It uses ECB as it is
 * assumed that the plaintext is of low redundancy If needed, the scheme might
 * be strengthened with CBC or CTR in order to be save for plaintext with high
 * redundancy.
 * 
 * The following algorithms are derived from
 * http://www.java2s.com/Code/Android/Security
 * /encryptdecryptAESencryptdecryptPBE.htm
 * 
 * @author reto
 * 
 */
public class AESUtil {

  public static byte[] encrypt(byte[] value, Key key, int iterations) throws Exception {
    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, key);

    byte[] valueToEnc = value.clone();
    for (int i = 0; i < iterations; i++) {
      valueToEnc = c.doFinal(valueToEnc);
    }
    return valueToEnc;
  }

  public static byte[] decrypt(byte[] value, Key key, int iterations) throws Exception {
    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");    
    c.init(Cipher.DECRYPT_MODE, key);
    
    byte[] valueToDecrypt = value.clone();
    for (int i = 0; i < iterations; i++) {
      valueToDecrypt = c.doFinal(valueToDecrypt);
    }
    return valueToDecrypt;
  }

  /**
   * The following method allows to derive a cryptographic (secret)key using a
   * human created password.
   * 
   * @param password
   *          The human created password
   * @param salt
   *          A salt created by some random-oracle
   * @param iterationCount
   *          (1024 is a reasonable value)
   * @param keyLength
   *          The length of the resulting cryptographic (secret)key (256 is a
   *          reasonable value)
   * @return the cryptographic (secret)key
   * @throws Exception
   * @throws InvalidKeyException : Illegal key size or default
   *           parameters if the JCE (Java crypto extension) is missing
   */
  public static Key generateKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");    
    KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);
    SecretKey tmp = factory.generateSecret(spec);    
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    return secret;
  }   
}
