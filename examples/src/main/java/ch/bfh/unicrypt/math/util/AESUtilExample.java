package ch.bfh.unicrypt.math.util;

import java.security.Key;

public class AESUtilExample {
  public static void main(String[] args) throws Exception {
    Key key = AESUtil.generateKey("123".toCharArray(), new byte[] { 7, 7, 7 }, 1024, 256);
    byte[] ciphertext = AESUtil.encrypt("HelloWorld".getBytes(), key, 1);
    byte[] plaintext = AESUtil.decrypt(ciphertext, key, 1);
    System.out.println(new String(plaintext));
    // java.security.InvalidKeyException: Illegal key size or default parameters
    // unlimitted strength missing
  }
}
