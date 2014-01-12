/* 
 * UniCrypt
 * 
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 * 
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 * 
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 * 
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 * 
 *
 * Redistributions of files must retain the above copyright notice.
 */
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
