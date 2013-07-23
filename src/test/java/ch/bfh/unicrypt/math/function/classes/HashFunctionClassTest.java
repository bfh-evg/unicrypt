package ch.bfh.unicrypt.math.function.classes;

import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.function.classes.HashFunction.HashAlgorithm;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;

public class HashFunctionClassTest {

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

//  @Test
//  public void testHashCode() {
//    fail("Not yet implemented");
//  }

  @Test
  public void testHashFunctionClassCompareToLinuxSha256Sum() {
    //This test is equal to a sha256sum of 'HelloWorld'
    final ZPlus zp = ZPlus.getInstance();    
    HashFunction hashFunction=new HashFunction();
    AtomicElement element=null;
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("HelloWorld".getBytes())));
    Assert.assertEquals(new BigInteger("872e4e50ce9990d8b041330c47c9ddd11bec6b503ae9386a99da8584e9bb12c4",16), element.getValue());
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("A".getBytes())));
    Assert.assertEquals(new BigInteger("559aead08264d5795d3909718cdd05abd49572e84fe55590eef31a88a08fdffd",16), element.getValue());
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("B".getBytes())));
    Assert.assertEquals(new BigInteger("df7e70e5021544f4834bbee64a9e3789febc4be81470df629cad6ddb03320a5c",16), element.getValue());
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("C".getBytes())));
    Assert.assertEquals(new BigInteger("6b23c0d5f35d1b11f9b683f0b0a617355deb11277d91ae091d399c655b87940d",16), element.getValue());
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("D".getBytes())));
    Assert.assertEquals(new BigInteger("3f39d5c348e5b79d06e842c114e6cc571583bbf44e4b0ebfda1a01ec05745d43",16), element.getValue());
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("E".getBytes())));
    Assert.assertEquals(new BigInteger("a9f51566bd6705f7ea6ad54bb9deb449f795582d6529a0e22207b8981233ec58",16), element.getValue());       
    
  }
  @Test
  public void testHashFunctionClassNotNegative() {
    //The Hash always has to be positive
    final ZPlus zp = ZPlus.getInstance();    
    HashFunction hashFunction=new HashFunction();
    AtomicElement element=null;
    element=hashFunction.apply(zp.createEncodedElement(new BigInteger("HelloWorld".getBytes())));        
    for(int i=0;i<10000;i++){
      element=hashFunction.apply(zp.createEncodedElement(new BigInteger((""+i+"").getBytes())));
      Assert.assertEquals(1,element.getValue().signum());  
    }
    
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

//  @Test
//  public void testHashFunctionClassZPlusMod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHashFunctionClassString() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHashFunctionClassHashAlgorithm() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHashFunctionClassStringZPlusMod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHashFunctionClassHashAlgorithmZPlusMod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testApplyElement() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testApplyElementRandom() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testGetDomain() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testGetCoDomain() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testEqualsObject() {
//    fail("Not yet implemented");
//  }

}
