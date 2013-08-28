package ch.bfh.unicrypt.encryption.classes;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.encryption.classes.AESEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.classes.PasswordDerivedKeyGeneratorClass;
import ch.bfh.unicrypt.crypto.keygen.interfaces.PasswordDerivedKeyGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.EncodedElement;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.utility.RandomUtil;

public class AESEncryptionClassTest {

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
  public void testAESEncryptionClass() {
    // byte[] bytes=new byte[]{-1, -91, -21, -3, 99, 28, -52, 77, 17, -4, 33,
    // -115, 101, -108, -70, 13};
    // String base64=DatatypeConverter.printBase64Binary(bytes);
    // BigInteger bb=new BigInteger(1,base64.getBytes());
    // byte[] bytesNew=DatatypeConverter.parseBase64Binary(new
    // String(bb.toByteArray()));
    // System.out.println(bytesNew);

    AESEncryptionScheme aes = new AESEncryptionScheme();
    Random random = RandomUtil.createRandomGenerator(new byte[] { 1, 2, 3, 4, 5 });
    byte[] randomBytes = null;
    for (int i = 0; i < 1000; i++) {
      randomBytes = new byte[random.nextInt(20) + 10];
      random.nextBytes(randomBytes);
      String humanPassphrase = new String(randomBytes);
      randomBytes = new byte[random.nextInt(20) + 10];
      random.nextBytes(randomBytes);
      byte[] salt = randomBytes;
      Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);
      randomBytes = new byte[random.nextInt(20) + 10];
      Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
      Element ciphertext = aes.encrypt(key, plaintext);
      Element recoveredPlaintext = aes.decrypt(key, ciphertext);
      Assert.assertEquals("Hello World, this is an AES-Test", new String(((EncodedElement) recoveredPlaintext).getValue().toByteArray()));

    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testAESEncryptionClassZeroInt() {
    AESEncryptionScheme aes = new AESEncryptionScheme(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAESEncryptionClassNegativeInt() {
    AESEncryptionScheme aes = new AESEncryptionScheme(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAESEncryptionClassNullKeyGenerator() {
    AESEncryptionScheme aes = new AESEncryptionScheme(1, null);
  }

  @Test
  public void testAESEncryptionClassIntKeyGenerator() {
    PasswordDerivedKeyGenerator keyGenerator = new PasswordDerivedKeyGeneratorClass();
    AESEncryptionScheme aes = new AESEncryptionScheme(1, keyGenerator);
  }

  @Test
  public void testAESEncryptionClassIntZPlusModInt() {
    AESEncryptionScheme aes = new AESEncryptionScheme(1,new ZPlusModClass(BigInteger.valueOf(2).pow(256)),1024);    
    
  }

  
  @Test
  public void testGetEncryptionIterations() {
    AESEncryptionScheme aes = new AESEncryptionScheme(1,new ZPlusModClass(BigInteger.valueOf(2).pow(256)),1024);
    Assert.assertEquals(1, aes.getEncryptionIterations());
  }

  @Test
  public void testEncryptElementElement() {
    AESEncryptionScheme aes = new AESEncryptionScheme();          
      String humanPassphrase = new String("123456");      
      byte[] salt = new byte[]{1,2,3,4,5};
      Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
      Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
      Element ciphertext = aes.encrypt(key, plaintext);
      Assert.assertNotNull(ciphertext);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testEncryptElementElementNoKey() {
    AESEncryptionScheme aes = new AESEncryptionScheme();     
      Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
      Element ciphertext = aes.encrypt(null, plaintext);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testEncryptElementElementNoPlaintext() {
    AESEncryptionScheme aes = new AESEncryptionScheme();          
    String humanPassphrase = new String("123456");      
    byte[] salt = new byte[]{1,2,3,4,5};
    Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
    Element ciphertext = aes.encrypt(key, null);
  }
  
  @Test
  public void testDecryptElementElement() {
    AESEncryptionScheme aes = new AESEncryptionScheme();          
    String humanPassphrase = new String("123456");      
    byte[] salt = new byte[]{1,2,3,4,5};
    Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
    Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
    Element ciphertext = aes.encrypt(key, plaintext);
    Element decryptedText=aes.decrypt(key, ciphertext);
    Assert.assertEquals(plaintext, decryptedText);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testDecryptElementElementNoKey() {
    AESEncryptionScheme aes = new AESEncryptionScheme();          
    String humanPassphrase = new String("123456");      
    byte[] salt = new byte[]{1,2,3,4,5};
    Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
    Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
    Element ciphertext = aes.encrypt(key, plaintext);
    Element decryptedText=aes.decrypt(null, ciphertext);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testDecryptElementElementNoCipherText() {
    AESEncryptionScheme aes = new AESEncryptionScheme();          
    String humanPassphrase = new String("123456");      
    byte[] salt = new byte[]{1,2,3,4,5};
    Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
    Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
    Element ciphertext = aes.encrypt(key, plaintext);
    Element decryptedText=aes.decrypt(key, null);
  }

  @Test
  public void testGetDecryptionFunction() {
    AESEncryptionScheme aes = new AESEncryptionScheme();
    Assert.assertNotNull(aes.getDecryptionFunction());
    Assert.assertNotSame(aes.getDecryptionFunction(), aes.getEncryptionFunction());
    }

  @Test
  public void testGetEncryptionFunction() {
    AESEncryptionScheme aes = new AESEncryptionScheme();
    Assert.assertNotNull(aes.getEncryptionFunction());
    Assert.assertNotSame(aes.getEncryptionFunction(), aes.getDecryptionFunction());
  }

  

  @Test
  public void testGetKeyGenerator() {
    PasswordDerivedKeyGenerator keyGenerator = new PasswordDerivedKeyGeneratorClass();
    AESEncryptionScheme aes = new AESEncryptionScheme(1, keyGenerator);
    Assert.assertSame(keyGenerator, aes.getKeyGenerator());
    }

  @Test
  public void testGetPlaintextSpace() {
    AESEncryptionScheme aes = new AESEncryptionScheme();
    Assert.assertTrue(aes.getPlaintextSpace() instanceof ZPlus);
  }

  @Test
  public void testGetCiphertextSpace() {
    AESEncryptionScheme aes = new AESEncryptionScheme();
    Assert.assertTrue(aes.getPlaintextSpace() instanceof ZPlus);
  }

}
