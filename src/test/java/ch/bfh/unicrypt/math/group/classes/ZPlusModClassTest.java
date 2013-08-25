package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;

@SuppressWarnings("static-method")
public class ZPlusModClassTest {

  private static ZPlusMod zPlusMod = new ZPlusModClass(BigInteger.TEN);
  private static Random random = new Random();
  private static AdditiveElement element1, element2;
  private static final AdditiveElement MINUS_TEN = zPlusMod.getElement(-10);
  private static final AdditiveElement MINUS_THREE = zPlusMod.getElement(-3);
  private static final AdditiveElement MINUS_TWO = zPlusMod.getElement(-2);
  private static final AdditiveElement MINUS_ONE = zPlusMod.getElement(-1);
  private static final AdditiveElement ZERO = zPlusMod.getElement(0);
  private static final AdditiveElement ONE = zPlusMod.getElement(1);
  private static final AdditiveElement TWO = zPlusMod.getElement(2);
  private static final AdditiveElement THREE = zPlusMod.getElement(3);
  private static final AdditiveElement FOUR = zPlusMod.getElement(4);
  private static final AdditiveElement FIVE = zPlusMod.getElement(5);
  private static final AdditiveElement SIX = zPlusMod.getElement(6);
  private static final AdditiveElement SEVEN = zPlusMod.getElement(7);
  private static final AdditiveElement EIGHT = zPlusMod.getElement(8);
  private static final AdditiveElement NINE = zPlusMod.getElement(9);
  private static final AdditiveElement TEN = zPlusMod.getElement(10);
  private static final AdditiveElement TWELVE = zPlusMod.getElement(12);
  private static final AdditiveElement OTHER_ELEMENT = BooleanGroup.getInstance().getIdentityElement();


  @Test (expected=IllegalArgumentException.class)
  public void testZPlusModClassException1() {
    new ZPlusModClass(BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZPlusModClassException2() {
    new ZPlusModClass(BigInteger.valueOf(-10));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testZPlusModClassException3() {
    new ZPlusModClass(null);
  }

  @Test 
  public void testZPlusModOne() {
    ZPlusMod zPlusModOne = new ZPlusModClass(BigInteger.ONE);
    AdditiveElement elt1 = zPlusModOne.getElement(0);
    AdditiveElement elt2 = zPlusModOne.getElement(1);
    AdditiveElement elt3 = zPlusModOne.getElement(10);
    AdditiveElement elt4 = zPlusModOne.getElement(-5);
    Assert.assertTrue(elt1.equals(elt2));
    Assert.assertTrue(elt1.equals(elt3));
    Assert.assertTrue(elt1.equals(elt4));
    Assert.assertTrue(elt1.equals(elt2.apply(elt3)));
    Assert.assertTrue(elt1.equals(elt2.invert()));
    Assert.assertTrue(elt1.equals(elt2.selfApply()));
    Assert.assertTrue(elt1.equals(elt2.selfApply(5)));
    Assert.assertTrue(zPlusModOne.isGenerator(elt1));
    Assert.assertTrue(zPlusModOne.isGenerator(elt2));
    Assert.assertTrue(zPlusModOne.areEqual(elt1, elt2));
    Assert.assertTrue(zPlusModOne.contains(BigInteger.ZERO));
    Assert.assertTrue(zPlusModOne.contains(BigInteger.ONE));
    Assert.assertTrue(zPlusModOne.contains(BigInteger.valueOf(10)));
    Assert.assertTrue(zPlusModOne.isIdentity(elt1));
    Assert.assertEquals(elt1.getValue(), BigInteger.ZERO);
    Assert.assertEquals(elt2.getValue(), BigInteger.ZERO);
    Assert.assertEquals(elt3.getValue(), BigInteger.ZERO);
    for (int i=1; i<=100; i++) {
      Assert.assertEquals(zPlusModOne.getRandomElement().getValue(), BigInteger.ZERO);
    }
    Assert.assertEquals(zPlusModOne.getOrder(),BigInteger.ONE);
    Assert.assertEquals(zPlusModOne.getMinOrder(),BigInteger.ONE);
    Assert.assertEquals(zPlusModOne, zPlusModOne.getOrderGroup());
    Assert.assertEquals(zPlusModOne, zPlusModOne.getMinOrderGroup());
    Assert.assertEquals(zPlusModOne.getModulus(), BigInteger.ONE);
  }

  @Test
  public void testGetModulus() {
    Assert.assertEquals(zPlusMod.getModulus(), BigInteger.TEN);
  }

  @Test
  public void testCreateRandomElement() {
    AdditiveElement element;
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      do {
        element = zPlusMod.getRandomElement();
        Assert.assertTrue(element != null);
        Assert.assertTrue(element.getValue() != null);
      } while (element.getValue().intValue() != i);
    }
  }

  @Test
  public void testCreateRandomElementRandom() {
    AdditiveElement element;
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      do {
        element = zPlusMod.getRandomElement(random);
        Assert.assertTrue(element != null);
        Assert.assertTrue(element.getValue() != null);
      } while (element.getValue().intValue() != i);
    }
  }

  @Test
  public void testGetIdentityElement() {
    element1 = zPlusMod.getIdentityElement();
    element2 = zPlusMod.getIdentityElement();
    Assert.assertEquals(element1.getValue(), BigInteger.ZERO);
    Assert.assertEquals(element1, element2);
    Assert.assertSame(element1, element2);
  }

  @Test
  public void testApplyElementElement() {
    Assert.assertEquals(zPlusMod.apply(ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlusMod.apply(MINUS_ONE, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(ZERO, ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(MINUS_ONE, MINUS_TEN).getValue(), BigInteger.valueOf(9));
    Assert.assertEquals(zPlusMod.apply(TWELVE, TEN).getValue(), BigInteger.valueOf(2));
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        element2 = zPlusMod.getElement(j);
        Assert.assertTrue(zPlusMod.apply(element1, element2).getValue().intValue() == (i+j) % zPlusMod.getModulus().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException1() {
    zPlusMod.apply((Element)null, (Element)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException2() {
    zPlusMod.apply(TEN, (Element)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException3() {
    zPlusMod.apply((Element)null, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException4() {
    zPlusMod.apply(OTHER_ELEMENT, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException5() {
    zPlusMod.apply(TEN, OTHER_ELEMENT);
  }

  @Test
  public void testApplyElementArray() {
    Assert.assertEquals(zPlusMod.apply().getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zPlusMod.apply(MINUS_ONE).getValue(), BigInteger.valueOf(9));
    Assert.assertEquals(zPlusMod.apply(ZERO, ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlusMod.apply(MINUS_ONE, ZERO, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(new Element[] {MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN}).getValue(), BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException1() {
    zPlusMod.apply((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException2() {
    zPlusMod.apply(new Element[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException3() {
    zPlusMod.apply(new Element[] {ONE, TEN, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException4() {
    zPlusMod.apply(new Element[] {ONE, TEN, OTHER_ELEMENT});
  }

  @Test
  public void testSelfApplyElementBigInteger() {
    Assert.assertEquals(zPlusMod.selfApply(ZERO, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(ONE, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TEN, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(ONE, BigInteger.TEN), TEN);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_ONE, BigInteger.TEN), MINUS_TEN);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, BigInteger.TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TWO, BigInteger.valueOf(3)).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.selfApply(TWO, BigInteger.valueOf(-1)).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.selfApply(TWO, BigInteger.valueOf(-3)).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.selfApply(TWO, BigInteger.valueOf(-10)), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, BigInteger.valueOf(-10)), ZERO);
    BigInteger amount;
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        amount = BigInteger.valueOf(j);
        Assert.assertTrue(zPlusMod.selfApply(element1, amount).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException1() {
    zPlusMod.selfApply(ZERO, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException2() {
    zPlusMod.selfApply(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException3() {
    zPlusMod.selfApply(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException4() {
    zPlusMod.selfApply(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testSelfApplyElementAtomicElement() {
    Assert.assertEquals(zPlusMod.selfApply(ZERO, ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TEN, ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(ONE, TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_ONE, TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TEN, MINUS_TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, MINUS_TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TWO, THREE).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.selfApply(TWO, MINUS_ONE).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.selfApply(TWO, MINUS_THREE).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.selfApply(TWO, BigInteger.valueOf(-10)), ZERO);
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        element2 = zPlusMod.getElement(j);
        Assert.assertTrue(zPlusMod.selfApply(element1, element2).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException1() {
    zPlusMod.selfApply(ZERO, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException2() {
    zPlusMod.selfApply(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyAtomicElementException3() {
    zPlusMod.selfApply(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException4() {
    zPlusMod.selfApply(OTHER_ELEMENT, TEN);
  }

  @Test
  public void testSelfApplyElementInt() {
    Assert.assertEquals(zPlusMod.selfApply(ZERO, 0), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TEN, 0), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(ONE, 10), TEN);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_ONE, 10), MINUS_TEN);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, 10), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TEN, -10), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, -10), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(TWO, 3).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.selfApply(TWO, -1).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.selfApply(TWO, -3).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.selfApply(TWO, -10), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_TEN, -10), ZERO);
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        Assert.assertTrue(zPlusMod.selfApply(element1, j).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException1() {
    zPlusMod.selfApply(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException2() {
    zPlusMod.selfApply(OTHER_ELEMENT, 10);
  }

  @Test
  public void testSelfApplyElement() {
    Assert.assertEquals(zPlusMod.selfApply(ZERO), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(ONE), TWO);
    Assert.assertEquals(zPlusMod.selfApply(zPlusMod.getElement(6)), TWO);
    Assert.assertEquals(zPlusMod.selfApply(TEN), ZERO);
    Assert.assertEquals(zPlusMod.selfApply(MINUS_ONE), MINUS_TWO);
  }

  @Test
  public void testMultiSelfApplyElementArrayBigIntegerArray() {
    Assert.assertEquals(zPlusMod.multiSelfApply(new Element[] {}, new BigInteger[] {}), ZERO);
    Assert.assertEquals(zPlusMod.multiSelfApply(new Element[] {ZERO}, new BigInteger[] {BigInteger.ZERO}), ZERO);
    Assert.assertEquals(zPlusMod.multiSelfApply(new Element[] {ONE}, new BigInteger[] {BigInteger.TEN}), ZERO);
    Assert.assertEquals(zPlusMod.multiSelfApply(new Element[] {ZERO, TWO}, new BigInteger[] {BigInteger.TEN, BigInteger.ONE}), TWO);
    Assert.assertEquals(zPlusMod.multiSelfApply(new Element[] {ZERO, ONE, TWO}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE}), ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    zPlusMod.multiSelfApply(new Element[] {ZERO, ONE, TWO}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    zPlusMod.multiSelfApply(null, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    zPlusMod.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    zPlusMod.multiSelfApply(new Element[] {ZERO, ONE, TWO}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    zPlusMod.multiSelfApply(new Element[] {OTHER_ELEMENT, TWO}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test
  public void testInvert() {
    Assert.assertEquals(zPlusMod.invert(MINUS_ONE), ONE);
    Assert.assertEquals(zPlusMod.invert(ZERO), ZERO);
    Assert.assertEquals(zPlusMod.invert(TWO), MINUS_TWO);
    Assert.assertEquals(zPlusMod.invert(TEN), ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException1() {
    zPlusMod.invert(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException2() {
    zPlusMod.invert(OTHER_ELEMENT);
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(zPlusMod.getOrder(), BigInteger.TEN);  
  }

  @Test
  public void testGetMinOrder() {
    Assert.assertEquals(zPlusMod.getMinOrder(), BigInteger.TEN);  
  }

  @Test
  public void testHasSameOrder() {
    Assert.assertTrue(zPlusMod.hasSameOrder(zPlusMod));
    Assert.assertTrue(zPlusMod.hasSameOrder(new ProductGroup(new ZPlusModClass(BigInteger.valueOf(2)), new ZPlusModClass(BigInteger.valueOf(5)))));
    Assert.assertFalse(zPlusMod.hasSameOrder(new ZPlusModClass(BigInteger.valueOf(9))));
    Assert.assertFalse(zPlusMod.hasSameOrder(SingletonGroup.getInstance()));
    Assert.assertFalse(zPlusMod.hasSameOrder(BooleanGroup.getInstance()));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasSameOrderException() {
    zPlusMod.hasSameOrder(null);  
  }

  @Test 
  public void testGetOrderGroup() {
    Assert.assertEquals(zPlusMod.getOrderGroup(), zPlusMod);
  }

  @Test 
  public void testGetMinOrderGroup() {
    Assert.assertEquals(zPlusMod.getMinOrderGroup(), zPlusMod);
  }

  @Test
  public void testContainsElement() {
    Assert.assertTrue(zPlusMod.contains(MINUS_ONE));
    Assert.assertTrue(zPlusMod.contains(ZERO));
    Assert.assertTrue(zPlusMod.contains(TWO));
    Assert.assertTrue(zPlusMod.contains(TEN));
    Assert.assertFalse(zPlusMod.contains(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testContainsElementException() {
    zPlusMod.contains(null);
  }

  @Test
  public void testIsIdentityElement() {
    Assert.assertTrue(zPlusMod.isIdentity(zPlusMod.getIdentityElement()));
    Assert.assertTrue(zPlusMod.isIdentity(ZERO));
    Assert.assertFalse(zPlusMod.isIdentity(ONE));
    Assert.assertFalse(zPlusMod.isIdentity(TWO));
    Assert.assertFalse(zPlusMod.isIdentity(THREE));
    Assert.assertFalse(zPlusMod.isIdentity(FOUR));
    Assert.assertTrue(zPlusMod.isIdentity(TEN));
    Assert.assertFalse(zPlusMod.isIdentity(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testIsIdentityElementException() {
    zPlusMod.isIdentity(null);
  }

  @Test
  public void testAreEqualElements() {
    Assert.assertTrue(zPlusMod.areEqual(ZERO, ZERO));
    Assert.assertTrue(zPlusMod.areEqual(TWO, TWO));
    Assert.assertTrue(zPlusMod.areEqual(ZERO, TEN));
    Assert.assertTrue(zPlusMod.areEqual(ZERO, MINUS_TEN));
    Assert.assertTrue(zPlusMod.areEqual(OTHER_ELEMENT, OTHER_ELEMENT));
    Assert.assertFalse(zPlusMod.areEqual(ONE, ZERO));
    Assert.assertFalse(zPlusMod.areEqual(ONE, MINUS_ONE));
    Assert.assertFalse(zPlusMod.areEqual(ONE, OTHER_ELEMENT));
    Assert.assertFalse(zPlusMod.areEqual(OTHER_ELEMENT, TEN));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException1() {
    zPlusMod.areEqual(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException2() {
    zPlusMod.areEqual(TEN, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException3() {
    zPlusMod.areEqual(null, null);
  }

  @Test
  public void testGetArity() {
    Assert.assertTrue(zPlusMod.getArity()==1);
  }

  @Test
  public void testHasElement() {
    Assert.assertTrue(zPlusMod.contains(BigInteger.valueOf(-1)));
    Assert.assertTrue(zPlusMod.contains(BigInteger.ZERO));
    Assert.assertTrue(zPlusMod.contains(BigInteger.TEN));
    Assert.assertTrue(zPlusMod.contains(BigInteger.valueOf(100000)));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasElementException() {
    zPlusMod.contains(null);
  }

  @Test
  public void testCreateElementBigInteger() {
    for (int i=-5; i<=15; i++) {
      Assert.assertEquals(zPlusMod.getElement(BigInteger.valueOf(i)).getValue(), BigInteger.valueOf(i).mod(zPlusMod.getModulus()));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementBigIntegerException() {
    zPlusMod.getElement((BigInteger) null);
  }

  @Test
  public void testCreateElementInt() {
    for (int i=-5; i<=15; i++) {
      Assert.assertEquals(zPlusMod.getElement(i).getValue(), BigInteger.valueOf(i).mod(zPlusMod.getModulus()));
    }
  }

  @Test
  public void testCreateElementAtomicElement() {
    for (int i=-5; i<=15; i++) {
      Assert.assertEquals(zPlusMod.getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(zPlusMod.getModulus()));
    }
    for (int i=-5; i<=15; i++) {
      Assert.assertEquals(zPlusMod.getElement(zPlusMod.getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(zPlusMod.getModulus()));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementAtomicElementException() {
    zPlusMod.getElement((AtomicElement) null);
  }  

  @Test
  public void testGetBigInteger() {
    // has been tested in many test methods above
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException1() {
    zPlusMod.getValue(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException2() {
    zPlusMod.getValue(ZPlus.getInstance().getElement(5));
  }

  @Test
  public void testAdd() {
    Assert.assertEquals(zPlusMod.add(ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlusMod.add(MINUS_ONE, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.add(ZERO, ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.add(MINUS_ONE, MINUS_TEN).getValue(), BigInteger.valueOf(9));
    Assert.assertEquals(zPlusMod.add(TWELVE, TEN).getValue(), BigInteger.valueOf(2));
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        element2 = zPlusMod.getElement(j);
        Assert.assertTrue(zPlusMod.add(element1, element2).getValue().intValue() == (i+j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException1() {
    zPlusMod.add((AdditiveElement)null, (AdditiveElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException2() {
    zPlusMod.add(TEN, (AdditiveElement)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException3() {
    zPlusMod.add((AdditiveElement)null, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException4() {
    zPlusMod.add(OTHER_ELEMENT, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException5() {
    zPlusMod.add(TEN, OTHER_ELEMENT);
  }

  @Test
  public void testAddElementArray() {
    Assert.assertEquals(zPlusMod.add().getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.add(ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.add(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zPlusMod.add(MINUS_ONE).getValue(), BigInteger.valueOf(9));
    Assert.assertEquals(zPlusMod.add(ZERO, ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlusMod.add(MINUS_ONE, ZERO, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.add(MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlusMod.apply(new AdditiveElement[] {MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN}).getValue(), BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException1() {
    zPlusMod.add((AdditiveElement[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException2() {
    zPlusMod.add(new AdditiveElement[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException3() {
    zPlusMod.add(new AdditiveElement[] {ONE, TEN, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException4() {
    zPlusMod.add(new AdditiveElement[] {ONE, TEN, OTHER_ELEMENT});
  }

  @Test
  public void testTimesAdditiveElementBigInteger() {
    Assert.assertEquals(zPlusMod.times(ZERO, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.times(ONE, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.times(TEN, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlusMod.times(ONE, BigInteger.TEN), TEN);
    Assert.assertEquals(zPlusMod.times(MINUS_ONE, BigInteger.TEN), MINUS_TEN);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, BigInteger.TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(TWO, BigInteger.valueOf(3)).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.times(TWO, BigInteger.valueOf(-1)).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.times(TWO, BigInteger.valueOf(-3)).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.times(TWO, BigInteger.valueOf(-10)), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, BigInteger.valueOf(-10)), ZERO);
    BigInteger amount;
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        amount = BigInteger.valueOf(j);
        Assert.assertTrue(zPlusMod.times(element1, amount).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException1() {
    zPlusMod.times(ZERO, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException2() {
    zPlusMod.times(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException3() {
    zPlusMod.times(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException4() {
    zPlusMod.times(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testTimesAdditiveElementAtomicElement() {
    Assert.assertEquals(zPlusMod.times(ZERO, ZERO), ZERO);
    Assert.assertEquals(zPlusMod.times(TEN, ZERO), ZERO);
    Assert.assertEquals(zPlusMod.times(ONE, TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_ONE, TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(TEN, MINUS_TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, MINUS_TEN), ZERO);
    Assert.assertEquals(zPlusMod.times(TWO, THREE).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.times(TWO, MINUS_ONE).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.times(TWO, MINUS_THREE).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.times(TWO, BigInteger.valueOf(-10)), ZERO);
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        element2 = zPlusMod.getElement(j);
        Assert.assertTrue(zPlusMod.times(element1, element2).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException1() {
    zPlusMod.times(ZERO, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException2() {
    zPlusMod.times(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException3() {
    zPlusMod.times(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException4() {
    zPlusMod.times(OTHER_ELEMENT, TEN);
  }

  @Test
  public void testTimesAdditiveElementInt() {
    Assert.assertEquals(zPlusMod.times(ZERO, 0), ZERO);
    Assert.assertEquals(zPlusMod.times(TEN, 0), ZERO);
    Assert.assertEquals(zPlusMod.times(ONE, 10), TEN);
    Assert.assertEquals(zPlusMod.times(MINUS_ONE, 10), MINUS_TEN);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, 10), ZERO);
    Assert.assertEquals(zPlusMod.times(TEN, -10), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, -10), ZERO);
    Assert.assertEquals(zPlusMod.times(TWO, 3).getValue(), BigInteger.valueOf(6));
    Assert.assertEquals(zPlusMod.times(TWO, -1).getValue(), BigInteger.valueOf(8));
    Assert.assertEquals(zPlusMod.times(TWO, -3).getValue(), BigInteger.valueOf(4));
    Assert.assertEquals(zPlusMod.times(TWO, -10), ZERO);
    Assert.assertEquals(zPlusMod.times(MINUS_TEN, -10), ZERO);
    for (int i=0; i<zPlusMod.getOrder().intValue();i++) {
      element1 = zPlusMod.getElement(i);
      for (int j=0; j<zPlusMod.getOrder().intValue();j++) {
        Assert.assertTrue(zPlusMod.times(element1, j).getValue().intValue() == (i*j) % zPlusMod.getOrder().intValue());        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementIntException1() {
    zPlusMod.times(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementIntException2() {
    zPlusMod.times(OTHER_ELEMENT, 10);
  }

  @Test
  public void testGetDefaultGenerator() {
    Assert.assertEquals(zPlusMod.getDefaultGenerator(), ONE);
  }

  @Test
  public void testCreateRandomGenerator() {
    for (int i=0; i<=50; i++) {
      AdditiveElement gen = zPlusMod.getRandomGenerator();
      Assert.assertTrue(gen.equals(ONE) || gen.equals(THREE) || gen.equals(SEVEN) || gen.equals(NINE));
    }
  }

  @Test
  public void testCreateRandomGeneratorRandom() {
    for (int i=0; i<=50; i++) {
      AdditiveElement gen = zPlusMod.getRandomGenerator(random);
      Assert.assertTrue(gen.equals(ONE) || gen.equals(THREE) || gen.equals(SEVEN) || gen.equals(NINE));
    }
  }

  @Test
  public void testIsGenerator() {
    Assert.assertTrue(zPlusMod.isGenerator(MINUS_ONE));
    Assert.assertTrue(zPlusMod.isGenerator(ONE));
    Assert.assertTrue(zPlusMod.isGenerator(THREE));
    Assert.assertTrue(zPlusMod.isGenerator(SEVEN));
    Assert.assertTrue(zPlusMod.isGenerator(NINE));
    Assert.assertFalse(zPlusMod.isGenerator(ZERO));
    Assert.assertFalse(zPlusMod.isGenerator(TWO));
    Assert.assertFalse(zPlusMod.isGenerator(FOUR));
    Assert.assertFalse(zPlusMod.isGenerator(FIVE));
    Assert.assertFalse(zPlusMod.isGenerator(SIX));
    Assert.assertFalse(zPlusMod.isGenerator(EIGHT));
    Assert.assertFalse(zPlusMod.isGenerator(TEN));
    Assert.assertFalse(zPlusMod.isGenerator(MINUS_TEN));
  }

  @Test
  public void testHashCode() {
    Assert.assertTrue(zPlusMod.hashCode() == zPlusMod.hashCode());
    Assert.assertTrue(zPlusMod.hashCode() == new ZPlusModClass(BigInteger.TEN).hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == new ZPlusModClass(BigInteger.ONE).hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == SingletonGroup.getInstance().hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == BooleanGroup.getInstance().hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == ZERO.hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == BigInteger.ZERO.hashCode());
    Assert.assertFalse(zPlusMod.hashCode() == "".hashCode());    
  }

  @Test
  public void testEqualsObject() {
    Assert.assertTrue(zPlusMod.equals(zPlusMod));
    Assert.assertTrue(zPlusMod.equals(new ZPlusModClass(BigInteger.TEN)));
    Assert.assertFalse(zPlusMod.equals(ZPlus.getInstance()));
    Assert.assertFalse(zPlusMod.equals(SingletonGroup.getInstance()));
    Assert.assertFalse(zPlusMod.equals(BooleanGroup.getInstance()));
    Assert.assertFalse(zPlusMod.equals(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertFalse(zPlusMod.equals(null));
    Assert.assertFalse(zPlusMod.equals(ZERO));
    Assert.assertFalse(zPlusMod.equals(BigInteger.ZERO));
    Assert.assertFalse(zPlusMod.equals(""));    
  }

  @Test
  public void testToString() {
    Assert.assertEquals(zPlusMod.toString(), "ZPlusModClass[modulo 10]");
  }

}
