package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import ch.bfh.unicrypt.math.group.interfaces.GStarMod;
import ch.bfh.unicrypt.math.utility.MathUtil;

import org.junit.Ignore;

@SuppressWarnings("static-method")
public class GStarSaveClassTest {

  private static final GStarMod g_5_2 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2));
  private static final GStarMod g_5_4 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_7_3 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(3));
  private static final GStarMod g_7_6 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(2), BigInteger.valueOf(3));

  @Test
  public final void testGStarSaveClassBigInteger() {
    Assert.assertEquals(g_5_2.getModulus(), new GStarSave(BigInteger.valueOf(5)).getModulus());
    Assert.assertEquals(g_5_2.getOrder(), new GStarSave(BigInteger.valueOf(5)).getOrder());
    Assert.assertEquals(g_5_4.getModulus(), new GStarSave(BigInteger.valueOf(5), false).getModulus());
    Assert.assertEquals(g_5_4.getOrder(), new GStarSave(BigInteger.valueOf(5), false).getOrder());
    Assert.assertEquals(g_7_3.getModulus(), new GStarSave(BigInteger.valueOf(7)).getModulus());
    Assert.assertEquals(g_7_3.getOrder(), new GStarSave(BigInteger.valueOf(7)).getOrder());
    Assert.assertEquals(g_7_6.getModulus(), new GStarSave(BigInteger.valueOf(7), false).getModulus());
    Assert.assertEquals(g_7_6.getOrder(), new GStarSave(BigInteger.valueOf(7), false).getOrder());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarSaveConstructorException1() {
    new GStarSave(BigInteger.valueOf(8));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarSaveConstructorException2() {
    new GStarSave(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarSaveConstructorException3() {
    new GStarSave(BigInteger.valueOf(13));
  }
}
