package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.cyclicgroup.classes.BooleanGroup;
import java.math.BigInteger;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.EncodedElement;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;

@SuppressWarnings("static-method")
public class ZPlusClassTest {

  private static ZPlus zPlus = ZPlus.getInstance();
  private static Random random = new Random();
  private static AdditiveElement element1, element2;
  private static BigInteger value1, value2;
  private static final AdditiveElement MINUS_TEN = zPlus.getElement(-10);
  private static final AdditiveElement MINUS_ONE = zPlus.getElement(-1);
  private static final AdditiveElement MINUS_TWO = zPlus.getElement(-2);
  private static final AdditiveElement ZERO = zPlus.getElement(0);
  private static final AdditiveElement ONE = zPlus.getElement(1);
  private static final AdditiveElement TWO = zPlus.getElement(2);
  private static final AdditiveElement TEN = zPlus.getElement(10);
  private static final AdditiveElement OTHER_ELEMENT = BooleanGroup.getInstance().getIdentityElement();


  @Test
  public void testGetInstance() {
    ZPlus zPlus2 = ZPlus.getInstance();    
    Assert.assertSame(zPlus, zPlus2);
  }

  @Test
  public void testCreateRandomElement() {
    element1 = zPlus.getRandomElement();
    element2 = zPlus.getRandomElement();
    value1 = element1.getValue();
    value2 = element2.getValue();
    Assert.assertTrue(element1 != null);
    Assert.assertTrue(value1 != null);
    Assert.assertTrue(element2 != null);
    Assert.assertTrue(value2 != null);
    Assert.assertFalse(value1.equals(value2));
  }

  @Test
  public void testCreateRandomElementRandom() {
    element1 = zPlus.getRandomElement(random);
    element2 = zPlus.getRandomElement(random);
    value1 = element1.getValue();
    value2 = element2.getValue();
    Assert.assertTrue(element1 != null);
    Assert.assertTrue(value1 != null);
    Assert.assertTrue(element2 != null);
    Assert.assertTrue(value2 != null);
    Assert.assertFalse(value1.equals(value2));
  }

  @Test
  public void testGetIdentityElement() {
    element1 = zPlus.getIdentityElement();
    element2 = zPlus.getIdentityElement();
    Assert.assertEquals(element1.getValue(), BigInteger.ZERO);
    Assert.assertSame(element1, element2);
  }

  @Test
  public void testApplyElementElement() {
    Assert.assertEquals(zPlus.apply(ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlus.apply(MINUS_ONE, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(ZERO, ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(MINUS_ONE, MINUS_TEN).getValue(), BigInteger.valueOf(-11));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException1() {
    zPlus.apply((Element)null, (Element)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException2() {
    zPlus.apply(TEN, (Element)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException3() {
    zPlus.apply((Element)null, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException4() {
    zPlus.apply(OTHER_ELEMENT, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException5() {
    zPlus.apply(TEN, OTHER_ELEMENT);
  }

  @Test
  public void testApplyElementArray() {
    Assert.assertEquals(zPlus.apply().getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zPlus.apply(MINUS_ONE).getValue(), BigInteger.valueOf(-1));
    Assert.assertEquals(zPlus.apply(ZERO, ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlus.apply(MINUS_ONE, ZERO, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.apply(new Element[] {MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN}).getValue(), BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException1() {
    zPlus.apply((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException2() {
    zPlus.apply(new Element[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException3() {
    zPlus.apply(new Element[] {ONE, TEN, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException4() {
    zPlus.apply(new Element[] {ONE, TEN, OTHER_ELEMENT});
  }

  @Test
  public void testSelfApplyElementBigInteger() {
    Assert.assertEquals(zPlus.selfApply(ZERO, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlus.selfApply(TEN, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlus.selfApply(ONE, BigInteger.TEN), TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_ONE, BigInteger.TEN), MINUS_TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, BigInteger.TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(TEN, BigInteger.valueOf(-10)).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, BigInteger.valueOf(-10)).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException1() {
    zPlus.selfApply(ZERO, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException2() {
    zPlus.selfApply(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException3() {
    zPlus.selfApply(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException4() {
    zPlus.selfApply(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testSelfApplyElementAtomicElement() {
    Assert.assertEquals(zPlus.selfApply(ZERO, ZERO), ZERO);
    Assert.assertEquals(zPlus.selfApply(TEN, ZERO), ZERO);
    Assert.assertEquals(zPlus.selfApply(ONE, TEN), TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_ONE, TEN), MINUS_TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(TEN, MINUS_TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, MINUS_TEN).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException1() {
    zPlus.selfApply(ZERO, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException2() {
    zPlus.selfApply(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyAtomicElementException3() {
    zPlus.selfApply(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException4() {
    zPlus.selfApply(OTHER_ELEMENT, TEN);
  }

  @Test
  public void testSelfApplyElementInt() {
    Assert.assertEquals(zPlus.selfApply(ZERO, 0), ZERO);
    Assert.assertEquals(zPlus.selfApply(TEN, 0), ZERO);
    Assert.assertEquals(zPlus.selfApply(ONE, 10), TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_ONE, 10), MINUS_TEN);
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, 10).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(TEN, -10).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.selfApply(MINUS_TEN, -10).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException1() {
    zPlus.selfApply(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException2() {
    zPlus.selfApply(OTHER_ELEMENT, 10);
  }

  @Test
  public void testSelfApplyElement() {
    Assert.assertEquals(zPlus.selfApply(ZERO), ZERO);
    Assert.assertEquals(zPlus.selfApply(ONE), TWO);
    Assert.assertEquals(zPlus.selfApply(TEN), zPlus.getElement(20));
    Assert.assertEquals(zPlus.selfApply(MINUS_ONE), MINUS_TWO);
  }

  @Test
  public void testMultiSelfApplyElementArrayBigIntegerArray() {
    Assert.assertEquals(zPlus.multiSelfApply(new Element[] {}, new BigInteger[] {}), ZERO);
    Assert.assertEquals(zPlus.multiSelfApply(new Element[] {ZERO}, new BigInteger[] {BigInteger.ZERO}), ZERO);
    Assert.assertEquals(zPlus.multiSelfApply(new Element[] {ONE}, new BigInteger[] {BigInteger.TEN}), TEN);
    Assert.assertEquals(zPlus.multiSelfApply(new Element[] {ZERO, TWO}, new BigInteger[] {BigInteger.TEN, BigInteger.ONE}), TWO);
    Assert.assertEquals(zPlus.multiSelfApply(new Element[] {ZERO, ONE, TWO}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE}), TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    zPlus.multiSelfApply(new Element[] {ZERO, ONE, TWO}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    zPlus.multiSelfApply(null, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    zPlus.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    zPlus.multiSelfApply(new Element[] {ZERO, ONE, TWO}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    zPlus.multiSelfApply(new Element[] {OTHER_ELEMENT, TWO}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test
  public void testInvert() {
    Assert.assertEquals(zPlus.invert(ZERO), ZERO);
    Assert.assertEquals(zPlus.invert(TWO), MINUS_TWO);
    Assert.assertEquals(zPlus.invert(TEN), MINUS_TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException1() {
    zPlus.invert(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException2() {
    zPlus.invert(OTHER_ELEMENT);
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(zPlus.getOrder(), Group.INFINITE_ORDER);  
  }

  @Test
  public void testGetMinOrder() {
    Assert.assertEquals(zPlus.getMinOrder(), Group.INFINITE_ORDER);  
  }

  @Test
  public void testHasSameOrder() {
    Assert.assertTrue(zPlus.hasSameOrder(zPlus));
    Assert.assertTrue(zPlus.hasSameOrder(ZPlus.getInstance()));
    Assert.assertFalse(zPlus.hasSameOrder(SingletonGroup.getInstance()));
    Assert.assertFalse(zPlus.hasSameOrder(BooleanGroup.getInstance()));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasSameOrderException() {
    zPlus.hasSameOrder(null);  
  }

  @Test (expected=UnsupportedOperationException.class)
  public void testGetOrderGroup() {
    zPlus.getOrderGroup();
  }

  @Test (expected=UnsupportedOperationException.class)
  public void testGetMinOrderGroup() {
    zPlus.getMinOrderGroup();
  }

  @Test
  public void testContainsElement() {
    Assert.assertTrue(zPlus.contains(MINUS_ONE));
    Assert.assertTrue(zPlus.contains(ZERO));
    Assert.assertTrue(zPlus.contains(TEN));
    Assert.assertFalse(zPlus.contains(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testContainsElementException() {
    zPlus.contains(null);
  }

  @Test
  public void testIsIdentityElement() {
    Assert.assertTrue(zPlus.isIdentity(ZERO));
    Assert.assertTrue(zPlus.isIdentity(zPlus.getIdentityElement()));
    Assert.assertFalse(zPlus.isIdentity(ONE));
    Assert.assertFalse(zPlus.isIdentity(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testIsIdentityElementException() {
    zPlus.isIdentity(null);
  }

  @Test
  public void testAreEqualElements() {
    Assert.assertTrue(zPlus.areEqual(ZERO, ZERO));
    Assert.assertTrue(zPlus.areEqual(zPlus.getElement(10), TEN));
    Assert.assertTrue(zPlus.areEqual(zPlus.invert(TEN), MINUS_TEN));
    Assert.assertTrue(zPlus.areEqual(OTHER_ELEMENT, OTHER_ELEMENT));
    Assert.assertFalse(zPlus.areEqual(ONE, ZERO));
    Assert.assertFalse(zPlus.areEqual(ONE, OTHER_ELEMENT));
    Assert.assertFalse(zPlus.areEqual(OTHER_ELEMENT, TEN));
    // encoded elements should be the same (to be removed in future versions)
    Assert.assertTrue(zPlus.areEqual(ONE, zPlus.createEncodedElement(BigInteger.ONE)));
    Assert.assertFalse(zPlus.areEqual(ONE, zPlus.createEncodedElement(BigInteger.TEN)));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException1() {
    zPlus.areEqual(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException2() {
    zPlus.areEqual(TEN, null);
  }
  
  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException3() {
    zPlus.areEqual(null, null);
  }
  
  @Test
  public void testGetArity() {
    Assert.assertTrue(zPlus.getArity()==1);
  }
  
  @Test
  public void testHasElement() {
    Assert.assertTrue(zPlus.contains(BigInteger.valueOf(-1)));
    Assert.assertTrue(zPlus.contains(BigInteger.ZERO));
    Assert.assertTrue(zPlus.contains(BigInteger.TEN));
    Assert.assertTrue(zPlus.contains(BigInteger.valueOf(100000)));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasElementException() {
    zPlus.contains(null);
  }
  
  @Test
  public void testCreateElementBigInteger() {
    for (int i=-5; i<=5; i++) {
      Assert.assertEquals(zPlus.getElement(BigInteger.valueOf(i)).getValue(), BigInteger.valueOf(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementBigIntegerException() {
    zPlus.getElement((BigInteger) null);
  }

  @Test
  public void testCreateElementInt() {
    for (int i=-5; i<=5; i++) {
      Assert.assertEquals(zPlus.getElement(i).getValue(), BigInteger.valueOf(i));
    }
  }

  @Test
  public void testCreateElementAtomicElement() {
    for (int i=-5; i<=5; i++) {
      Assert.assertEquals(zPlus.getElement(zPlus.getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i));
    }
    ZPlusMod zPlusMod = new ZPlusModClass(BigInteger.TEN);
    for (int i=0; i<=9; i++) {
      Assert.assertEquals(zPlus.getElement(zPlusMod.getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementAtomicElementException() {
    zPlus.getElement((AtomicElement) null);
  }  
  
  @Test
  public void testGetBigInteger() {
    // has been tested in many test methods above
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException1() {
    zPlus.getValue(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException2() {
    zPlus.getValue(new ZPlusModClass(BigInteger.TEN).getElement(5));
  }

  @Test
  public void testAdd() {
    Assert.assertEquals(zPlus.add(ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlus.add(MINUS_ONE, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(ZERO, ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(MINUS_ONE, MINUS_TEN).getValue(), BigInteger.valueOf(-11));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException1() {
    zPlus.add((AdditiveElement)null, (AdditiveElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException2() {
    zPlus.add(TEN, (AdditiveElement)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException3() {
    zPlus.add((AdditiveElement)null, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException4() {
    zPlus.add(OTHER_ELEMENT, TEN);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testAddElementElementException5() {
    zPlus.add(TEN, OTHER_ELEMENT);
  }

  @Test
  public void testAddElementArray() {
    Assert.assertEquals(zPlus.add().getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(ZERO).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(ONE).getValue(), BigInteger.ONE);
    Assert.assertEquals(zPlus.add(MINUS_ONE).getValue(), BigInteger.valueOf(-1));
    Assert.assertEquals(zPlus.add(ZERO, ONE, TWO).getValue(), BigInteger.valueOf(3));
    Assert.assertEquals(zPlus.add(MINUS_ONE, ZERO, ONE).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN).getValue(), BigInteger.ZERO);
    Assert.assertEquals(zPlus.add(new AdditiveElement[] {MINUS_TEN, MINUS_ONE, ZERO, ONE, TEN}).getValue(), BigInteger.ZERO);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException1() {
    zPlus.add((AdditiveElement[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException2() {
    zPlus.add(new AdditiveElement[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException3() {
    zPlus.add(new AdditiveElement[] {ONE, TEN, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAddElementArrayException4() {
    zPlus.add(new AdditiveElement[] {ONE, TEN, OTHER_ELEMENT});
  }

  @Test
  public void testTimesAdditiveElementBigInteger() {
    Assert.assertEquals(zPlus.times(ZERO, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlus.times(TEN, BigInteger.ZERO), ZERO);
    Assert.assertEquals(zPlus.times(ONE, BigInteger.TEN), TEN);
    Assert.assertEquals(zPlus.times(MINUS_ONE, BigInteger.TEN), MINUS_TEN);
    Assert.assertEquals(zPlus.times(MINUS_TEN, BigInteger.TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(TEN, BigInteger.valueOf(-10)).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(MINUS_TEN, BigInteger.valueOf(-10)).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException1() {
    zPlus.times(ZERO, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException2() {
    zPlus.times(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException3() {
    zPlus.times(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementBigIntegerException4() {
    zPlus.times(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testTimesAdditiveElementAtomicElement() {
    Assert.assertEquals(zPlus.times(ZERO, ZERO), ZERO);
    Assert.assertEquals(zPlus.times(TEN, ZERO), ZERO);
    Assert.assertEquals(zPlus.times(ONE, TEN), TEN);
    Assert.assertEquals(zPlus.times(MINUS_ONE, TEN), MINUS_TEN);
    Assert.assertEquals(zPlus.times(MINUS_TEN, TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(TEN, MINUS_TEN).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(MINUS_TEN, MINUS_TEN).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException1() {
    zPlus.times(ZERO, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException2() {
    zPlus.times(null, TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException3() {
    zPlus.times(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementAtomicElementException4() {
    zPlus.times(OTHER_ELEMENT, TEN);
  }

  @Test
  public void testTimesAdditiveElementInt() {
    Assert.assertEquals(zPlus.times(ZERO, 0), ZERO);
    Assert.assertEquals(zPlus.times(TEN, 0), ZERO);
    Assert.assertEquals(zPlus.times(ONE, 10), TEN);
    Assert.assertEquals(zPlus.times(MINUS_ONE, 10), MINUS_TEN);
    Assert.assertEquals(zPlus.times(MINUS_TEN, 10).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(TEN, -10).getValue(), BigInteger.valueOf(-100));
    Assert.assertEquals(zPlus.times(MINUS_TEN, -10).getValue(), BigInteger.valueOf(100));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementIntException1() {
    zPlus.times(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testTimesAdditiveElementIntException2() {
    zPlus.times(OTHER_ELEMENT, 10);
  }


  


  @Test
  public void testGetDefaultGenerator() {
    Assert.assertEquals(zPlus.getDefaultGenerator(), ONE);
  }

  @Test
  public void testCreateRandomGenerator() {
    for (int i=0; i<=50; i++) {
      AdditiveElement gen = zPlus.getRandomGenerator();
      Assert.assertTrue(gen.equals(ONE) || gen.equals(MINUS_ONE));
    }
  }

  @Test
  public void testCreateRandomGeneratorRandom() {
    for (int i=0; i<=50; i++) {
      AdditiveElement gen = zPlus.getRandomGenerator(random);
      Assert.assertTrue(gen.equals(ONE) || gen.equals(MINUS_ONE));
    }
  }

  @Test
  public void testIsGenerator() {
    Assert.assertTrue(zPlus.isGenerator(ONE));
    Assert.assertTrue(zPlus.isGenerator(MINUS_ONE));
    Assert.assertFalse(zPlus.isGenerator(ZERO));
    Assert.assertFalse(zPlus.isGenerator(MINUS_TEN));
    Assert.assertFalse(zPlus.isGenerator(TEN));
  }

  @Test
  public void testCreateEncodedElement() {
    for (int i=-5; i<=5; i++) {
      EncodedElement element = zPlus.createEncodedElement(BigInteger.valueOf(i));
      Assert.assertEquals(element.getValue(), BigInteger.valueOf(i));
    }
  }

  @Test
  public void testHashCode() {
    Assert.assertTrue(zPlus.hashCode() == zPlus.hashCode());
    Assert.assertTrue(zPlus.hashCode() == ZPlus.getInstance().hashCode());
    Assert.assertFalse(zPlus.hashCode() == SingletonGroup.getInstance().hashCode());
    Assert.assertFalse(zPlus.hashCode() == BooleanGroup.getInstance().hashCode());
    Assert.assertFalse(zPlus.hashCode() == new ZPlusModClass(BigInteger.ONE).hashCode());
    Assert.assertFalse(zPlus.hashCode() == ZERO.hashCode());
    Assert.assertFalse(zPlus.hashCode() == BigInteger.ZERO.hashCode());
    Assert.assertFalse(zPlus.hashCode() == "".hashCode());    
  }


  @Test
  public void testEqualsObject() {
    Assert.assertTrue(zPlus.equals(zPlus));
    Assert.assertTrue(zPlus.equals(ZPlus.getInstance()));
    Assert.assertFalse(zPlus.equals(SingletonGroup.getInstance()));
    Assert.assertFalse(zPlus.equals(BooleanGroup.getInstance()));
    Assert.assertFalse(zPlus.equals(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertFalse(zPlus.equals(null));
    Assert.assertFalse(zPlus.equals(ZERO));
    Assert.assertFalse(zPlus.equals(BigInteger.ZERO));
    Assert.assertFalse(zPlus.equals(""));    
  }

  @Test
  public void testToString() {
    Assert.assertEquals(zPlus.toString(), "ZPlusClass");
  }

}
