package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.cyclicgroup.classes.BooleanGroup;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.classes.MultiplicativeElement;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZStarMod;

@SuppressWarnings("static-method")
public class ZStarModClassTest {

  private static ZStarMod zStarMod = new ZStarMod(BigInteger.valueOf(2),BigInteger.valueOf(2),BigInteger.valueOf(5));
  private static Random random = new Random();
  private static MultiplicativeElement element1, element2;
  private static final MultiplicativeElement MINUS_ONE = zStarMod.getElement(-1);
  private static final MultiplicativeElement ONE = zStarMod.getElement(1);
  private static final MultiplicativeElement THREE = zStarMod.getElement(3);
  private static final MultiplicativeElement SEVEN = zStarMod.getElement(7);
  private static final MultiplicativeElement NINE = zStarMod.getElement(9);
  private static final MultiplicativeElement ELEVEN = zStarMod.getElement(11);
  private static final MultiplicativeElement THIRTEEN = zStarMod.getElement(13);
  private static final MultiplicativeElement SEVENTEEN = zStarMod.getElement(17);
  private static final MultiplicativeElement NINETEEN = zStarMod.getElement(19);
  private static final MultiplicativeElement OTHER_ELEMENT = new GStarSave(BigInteger.valueOf(11)).getIdentityElement();

  @Test 
  public void testZStarModTwo() {
    ZStarMod zStarModTwo = new ZStarMod(BigInteger.valueOf(2));
    MultiplicativeElement elt1 = zStarModTwo.getElement(-1);
    MultiplicativeElement elt2 = zStarModTwo.getElement(1);
    MultiplicativeElement elt3 = zStarModTwo.getElement(9);
    MultiplicativeElement elt4 = zStarModTwo.getElement(-5);
    Assert.assertTrue(elt1.equals(elt2));
    Assert.assertTrue(elt1.equals(elt3));
    Assert.assertTrue(elt1.equals(elt4));
    Assert.assertTrue(elt1.equals(elt2.apply(elt3)));
    Assert.assertTrue(elt1.equals(elt2.invert()));
    Assert.assertTrue(elt1.equals(elt2.selfApply()));
    Assert.assertTrue(elt1.equals(elt2.selfApply(5)));
    Assert.assertTrue(zStarModTwo.areEqual(elt1, elt2));
    Assert.assertFalse(zStarModTwo.contains(BigInteger.ZERO));
    Assert.assertTrue(zStarModTwo.contains(BigInteger.ONE));
    Assert.assertFalse(zStarModTwo.contains(BigInteger.valueOf(10)));
    Assert.assertTrue(zStarModTwo.isIdentity(elt1));
    Assert.assertEquals(elt1.getValue(), BigInteger.ONE);
    Assert.assertEquals(elt2.getValue(), BigInteger.ONE);
    Assert.assertEquals(elt3.getValue(), BigInteger.ONE);
    for (int i=1; i<=100; i++) {
      Assert.assertEquals(zStarModTwo.getRandomElement().getValue(), BigInteger.ONE);
    }
    Assert.assertEquals(zStarModTwo.getOrder(),BigInteger.ONE);
    Assert.assertEquals(zStarModTwo.getMinOrder(),BigInteger.ONE);
    Assert.assertEquals(zStarModTwo.getOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(zStarModTwo.getMinOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(zStarModTwo.getModulus(), BigInteger.valueOf(2));
  }

  @Test
  public void testZStarModClassBigInteger() {
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(2)).getModulus(), BigInteger.valueOf(2));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(3)).getModulus(), BigInteger.valueOf(3));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(10)).getModulus(), BigInteger.valueOf(10));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerException1() {
    new ZStarMod(BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerException2() {
    new ZStarMod(BigInteger.ONE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerException3() {
    new ZStarMod(BigInteger.valueOf(-10));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerException4() {
    new ZStarMod((BigInteger)null);
  }

  @Test
  public void testZStarModClassBigIntegerArray() {
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)).getModulus(), BigInteger.valueOf(12));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(5)).getModulus(), BigInteger.valueOf(45));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(5)).getModulus(), BigInteger.valueOf(75));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayException1() {
    new ZStarMod((BigInteger[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayException2() {
    new ZStarMod(new BigInteger[] {});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayException3() {
    new ZStarMod(new BigInteger[] {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayException4() {
    new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), null, BigInteger.valueOf(3)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayException5() {
    new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4)});
  }

  @Test
  public void testZStarModClassBigIntegerArrayIntegerArray() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)}, 
        new int[] {1, 2, 1}).getModulus(), BigInteger.valueOf(24));
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(5)},
        new int[] {2, 3, 1}).getModulus(), BigInteger.valueOf(9*27*5));
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(5)}, 
        new int[] {1, 1, 1}).getModulus(), BigInteger.valueOf(75));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException1() {
    Assert.assertEquals(new ZStarMod(null, new int[] {1, 2, 1}).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException2() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)}, null).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException3() {
    Assert.assertEquals(new ZStarMod((BigInteger[]) null, null).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException4() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), null, BigInteger.valueOf(3)}, 
        new int[] {1, 2, 1}).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException5() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)}, 
        new int[] {1, -2, 1}).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException6() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3)}, 
        new int[] {1, 2}).getModulus(), BigInteger.valueOf(24));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZStarModClassBigIntegerArrayIntegerArrayException7() {
    Assert.assertEquals(new ZStarMod(new BigInteger[] {}, 
        new int[] {}).getModulus(), BigInteger.valueOf(24));
  }

  @Test
  public void testGetModulus() {
    Assert.assertEquals(zStarMod.getModulus(), BigInteger.valueOf(20));
  }

  @Test
  public void testGetPrimeFactors() {
    BigInteger[] primes = new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(5)};
    Assert.assertTrue(Arrays.equals(zStarMod.getPrimeFactors(), primes));
  }

  @Test
  public void testCreateRandomElement() {
    MultiplicativeElement element;
    for (int i=1; i<zStarMod.getModulus().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        do {
          element = zStarMod.getRandomElement();
          Assert.assertTrue(element != null);
          Assert.assertTrue(element.getValue() != null);
        } while (element.getValue().intValue() != i);
      }
    }
  }

  @Test
  public void testCreateRandomElementRandom() {
    MultiplicativeElement element;
    for (int i=1; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        do {
          element = zStarMod.getRandomElement(random);
          Assert.assertTrue(element != null);
          Assert.assertTrue(element.getValue() != null);
        } while (element.getValue().intValue() != i);
      }
    }
  }

  @Test
  public void testGetIdentityElement() {
    element1 = zStarMod.getIdentityElement();
    element2 = zStarMod.getIdentityElement();
    Assert.assertEquals(element1.getValue(), BigInteger.ONE);
    Assert.assertEquals(element1, element2);
    Assert.assertSame(element1, element2);
  }

  @Test
  public void testApplyElementElement() {
    Assert.assertEquals(zStarMod.apply(ONE, THREE), THREE);
    Assert.assertEquals(zStarMod.apply(THREE, THREE), NINE);
    Assert.assertEquals(zStarMod.apply(THREE, SEVEN), ONE);
    Assert.assertEquals(zStarMod.apply(MINUS_ONE, ONE), NINETEEN);
    Assert.assertEquals(zStarMod.apply(ONE, ONE), ONE);
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          if (zStarMod.getModulus().gcd(BigInteger.valueOf(j)).equals(BigInteger.ONE)) {
            element2 = zStarMod.getElement(j);
            Assert.assertTrue(zStarMod.apply(element1, element2).getValue().intValue() == (i*j) % zStarMod.getModulus().intValue());
          }
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException1() {
    zStarMod.apply((Element)null, (Element)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException2() {
    zStarMod.apply(NINE, (Element)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException3() {
    zStarMod.apply((Element)null, NINE);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException4() {
    zStarMod.apply(OTHER_ELEMENT, NINE);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException5() {
    zStarMod.apply(NINE, OTHER_ELEMENT);
  }

  @Test
  public void testApplyElementArray() {
    Assert.assertEquals(zStarMod.apply().getValue(), BigInteger.ONE);
    Assert.assertEquals(zStarMod.apply(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zStarMod.apply(MINUS_ONE), NINETEEN);
    Assert.assertEquals(zStarMod.apply(ONE, ONE, ONE), ONE);
    Assert.assertEquals(zStarMod.apply(MINUS_ONE, ONE, ONE), NINETEEN);
    Assert.assertEquals(zStarMod.apply(MINUS_ONE, ONE, MINUS_ONE), ONE);
    Assert.assertEquals(zStarMod.apply(ONE, THREE, SEVEN, NINE), NINE);
    Assert.assertEquals(zStarMod.apply(new Element[] {}), ONE);
    Assert.assertEquals(zStarMod.apply(new Element[] {ONE, THREE, SEVEN, NINE}), NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException1() {
    zStarMod.apply((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException2() {
    zStarMod.apply(new Element[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException3() {
    zStarMod.apply(new Element[] {ONE, NINE, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException4() {
    zStarMod.apply(new Element[] {ONE, NINE, OTHER_ELEMENT});
  }

  @Test
  public void testSelfApplyElementBigInteger() {
    Assert.assertEquals(zStarMod.selfApply(ONE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.selfApply(NINE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.selfApply(ONE, BigInteger.TEN), ONE);
    Assert.assertEquals(zStarMod.selfApply(MINUS_ONE, BigInteger.ONE), NINETEEN);
    Assert.assertEquals(zStarMod.selfApply(MINUS_ONE, BigInteger.valueOf(8)), ONE);
    Assert.assertEquals(zStarMod.selfApply(SEVEN, BigInteger.valueOf(2)), NINE);
    Assert.assertEquals(zStarMod.selfApply(THREE, BigInteger.valueOf(-6)), NINE);
    Assert.assertEquals(zStarMod.selfApply(THREE, BigInteger.valueOf(10)), NINE);
    BigInteger amount;
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          amount = BigInteger.valueOf(j);
          Assert.assertEquals(zStarMod.selfApply(element1, amount).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException1() {
    zStarMod.selfApply(ONE, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException2() {
    zStarMod.selfApply(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException3() {
    zStarMod.selfApply(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException4() {
    zStarMod.selfApply(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testSelfApplyElementAtomicElement() {
    AtomicElement amount;
    ZPlus zPlus = ZPlus.getInstance();
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          amount = zPlus.getElement(j);
          Assert.assertEquals(zStarMod.selfApply(element1, amount).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException1() {
    zStarMod.selfApply(ONE, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException2() {
    zStarMod.selfApply(null, NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyAtomicElementException3() {
    zStarMod.selfApply(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException4() {
    zStarMod.selfApply(OTHER_ELEMENT, NINE);
  }

  @Test
  public void testSelfApplyElementInt() {
    Assert.assertEquals(zStarMod.selfApply(ONE, 0), ONE);
    Assert.assertEquals(zStarMod.selfApply(NINE, 0), ONE);
    Assert.assertEquals(zStarMod.selfApply(ONE, 10), ONE);
    Assert.assertEquals(zStarMod.selfApply(MINUS_ONE, 1), NINETEEN);
    Assert.assertEquals(zStarMod.selfApply(MINUS_ONE, 8), ONE);
    Assert.assertEquals(zStarMod.selfApply(SEVEN, 2), NINE);
    Assert.assertEquals(zStarMod.selfApply(THREE, -6), NINE);
    Assert.assertEquals(zStarMod.selfApply(THREE, 10), NINE);
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          Assert.assertEquals(zStarMod.selfApply(element1, j).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException1() {
    zStarMod.selfApply(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException2() {
    zStarMod.selfApply(OTHER_ELEMENT, 10);
  }

  @Test
  public void testSelfApplyElement() {
    Assert.assertEquals(zStarMod.selfApply(MINUS_ONE), ONE);
    Assert.assertEquals(zStarMod.selfApply(ONE), ONE);
    Assert.assertEquals(zStarMod.selfApply(THREE), NINE);
    Assert.assertEquals(zStarMod.selfApply(SEVEN), NINE);
    Assert.assertEquals(zStarMod.selfApply(ELEVEN), ONE);
    Assert.assertEquals(zStarMod.selfApply(THIRTEEN), NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException1() {
    zStarMod.selfApply(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException2() {
    zStarMod.selfApply(OTHER_ELEMENT);
  }

  @Test
  public void testMultiSelfApplyElementArrayBigIntegerArray() {
    Assert.assertEquals(zStarMod.multiSelfApply(new Element[] {}, new BigInteger[] {}), ONE);
    Assert.assertEquals(zStarMod.multiSelfApply(new Element[] {ONE}, new BigInteger[] {BigInteger.ZERO}), ONE);
    Assert.assertEquals(zStarMod.multiSelfApply(new Element[] {ONE}, new BigInteger[] {BigInteger.TEN}), ONE);
    Assert.assertEquals(zStarMod.multiSelfApply(new Element[] {ONE, THREE}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(2)}), NINE);
    Assert.assertEquals(zStarMod.multiSelfApply(new Element[] {ONE, THREE, SEVEN}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(2), BigInteger.ONE}), THREE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    zStarMod.multiSelfApply(new Element[] {ONE, ONE}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    zStarMod.multiSelfApply(null, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    zStarMod.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    zStarMod.multiSelfApply(new Element[] {ONE, ONE, THREE}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    zStarMod.multiSelfApply(new Element[] {OTHER_ELEMENT, ONE}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test
  public void testInvert() {
    Assert.assertEquals(zStarMod.invert(MINUS_ONE), NINETEEN);
    Assert.assertEquals(zStarMod.invert(ONE), ONE);
    Assert.assertEquals(zStarMod.invert(THREE), SEVEN);
    Assert.assertEquals(zStarMod.invert(SEVEN), THREE);
    Assert.assertEquals(zStarMod.invert(NINE), NINE);
    Assert.assertEquals(zStarMod.invert(ELEVEN), ELEVEN);
    Assert.assertEquals(zStarMod.invert(THIRTEEN), SEVENTEEN);
    Assert.assertEquals(zStarMod.invert(SEVENTEEN), THIRTEEN);
    Assert.assertEquals(zStarMod.invert(NINETEEN), NINETEEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException1() {
    zStarMod.invert(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException2() {
    zStarMod.invert(OTHER_ELEMENT);
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(zStarMod.getOrder(), BigInteger.valueOf(8));  
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(2),BigInteger.valueOf(2),BigInteger.valueOf(2)).getOrder(), BigInteger.valueOf(4));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(3)).getOrder(), BigInteger.valueOf(2));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(4)).getOrder(), Group.UNKNOWN_ORDER);
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(5)).getOrder(), BigInteger.valueOf(4));
  }

  @Test
  public void testGetMinOrder() {
    Assert.assertEquals(zStarMod.getMinOrder(), BigInteger.valueOf(8));  
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(2),BigInteger.valueOf(2),BigInteger.valueOf(2)).getMinOrder(), BigInteger.valueOf(4));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(3)).getMinOrder(), BigInteger.valueOf(2));
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(4)).getMinOrder(), BigInteger.ONE);
    Assert.assertEquals(new ZStarMod(BigInteger.valueOf(5)).getMinOrder(), BigInteger.valueOf(4));
  }

  @Test
  public void testHasSameOrder() {
    Assert.assertTrue(zStarMod.hasSameOrder(zStarMod));
    Assert.assertTrue(zStarMod.hasSameOrder(new ZPlusModClass(BigInteger.valueOf(8))));
    Assert.assertFalse(zStarMod.hasSameOrder(new ZStarMod(BigInteger.valueOf(9))));
    Assert.assertFalse(zStarMod.hasSameOrder(SingletonGroup.getInstance()));
    Assert.assertFalse(zStarMod.hasSameOrder(BooleanGroup.getInstance()));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasSameOrderException() {
    zStarMod.hasSameOrder(null);  
  }

  @Test 
  public void testGetOrderGroup() {
    Assert.assertEquals(zStarMod.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(8)));
  }

  @Test (expected=UnsupportedOperationException.class)
  public void testGetOrderGroupException() {
    new ZStarMod(BigInteger.valueOf(4)).getOrderGroup();
  }

  @Test
  public void testContainsElement() {
    Assert.assertTrue(zStarMod.contains(MINUS_ONE));
    Assert.assertTrue(zStarMod.contains(ONE));
    Assert.assertTrue(zStarMod.contains(THREE));
    Assert.assertTrue(zStarMod.contains(NINE));
    Assert.assertFalse(zStarMod.contains(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testContainsElementException() {
    zStarMod.contains(null);
  }

  @Test
  public void testIsIdentityElement() {
    Assert.assertTrue(zStarMod.isIdentity(zStarMod.getIdentityElement()));
    Assert.assertTrue(zStarMod.isIdentity(ONE));
    Assert.assertFalse(zStarMod.isIdentity(THREE));
    Assert.assertFalse(zStarMod.isIdentity(SEVEN));
    Assert.assertFalse(zStarMod.isIdentity(NINETEEN));
    Assert.assertFalse(zStarMod.isIdentity(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testIsIdentityElementException() {
    zStarMod.isIdentity(null);
  }

  @Test
  public void testAreEqualElements() {
    Assert.assertTrue(zStarMod.areEqual(MINUS_ONE, MINUS_ONE));
    Assert.assertTrue(zStarMod.areEqual(MINUS_ONE, NINETEEN));
    Assert.assertTrue(zStarMod.areEqual(ONE, ONE));
    Assert.assertTrue(zStarMod.areEqual(THREE, THREE));
    Assert.assertTrue(zStarMod.areEqual(OTHER_ELEMENT, OTHER_ELEMENT));
    Assert.assertFalse(zStarMod.areEqual(ONE, THREE));
    Assert.assertFalse(zStarMod.areEqual(ONE, MINUS_ONE));
    Assert.assertFalse(zStarMod.areEqual(ONE, OTHER_ELEMENT));
    Assert.assertFalse(zStarMod.areEqual(OTHER_ELEMENT, NINE));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException1() {
    zStarMod.areEqual(null, NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException2() {
    zStarMod.areEqual(NINE, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException3() {
    zStarMod.areEqual(null, null);
  }

  @Test
  public void testGetArity() {
    Assert.assertTrue(zStarMod.getArity()==1);
  }

  @Test
  public void testHasElement() {
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(-1)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(1)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(3)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(7)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(9)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(13)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(17)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(19)));
    Assert.assertTrue(zStarMod.contains(BigInteger.valueOf(11)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(-2)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(0)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(2)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(4)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(5)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(6)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(8)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(10)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(20)));
    Assert.assertFalse(zStarMod.contains(BigInteger.valueOf(30)));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasElementException() {
    zStarMod.contains(null);
  }

  @Test
  public void testCreateElementBigInteger() {
    for (int i=-5; i<=25; i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        Assert.assertEquals(zStarMod.getElement(BigInteger.valueOf(i)).getValue(), BigInteger.valueOf(i).mod(zStarMod.getModulus()));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementBigIntegerException() {
    zStarMod.getElement((BigInteger) null);
  }

  @Test
  public void testCreateElementInt() {
    for (int i=-5; i<=25; i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        Assert.assertEquals(zStarMod.getElement(i).getValue(), BigInteger.valueOf(i).mod(zStarMod.getModulus()));
      }
    }
  }

  @Test
  public void testCreateElementAtomicElement() {
    for (int i=-5; i<=25; i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        Assert.assertEquals(zStarMod.getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(zStarMod.getModulus()));
        Assert.assertEquals(zStarMod.getElement(zStarMod.getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(zStarMod.getModulus()));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementAtomicElementException() {
    zStarMod.getElement((AtomicElement) null);
  }  

  @Test
  public void testGetBigInteger() {
    // has been tested in many test methods above
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException1() {
    zStarMod.getValue(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException2() {
    zStarMod.getValue(ZPlus.getInstance().getElement(5));
  }

  @Test
  public void testMultiplyElementElement() {
    Assert.assertEquals(zStarMod.multiply(ONE, THREE), THREE);
    Assert.assertEquals(zStarMod.multiply(THREE, THREE), NINE);
    Assert.assertEquals(zStarMod.multiply(THREE, SEVEN), ONE);
    Assert.assertEquals(zStarMod.multiply(MINUS_ONE, ONE), NINETEEN);
    Assert.assertEquals(zStarMod.multiply(ONE, ONE), ONE);
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          if (zStarMod.getModulus().gcd(BigInteger.valueOf(j)).equals(BigInteger.ONE)) {
            element2 = zStarMod.getElement(j);
            Assert.assertTrue(zStarMod.multiply(element1, element2).getValue().intValue() == (i*j) % zStarMod.getModulus().intValue());
          }
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException1() {
    zStarMod.multiply((MultiplicativeElement)null, (MultiplicativeElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException2() {
    zStarMod.multiply(NINE, (MultiplicativeElement)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException3() {
    zStarMod.multiply((MultiplicativeElement)null, NINE);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException4() {
    zStarMod.multiply(OTHER_ELEMENT, NINE);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException5() {
    zStarMod.multiply(NINE, OTHER_ELEMENT);
  }

  @Test
  public void testMultiplyElementArray() {
    Assert.assertEquals(zStarMod.multiply().getValue(), BigInteger.ONE);
    Assert.assertEquals(zStarMod.multiply(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zStarMod.multiply(MINUS_ONE), NINETEEN);
    Assert.assertEquals(zStarMod.multiply(ONE, ONE, ONE), ONE);
    Assert.assertEquals(zStarMod.multiply(MINUS_ONE, ONE, ONE), NINETEEN);
    Assert.assertEquals(zStarMod.multiply(MINUS_ONE, ONE, MINUS_ONE), ONE);
    Assert.assertEquals(zStarMod.multiply(ONE, THREE, SEVEN, NINE), NINE);
    Assert.assertEquals(zStarMod.multiply(new MultiplicativeElement[] {}), ONE);
    Assert.assertEquals(zStarMod.multiply(new MultiplicativeElement[] {ONE, THREE, SEVEN, NINE}), NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException1() {
    zStarMod.multiply((MultiplicativeElement[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException2() {
    zStarMod.multiply(new MultiplicativeElement[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException3() {
    zStarMod.multiply(new MultiplicativeElement[] {ONE, NINE, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException4() {
    zStarMod.multiply(new MultiplicativeElement[] {ONE, NINE, OTHER_ELEMENT});
  }

  @Test
  public void testPowerElementBigInteger() {
    Assert.assertEquals(zStarMod.power(ONE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.power(NINE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.power(ONE, BigInteger.TEN), ONE);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, BigInteger.ONE), NINETEEN);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, BigInteger.valueOf(8)), ONE);
    Assert.assertEquals(zStarMod.power(SEVEN, BigInteger.valueOf(2)), NINE);
    Assert.assertEquals(zStarMod.power(THREE, BigInteger.valueOf(-6)), NINE);
    Assert.assertEquals(zStarMod.power(THREE, BigInteger.valueOf(10)), NINE);
    BigInteger amount;
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          amount = BigInteger.valueOf(j);
          Assert.assertEquals(zStarMod.power(element1, amount).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException1() {
    zStarMod.power(ONE, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException2() {
    zStarMod.power(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException3() {
    zStarMod.power(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException4() {
    zStarMod.power(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testPowerElementAtomicElement() {
    Assert.assertEquals(zStarMod.power(ONE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.power(NINE, BigInteger.ZERO), ONE);
    Assert.assertEquals(zStarMod.power(ONE, BigInteger.TEN), ONE);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, BigInteger.ONE), NINETEEN);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, BigInteger.valueOf(8)), ONE);
    Assert.assertEquals(zStarMod.power(SEVEN, BigInteger.valueOf(2)), NINE);
    Assert.assertEquals(zStarMod.power(THREE, BigInteger.valueOf(-6)), NINE);
    Assert.assertEquals(zStarMod.power(THREE, BigInteger.valueOf(10)), NINE);
    AtomicElement amount;
    ZPlus zPlus = ZPlus.getInstance();
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          amount = zPlus.getElement(j);
          Assert.assertEquals(zStarMod.power(element1, amount).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException1() {
    zStarMod.power(ONE, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException2() {
    zStarMod.power(null, NINE);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerAtomicElementException3() {
    zStarMod.power(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException4() {
    zStarMod.power(OTHER_ELEMENT, NINE);
  }

  @Test
  public void testPowerElementInt() {
    Assert.assertEquals(zStarMod.power(ONE, 0), ONE);
    Assert.assertEquals(zStarMod.power(NINE, 0), ONE);
    Assert.assertEquals(zStarMod.power(ONE, 10), ONE);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, 1), NINETEEN);
    Assert.assertEquals(zStarMod.power(MINUS_ONE, 8), ONE);
    Assert.assertEquals(zStarMod.power(SEVEN, 2), NINE);
    Assert.assertEquals(zStarMod.power(THREE, -6), NINE);
    Assert.assertEquals(zStarMod.power(THREE, 10), NINE);
    for (int i=0; i<zStarMod.getOrder().intValue();i++) {
      if (zStarMod.getModulus().gcd(BigInteger.valueOf(i)).equals(BigInteger.ONE)) {
        element1 = zStarMod.getElement(i);
        for (int j=0; j<zStarMod.getOrder().intValue();j++) {
          Assert.assertEquals(zStarMod.power(element1, j).getValue(),
              BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), zStarMod.getModulus()));        
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementIntException1() {
    zStarMod.power(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementIntException2() {
    zStarMod.power(OTHER_ELEMENT, 10);
  }

  @Test
  public void testHashCode() {
    Assert.assertTrue(zStarMod.hashCode() == zStarMod.hashCode());
    Assert.assertTrue(zStarMod.hashCode() == new ZStarMod(BigInteger.valueOf(2),BigInteger.valueOf(2),BigInteger.valueOf(5)).hashCode());
    Assert.assertTrue(zStarMod.hashCode() == new ZStarMod(BigInteger.valueOf(20)).hashCode());    
    Assert.assertFalse(zStarMod.hashCode() == new ZStarMod(BigInteger.valueOf(2)).hashCode());
    Assert.assertFalse(zStarMod.hashCode() == new ZStarMod(BigInteger.valueOf(10)).hashCode());
    Assert.assertFalse(zStarMod.hashCode() == SingletonGroup.getInstance().hashCode());
    Assert.assertFalse(zStarMod.hashCode() == BooleanGroup.getInstance().hashCode());
    Assert.assertFalse(zStarMod.hashCode() == ONE.hashCode());
    Assert.assertFalse(zStarMod.hashCode() == BigInteger.ZERO.hashCode());
    Assert.assertFalse(zStarMod.hashCode() == "".hashCode());    
  }

    @Test
    public void testEqualsObject() {
      Assert.assertTrue(zStarMod.equals(zStarMod));
      Assert.assertTrue(zStarMod.equals( new ZStarMod(BigInteger.valueOf(2),BigInteger.valueOf(2),BigInteger.valueOf(5))));
      Assert.assertTrue(zStarMod.equals(new ZStarMod(BigInteger.valueOf(20))));
      Assert.assertFalse(zStarMod.equals(ZPlus.getInstance()));
      Assert.assertFalse(zStarMod.equals(new ZStarMod(BigInteger.valueOf(2))));
      Assert.assertFalse(zStarMod.equals(new ZStarMod(BigInteger.valueOf(10))));
      Assert.assertFalse(zStarMod.equals(SingletonGroup.getInstance()));
      Assert.assertFalse(zStarMod.equals(BooleanGroup.getInstance()));
      Assert.assertFalse(zStarMod.equals(new ZPlusModClass(BigInteger.ONE)));
      Assert.assertFalse(zStarMod.equals(null));
      Assert.assertFalse(zStarMod.equals(ONE));
      Assert.assertFalse(zStarMod.equals(BigInteger.ZERO));
      Assert.assertFalse(zStarMod.equals(""));    
    }
  
    @Test
    public void testToString() {
      Assert.assertEquals(zStarMod.toString(), "ZStarModClass[modulo=20]");
    }
   

}
