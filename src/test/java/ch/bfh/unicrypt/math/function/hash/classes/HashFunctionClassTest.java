/**
 * 
 */
package ch.bfh.unicrypt.math.function.hash.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction.HashAlgorithm;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author reto
 * 
 */
public class HashFunctionClassTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#HashFunctionClass(String)}
   * .
   */
  @Test(expected = IllegalArgumentException.class)
  public void testHashFunctionClassNullString() {
    final Function hash = new HashFunction((String) null);
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#HashFunctionClass(String)}
   * .
   */
  @Test(expected = IllegalArgumentException.class)
  public void testHashFunctionClassWrongString() {
    final Function hash = new HashFunction("asdf");
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#HashFunctionClass(String)}
   * .
   */
  @Test(expected = NullPointerException.class)
  public void testHashFunctionClassNullHashAlgorithm() {
    final Function hash = new HashFunction((HashAlgorithm) null);
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#HashFunctionClass(String)}
   * .
   */
  @Test
  public void testHashFunctionClass() {
    Function hash1 = new HashFunction(HashAlgorithm.SHA1);
    Function hash2 = new HashFunction("SHA-1");
    Assert.assertEquals(hash1, hash2);
    hash1 = new HashFunction(HashAlgorithm.SHA512);
    hash2 = new HashFunction("SHA-512");
    Assert.assertEquals(hash1, hash2);
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement[])}
   * .
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testApplyGroupElementArrayNull() {
    final Function hash = new HashFunction("SHA-1");
    final Element[] elements = null;
    hash.apply(elements);
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement[])}
   * .
   */
  @Test(expected = IllegalArgumentException.class)
  public void testApplyGroupElementNull() {
    final Function hash = new HashFunction("SHA-1");
    final Element element = null;
    hash.apply(element);
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement)}
   * .
   */
  @Test
  public void testApplyGroupElementSHA1() {
     final Function hash = new HashFunction("SHA-1");
     final ZPlus zPlus = ZPlus.getInstance();
     final Element element = zPlus.getElement(new BigInteger(1,
     "Hello World".getBytes()));
     final AtomicElement hashValue = (AtomicElement) hash.apply(element);
     final BigInteger linuxSHA1Value = new
     BigInteger("0a4d55a8d778e5022fab701977c5d840bbc486d0", 16);
     Assert.assertEquals(linuxSHA1Value, hashValue.getValue());
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement[])}
   * .
   */
  @Test
  public void testApplyGroupElementArraySHA1() {
//     final Function hash = new HashFunctionClass("SHA-1");
//     final ZPlus zPlus = ZPlusClass.getInstance();
//     final Element elementHello = zPlus.createElement(new BigInteger(1,
//     "Hello".getBytes()));
//     final Element elementSpace = zPlus.createElement(new BigInteger(1,
//     " ".getBytes()));
//     final Element elementWorld = zPlus.createElement(new BigInteger(1,
//     "World".getBytes()));
//     final AtomicElement hashValue = (AtomicElement) hash.apply(elementHello,
//     elementSpace, elementWorld);
//     final BigInteger linuxSHA1Value = new
//     BigInteger("0a4d55a8d778e5022fab701977c5d840bbc486d0", 16);
//     Assert.assertEquals(linuxSHA1Value, hashValue.getBigInteger());
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement)}
   * .
   */
  @Test
  public void testApplyGroupElementSHA256() {
     final Function hash = new HashFunction(HashAlgorithm.SHA256);
     final ZPlus zPlus = ZPlus.getInstance();
     final Element element = zPlus.getElement(new BigInteger(1,
     "Hello World".getBytes()));
     final AtomicElement hashValue = (AtomicElement) hash.apply(element);
     final BigInteger linuxSHA1Value = new
     BigInteger("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
     16);
     Assert.assertEquals(linuxSHA1Value, hashValue.getValue());
  }

  /**
   * Test method for
   * {@link ch.bfh.unicrypt.math.function.hash.classes.HashFunction#apply(ch.bfh.unicrypt.math.element.interfaces.GroupElement[])}
   * .
   */
  @Test
  public void testApplyGroupElementArraySHA256() {
    // final Function hash = new HashFunctionClass(HashAlgorithm.SHA256);
    // final ZPlus zPlus = ZPlusClass.getInstance();
    // final Element elementHello = zPlus.createElement(new BigInteger(1,
    // "Hello".getBytes()));
    // final Element elementSpace = zPlus.createElement(new BigInteger(1,
    // " ".getBytes()));
    // final Element elementWorld = zPlus.createElement(new BigInteger(1,
    // "World".getBytes()));
    // final AtomicElement hashValue = (AtomicElement) hash.apply(elementHello,
    // elementSpace, elementWorld);
    // final BigInteger linuxSHA1Value = new
    // BigInteger("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
    // 16);
    // Assert.assertEquals(linuxSHA1Value, hashValue.getBigInteger());
  }

}
