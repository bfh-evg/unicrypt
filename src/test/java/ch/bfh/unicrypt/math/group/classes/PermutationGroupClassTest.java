package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.group.interfaces.PermutationGroup;

@SuppressWarnings("static-method")
public class PermutationGroupClassTest {

  private static Random random = new Random();
  private static PermutationGroup group = new PermutationGroup(5);
  private static PermutationElement elt1 = group.getElement(501234);
  private static PermutationElement elt2 = group.getElement(543210);
  private static PermutationElement elt3 = group.getElement(503241);
  private static PermutationElement elt4 = group.getElement(540321);
  private static PermutationElement elt5 = group.getElement(504321);
  private static PermutationElement elt6 = group.getElement(512340);
  private static PermutationElement otherElt = new PermutationGroup(6).getElement(6012345);

  @Test
  public final void testPermutationGroupZero() {
    PermutationGroup group = new PermutationGroup(0);
    PermutationElement elt = group.getIdentityElement();
    Assert.assertEquals(elt.getValue(), BigInteger.ZERO);
    Assert.assertEquals(group.apply(elt, elt, elt, elt), elt);
    Assert.assertEquals(group.apply(elt, elt), elt);
    Assert.assertTrue(group.areEqual(elt, elt));
    Assert.assertTrue(group.contains(elt));
    Assert.assertEquals(group.getElement(ZPlus.getInstance().getElement(BigInteger.ZERO)), elt);    
    Assert.assertEquals(group.getElement(BigInteger.ZERO), elt);
    Assert.assertEquals(group.getElement(new int[]{}), elt);
    Assert.assertEquals(group.getRandomElement(), elt);
    Assert.assertEquals(group.getRandomElement(random), elt);
    Assert.assertTrue(group.equals(new PermutationGroup(0)));
    Assert.assertFalse(group.equals(new PermutationGroup(1)));
    Assert.assertTrue(group.getArity() == 1);
    Assert.assertEquals(group.getValue(elt), BigInteger.ZERO);
    Assert.assertEquals(group.getIdentityElement(), elt);
    Assert.assertEquals(group.getMinOrder(), BigInteger.ONE);
    Assert.assertEquals(group.getMinOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(group.getOrder(), BigInteger.ONE);
    Assert.assertEquals(group.getOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(group.getSize(), 0);
    Assert.assertTrue(group.contains(BigInteger.ZERO));
    Assert.assertTrue(group.hasSameOrder(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertEquals(group.invert(elt), elt);
    Assert.assertTrue(group.isIdentity(elt));
    Assert.assertEquals(group.selfApply(elt), elt);
    Assert.assertEquals(group.selfApply(elt, 5), elt);
    Assert.assertEquals(group.selfApply(elt, BigInteger.valueOf(5)), elt);
    Assert.assertEquals(group.toString(), "PermutationGroupClass[size=0]");
  }

  @Test
  public final void testPermutationGroupOne() {
    PermutationGroup group = new PermutationGroup(1);
    PermutationElement elt = group.getIdentityElement();
    Assert.assertEquals(elt.getValue(), BigInteger.TEN);
    Assert.assertEquals(group.apply(elt, elt, elt, elt), elt);
    Assert.assertEquals(group.apply(elt, elt), elt);
    Assert.assertTrue(group.areEqual(elt, elt));
    Assert.assertTrue(group.contains(elt));
    Assert.assertEquals(group.getElement(ZPlus.getInstance().getElement(BigInteger.ZERO)), elt);    
    Assert.assertEquals(group.getElement(BigInteger.ZERO), elt);
    Assert.assertEquals(group.getElement(new int[]{0}), elt);
    Assert.assertEquals(group.getRandomElement(), elt);
    Assert.assertEquals(group.getRandomElement(random), elt);
    Assert.assertTrue(group.equals(new PermutationGroup(1)));
    Assert.assertFalse(group.equals(new PermutationGroup(0)));
    Assert.assertTrue(group.getArity() == 1);
    Assert.assertEquals(group.getValue(elt), BigInteger.TEN);
    Assert.assertEquals(group.getIdentityElement(), elt);
    Assert.assertEquals(group.getMinOrder(), BigInteger.ONE);
    Assert.assertEquals(group.getMinOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(group.getOrder(), BigInteger.ONE);
    Assert.assertEquals(group.getOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    Assert.assertEquals(group.getSize(), 1);
    Assert.assertTrue(group.contains(BigInteger.ZERO));
    Assert.assertTrue(group.hasSameOrder(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertEquals(group.invert(elt), elt);
    Assert.assertTrue(group.isIdentity(elt));
    Assert.assertEquals(group.selfApply(elt), elt);
    Assert.assertEquals(group.selfApply(elt, 5), elt);
    Assert.assertEquals(group.selfApply(elt, BigInteger.valueOf(5)), elt);
    Assert.assertEquals(group.toString(), "PermutationGroupClass[size=1]");
  }

  @Test
  public final void testPermutationGroupTwo() {
    PermutationGroup group = new PermutationGroup(2);
    PermutationElement elt1 = group.getElement(201);
    PermutationElement elt2 = group.getElement(210);
    Assert.assertEquals(elt1.getValue(), BigInteger.valueOf(201));
    Assert.assertEquals(elt2.getValue(), BigInteger.valueOf(210));
    Assert.assertEquals(group.apply(elt1, elt1, elt1, elt1), elt1);
    Assert.assertEquals(group.apply(elt2, elt2, elt2, elt2), elt1);
    Assert.assertEquals(group.apply(elt2, elt2, elt2, elt2, elt2), elt2);
    Assert.assertEquals(group.apply(elt2, elt2, elt2, elt2, elt2, elt1), elt2);
    Assert.assertEquals(group.apply(elt1, elt1), elt1);
    Assert.assertEquals(group.apply(elt1, elt2), elt2);
    Assert.assertEquals(group.apply(elt2, elt1), elt2);
    Assert.assertEquals(group.apply(elt2, elt2), elt1);
    Assert.assertTrue(group.areEqual(elt1, elt1));
    Assert.assertTrue(group.areEqual(elt2, elt2));
    Assert.assertFalse(group.areEqual(elt1, elt2));
    Assert.assertFalse(group.areEqual(elt2, elt1));
    Assert.assertTrue(group.contains(elt1));
    Assert.assertTrue(group.contains(elt2));
    Assert.assertEquals(group.getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(201))), elt1);    
    Assert.assertEquals(group.getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(210))), elt2);    
    Assert.assertEquals(group.getElement(BigInteger.valueOf(201)), elt1);
    Assert.assertEquals(group.getElement(BigInteger.valueOf(210)), elt2);
    Assert.assertEquals(group.getElement(new int[]{0, 1}), elt1);
    Assert.assertEquals(group.getElement(new int[]{1, 0}), elt2);
    Assert.assertTrue(group.equals(new PermutationGroup(2)));
    Assert.assertFalse(group.equals(new PermutationGroup(1)));
    Assert.assertTrue(group.getArity() == 1);
    Assert.assertEquals(group.getValue(elt1), BigInteger.valueOf(201));
    Assert.assertEquals(group.getValue(elt2), BigInteger.valueOf(210));
    Assert.assertEquals(group.getIdentityElement(), elt1);
    Assert.assertEquals(group.getMinOrder(), BigInteger.valueOf(2));
    Assert.assertEquals(group.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(2)));
    Assert.assertEquals(group.getOrder(), BigInteger.valueOf(2));
    Assert.assertEquals(group.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(2)));
    Assert.assertEquals(group.getSize(), 2);
    Assert.assertTrue(group.contains(BigInteger.valueOf(210)));
    Assert.assertTrue(group.contains(BigInteger.valueOf(201)));
    Assert.assertFalse(group.contains(BigInteger.valueOf(20331)));
    Assert.assertTrue(group.hasSameOrder(new ZPlusModClass(BigInteger.valueOf(2))));
    Assert.assertEquals(group.invert(elt1), elt1);
    Assert.assertEquals(group.invert(elt2), elt2);
    Assert.assertTrue(group.isIdentity(elt1));
    Assert.assertEquals(group.selfApply(elt1), elt1);
    Assert.assertEquals(group.selfApply(elt1, 5), elt1);
    Assert.assertEquals(group.selfApply(elt2, 5), elt2);
    Assert.assertEquals(group.selfApply(elt2, 4), elt1);
    Assert.assertEquals(group.selfApply(elt1, BigInteger.valueOf(5)), elt1);
    Assert.assertEquals(group.toString(), "PermutationGroupClass[size=2]");
  }

  @Test
  public final void testApplyElementArray() {
    Assert.assertEquals(group.apply(), group.getIdentityElement());
    Assert.assertEquals(group.apply(elt1), elt1);
    Assert.assertEquals(group.apply(elt2), elt2);
    Assert.assertEquals(group.apply(elt3), elt3);
    Assert.assertEquals(group.apply(elt4), elt4);
    Assert.assertEquals(group.apply(elt5), elt5);
    Assert.assertEquals(group.apply(elt1, elt1, elt1), elt1);
    Assert.assertEquals(group.apply(elt2, elt1, elt1), elt2);
    Assert.assertEquals(group.apply(elt1, elt2, elt2, elt1), elt1);
    Assert.assertEquals(group.apply(new PermutationElement[]{elt1, elt1, elt1}), elt1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException1() {
    group.apply((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException2() {
    group.apply(elt1, elt2, null, elt3);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException3() {
    group.apply(elt1, elt2, otherElt);
  }

  @Test
  public final void testApplyElementElement() {
    Assert.assertEquals(group.apply(elt1, elt1), elt1);
    Assert.assertEquals(group.apply(elt2, elt1), elt2);
    Assert.assertEquals(group.apply(elt2, elt2), elt1);
    Assert.assertEquals(group.apply(elt3, elt1), elt3);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException1() {
    group.apply(elt1, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException2() {
    group.apply(null, elt1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException3() {
    group.apply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException4() {
    group.apply(elt1, otherElt);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException5() {
    group.apply(otherElt, elt1);
  }

  @Test
  public final void testAreEqualElements() {
    Assert.assertTrue(group.areEqual(elt1, elt1));
    Assert.assertTrue(group.areEqual(elt1, group.getElement(501234)));
    Assert.assertFalse(group.areEqual(elt1, elt2));
    Assert.assertFalse(group.areEqual(elt1, otherElt));
    Assert.assertFalse(group.areEqual(otherElt, elt1));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testAreEqualElementsException1() {
    group.areEqual(elt1, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testAreEqualElementsException2() {
    group.areEqual(null, elt1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testAreEqualElementsException3() {
    group.areEqual(null, null);
  }

  @Test
  public final void testContainsElement() {
    Assert.assertTrue(group.contains(elt1));
    Assert.assertTrue(group.contains(elt2));
    Assert.assertTrue(group.contains(elt3));
    Assert.assertTrue(group.contains(elt4));
    Assert.assertTrue(group.contains(elt5));
    Assert.assertFalse(group.contains(otherElt));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testContainsElementException() {
    group.contains(null);
  }

  @Test
  public final void testCreateElementAtomicElement() {
    Assert.assertEquals(new PermutationGroup(2).getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(210))).getValue(), BigInteger.valueOf(210));
    // more tests in testCreateElementBigInteger()
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementAtomicElementException1() {
    group.getElement((AtomicElement) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementAtomicElementException2() {
    new PermutationGroup(2).getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(211)));
    // more tests in testCreateElementBigInteger()
  }

  @Test
  public final void testCreateElementBigInteger() {
    Assert.assertEquals(new PermutationGroup(0).getElement(BigInteger.valueOf(0)).getValue(), BigInteger.valueOf(0));
    Assert.assertEquals(new PermutationGroup(1).getElement(BigInteger.valueOf(10)).getValue(), BigInteger.valueOf(10));
    Assert.assertEquals(new PermutationGroup(2).getElement(BigInteger.valueOf(201)).getValue(), BigInteger.valueOf(201));
    Assert.assertEquals(new PermutationGroup(2).getElement(BigInteger.valueOf(210)).getValue(), BigInteger.valueOf(210));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3012)).getValue(), BigInteger.valueOf(3012));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3021)).getValue(), BigInteger.valueOf(3021));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3102)).getValue(), BigInteger.valueOf(3102));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3120)).getValue(), BigInteger.valueOf(3120));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3201)).getValue(), BigInteger.valueOf(3201));
    Assert.assertEquals(new PermutationGroup(3).getElement(BigInteger.valueOf(3210)).getValue(), BigInteger.valueOf(3210));
    Assert.assertEquals(new PermutationGroup(10).getElement(new BigInteger("100123456789")).getValue(), new BigInteger("100123456789"));
    Assert.assertEquals(new PermutationGroup(11).getElement(new BigInteger("111000010203040506070809")).getValue(), new BigInteger("111000010203040506070809"));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementBigIntegerException1() {
    group.getElement((BigInteger) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementBigIntegerException2() {
    group.getElement(BigInteger.valueOf(51234));
  }

  @Test
  public final void testCreateElementInt() {
    Assert.assertEquals(new PermutationGroup(0).getElement(0).getValue(), BigInteger.valueOf(0));
    Assert.assertEquals(new PermutationGroup(1).getElement(10).getValue(), BigInteger.valueOf(10));
    Assert.assertEquals(new PermutationGroup(2).getElement(201).getValue(), BigInteger.valueOf(201));
    Assert.assertEquals(new PermutationGroup(2).getElement(210).getValue(), BigInteger.valueOf(210));
    Assert.assertEquals(new PermutationGroup(3).getElement(3012).getValue(), BigInteger.valueOf(3012));
    Assert.assertEquals(new PermutationGroup(3).getElement(3021).getValue(), BigInteger.valueOf(3021));
    // more tests in testCreateElementBigInteger()
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementIntException() {
    group.getElement(51234);
  }

  @Test
  public final void testCreateElementIntArray() {
    Assert.assertEquals(new PermutationGroup(0).getElement(new int[]{}).getValue(), BigInteger.valueOf(0));
    Assert.assertEquals(new PermutationGroup(1).getElement(new int[]{0}).getValue(), BigInteger.valueOf(10));
    Assert.assertEquals(new PermutationGroup(2).getElement(new int[]{0,1}).getValue(), BigInteger.valueOf(201));
    Assert.assertEquals(new PermutationGroup(2).getElement(new int[]{1,0}).getValue(), BigInteger.valueOf(210));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{0,1,2}).getValue(), BigInteger.valueOf(3012));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{0,2,1}).getValue(), BigInteger.valueOf(3021));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{1,0,2}).getValue(), BigInteger.valueOf(3102));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{1,2,0}).getValue(), BigInteger.valueOf(3120));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{2,0,1}).getValue(), BigInteger.valueOf(3201));
    Assert.assertEquals(new PermutationGroup(3).getElement(new int[]{2,1,0}).getValue(), BigInteger.valueOf(3210));
    Assert.assertEquals(new PermutationGroup(10).getElement(new int[]{0,1,2,3,4,5,6,7,8,9}).getValue(), new BigInteger("100123456789"));
    Assert.assertEquals(new PermutationGroup(11).getElement(new int[]{10,0,1,2,3,4,5,6,7,8,9}).getValue(), new BigInteger("111000010203040506070809"));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementIntArrayException1() {
    group.getElement((int[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementIntArrayException2() {
    group.getElement(new int[]{1,2,3,4,5});
  }

  @Test
  public final void testCreateRandomElement() {
    for (int s=0; s<=6; s++) {
      PermutationGroup group = new PermutationGroup(s);
      HashSet<PermutationElement> set = new HashSet<PermutationElement>();
      do {
        PermutationElement elt = group.getRandomElement();
        set.add(elt);
        Assert.assertEquals(group.getElement(elt.getValue()), elt);
      } while (set.size()<group.getOrder().intValue());
    }
  }

  @Test
  public final void testCreateRandomElementRandom() {
    for (int s=0; s<=6; s++) {
      PermutationGroup group = new PermutationGroup(s);
      HashSet<PermutationElement> set = new HashSet<PermutationElement>();
      do {
        PermutationElement elt = group.getRandomElement(random);
        set.add(elt);
        Assert.assertEquals(group.getElement(elt.getValue()), elt);
      } while (set.size()<group.getOrder().intValue());
    }
  }

  @Test
  public final void testEqualsObject() {
    Assert.assertTrue(group.equals(group));
    Assert.assertTrue(group.equals(new PermutationGroup(5)));
    Assert.assertFalse(group.equals(ZPlus.getInstance()));
    Assert.assertFalse(group.equals(new PermutationGroup(4)));
    Assert.assertFalse(group.equals(new PermutationGroup(10)));
    Assert.assertFalse(group.equals(SingletonGroup.getInstance()));
    Assert.assertFalse(group.equals(BooleanGroup.getInstance()));
    Assert.assertFalse(group.equals(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertFalse(group.equals(null));
    Assert.assertFalse(group.equals(group.getIdentityElement()));
    Assert.assertFalse(group.equals(BigInteger.ZERO));
    Assert.assertFalse(group.equals(""));    
  }

  @Test
  public final void testGetArity() {
    Assert.assertEquals(group.getArity(), 1);    
  }

  @Test
  public final void testGetBigInteger() {
    for (int s=0; s<=110; s++) {
      PermutationGroup group = new PermutationGroup(s);
      PermutationElement elt = group.getIdentityElement();
      Assert.assertEquals(group.getElement(group.getValue(elt)), elt);
      Assert.assertEquals(group.getElement(group.getValue(elt.invert())), elt.invert());
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testGetBigIntegerException1() {
    group.getValue(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testGetBigIntegerException2() {
    group.getValue(otherElt);
  }

  @Test
  public final void testGetIdentityElement() {
    Assert.assertEquals(group.getIdentityElement(), group.getElement(new int[]{0, 1, 2, 3, 4}));
  }

  @Test
  public final void testGetMinOrder() {
    Assert.assertEquals(group.getMinOrder().intValue(), 120);
  }

  @Test
  public final void testGetMinOrderGroup() {
    Assert.assertEquals(group.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(120)));
  }

  @Test
  public final void testGetOrder() {
    Assert.assertEquals(group.getOrder().intValue(), 120);
  }

  @Test
  public final void testGetOrderGroup() {
    Assert.assertEquals(group.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(120)));
  }

  @Test
  public final void testGetSize() {
    for (int s=0; s<=110; s++) {
      PermutationGroup group = new PermutationGroup(s);
      Assert.assertEquals(group.getSize(), s);
    }
  }

  @Test
  public final void testHasElement() {
    Assert.assertTrue(group.contains(elt1.getValue()));
    Assert.assertTrue(group.contains(elt2.getValue()));
    Assert.assertTrue(group.contains(elt3.getValue()));
    Assert.assertTrue(group.contains(elt4.getValue()));
    Assert.assertTrue(group.contains(elt5.getValue()));
    int order = 0;
    for (int i=200000; i<300000; i++) {
      if (group.contains(BigInteger.valueOf(i))) {
        order++;
      }
    }
    Assert.assertEquals(order, 120);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testHasElementException() {
    group.contains(null);
  }
  
  @Test
  public final void testHashCode() {
    Assert.assertTrue(group.hashCode() == new PermutationGroup(5).hashCode());
    Assert.assertFalse(group.hashCode() == new PermutationGroup(4).hashCode());
    Assert.assertFalse(group.hashCode() == new PermutationGroup(10).hashCode());
    Assert.assertFalse(group.hashCode() == SingletonGroup.getInstance().hashCode());
    Assert.assertFalse(group.hashCode() == BooleanGroup.getInstance().hashCode());
    Assert.assertFalse(group.hashCode() == elt1.hashCode());
    Assert.assertFalse(group.hashCode() == BigInteger.ZERO.hashCode());
    Assert.assertFalse(group.hashCode() == "".hashCode());    
  }

  @Test
  public final void testHasSameOrder() {
    Assert.assertTrue(group.hasSameOrder(group));
    Assert.assertTrue(group.hasSameOrder(new PermutationGroup(5)));
    Assert.assertTrue(group.hasSameOrder(new ZPlusModClass(BigInteger.valueOf(120))));
    Assert.assertFalse(group.hasSameOrder(new PermutationGroup(4)));
    Assert.assertFalse(group.hasSameOrder(new PermutationGroup(6)));
    Assert.assertFalse(group.hasSameOrder(new ZStarMod(BigInteger.valueOf(8))));    
  }

  @Test
  public final void testInvert() {
    Assert.assertEquals(elt1.invert(), elt1);
    Assert.assertEquals(elt2.invert().apply(elt2), elt1);
    Assert.assertEquals(elt3.invert().apply(elt3), elt1);
    Assert.assertEquals(elt4.invert().apply(elt4), elt1);
    Assert.assertEquals(elt5.invert().apply(elt5), elt1);
    Assert.assertEquals(elt1.invert().invert(), elt1);
    Assert.assertEquals(elt2.invert().invert(), elt2);
    Assert.assertEquals(elt3.invert().invert(), elt3);
    Assert.assertEquals(elt4.invert().invert(), elt4);
    Assert.assertEquals(elt5.invert().invert(), elt5);
  }

  @Test
  public final void testIsIdentityElement() {
    Assert.assertTrue(group.isIdentity(elt1));
    Assert.assertFalse(group.isIdentity(elt2));
    Assert.assertFalse(group.isIdentity(elt3));
    Assert.assertFalse(group.isIdentity(elt4));
    Assert.assertFalse(group.isIdentity(elt5));
    Assert.assertFalse(group.isIdentity(otherElt));
    Assert.assertFalse(group.isIdentity(new PermutationGroup(4).getIdentityElement()));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testIsIdentityElementException1() {
    group.isIdentity(null);
  }

  @Test
  public final void testMultiSelfApplyElementArrayBigIntegerArray() {
    Assert.assertEquals(group.multiSelfApply(new Element[] {}, new BigInteger[] {}), elt1);
    Assert.assertEquals(group.multiSelfApply(new Element[] {elt1}, new BigInteger[] {BigInteger.ZERO}), elt1);
    Assert.assertEquals(group.multiSelfApply(new Element[] {elt2}, new BigInteger[] {BigInteger.ZERO}), elt1);
    Assert.assertEquals(group.multiSelfApply(new Element[] {elt2}, new BigInteger[] {BigInteger.ONE}), elt2);
    Assert.assertEquals(group.multiSelfApply(new Element[] {elt1, elt2}, new BigInteger[] {BigInteger.TEN, BigInteger.ONE}), elt2);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    group.multiSelfApply(new Element[] {elt1, elt1}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    group.multiSelfApply(null, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    group.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    group.multiSelfApply(new Element[] {elt1, elt1, elt3}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    group.multiSelfApply(new Element[] {otherElt, elt1}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test
  public final void testSelfApplyElement() {
    Assert.assertEquals(group.selfApply(elt1), group.selfApply(elt1, 2));
    Assert.assertEquals(group.selfApply(elt2), group.selfApply(elt2, 2));
    Assert.assertEquals(group.selfApply(elt3), group.selfApply(elt3, 2));
    Assert.assertEquals(group.selfApply(elt4), group.selfApply(elt4, 2));
    Assert.assertEquals(group.selfApply(elt5), group.selfApply(elt5, 2));
    Assert.assertEquals(group.selfApply(elt6), group.selfApply(elt6, 2));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException1() {
    group.selfApply(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException2() {
    group.selfApply(otherElt);
  }

  @Test
  public final void testSelfApplyElementAtomicElement() {
    Assert.assertEquals(group.selfApply(elt6,ZPlus.getInstance().getElement(10)), elt1);
    // More test see testSelfApplyElementBigInteger()
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException1() {
    group.selfApply(elt1, (PermutationElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException2() {
    group.selfApply(null, elt1);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyAtomicElementException3() {
    group.selfApply(null, (PermutationElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException4() {
    group.selfApply(otherElt, elt1);
  }

  @Test
  public final void testSelfApplyElementBigInteger() {
    Assert.assertEquals(group.selfApply(elt1,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt1,BigInteger.valueOf(1)), elt1);
    Assert.assertEquals(group.selfApply(elt1,BigInteger.valueOf(2)), elt1);
    Assert.assertEquals(group.selfApply(elt1,BigInteger.valueOf(3)), elt1);
    Assert.assertEquals(group.selfApply(elt2,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt3,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt4,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt5,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(0)), elt1);
    Assert.assertEquals(group.selfApply(elt1,BigInteger.valueOf(1)), elt1);
    Assert.assertEquals(group.selfApply(elt2,BigInteger.valueOf(1)), elt2);
    Assert.assertEquals(group.selfApply(elt3,BigInteger.valueOf(1)), elt3);
    Assert.assertEquals(group.selfApply(elt4,BigInteger.valueOf(1)), elt4);
    Assert.assertEquals(group.selfApply(elt5,BigInteger.valueOf(1)), elt5);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(1)), elt6);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(5)), elt1);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(10)), elt1);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(15)), elt1);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(20)), elt1);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(6)), elt6);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(11)), elt6);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(16)), elt6);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(21)), elt6);
    Assert.assertEquals(group.selfApply(elt6,BigInteger.valueOf(26)), elt6);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException1() {
    group.selfApply(elt1, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException2() {
    group.selfApply(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException3() {
    group.selfApply(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException4() {
    group.selfApply(otherElt, BigInteger.TEN);
  }

  @Test
  public final void testSelfApplyElementInt() {
    Assert.assertEquals(group.selfApply(elt1,0), elt1);
    Assert.assertEquals(group.selfApply(elt1,1), elt1);
    Assert.assertEquals(group.selfApply(elt1,2), elt1);
    Assert.assertEquals(group.selfApply(elt1,3), elt1);
    Assert.assertEquals(group.selfApply(elt2,0), elt1);
    Assert.assertEquals(group.selfApply(elt3,0), elt1);
    Assert.assertEquals(group.selfApply(elt4,0), elt1);
    Assert.assertEquals(group.selfApply(elt5,0), elt1);
    Assert.assertEquals(group.selfApply(elt6,0), elt1);
    Assert.assertEquals(group.selfApply(elt1,1), elt1);
    Assert.assertEquals(group.selfApply(elt2,1), elt2);
    Assert.assertEquals(group.selfApply(elt3,1), elt3);
    Assert.assertEquals(group.selfApply(elt4,1), elt4);
    Assert.assertEquals(group.selfApply(elt5,1), elt5);
    Assert.assertEquals(group.selfApply(elt6,1), elt6);
    Assert.assertEquals(group.selfApply(elt6,5), elt1);
    Assert.assertEquals(group.selfApply(elt6,10), elt1);
    Assert.assertEquals(group.selfApply(elt6,15), elt1);
    Assert.assertEquals(group.selfApply(elt6,20), elt1);
    Assert.assertEquals(group.selfApply(elt6,6), elt6);
    Assert.assertEquals(group.selfApply(elt6,11), elt6);
    Assert.assertEquals(group.selfApply(elt6,16), elt6);
    Assert.assertEquals(group.selfApply(elt6,21), elt6);
    Assert.assertEquals(group.selfApply(elt6,26), elt6);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException1() {
    group.selfApply(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException2() {
    group.selfApply(otherElt, 10);
  }

  @Test
  public final void testToString() {
    Assert.assertEquals(group.toString(), "PermutationGroupClass[size=5]");
  }

}
