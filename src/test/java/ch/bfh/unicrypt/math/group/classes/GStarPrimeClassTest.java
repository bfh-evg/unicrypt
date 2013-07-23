package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import ch.bfh.unicrypt.math.group.interfaces.GStarMod;

@SuppressWarnings("static-method")
public class GStarPrimeClassTest {

  private static final GStarMod g_2_1 = new GStarMod(BigInteger.valueOf(2), false); // order=1
  private static final GStarMod g_3_1 = new GStarMod(BigInteger.valueOf(3), false); // order=2
  private static final GStarMod g_3_2 = new GStarMod(BigInteger.valueOf(3), false, BigInteger.valueOf(2));
  private static final GStarMod g_5_1 = new GStarMod(BigInteger.valueOf(5), false); // order=4
  private static final GStarMod g_5_2 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2));
  private static final GStarMod g_5_4 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_7_1 = new GStarMod(BigInteger.valueOf(7), false); // order=6
  private static final GStarMod g_7_2 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(2)); 
  private static final GStarMod g_7_3 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(3)); 
  private static final GStarMod g_7_6 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(2), BigInteger.valueOf(3)); 

  @Test
  public final void testGStarPrimeClassBigIntegerBigIntegerArray() {
    Assert.assertEquals(g_2_1.getModulus(), new GStarPrime(BigInteger.valueOf(2)).getModulus());
    Assert.assertEquals(g_2_1.getOrder(), new GStarPrime(BigInteger.valueOf(2)).getOrder());
    Assert.assertEquals(g_3_1.getModulus(), new GStarPrime(BigInteger.valueOf(3)).getModulus());
    Assert.assertEquals(g_3_1.getOrder(), new GStarPrime(BigInteger.valueOf(3)).getOrder());
    Assert.assertEquals(g_3_2.getModulus(), new GStarPrime(BigInteger.valueOf(3), BigInteger.valueOf(2)).getModulus());
    Assert.assertEquals(g_3_2.getOrder(), new GStarPrime(BigInteger.valueOf(3), BigInteger.valueOf(2)).getOrder());
    Assert.assertEquals(g_5_1.getModulus(), new GStarPrime(BigInteger.valueOf(5)).getModulus());
    Assert.assertEquals(g_5_1.getOrder(), new GStarPrime(BigInteger.valueOf(5)).getOrder());
    Assert.assertEquals(g_5_2.getModulus(), new GStarPrime(BigInteger.valueOf(5), BigInteger.valueOf(2)).getModulus());
    Assert.assertEquals(g_5_2.getOrder(), new GStarPrime(BigInteger.valueOf(5), BigInteger.valueOf(2)).getOrder());
    Assert.assertEquals(g_5_4.getModulus(), new GStarPrime(BigInteger.valueOf(5), BigInteger.valueOf(2), BigInteger.valueOf(2)).getModulus());
    Assert.assertEquals(g_5_4.getOrder(), new GStarPrime(BigInteger.valueOf(5), BigInteger.valueOf(2), BigInteger.valueOf(2)).getOrder());
    Assert.assertEquals(g_7_1.getModulus(), new GStarPrime(BigInteger.valueOf(7)).getModulus());
    Assert.assertEquals(g_7_1.getOrder(), new GStarPrime(BigInteger.valueOf(7)).getOrder());
    Assert.assertEquals(g_7_2.getModulus(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(2)).getModulus());
    Assert.assertEquals(g_7_2.getOrder(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(2)).getOrder());
    Assert.assertEquals(g_7_3.getModulus(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(3)).getModulus());
    Assert.assertEquals(g_7_3.getOrder(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(3)).getOrder());
    Assert.assertEquals(g_7_6.getModulus(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(2), BigInteger.valueOf(3)).getModulus());
    Assert.assertEquals(g_7_6.getOrder(), new GStarPrime(BigInteger.valueOf(7), BigInteger.valueOf(2), BigInteger.valueOf(3)).getOrder());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException1() {
    new GStarPrime(BigInteger.valueOf(8));
  }
  
  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException2() {
    new GStarPrime(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException3() {
    new GStarPrime(BigInteger.valueOf(11), (BigInteger[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException4() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(4)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException5() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException6() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2), null});
  }

  @Test
  public final void testGStarPrimeClassBigIntegerBigIntegerArrayIntArray() {
    Assert.assertEquals(g_2_1.getModulus(), new GStarPrime(BigInteger.valueOf(2), new BigInteger[]{}, new int[]{}).getModulus());
    Assert.assertEquals(g_2_1.getOrder(), new GStarPrime(BigInteger.valueOf(2), new BigInteger[]{}, new int[]{}).getOrder());
    Assert.assertEquals(g_3_1.getModulus(), new GStarPrime(BigInteger.valueOf(3), new BigInteger[]{}, new int[]{}).getModulus());
    Assert.assertEquals(g_3_1.getOrder(), new GStarPrime(BigInteger.valueOf(3), new BigInteger[]{}, new int[]{}).getOrder());
    Assert.assertEquals(g_3_2.getModulus(), new GStarPrime(BigInteger.valueOf(3), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getModulus());
    Assert.assertEquals(g_3_2.getOrder(), new GStarPrime(BigInteger.valueOf(3), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getOrder());
    Assert.assertEquals(g_5_1.getModulus(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{}, new int[]{}).getModulus());
    Assert.assertEquals(g_5_1.getOrder(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{}, new int[]{}).getOrder());
    Assert.assertEquals(g_5_2.getModulus(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getModulus());
    Assert.assertEquals(g_5_2.getOrder(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getOrder());
    Assert.assertEquals(g_5_4.getModulus(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{2}).getModulus());
    Assert.assertEquals(g_5_4.getOrder(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{2}).getOrder());
    Assert.assertEquals(g_5_4.getModulus(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2),BigInteger.valueOf(2)}, new int[]{1, 1}).getModulus());
    Assert.assertEquals(g_5_4.getOrder(), new GStarPrime(BigInteger.valueOf(5), new BigInteger[]{BigInteger.valueOf(2),BigInteger.valueOf(2)}, new int[]{1, 1}).getOrder());
    Assert.assertEquals(g_7_1.getModulus(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{}, new int[]{}).getModulus());
    Assert.assertEquals(g_7_1.getOrder(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{}, new int[]{}).getOrder());
    Assert.assertEquals(g_7_2.getModulus(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getModulus());
    Assert.assertEquals(g_7_2.getOrder(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1}).getOrder());
    Assert.assertEquals(g_7_3.getModulus(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(3)}, new int[]{1}).getModulus());
    Assert.assertEquals(g_7_3.getOrder(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(3)}, new int[]{1}).getOrder());
    Assert.assertEquals(g_7_6.getModulus(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(3)}, new int[]{1, 1}).getModulus());
    Assert.assertEquals(g_7_6.getOrder(), new GStarPrime(BigInteger.valueOf(7), new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(3)}, new int[]{1, 1}).getOrder());
  }
  
  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException7() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2)}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException8() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException9() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{0});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException10() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{2});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException11() {
    new GStarPrime(BigInteger.valueOf(11), new BigInteger[]{BigInteger.valueOf(2)}, new int[]{1, 1});
  }

}
