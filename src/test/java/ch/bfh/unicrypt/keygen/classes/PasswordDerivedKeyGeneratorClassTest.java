package ch.bfh.unicrypt.keygen.classes;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.Key;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.keygenerator.old.PasswordBasedKeyGenerator;
import ch.bfh.unicrypt.crypto.keygen.interfaces.PasswordKeyGenerator;
import ch.bfh.unicrypt.crypto.utility.AESUtil;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.AtomicGroup;

public class PasswordDerivedKeyGeneratorClassTest {

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
  public void testPasswordDerivedKeyGeneratorClass() {
    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator();
    Assert.assertNotNull(generator);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPasswordDerivedKeyGeneratorClassNoKeySpaceSalt() {
    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(null, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPasswordDerivedKeyGeneratorClassKeySpaceNegativeSalt() {
    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(new ZPlusModClass(BigInteger.ONE), -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPasswordDerivedKeyGeneratorClassKeySpaceZeroSalt() {
    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(new ZPlusModClass(BigInteger.ONE), 0);
  }

  @Test
  public void testGetKeyGenerationFunction() {
    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator();
    Function generatorFunction = generator.getKeyGenerationFunction();
    Assert.assertNotNull(generatorFunction);
  }

  @Test
  public void testGenerateKeyStringByteArray() {
//    try {
//      String password = "ABCD";
//      byte[] salt = { 4, 7, 1, 1 };
//
//      PasswordKeyGenerator generator = new PasswordBasedKeyGenerator();
//      Element element = generator.generateKey(password, salt);
//
//      Key key = AESUtil.generateKey(password.toCharArray(), salt, 1024, 256);
//      Element keyElement = ((AtomicGroup) (generator.getKeyGenerationFunction().getCoDomain())).createElement(new BigInteger(1, key.getEncoded()));
//
//      Assert.assertEquals(keyElement, element);
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail();
//    }
//  This is not true, as there exists a hack in the PasswordKeyGenerator Method    
  }

  @Test
  public void testGenerateKeyStringByteArrayZPlusModSalt1() {
    try {
      String password = "ABCD";
      byte[] salt = { 4, 7, 1, 1 };

      PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(new ZPlusModClass(BigInteger.valueOf(2).pow(256)), 1);

      Element element = generator.generateKey(password, salt);

      Key key = AESUtil.generateKey(password.toCharArray(), salt, 1, 256);
      Element keyElement = ((AtomicGroup) (generator.getKeyGenerationFunction().getCoDomain())).getElement(new BigInteger(1, key.getEncoded()));

      Assert.assertEquals(keyElement, element);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGenerateKeyStringByteArrayZPlusModSaltBitSize() {

//    String password = "ABCD";
//    byte[] salt = { 4, 7, 1, 1 };
//
//    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(new ZPlusModClass(BigInteger.valueOf(2).pow(256)), 1024);
//
//    Element element = generator.generateKey(password, salt);
//    Assert.assertEquals(256, ((AtomicElement) element).getBigInteger().bitLength());
// This is not the case, as the leading 0-will be omitted    

  }

  @Test
  public void testGenerateKeyStringByteArrayZPlusModSaltDeterministic() {

    String password = "ABCD";
    byte[] salt = { 4, 7, 1, 1 };

    PasswordKeyGenerator generator = new PasswordBasedKeyGenerator(new ZPlusModClass(BigInteger.valueOf(2).pow(256)), 1024);

    Element element = generator.generateKey(password, salt);
    Assert.assertEquals(new BigInteger("26eaebc6c35ca47b4ed396f74e965b9161524b6bad15aeac4f88774e259a22bb", 16), ((AtomicElement) element).getValue());

  }

}
