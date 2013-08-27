package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.crypto.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.group.interfaces.GStarMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.ZStarMod;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;
import ch.bfh.unicrypt.math.java2unicrypt.interfaces.ExternalDataMapper;
import ch.bfh.unicrypt.math.utility.mapper.classes.CharsetXRadixYMapperClass;

@SuppressWarnings("static-method")
public class ProductGroupClassTest {

  private static Random random = new Random();
  private static final GStarMod g1 = new GStarMod(BigInteger.valueOf(11), false); // order=10
  private static final GStarMod g2 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(2)); 
  private static final GStarMod g5 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(5)); 
  private static final GStarMod g10 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(2), BigInteger.valueOf(5)); // order=4
  private static final ZPlus zPlus = ZPlus.getInstance();
  private static final ZStarMod zStarMod = new ZStarMod(BigInteger.valueOf(10));

  private static final ProductGroup pg0 = new ProductGroup();
  private static final ProductGroup pg1 = new ProductGroup(g1, g2, g5, g10);
  private static final ProductGroup pg2 = new ProductGroup(g1, g2, zPlus, g5, g10);
  private static final ProductGroup pg3 = new ProductGroup(g1, g2, zStarMod, g5, g10);
  private static final ProductGroup pg4 = new ProductGroup(g1, g2, zStarMod, g5, zPlus, g10);
  private static final ProductGroup pg5 = new ProductGroup(g1, g1, g1, g1);
  private static final ProductGroup pg6 = new ProductGroup(pg0, pg1, pg3, pg5);
  private static final ProductGroup pg7 = new ProductGroup(pg0, pg1, pg2);
  private static final ProductGroup pg8 = new ProductGroup(pg0, pg0, pg0);
  private static final ProductGroup[] pgs = new ProductGroup[]{pg0, pg1, pg2, pg3, pg4, pg5, pg6, pg7, pg8};


  private static final Element elt1 = g1.getIdentityElement();
  private static final Element elt2 = g2.getIdentityElement();
  private static final Element elt5 = g5.getIdentityElement();
  private static final Element elt10 = g10.getIdentityElement();
  private static final Element elt = zPlus.getIdentityElement();


  @Test
  public final void testGetMinOrder() {
    Assert.assertEquals(pg0.getMinOrder(), BigInteger.valueOf(1));
    Assert.assertEquals(pg1.getMinOrder(), BigInteger.valueOf(100));
    Assert.assertEquals(pg2.getMinOrder(), Group.INFINITE_ORDER);
    Assert.assertEquals(pg3.getMinOrder(), BigInteger.valueOf(100));
    Assert.assertEquals(pg4.getMinOrder(), Group.INFINITE_ORDER);
    Assert.assertEquals(pg5.getMinOrder(), BigInteger.valueOf(1));
    Assert.assertEquals(pg6.getMinOrder(), BigInteger.valueOf(10000));
  }

  @Test
  public final void testGetMinOrderGroup() {
    Assert.assertEquals(pg0.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(1)));
    Assert.assertEquals(pg1.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(100)));
    Assert.assertEquals(pg3.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(100)));
    Assert.assertEquals(pg5.getMinOrderGroup(), new ZPlusModClass(BigInteger.valueOf(1)));
    Assert.assertEquals(pg6.getMinOrder(), BigInteger.valueOf(10000));
  }

  @Test (expected=UnsupportedOperationException.class)
  public final void testGetMinOrderGroupException() {
    pg2.getMinOrderGroup();
  }

  @Test
  public final void testGetOrder() {
    Assert.assertEquals(pg0.getOrder(), BigInteger.valueOf(1));
    Assert.assertEquals(pg1.getOrder(), BigInteger.valueOf(100));
    Assert.assertEquals(pg2.getOrder(), Group.INFINITE_ORDER);
    Assert.assertEquals(pg3.getOrder(), Group.UNKNOWN_ORDER);
    Assert.assertEquals(pg4.getOrder(), Group.INFINITE_ORDER);
    Assert.assertEquals(pg5.getOrder(), BigInteger.valueOf(1));
    Assert.assertEquals(pg6.getOrder(), Group.UNKNOWN_ORDER);
  }

  @Test
  public final void testGetOrderGroup() {
    Assert.assertEquals(pg0.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(1)));
    Assert.assertEquals(pg1.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(100)));
  }

  @Test (expected=UnsupportedOperationException.class)
  public final void testGetOrderGroupException1() {
    pg2.getOrderGroup();
  }

  @Test (expected=UnsupportedOperationException.class)
  public final void testGetOrderGroupException2() {
    pg3.getOrderGroup();
  }

  @Test 
  public final void testProductGroupClass() {
    // the constructor has been tested above
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testProductGroupClassException1() {
    new ProductGroup((Group[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testProductGroupClassException2() {
    new ProductGroup(g1, g2, g5, null, g10);
  }

  @Test
  public final void testGetGroupAtInt() {
    Assert.assertEquals(pg4.getGroupAt(0), g1);
    Assert.assertEquals(pg4.getGroupAt(1), g2);
    Assert.assertEquals(pg4.getGroupAt(2), zStarMod);
    Assert.assertEquals(pg4.getGroupAt(3), g5);
    Assert.assertEquals(pg4.getGroupAt(4), zPlus);
    Assert.assertEquals(pg4.getGroupAt(5), g10);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntException1() {
    pg4.getGroupAt(-1);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntException2() {
    pg4.getGroupAt(-6);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntException3() {
    pg0.getGroupAt(0);
  }

  @Test
  public final void testGetArity() {
    Assert.assertEquals(pg0.getArity(), 0);
    Assert.assertEquals(pg1.getArity(), 4);
    Assert.assertEquals(pg2.getArity(), 5);
    Assert.assertEquals(pg3.getArity(), 5);
    Assert.assertEquals(pg4.getArity(), 6);
    Assert.assertEquals(pg5.getArity(), 4);
    Assert.assertEquals(pg6.getArity(), 4);
  }

  @Test
  public final void testIsPowerGroup() {
    Assert.assertTrue(pg0.isPowerGroup());
    Assert.assertFalse(pg1.isPowerGroup());
    Assert.assertFalse(pg2.isPowerGroup());
    Assert.assertFalse(pg3.isPowerGroup());
    Assert.assertFalse(pg4.isPowerGroup());
    Assert.assertTrue(pg5.isPowerGroup());
    Assert.assertFalse(pg6.isPowerGroup());
  }

  @Test
  public final void testHasSameOrder() {
    Assert.assertTrue(pg0.hasSameOrder(pg0));
    Assert.assertFalse(pg0.hasSameOrder(pg1));
    Assert.assertFalse(pg0.hasSameOrder(pg2));
    Assert.assertFalse(pg0.hasSameOrder(pg3));
    Assert.assertFalse(pg0.hasSameOrder(pg4));
    Assert.assertTrue(pg0.hasSameOrder(pg5));
    Assert.assertFalse(pg0.hasSameOrder(pg6));

    Assert.assertFalse(pg1.hasSameOrder(pg0));
    Assert.assertTrue(pg1.hasSameOrder(pg1));
    Assert.assertFalse(pg1.hasSameOrder(pg2));
    Assert.assertFalse(pg1.hasSameOrder(pg3));
    Assert.assertFalse(pg1.hasSameOrder(pg4));
    Assert.assertFalse(pg1.hasSameOrder(pg5));
    Assert.assertFalse(pg1.hasSameOrder(pg6));

    Assert.assertFalse(pg2.hasSameOrder(pg0));
    Assert.assertFalse(pg2.hasSameOrder(pg1));
    Assert.assertTrue(pg2.hasSameOrder(pg2));
    Assert.assertFalse(pg2.hasSameOrder(pg3));
    Assert.assertTrue(pg2.hasSameOrder(pg4));
    Assert.assertFalse(pg2.hasSameOrder(pg5));
    Assert.assertFalse(pg2.hasSameOrder(pg6));

    Assert.assertFalse(pg3.hasSameOrder(pg0));
    Assert.assertFalse(pg3.hasSameOrder(pg1));
    Assert.assertFalse(pg3.hasSameOrder(pg2));
    Assert.assertTrue(pg3.hasSameOrder(pg3));
    Assert.assertFalse(pg3.hasSameOrder(pg4));
    Assert.assertFalse(pg3.hasSameOrder(pg5));
    Assert.assertFalse(pg3.hasSameOrder(pg6));

    Assert.assertFalse(pg4.hasSameOrder(pg0));
    Assert.assertFalse(pg4.hasSameOrder(pg1));
    Assert.assertTrue(pg4.hasSameOrder(pg2));
    Assert.assertFalse(pg4.hasSameOrder(pg3));
    Assert.assertTrue(pg4.hasSameOrder(pg4));
    Assert.assertFalse(pg4.hasSameOrder(pg5));
    Assert.assertFalse(pg4.hasSameOrder(pg6));

    Assert.assertTrue(pg5.hasSameOrder(pg0));
    Assert.assertFalse(pg5.hasSameOrder(pg1));
    Assert.assertFalse(pg5.hasSameOrder(pg2));
    Assert.assertFalse(pg5.hasSameOrder(pg3));
    Assert.assertFalse(pg5.hasSameOrder(pg4));
    Assert.assertTrue(pg5.hasSameOrder(pg5));
    Assert.assertFalse(pg5.hasSameOrder(pg6));

    Assert.assertFalse(pg6.hasSameOrder(pg0));
    Assert.assertFalse(pg6.hasSameOrder(pg1));
    Assert.assertFalse(pg6.hasSameOrder(pg2));
    Assert.assertFalse(pg6.hasSameOrder(pg3));
    Assert.assertFalse(pg6.hasSameOrder(pg4));
    Assert.assertFalse(pg6.hasSameOrder(pg5));
    Assert.assertTrue(pg6.hasSameOrder(pg6));
  }

  @Test
  public final void testGetGroupAtIntArray() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    Assert.assertEquals(pg.getGroupAt(), pg);
    Assert.assertEquals(pg.getGroupAt(0), pg6);
    Assert.assertEquals(pg.getGroupAt(1, 0), pg0);
    Assert.assertEquals(pg.getGroupAt(1, 1), pg1);
    Assert.assertEquals(pg.getGroupAt(1, 2), pg6);
    Assert.assertEquals(pg.getGroupAt(1, 2, 0), pg0);
    Assert.assertEquals(pg.getGroupAt(1, 2, 1), pg1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2), pg3);
    Assert.assertEquals(pg.getGroupAt(1, 2, 3), pg5);
    Assert.assertEquals(pg.getGroupAt(1, 2, 1, 0), g1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 1, 1), g2);
    Assert.assertEquals(pg.getGroupAt(1, 2, 1, 2), g5);
    Assert.assertEquals(pg.getGroupAt(1, 2, 1, 3), g10);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2, 0), g1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2, 1), g2);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2, 2), zStarMod);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2, 3), g5);
    Assert.assertEquals(pg.getGroupAt(1, 2, 2, 4), g10);
    Assert.assertEquals(pg.getGroupAt(1, 2, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(1, 2, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(1, 1, 0), g1);
    Assert.assertEquals(pg.getGroupAt(1, 1, 1), g2);
    Assert.assertEquals(pg.getGroupAt(1, 1, 2), g5);
    Assert.assertEquals(pg.getGroupAt(1, 1, 3), g10);
    Assert.assertEquals(pg.getGroupAt(0, 0), pg0);
    Assert.assertEquals(pg.getGroupAt(0, 1), pg1);
    Assert.assertEquals(pg.getGroupAt(0, 2), pg3);
    Assert.assertEquals(pg.getGroupAt(0, 3), pg5);
    Assert.assertEquals(pg.getGroupAt(0, 1, 0), g1);
    Assert.assertEquals(pg.getGroupAt(0, 1, 1), g2);
    Assert.assertEquals(pg.getGroupAt(0, 1, 2), g5);
    Assert.assertEquals(pg.getGroupAt(0, 1, 3), g10);
    Assert.assertEquals(pg.getGroupAt(0, 2, 0), g1);
    Assert.assertEquals(pg.getGroupAt(0, 2, 1), g2);
    Assert.assertEquals(pg.getGroupAt(0, 2, 2), zStarMod);
    Assert.assertEquals(pg.getGroupAt(0, 2, 3), g5);
    Assert.assertEquals(pg.getGroupAt(0, 2, 4), g10);
    Assert.assertEquals(pg.getGroupAt(0, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(0, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(0, 3, 1), g1);
    Assert.assertEquals(pg.getGroupAt(0, 3, 1), g1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testGetGroupAtIntArrayException1() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg.getGroupAt(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testGetGroupAtIntArrayException2() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg.getGroupAt(0, 1, 1, 1);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntArrayException3() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg.getGroupAt(0, 1, 5);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntArrayException4() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg.getGroupAt(0, 0, 2);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testGetGroupAtIntArrayException5() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg.getGroupAt(0, 0, 0, 0, 0);
  }

  @Test
  public final void testRemoveGroupAt() {
    ProductGroup pg = new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6));
    pg = pg.removeGroupAt(0);
    pg = (ProductGroup) pg.getGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = (ProductGroup) pg.getGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = (ProductGroup) pg.getGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = pg.removeGroupAt(0);
    pg = pg.removeGroupAt(0);
    Assert.assertEquals(pg.getGroupAt(0), g1);
    pg = pg.removeGroupAt(0);
    Assert.assertEquals(pg, pg0);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testRemoveGroupAtExceptio1() {
    pg1.removeGroupAt(-1);
  }

  @Test (expected=IndexOutOfBoundsException.class)
  public final void testRemoveGroupAtExceptio2() {
    pg1.removeGroupAt(5);
  }

  @Test
  public final void testEqualsObject() {
    Assert.assertTrue(pg1.equals(pg1));
    Assert.assertTrue(new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6)).equals(new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6))));
    Assert.assertTrue(new ProductGroup().equals(new ProductGroup()));
    Assert.assertTrue(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup()))).equals(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup())))));
    Assert.assertFalse(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup()))).equals(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup()))));
    Assert.assertFalse(pg1.equals(null));
    Assert.assertFalse(pg1.equals(pg2));
    Assert.assertFalse(pg1.equals(g1));
    Assert.assertFalse(pg1.equals(""));
    Assert.assertTrue(pg5.equals(new PowerGroup(g1,4)));
  }

  @Test
  public final void testHashCode() {
    Assert.assertTrue(pg1.hashCode() == pg1.hashCode());
    Assert.assertTrue(new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6)).hashCode() == new ProductGroup(pg6, new ProductGroup(pg0, pg1, pg6)).hashCode());
    Assert.assertTrue(new ProductGroup().hashCode()==new ProductGroup().hashCode());
    Assert.assertTrue(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup()))).hashCode()==new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup()))).hashCode());
    Assert.assertFalse(new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup(new ProductGroup()))).hashCode()==new ProductGroup(new ProductGroup(), new ProductGroup(), new ProductGroup(new ProductGroup())).hashCode());
    Assert.assertFalse(pg1.hashCode()==pg2.hashCode());
    Assert.assertFalse(pg1.hashCode()==g1.hashCode());
    Assert.assertFalse(pg1.hashCode()=="".hashCode());
    Assert.assertTrue(pg5.hashCode()==new PowerGroup(g1,4).hashCode());
  }

  @Test
  public final void testToString() {
    Assert.assertEquals(pg0.toString(), "ProductGroupClass[groups=[], arity=0]");
  }

  @Test
  public final void testCreateTupleElementListOfElement() {
    List<Element> elts = new ArrayList<Element>();
    elts.add(elt1);
    elts.add(elt2);
    elts.add(elt5);
    Tuple telt1 = ProductGroup.createTupleElement(elts);
    Assert.assertEquals(telt1.getGroup().getIdentityElement(), telt1);
    Assert.assertFalse(telt1.getGroup() instanceof PowerGroup);
    Assert.assertTrue(telt1.getGroup().contains(telt1));
    Assert.assertEquals(new ProductGroup(g1, g2, g5).getElement(elt1, elt2, elt5), telt1);
    // more tests see below
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateTupleElementListOfElementException1() {
    ProductGroup.createTupleElement((List<Element>)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateTupleElementListOfElementException2() {
    List<Element> elts = new ArrayList<Element>();
    elts.add(elt1);
    elts.add(null);
    elts.add(elt5);
    ProductGroup.createTupleElement(elts);
  }

  @Test
  public final void testCreateTupleElementElementArray() {
    Tuple telt1 = ProductGroup.createTupleElement(elt1, elt2, elt5);
    Assert.assertEquals(telt1.getGroup().getIdentityElement(), telt1);
    Assert.assertFalse(telt1.getGroup() instanceof PowerGroup);
    Assert.assertTrue(telt1.getGroup().contains(telt1));
    Assert.assertEquals(new ProductGroup(g1, g2, g5).getElement(elt1, elt2, elt5), telt1);

    Tuple telt2 = ProductGroup.createTupleElement(elt1);
    Assert.assertEquals(telt2.getGroup().getIdentityElement(), telt2);
    Assert.assertTrue(telt2.getGroup() instanceof PowerGroup);
    Assert.assertTrue(telt2.getGroup().contains(telt2));
    Assert.assertEquals(new ProductGroup(g1).getElement(elt1), telt2);
    Assert.assertEquals(new PowerGroup(g1, 1).getElement(elt1), telt2);

    Tuple telt3 = ProductGroup.createTupleElement();
    Assert.assertEquals(telt3.getGroup().getIdentityElement(), telt3);
    Assert.assertTrue(telt3.getGroup() instanceof PowerGroup);
    Assert.assertTrue(telt3.getGroup().contains(telt3));
    Assert.assertEquals(new ProductGroup().getElement(), telt3);
    Assert.assertEquals(new PowerGroup().getElement(), telt3);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateTupleElementElementArrayException1() {
    ProductGroup.createTupleElement((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateTupleElementElementArrayException2() {
    ProductGroup.createTupleElement(new Element[]{elt1, null, elt5});
  }

  @Test 
  public final void testCreateElementElementArray() {
    Element e0 = pg0.getElement();
    Element e1 = pg1.getElement(elt1, elt2, elt5, elt10);
    Element e2 = pg2.getElement(elt1, elt2, elt, elt5, elt10);
    Element e5 = pg5.getElement(elt1, elt1, elt1, elt1);
    Element e7 = pg7.getElement(e0, e1, e2);
    Element e8 = pg8.getElement(e0, e0, e0);

    Assert.assertEquals(e0, ProductGroup.createTupleElement());
    Assert.assertEquals(e1, ProductGroup.createTupleElement(elt1, elt2, elt5, elt10));
    Assert.assertEquals(e2, ProductGroup.createTupleElement(elt1, elt2, elt, elt5, elt10));
    Assert.assertEquals(e5, ProductGroup.createTupleElement(elt1, elt1, elt1, elt1));
    Assert.assertEquals(e7, ProductGroup.createTupleElement(e0, e1, e2));
    Assert.assertEquals(e8, ProductGroup.createTupleElement(e0, e0, e0));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementElementArrayException1() {
    pg1.getElement((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementElementArrayException2() {
    pg1.getElement(elt1, elt2, null, elt10);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementElementArrayException3() {
    pg1.getElement(elt1, elt2, elt5, elt10, elt10);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementElementArrayException4() {
    pg1.getElement(elt1, elt2, elt10, elt5);
  }

  @Test 
  public final void testCreateElementListOfElement() {
    Element e0 = pg0.getElement(new ArrayList<Element>(Arrays.asList(new Element[]{})));
    Element e1 = pg1.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt2, elt5, elt10)));
    Element e2 = pg2.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt2, elt, elt5, elt10)));
    Element e5 = pg5.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt1, elt1, elt1)));
    Element e7 = pg7.getElement(new ArrayList<Element>(Arrays.asList(e0, e1, e2)));
    Element e8 = pg8.getElement(new ArrayList<Element>(Arrays.asList(e0, e0, e0)));

    Assert.assertEquals(e0, ProductGroup.createTupleElement());
    Assert.assertEquals(e1, ProductGroup.createTupleElement(elt1, elt2, elt5, elt10));
    Assert.assertEquals(e2, ProductGroup.createTupleElement(elt1, elt2, elt, elt5, elt10));
    Assert.assertEquals(e5, ProductGroup.createTupleElement(elt1, elt1, elt1, elt1));
    Assert.assertEquals(e7, ProductGroup.createTupleElement(e0, e1, e2));
    Assert.assertEquals(e8, ProductGroup.createTupleElement(e0, e0, e0));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementListOfElementException1() {
    pg1.getElement((ArrayList<Element>)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementListOfElementException2() {
    pg1.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt2, null, elt10)));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementListOfElementException3() {
    pg1.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt2, elt5, elt10, elt10)));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testCreateElementListOfElementException4() {
    pg1.getElement(new ArrayList<Element>(Arrays.asList(elt1, elt2, elt10, elt5)));
  }

  @Test
  public final void testCreateRandomElement() {
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        do {
          elt = pg.getRandomElement();
          Assert.assertTrue(pg.contains(elt));
        } while (!elt.getElementAt(0).equals(z5.getElement(i)) || !elt.getElementAt(1).equals(z7.getElement(j)));
      }
    }
    for (int i=0; i<10; i++) {
      Assert.assertEquals(pg0.getRandomElement(), pg0.getIdentityElement());
    }
  }

  @Test
  public final void testCreateRandomElementRandom() {
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        do {
          elt = pg.getRandomElement(random);
          Assert.assertTrue(pg.contains(elt));
        } while (!elt.getElementAt(0).equals(z5.getElement(i)) || !elt.getElementAt(1).equals(z7.getElement(j)));
      }
    }
    for (int i=0; i<10; i++) {
      Assert.assertEquals(pg0.getRandomElement(random), pg0.getIdentityElement());
    }
  }

  @Test
  public final void testGetIdentityElement() {
    for (ProductGroup pg: pgs) {
      Assert.assertTrue(pg.isIdentity(pg.getIdentityElement()));
      for (int i=0; i<pg.getArity(); i++) {
        pg.isIdentity(pg.getGroupAt(i).getIdentityElement());
      }
    }
  }

  @Test
  public final void testIsIdentityElement() {
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt;
    int z=0;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        elt = pg.getElement(z5.getElement(i), z7.getElement(j));
        if (elt.isIdentity()) {
          z++;
        }
      }
    }
    Assert.assertEquals(z, 1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testIsIdentityElementException() {
    pg0.isIdentity(null);
  }

  @Test
  public final void testInvert() {
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt, inv;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        elt = pg.getElement(z5.getElement(i), z7.getElement(j));
        inv = pg.invert(elt);
        Assert.assertEquals(elt, inv.invert());
        Assert.assertEquals(elt.getElementAt(0).invert(), inv.getElementAt(0));
        Assert.assertEquals(elt.getElementAt(1).invert(), inv.getElementAt(1));
      }
    }
    Assert.assertEquals(pg0.getRandomElement(random).invert(), pg0.getIdentityElement());
  }

  @Test
  public final void testContainsElement() {
    for (ProductGroup pg: pgs) {
      Tuple rnd = pg.getRandomElement();
      Assert.assertTrue(pg.contains(rnd));
      Assert.assertFalse(pg.contains(elt2));
      Assert.assertTrue(pg.contains(pg.getElement(rnd.getElements()))); 
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testContainsElementException() {
    pg0.contains(null);
  }

  @Test
  public final void testAreEqualElements() {
    for (ProductGroup pg: pgs) {
      Tuple rnd = pg.getRandomElement();
      Assert.assertTrue(pg.areEqual(rnd, rnd));
      Assert.assertFalse(pg.areEqual(rnd, elt2));
      Assert.assertTrue(pg.areEqual(rnd, pg.getElement(rnd.getElements()))); 
    }
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt1, elt2;
    int z=0;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        for (int k=0; k<5; k++) {
          for (int l=1; l<7; l++) {
            elt1 = pg.getElement(z5.getElement(i), z7.getElement(j));
            elt2 = pg.getElement(z5.getElement(k), z7.getElement(l));
            if (pg.areEqual(elt1, elt2)) {
              z++;
            }
          }
        }
      }
    }
    Assert.assertEquals(z, pg.getOrder().intValue());
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testAreEqualElementsException1() {
    pg1.areEqual(elt1, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testAreEqualElementsException2() {
    pg1.areEqual(null, elt1);
  }


  @Test
  public final void testApplyElementElement() {
    ZPlusMod z5 = new ZPlusModClass(BigInteger.valueOf(5));
    ZStarMod z7 = new ZStarMod(BigInteger.valueOf(7));
    ProductGroup pg = new ProductGroup(z5, z7);
    Tuple elt1, elt2, res;
    for (int i=0; i<5; i++) {
      for (int j=1; j<7; j++) {
        for (int k=0; k<5; k++) {
          for (int l=1; l<7; l++) {
            elt1 = pg.getElement(z5.getElement(i), z7.getElement(j));
            elt2 = pg.getElement(z5.getElement(k), z7.getElement(l));
            res = pg.apply(elt1, elt2);
            Assert.assertEquals(res, pg.apply(elt2, elt1));
            Assert.assertEquals(res.getElementAt(0), z5.apply(elt1.getElementAt(0), elt2.getElementAt(0)));
            Assert.assertEquals(res.getElementAt(1), z7.apply(elt1.getElementAt(1), elt2.getElementAt(1)));
            Assert.assertEquals(res.getElementAt(0), z5.getElement(i+k));
            Assert.assertEquals(res.getElementAt(1), z7.getElement(j*l));
          }
        }
      }
    }
    Assert.assertEquals(pg0.apply(pg0.getIdentityElement(), pg0.getIdentityElement()), pg0.getIdentityElement());
  }

  @Test  (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException1() {
    pg0.apply(null, null);
  }

  @Test  (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException2() {
    pg0.apply(pg0.getIdentityElement(), null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException3() {
    pg0.apply(null, pg0.getIdentityElement());
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException4() {
    pg0.apply(pg1.getIdentityElement(), pg0.getIdentityElement());
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementElementException5() {
    pg0.apply(pg0.getIdentityElement(), pg1.getIdentityElement());
  }

  @Test
  public final void testApplyElementArray() {
    for (ProductGroup pg: pgs) {
      Assert.assertEquals(pg.apply(), pg.getIdentityElement());
      Element rnd = pg.getRandomElement();
      Assert.assertEquals(pg.apply(rnd), rnd);        
      Assert.assertEquals(pg.apply(pg.getIdentityElement()), pg.getIdentityElement());
      Assert.assertEquals(pg.apply(pg.getIdentityElement(), pg.getIdentityElement(), pg.getIdentityElement()), pg.getIdentityElement());
      Assert.assertEquals(pg.apply(rnd, pg.getIdentityElement(), pg.getIdentityElement(), pg.getIdentityElement()), rnd);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException1() {
    pg1.apply((Element[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException2() {
    pg1.apply(elt1, elt1, null, elt1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testApplyElementArrayException3() {
    pg1.apply(elt1, elt1, elt2, elt1);
  }

  @Test
  public final void testSelfApplyElementBigInteger() {
    for (ProductGroup pg: pgs) {
      Element rnd = pg.getRandomElement();
      Assert.assertEquals(pg.apply(), pg.selfApply(rnd, BigInteger.valueOf(0)));
      Assert.assertEquals(pg.apply(rnd), pg.selfApply(rnd, BigInteger.valueOf(1)));
      Assert.assertEquals(pg.apply(rnd, rnd), pg.selfApply(rnd, BigInteger.valueOf(2)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd), pg.selfApply(rnd, BigInteger.valueOf(3)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd), pg.selfApply(rnd, BigInteger.valueOf(4)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd, rnd), pg.selfApply(rnd, BigInteger.valueOf(5)));
      Assert.assertEquals(pg.selfApply(rnd, BigInteger.valueOf(-1)), pg.invert(rnd));
      if (!pg.getOrder().equals(Group.INFINITE_ORDER) && !pg.getOrder().equals(Group.UNKNOWN_ORDER)) {
        Assert.assertEquals(pg.getIdentityElement(), pg.selfApply(rnd, pg.getOrder()));
        Assert.assertEquals(rnd, pg.selfApply(rnd, pg.getOrder().add(BigInteger.ONE)));
        Assert.assertEquals(pg.selfApply(rnd, BigInteger.valueOf(-1)), pg.selfApply(rnd, pg.getOrder().subtract(BigInteger.ONE)));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementBigIntegerException1() {
    pg1.selfApply(elt1, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementBigIntegerException2() {
    pg1.selfApply(null, BigInteger.ONE);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementBigIntegerException3() {
    pg1.selfApply(elt2, BigInteger.ONE);
  }


  @Test
  public final void testSelfApplyElementAtomicElement() {
    ZPlus zPlus = ZPlus.getInstance();
    for (ProductGroup pg: pgs) {
      Element rnd = pg.getRandomElement();
      Assert.assertEquals(pg.apply(), pg.selfApply(rnd, zPlus.getElement(0)));
      Assert.assertEquals(pg.apply(rnd), pg.selfApply(rnd, zPlus.getElement(1)));
      Assert.assertEquals(pg.apply(rnd, rnd), pg.selfApply(rnd, zPlus.getElement(2)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd), pg.selfApply(rnd, zPlus.getElement(3)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd), pg.selfApply(rnd, zPlus.getElement(4)));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd, rnd), pg.selfApply(rnd, zPlus.getElement(5)));
      Assert.assertEquals(pg.selfApply(rnd, zPlus.getElement(-1)), pg.invert(rnd));
      if (!pg.getOrder().equals(Group.INFINITE_ORDER) && !pg.getOrder().equals(Group.UNKNOWN_ORDER)) {
        Assert.assertEquals(pg.getIdentityElement(), pg.selfApply(rnd, zPlus.getElement(pg.getOrder())));
        Assert.assertEquals(rnd, pg.selfApply(rnd, zPlus.getElement(pg.getOrder().add(BigInteger.ONE))));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementAtomicElementException1() {
    pg1.selfApply(elt1, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementAtomicElementException2() {
    pg1.selfApply(null, ZPlus.getInstance().getElement(BigInteger.ONE));
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementAtomicElementException3() {
    pg1.selfApply(elt2, ZPlus.getInstance().getElement(BigInteger.ONE));
  }



  @Test
  public final void testSelfApplyElementInt() {
    for (ProductGroup pg: pgs) {
      Element rnd = pg.getRandomElement();
      Assert.assertEquals(pg.apply(), pg.selfApply(rnd, 0));
      Assert.assertEquals(pg.apply(rnd), pg.selfApply(rnd, 1));
      Assert.assertEquals(pg.apply(rnd, rnd), pg.selfApply(rnd, 2));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd), pg.selfApply(rnd, 3));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd), pg.selfApply(rnd, 4));
      Assert.assertEquals(pg.apply(rnd, rnd, rnd, rnd, rnd), pg.selfApply(rnd, 5));
      Assert.assertEquals(pg.selfApply(rnd, -1), pg.invert(rnd));
      if (!pg.getOrder().equals(Group.INFINITE_ORDER) && !pg.getOrder().equals(Group.UNKNOWN_ORDER)) {
        Assert.assertEquals(pg.getIdentityElement(), pg.selfApply(rnd, pg.getOrder().intValue()));
        Assert.assertEquals(rnd, pg.selfApply(rnd, pg.getOrder().add(BigInteger.ONE).intValue()));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementIntException1() {
    pg1.selfApply(null, 1);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementIntException2() {
    pg1.selfApply(elt2, 1);
  }

  @Test
  public final void testSelfApplyElement() {
    for (ProductGroup pg: pgs) {
      Element rnd = pg.getRandomElement();
      Assert.assertEquals(pg.selfApply(rnd), pg.selfApply(rnd, 2));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementException1() {
    pg1.selfApply(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testSelfApplyElementException2() {
    pg1.selfApply(elt2);
  }

  @Test
  public final void testMultiSelfApplyElementArrayBigIntegerArray() {
    for (ProductGroup pg: pgs) {
      Element rnd1 = pg.getRandomElement();
      Element rnd2 = pg.getRandomElement();
      Element rnd3 = pg.getRandomElement();
      BigInteger order = pg.getOrder();
      if (!pg.getOrder().equals(Group.INFINITE_ORDER) && !pg.getOrder().equals(Group.UNKNOWN_ORDER)) {
        Assert.assertEquals(pg.multiSelfApply(new Element[]{rnd1,  rnd2, rnd3}, new BigInteger[]{order, order, order}), pg.getIdentityElement());
        Assert.assertEquals(pg.multiSelfApply(new Element[]{rnd1,  rnd2, rnd3}, new BigInteger[]{BigInteger.ONE, order, order}), rnd1);
        Assert.assertEquals(pg.multiSelfApply(new Element[]{rnd1,  rnd2, rnd3}, new BigInteger[]{order, BigInteger.ONE, order}), rnd2);
        Assert.assertEquals(pg.multiSelfApply(new Element[]{rnd1,  rnd2, rnd3}, new BigInteger[]{order, order, BigInteger.ONE}), rnd3);
        Assert.assertEquals(pg.multiSelfApply(new Element[]{rnd1,  rnd2, rnd3}, new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ONE}), pg.apply(rnd1, rnd2, rnd3));
        Assert.assertEquals(pg.multiSelfApply(new Element[]{}, new BigInteger[]{}), pg.getIdentityElement());
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    pg1.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    pg1.multiSelfApply(null, new BigInteger[]{});
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    pg1.multiSelfApply(new Element[]{}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    pg1.multiSelfApply(new Element[]{}, new BigInteger[]{BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    pg1.multiSelfApply(new Element[]{null}, new BigInteger[]{BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public final void testMultiSelfApplyElementArrayBigIntegerArrayException6() {
    pg1.multiSelfApply(new Element[]{elt1}, new BigInteger[]{null});
  }

  // Does this test belong to this test class? Reto? Actually, this is not a real test!
  @Test
  public void testCreateTupleElement() {
    String one="1";
    String two="2";
    String three="3";
    ExternalDataMapper mapper=new ExternalDataMapperClass();
    Tuple element1=mapper.getEncodedElement(one,two,three);
    Tuple element2=mapper.getEncodedElement(one,two,three);

    Tuple element3=ProductGroup.createTupleElement(element1,element2);
    ConcatScheme concat=new ConcatSchemeClass(ConcatParameter.PipeBrackets,new CharsetXRadixYMapperClass());
    AtomicElement concatElement=concat.concat(element3);
    //System.out.println(
    new String(concatElement.getValue().toByteArray());
    //);
  }

}
