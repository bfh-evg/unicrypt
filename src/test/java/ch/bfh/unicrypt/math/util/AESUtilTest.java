package ch.bfh.unicrypt.math.util;

import java.security.Key;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.utility.AESUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;

@SuppressWarnings("static-method")
public class AESUtilTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testEncrypt() throws Exception {
        
      String humanPassphrase = new String("123456");      
      byte[] salt = new byte[]{1,2,3,4,5,6,7,8,9,0};
      Key key = AESUtil.generateKey(humanPassphrase.toCharArray(), salt,1024,256);
           
      String plainText="Hello World, this is an AES-Test";
      byte[] ciphertext = AESUtil.encrypt(plainText.getBytes(),key,1);
      byte[] plainTextAsByteArray=AESUtil.decrypt(ciphertext, key, 1);
      Assert.assertEquals(plainText, new String(plainTextAsByteArray));    
  }

  @Test
  public void testRandomDecrypts() throws Exception {
    Random random = RandomUtil.getSecureRandom(new byte[] { 1, 2, 3, 4, 5 });
    byte[] randomBytes = null;
    for (int i = 0; i < 1000; i++) {
      randomBytes = new byte[random.nextInt(20) + 10];
      random.nextBytes(randomBytes);
      String humanPassphrase = new String(randomBytes);
      randomBytes = new byte[random.nextInt(20) + 10];
      random.nextBytes(randomBytes);
      byte[] salt = randomBytes;
      Key key = AESUtil.generateKey(humanPassphrase.toCharArray(), salt,1024,256);      
      String plainText="Hello World, this is an AES-Test";
      byte[] ciphertext = AESUtil.encrypt(plainText.getBytes(),key,1);
      byte[] plainTextAsByteArray=AESUtil.decrypt(ciphertext, key, 1);
      Assert.assertEquals(plainText, new String(plainTextAsByteArray));
    }
  }

  @Test
  public void testGenerateKeyCharArrayByteArrayIntInt() throws Exception {
    byte[] salt = new byte[]{1,2,3,4,5,6,7,8,9,0};
    @SuppressWarnings("unused")
    Key key = AESUtil.generateKey("123456".toCharArray(), salt,1024,256);  
    // here is something missing! Reto?
  }

  

}
