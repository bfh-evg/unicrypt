package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.cyclicgroup.classes.BooleanGroup;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.classes.MultiplicativeElement;
import ch.bfh.unicrypt.math.group.interfaces.GStarMod;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;

@SuppressWarnings("static-method")
public class GStarModClassTest {

  // testing all subgroups for permissible value n <= 18 plus n=27
  private static final GStarMod g_2_1 = new GStarMod(BigInteger.valueOf(2), false); // order=1
  private static final GStarMod g_3_1 = new GStarMod(BigInteger.valueOf(3), false); // order=2
  private static final GStarMod g_3_2 = new GStarMod(BigInteger.valueOf(3), false, BigInteger.valueOf(2));
  private static final GStarMod g_4_1 = new GStarMod(BigInteger.valueOf(2), true); // order=2
  private static final GStarMod g_4_2 = new GStarMod(BigInteger.valueOf(2), true, BigInteger.valueOf(2));
  private static final GStarMod g_5_1 = new GStarMod(BigInteger.valueOf(5), false); // order=4
  private static final GStarMod g_5_2 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2));
  private static final GStarMod g_5_4 = new GStarMod(BigInteger.valueOf(5), false, BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_6_1 = new GStarMod(BigInteger.valueOf(3), true); // order=2
  private static final GStarMod g_6_2 = new GStarMod(BigInteger.valueOf(3), true, BigInteger.valueOf(2));
  private static final GStarMod g_7_1 = new GStarMod(BigInteger.valueOf(7), false); // order=6
  private static final GStarMod g_7_2 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(2)); 
  private static final GStarMod g_7_3 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(3)); 
  private static final GStarMod g_7_6 = new GStarMod(BigInteger.valueOf(7), false, BigInteger.valueOf(2), BigInteger.valueOf(3)); 
  private static final GStarMod g_9_1 = new GStarMod(BigInteger.valueOf(3), 2); // order=6
  private static final GStarMod g_9_2 = new GStarMod(BigInteger.valueOf(3), 2, BigInteger.valueOf(2));
  private static final GStarMod g_9_3 = new GStarMod(BigInteger.valueOf(3), 2, BigInteger.valueOf(3));
  private static final GStarMod g_9_6 = new GStarMod(BigInteger.valueOf(3), 2, BigInteger.valueOf(2), BigInteger.valueOf(3));
  private static final GStarMod g_10_1 = new GStarMod(BigInteger.valueOf(5), true); // order=4
  private static final GStarMod g_10_2 = new GStarMod(BigInteger.valueOf(5), true, BigInteger.valueOf(2));
  private static final GStarMod g_10_4 = new GStarMod(BigInteger.valueOf(5), true, BigInteger.valueOf(2), BigInteger.valueOf(2)); // order=4
  private static final GStarMod g_11_1 = new GStarMod(BigInteger.valueOf(11), false); // order=10
  private static final GStarMod g_11_2 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(2)); 
  private static final GStarMod g_11_5 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(5)); 
  private static final GStarMod g_11_10 = new GStarMod(BigInteger.valueOf(11), false, BigInteger.valueOf(2), BigInteger.valueOf(5)); // order=4
  private static final GStarMod g_13_1 = new GStarMod(BigInteger.valueOf(13), false); // order=12
  private static final GStarMod g_13_2 = new GStarMod(BigInteger.valueOf(13), false, BigInteger.valueOf(2)); 
  private static final GStarMod g_13_3 = new GStarMod(BigInteger.valueOf(13), false, BigInteger.valueOf(3)); 
  private static final GStarMod g_13_4 = new GStarMod(BigInteger.valueOf(13), false, BigInteger.valueOf(2), BigInteger.valueOf(2)); 
  private static final GStarMod g_13_6 = new GStarMod(BigInteger.valueOf(13), false, BigInteger.valueOf(2), BigInteger.valueOf(3)); 
  private static final GStarMod g_13_12 = new GStarMod(BigInteger.valueOf(13), false, BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(3));
  private static final GStarMod g_14_1 = new GStarMod(BigInteger.valueOf(7), true); // order=6
  private static final GStarMod g_14_2 = new GStarMod(BigInteger.valueOf(7), true, BigInteger.valueOf(2));
  private static final GStarMod g_14_3 = new GStarMod(BigInteger.valueOf(7), true, BigInteger.valueOf(3));
  private static final GStarMod g_14_6 = new GStarMod(BigInteger.valueOf(7), true, BigInteger.valueOf(2), BigInteger.valueOf(3));
  private static final GStarMod g_17_1 = new GStarMod(BigInteger.valueOf(17), false); // order=16
  private static final GStarMod g_17_2 = new GStarMod(BigInteger.valueOf(17), false, BigInteger.valueOf(2));
  private static final GStarMod g_17_4 = new GStarMod(BigInteger.valueOf(17), false, BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_17_8 = new GStarMod(BigInteger.valueOf(17), false, BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_17_16 = new GStarMod(BigInteger.valueOf(17), false, BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_18_1 = new GStarMod(BigInteger.valueOf(3), 2, true); // order=6
  private static final GStarMod g_18_2 = new GStarMod(BigInteger.valueOf(3), 2, true, BigInteger.valueOf(2));
  private static final GStarMod g_18_3 = new GStarMod(BigInteger.valueOf(3), 2, true, BigInteger.valueOf(3));
  private static final GStarMod g_18_6 = new GStarMod(BigInteger.valueOf(3), 2, true, BigInteger.valueOf(2), BigInteger.valueOf(3));
  private static final GStarMod g_25_1 = new GStarMod(BigInteger.valueOf(5), 2, false); // order=20
  private static final GStarMod g_25_2 = new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2));
  private static final GStarMod g_25_4 = new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2), BigInteger.valueOf(2));
  private static final GStarMod g_25_5 = new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(5));
  private static final GStarMod g_25_10 = new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2), BigInteger.valueOf(5));
  private static final GStarMod g_25_20 = new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(5));
  private static final GStarMod g_27_1 = new GStarMod(BigInteger.valueOf(3), 3);
  private static final GStarMod g_27_2 = new GStarMod(BigInteger.valueOf(3), 3, BigInteger.valueOf(2));
  private static final GStarMod g_27_3 = new GStarMod(BigInteger.valueOf(3), 3, BigInteger.valueOf(3));
  private static final GStarMod g_27_6 = new GStarMod(BigInteger.valueOf(3), 3, BigInteger.valueOf(2), BigInteger.valueOf(3));
  private static final GStarMod g_27_9 = new GStarMod(BigInteger.valueOf(3), 3, BigInteger.valueOf(3), BigInteger.valueOf(3));
  private static final GStarMod g_27_18 = new GStarMod(BigInteger.valueOf(3), 3, BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(3));

  private static final GStarMod[] groups = new GStarMod[] {
    g_2_1,
    g_3_1, g_3_2,
    g_4_1, g_4_2,
    g_5_1, g_5_2, g_5_4,
    g_6_1, g_6_2,
    g_7_1, g_7_2, g_7_3, g_7_6,
    g_9_1, g_9_2, g_9_3, g_9_6,
    g_10_1, g_10_2, g_10_4,
    g_11_1, g_11_2, g_11_5, g_11_10,
    g_13_1, g_13_2, g_13_3, g_13_4, g_13_6, g_13_12,
    g_14_1, g_14_2, g_14_3, g_14_6,
    g_17_1, g_17_2, g_17_4, g_17_8, g_17_16,
    g_18_1, g_18_2, g_18_3, g_18_6,
    g_25_1, g_25_2, g_25_4, g_25_5, g_25_10, g_25_20,
    g_27_1, g_27_2, g_27_3, g_27_6, g_27_9, g_27_18};

  private static final int[] modulos = new int[] {
    2,
    3, 3,
    4, 4,
    5, 5, 5,
    6, 6,
    7, 7, 7, 7,
    9, 9, 9, 9,
    10, 10, 10,
    11, 11, 11, 11,
    13, 13, 13, 13, 13, 13,
    14, 14, 14, 14,
    17, 17, 17, 17, 17, 
    18, 18, 18, 18,
    25, 25, 25, 25, 25, 25, 
    27, 27, 27, 27, 27, 27};

  private static final int[] orders = new int[] {
    1,
    1, 2,
    1, 2,
    1, 2, 4,
    1, 2,
    1, 2, 3, 6,
    1, 2, 3, 6,
    1, 2, 4,
    1, 2, 5, 10,
    1, 2, 3, 4, 6, 12,
    1, 2, 3, 6,
    1, 2, 4, 8, 16,
    1, 2, 3, 6,
    1, 2, 4, 5, 10, 20,
    1, 2, 3, 6, 9, 18};

  private static final int[] groupOrders = new int[] {
    1,
    2, 2,
    2, 2,
    4, 4, 4,
    2, 2,
    6, 6, 6, 6,
    6, 6, 6, 6,
    4, 4, 4,
    10, 10, 10, 10,
    12, 12, 12, 12, 12, 12,
    6, 6, 6, 6,
    16, 16, 16, 16, 16, 
    6, 6, 6, 6,
    20, 20, 20, 20, 20, 20, 
    18, 18, 18, 18, 18, 18};

  @Test 
  public void testGStarModGeneral() {
    int k=0;
    for (GStarMod group: groups) {
      //       System.out.println(group);
      ArrayList<MultiplicativeElement> elts1 = new ArrayList<MultiplicativeElement>();
      for (int i=1; i<group.getModulus().intValue(); i++) { // generate all elements by trial
        if (group.contains(BigInteger.valueOf(i))) {
          elts1.add(group.getElement(i));
          //           System.out.println(i);
        }
      }
      MultiplicativeElement generator = group.getDefaultGenerator();
      ArrayList<MultiplicativeElement> elts2 = new ArrayList<MultiplicativeElement>(); // generate all elements using generator
      for (int i=1; i<=group.getOrder().intValue(); i++) {
        elts2.add(generator.selfApply(i));
      }
      Assert.assertEquals(group.getOrder().intValue(), elts1.size());
      Assert.assertEquals(group.getOrder().intValue(), orders[k]);
      Assert.assertEquals(group.getOrder(), group.getMinOrder());
      Assert.assertEquals(group.getSuperGroupOrder().intValue(), groupOrders[k]);
      Assert.assertEquals(group.getModulus().intValue(), modulos[k]);
      Assert.assertTrue(elts1.containsAll(elts2));
      Assert.assertTrue(elts2.containsAll(elts1));
      for (MultiplicativeElement elt: elts1) {
        if (group.isGenerator(elt)) {
          HashSet<MultiplicativeElement> set = new HashSet<MultiplicativeElement>();
          for (int i=0; i<group.getOrder().intValue(); i++) {
            set.add(elt.selfApply(i));          
          }
          Assert.assertEquals(group.getOrder().intValue(), set.size());
        }
      }
      Assert.assertTrue(group.isIdentity(group.getIdentityElement()));
      k++;
    }
  }

  @Test 
  public void testGStarModOrderONE() {
    for (GStarMod group: new GStarMod[] {g_2_1, g_3_1, g_4_1, g_5_1, g_6_1, g_7_1, g_9_1, g_10_1, g_11_1, g_13_1, g_14_1, g_17_1, g_18_1, g_27_1}) { 
      MultiplicativeElement elt1 = group.getElement(1);
      MultiplicativeElement elt2 = group.getElement(1);
      Assert.assertTrue(elt1.equals(elt2));
      Assert.assertTrue(elt1.equals(elt1.apply(elt2)));
      Assert.assertTrue(elt1.equals(elt2.invert()));
      Assert.assertTrue(elt1.equals(elt2.selfApply()));
      Assert.assertTrue(elt1.equals(elt2.selfApply(5)));
      Assert.assertTrue(group.areEqual(elt1, elt2));
      Assert.assertFalse(group.contains(BigInteger.ZERO));
      Assert.assertTrue(group.contains(BigInteger.ONE));
      Assert.assertFalse(group.contains(BigInteger.valueOf(0)));
      Assert.assertTrue(group.isIdentity(elt1));
      Assert.assertEquals(elt1.getValue(), BigInteger.ONE);
      Assert.assertEquals(elt2.getValue(), BigInteger.ONE);
      for (int i=1; i<=100; i++) {
        Assert.assertEquals(group.getRandomElement().getValue(), BigInteger.ONE);
      }
      Assert.assertEquals(group.getOrder(),BigInteger.ONE);
      Assert.assertEquals(group.getMinOrder(),BigInteger.ONE);
      Assert.assertEquals(group.getOrderGroup(), new ZPlusModClass(BigInteger.ONE));
      Assert.assertEquals(group.getMinOrderGroup(), new ZPlusModClass(BigInteger.ONE));
    }
  }

  @Test 
  public void testGStarModConstructor() {
    GStarMod g1_1 = new GStarMod(BigInteger.valueOf(19), false);
    GStarMod g1_2 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{});
    GStarMod g1_3 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{}, new int[]{});
    GStarMod g1_4 = new GStarMod(BigInteger.valueOf(19), 1);
    GStarMod g1_5 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{});
    GStarMod g1_6 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{}, new int[]{});
    GStarMod g1_7 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{});
    GStarMod g1_8 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{}, new int[]{});
    Assert.assertEquals(g1_1, g1_2);
    Assert.assertEquals(g1_1, g1_3);
    Assert.assertEquals(g1_1, g1_4);
    Assert.assertEquals(g1_1, g1_5);
    Assert.assertEquals(g1_1, g1_6);
    Assert.assertEquals(g1_1, g1_7);
    Assert.assertEquals(g1_1, g1_8);
    Assert.assertEquals(g1_1.getModulus().intValue(), 19);
    Assert.assertEquals(g1_1.getOrder().intValue(), 1);
    Assert.assertEquals(g1_1.getSuperGroupOrder().intValue(), 18);
    GStarMod g2_1 = new GStarMod(BigInteger.valueOf(19), false, BigInteger.valueOf(3));
    GStarMod g2_2 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3)});
    GStarMod g2_3 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{1});
    GStarMod g2_4 = new GStarMod(BigInteger.valueOf(19), 1, BigInteger.valueOf(3));
    GStarMod g2_5 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3)});
    GStarMod g2_6 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{1});
    GStarMod g2_7 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3)});
    GStarMod g2_8 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{1});
    Assert.assertEquals(g2_1, g2_2);
    Assert.assertEquals(g2_1, g2_3);
    Assert.assertEquals(g2_1, g2_4);
    Assert.assertEquals(g2_1, g2_5);
    Assert.assertEquals(g2_1, g2_6);
    Assert.assertEquals(g2_1, g2_7);
    Assert.assertEquals(g2_1, g2_8);
    Assert.assertEquals(g2_1.getModulus().intValue(), 19);
    Assert.assertEquals(g2_1.getOrder().intValue(), 3);
    Assert.assertEquals(g2_1.getSuperGroupOrder().intValue(), 18);
    GStarMod g3_1 = new GStarMod(BigInteger.valueOf(19), false, BigInteger.valueOf(3), BigInteger.valueOf(3));
    GStarMod g3_2 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)});
    GStarMod g3_9 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)}, new int[]{1, 1});
    GStarMod g3_3 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{2});
    GStarMod g3_4 = new GStarMod(BigInteger.valueOf(19), 1, BigInteger.valueOf(3), BigInteger.valueOf(3));
    GStarMod g3_5 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)});
    GStarMod g3_6 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{2});
    GStarMod g3_10 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)}, new int[]{1, 1});
    GStarMod g3_7 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)});
    GStarMod g3_8 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3)}, new int[]{2});
    GStarMod g3_11 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3)}, new int[]{1, 1});
    Assert.assertEquals(g3_1, g3_2);
    Assert.assertEquals(g3_1, g3_3);
    Assert.assertEquals(g3_1, g3_4);
    Assert.assertEquals(g3_1, g3_5);
    Assert.assertEquals(g3_1, g3_6);
    Assert.assertEquals(g3_1, g3_7);
    Assert.assertEquals(g3_1, g3_8);
    Assert.assertEquals(g3_1, g3_9);
    Assert.assertEquals(g3_1, g3_10);
    Assert.assertEquals(g3_1, g3_11);
    Assert.assertEquals(g3_1.getModulus().intValue(), 19);
    Assert.assertEquals(g3_1.getOrder().intValue(), 9);
    Assert.assertEquals(g3_1.getSuperGroupOrder().intValue(), 18);
    GStarMod g4_1 = new GStarMod(BigInteger.valueOf(19), false, BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2));
    GStarMod g4_2 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(3)});
    GStarMod g4_9 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
    GStarMod g4_3 = new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{2, 1});
    GStarMod g4_4 = new GStarMod(BigInteger.valueOf(19), 1, BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2));
    GStarMod g4_5 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)});
    GStarMod g4_6 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{2, 1});
    GStarMod g4_10 = new GStarMod(BigInteger.valueOf(19), 1, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
    GStarMod g4_7 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)});
    GStarMod g4_8 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{2, 1});
    GStarMod g4_11 = new GStarMod(BigInteger.valueOf(19), 1, false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
    Assert.assertEquals(g4_1, g4_2);
    Assert.assertEquals(g4_1, g4_3);
    Assert.assertEquals(g4_1, g4_4);
    Assert.assertEquals(g4_1, g4_5);
    Assert.assertEquals(g4_1, g4_6);
    Assert.assertEquals(g4_1, g4_7);
    Assert.assertEquals(g4_1, g4_8);
    Assert.assertEquals(g4_1, g4_9);
    Assert.assertEquals(g4_1, g4_10);
    Assert.assertEquals(g4_1, g4_11);
    Assert.assertEquals(g4_1.getModulus().intValue(), 19);
    Assert.assertEquals(g4_1.getOrder().intValue(), 18);
    Assert.assertEquals(g4_1.getSuperGroupOrder().intValue(), 18);
    GStarMod g5_1 = new GStarMod(BigInteger.valueOf(19), true, BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2));
    GStarMod g5_2 = new GStarMod(BigInteger.valueOf(19), true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(3)});
    GStarMod g5_9 = new GStarMod(BigInteger.valueOf(19), true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
    GStarMod g5_3 = new GStarMod(BigInteger.valueOf(19), true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{2, 1});
    GStarMod g5_7 = new GStarMod(BigInteger.valueOf(19), 1, true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)});
    GStarMod g5_8 = new GStarMod(BigInteger.valueOf(19), 1, true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{2, 1});
    GStarMod g5_11 = new GStarMod(BigInteger.valueOf(19), 1, true, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
    Assert.assertEquals(g5_1, g5_2);
    Assert.assertEquals(g5_1, g5_3);
    Assert.assertEquals(g5_1, g5_7);
    Assert.assertEquals(g5_1, g5_8);
    Assert.assertEquals(g5_1, g5_9);
    Assert.assertEquals(g5_1, g5_11);
    Assert.assertEquals(g5_1.getModulus().intValue(), 38);
    Assert.assertEquals(g5_1.getOrder().intValue(), 18);
    Assert.assertEquals(g5_1.getSuperGroupOrder().intValue(), 18);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException1() {
    new GStarMod(BigInteger.valueOf(8), false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException2() {
    new GStarMod(null, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException3() {
    new GStarMod(BigInteger.valueOf(8), 3);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException4() {
    new GStarMod(null, 3);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException5() {
    new GStarMod(BigInteger.valueOf(8), 3, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException6() {
    new GStarMod(null, 3, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException7() {
    new GStarMod(BigInteger.valueOf(19), false, (BigInteger[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException8() {
    new GStarMod(BigInteger.valueOf(19), 3, (BigInteger[]) null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException9() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), null, BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException10() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), null, BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException11() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(4), BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException12() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(4), BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException13() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException14() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(2)});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException15() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException16() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException17() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException18() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException19() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 2});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException20() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 2});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException21() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 0});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException22() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 0});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException23() {
    new GStarMod(BigInteger.valueOf(19), false, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException24() {
    new GStarMod(BigInteger.valueOf(19), 3, new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(2)}, new int[]{1, 1, 1});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException25() {
    new GStarMod(BigInteger.valueOf(2), 2, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException26() {
    new GStarMod(BigInteger.valueOf(2), 0, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException27() {
    new GStarMod(BigInteger.valueOf(3), 0, false);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException28() {
    new GStarMod(BigInteger.valueOf(2), 2);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException29() {
    new GStarMod(BigInteger.valueOf(2), 0);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGStarModConstructorException30() {
    new GStarMod(BigInteger.valueOf(3), 0);
  }

  private static GStarMod gStarMod = g_25_10;
  private static Random random = new Random();
  private static MultiplicativeElement element1, element2;
  private static final MultiplicativeElement MINUS_ONE = gStarMod.getElement(-1);
  private static final MultiplicativeElement e1 = gStarMod.getElement(1);
  private static final MultiplicativeElement e4 = gStarMod.getElement(4);
  private static final MultiplicativeElement e6 = gStarMod.getElement(6);
  private static final MultiplicativeElement e9 = gStarMod.getElement(9);
  private static final MultiplicativeElement e11 = gStarMod.getElement(11);
  private static final MultiplicativeElement e14 = gStarMod.getElement(14);
  private static final MultiplicativeElement e16 = gStarMod.getElement(16);
  private static final MultiplicativeElement e19 = gStarMod.getElement(19);
  private static final MultiplicativeElement e21 = gStarMod.getElement(21);
  private static final MultiplicativeElement e24 = gStarMod.getElement(24);
  private static final MultiplicativeElement OTHER_ELEMENT = new GStarSave(BigInteger.valueOf(11)).getIdentityElement();
  private static final MultiplicativeElement[] elts= new MultiplicativeElement[] {e1, e4, e6, e9, e11, e14, e16, e19, e21, e24};


  @Test
  public void testGetDefaultGenerator() {
    for (GStarMod group: groups) {
      Assert.assertTrue(group.contains(group.getDefaultGenerator()));
      Assert.assertTrue(group.isGenerator(group.getDefaultGenerator()));
      HashSet<Element> set = new HashSet<Element>();
      for (int i=0; i<group.getOrder().intValue(); i++) {
        set.add(group.getDefaultGenerator().selfApply(i));      
      }
      Assert.assertEquals(set.size(), group.getOrder().intValue());
    }
  }

  @Test
  public void testCreateRandomGenerator() {
    for (GStarMod group: groups) {
      for (int j=1; j<50; j++) {
        MultiplicativeElement gen = group.getRandomGenerator();
        Assert.assertTrue(group.contains(gen));
        Assert.assertTrue(group.isGenerator(gen));
        HashSet<Element> set = new HashSet<Element>();
        for (int i=0; i<group.getOrder().intValue(); i++) {
          set.add(group.getDefaultGenerator().selfApply(i));      
        }
        Assert.assertEquals(set.size(), group.getOrder().intValue());
      }
    }
  }

  @Test
  public void testCreateRandomGeneratorRandom() {
    for (GStarMod group: groups) {
      for (int j=1; j<50; j++) {
        MultiplicativeElement gen = group.getRandomGenerator(random);
        Assert.assertTrue(group.contains(gen));
        Assert.assertTrue(group.isGenerator(gen));
        HashSet<Element> set = new HashSet<Element>();
        for (int i=0; i<group.getOrder().intValue(); i++) {
          set.add(group.getDefaultGenerator().selfApply(i));      
        }
        Assert.assertEquals(set.size(), group.getOrder().intValue());
      }
    }
  }

  @Test
  public void testIsGeneratorElement() {
    // already tested in testGStarModGeneral()
  }


  @Test
  public void testGetGroupOrder() {
    Assert.assertEquals(gStarMod.getSuperGroupOrder(), BigInteger.valueOf(20));
  }

  @Test
  public void testGetOrderQuotient() {
    Assert.assertEquals(gStarMod.getOrderQuotient(), BigInteger.valueOf(2));
  }

  @Test
  public void testIsSubgroup() {
    Assert.assertTrue(gStarMod.isSubgroup());
  }

  @Test
  public void testGetOrderPrimeFactors() {
    BigInteger[] primes = new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(5)};
    Assert.assertTrue(Arrays.equals(gStarMod.getOrderPrimeFactors(), primes));
  }

  @Test
  public void testGetModulus() {
    Assert.assertEquals(gStarMod.getModulus(), BigInteger.valueOf(25));
  }

  @Test
  public void testGetPrimeFactors() {
    BigInteger[] primes = new BigInteger[]{BigInteger.valueOf(5)};
    Assert.assertTrue(Arrays.equals(gStarMod.getPrimeFactors(), primes));
  }

  @Test
  public void testCreateRandomElement() {
    MultiplicativeElement element;
    for (MultiplicativeElement elt: elts) {
      do {
        element = gStarMod.getRandomElement();
        Assert.assertTrue(element != null);
        Assert.assertTrue(element.getValue() != null);
      } while (!element.equals(elt));
    }
  }

  @Test
  public void testCreateRandomElementRandom() {
    MultiplicativeElement element;
    for (MultiplicativeElement elt: elts) {
      do {
        element = gStarMod.getRandomElement(random);
        Assert.assertTrue(element != null);
        Assert.assertTrue(element.getValue() != null);
      } while (!element.equals(elt));
    }
  }

  @Test
  public void testGetIdentityElement() {
    element1 = gStarMod.getIdentityElement();
    element2 = gStarMod.getIdentityElement();
    Assert.assertEquals(element1.getValue(), BigInteger.ONE);
    Assert.assertEquals(element1, element2);
    Assert.assertSame(element1, element2);
  }

  @Test
  public void testApplyElementElement() {
    for (MultiplicativeElement elt1: elts) {
      for (MultiplicativeElement elt2: elts) {
        Assert.assertEquals(gStarMod.apply(elt1, elt2).getValue(), (elt1.getValue().multiply(elt2.getValue()).mod(gStarMod.getModulus())));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException1() {
    gStarMod.apply((Element)null, (Element)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException2() {
    gStarMod.apply(e11, (Element)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException3() {
    gStarMod.apply((Element)null, e11);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException4() {
    gStarMod.apply(OTHER_ELEMENT, e11);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementElementException5() {
    gStarMod.apply(e11, OTHER_ELEMENT);
  }

  @Test
  public void testApplyElementArray() {
    Assert.assertEquals(gStarMod.apply(), e1);
    Assert.assertEquals(gStarMod.apply(e1), e1);
    Assert.assertEquals(gStarMod.apply(MINUS_ONE), e24);
    Assert.assertEquals(gStarMod.apply(e1, e1, e1), e1);
    Assert.assertEquals(gStarMod.apply(MINUS_ONE, e1, e1), e24);
    Assert.assertEquals(gStarMod.apply(MINUS_ONE, e4, MINUS_ONE), e4);
    Assert.assertEquals(gStarMod.apply(e1, e4, e6, e24), e1);
    Assert.assertEquals(gStarMod.apply(new Element[] {}), e1);
    Assert.assertEquals(gStarMod.apply(new Element[] {e1, e4, e6, e24}), e1);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException1() {
    gStarMod.apply((Element[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException2() {
    gStarMod.apply(new Element[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException3() {
    gStarMod.apply(new Element[] {e1, e1, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testApplyElementArrayException4() {
    gStarMod.apply(new Element[] {e1, e1, OTHER_ELEMENT});
  }

  @Test
  public void testSelfApplyElementBigInteger() {
    Assert.assertEquals(gStarMod.selfApply(e1, BigInteger.ZERO), e1);
    Assert.assertEquals(gStarMod.selfApply(e9, BigInteger.ZERO), e1);
    Assert.assertEquals(gStarMod.selfApply(e1, BigInteger.TEN), e1);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, BigInteger.ONE), e24);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, BigInteger.valueOf(10)), e1);
    Assert.assertEquals(gStarMod.selfApply(e6, BigInteger.valueOf(2)), e11);
    Assert.assertEquals(gStarMod.selfApply(e4, BigInteger.valueOf(-8)), e16);
    Assert.assertEquals(gStarMod.selfApply(e4, BigInteger.valueOf(10)), e1);
    BigInteger amount;
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        amount = BigInteger.valueOf(j);
        Assert.assertEquals(gStarMod.selfApply(elt, amount).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
    for (GStarMod group: groups) {
      Element rnd = group.getRandomElement();
      Assert.assertEquals(group.apply(), group.selfApply(rnd, BigInteger.valueOf(0)));
      Assert.assertEquals(group.apply(rnd), group.selfApply(rnd, BigInteger.valueOf(1)));
      Assert.assertEquals(group.apply(rnd, rnd), group.selfApply(rnd, BigInteger.valueOf(2)));
      Assert.assertEquals(group.apply(rnd, rnd, rnd), group.selfApply(rnd, BigInteger.valueOf(3)));
      Assert.assertEquals(group.apply(rnd, rnd, rnd, rnd), group.selfApply(rnd, BigInteger.valueOf(4)));
      Assert.assertEquals(group.getIdentityElement(), group.selfApply(rnd, group.getOrder()));
      Assert.assertEquals(rnd, group.selfApply(rnd, group.getOrder().add(BigInteger.ONE)));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException1() {
    gStarMod.selfApply(e1, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException2() {
    gStarMod.selfApply(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException3() {
    gStarMod.selfApply(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementBigIntegerException4() {
    gStarMod.selfApply(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testSelfApplyElementAtomicElement() {
    AtomicElement amount;
    ZPlus zPlus = ZPlus.getInstance();
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        amount = zPlus.getElement(j);
        Assert.assertEquals(gStarMod.selfApply(elt, amount).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
    for (GStarMod group: groups) {
      Element rnd = group.getRandomElement();
      Assert.assertEquals(group.apply(), group.selfApply(rnd, zPlus.getElement(0)));
      Assert.assertEquals(group.apply(rnd), group.selfApply(rnd, zPlus.getElement(1)));
      Assert.assertEquals(group.apply(rnd, rnd), group.selfApply(rnd, zPlus.getElement(2)));
      Assert.assertEquals(group.apply(rnd, rnd, rnd), group.selfApply(rnd, zPlus.getElement(3)));
      Assert.assertEquals(group.apply(rnd, rnd, rnd, rnd), group.selfApply(rnd, zPlus.getElement(4)));
      Assert.assertEquals(group.getIdentityElement(), group.selfApply(rnd, zPlus.getElement(group.getOrder())));
      Assert.assertEquals(rnd, group.selfApply(rnd, zPlus.getElement(group.getOrder().add(BigInteger.ONE))));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException1() {
    gStarMod.selfApply(e1, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException2() {
    gStarMod.selfApply(null, e9);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyAtomicElementException3() {
    gStarMod.selfApply(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementAtomicElementException4() {
    gStarMod.selfApply(OTHER_ELEMENT, e9);
  }

  @Test
  public void testSelfApplyElementInt() {
    Assert.assertEquals(gStarMod.selfApply(e1, 0), e1);
    Assert.assertEquals(gStarMod.selfApply(e9, 0), e1);
    Assert.assertEquals(gStarMod.selfApply(e1, 10), e1);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, 1), e24);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, 10), e1);
    Assert.assertEquals(gStarMod.selfApply(e6, 2), e11);
    Assert.assertEquals(gStarMod.selfApply(e4, -8), e16);
    Assert.assertEquals(gStarMod.selfApply(e4, 10), e1);
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        Assert.assertEquals(gStarMod.selfApply(elt, j).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
    for (GStarMod group: groups) {
      Element rnd = group.getRandomElement();
      Assert.assertEquals(group.apply(), group.selfApply(rnd, 0));
      Assert.assertEquals(group.apply(rnd), group.selfApply(rnd, 1));
      Assert.assertEquals(group.apply(rnd, rnd), group.selfApply(rnd, 2));
      Assert.assertEquals(group.apply(rnd, rnd, rnd), group.selfApply(rnd, 3));
      Assert.assertEquals(group.apply(rnd, rnd, rnd, rnd), group.selfApply(rnd, 4));
      Assert.assertEquals(group.getIdentityElement(), group.selfApply(rnd, group.getOrder().intValue()));
      Assert.assertEquals(rnd, group.selfApply(rnd, group.getOrder().add(BigInteger.ONE).intValue()));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException1() {
    gStarMod.selfApply(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementIntException2() {
    gStarMod.selfApply(OTHER_ELEMENT, 10);
  }

  @Test
  public void testSelfApplyElement() {
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE), e1);
    Assert.assertEquals(gStarMod.selfApply(e1), e1);
    Assert.assertEquals(gStarMod.selfApply(e4), e16);
    Assert.assertEquals(gStarMod.selfApply(e11), e21);
    Assert.assertEquals(gStarMod.selfApply(e24), e1);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException1() {
    gStarMod.selfApply(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testSelfApplyElementException2() {
    gStarMod.selfApply(OTHER_ELEMENT);
  }

  @Test
  public void testMultiSelfApplyElementArrayBigIntegerArray() {
    Assert.assertEquals(gStarMod.multiSelfApply(new Element[] {}, new BigInteger[] {}), e1);
    Assert.assertEquals(gStarMod.multiSelfApply(new Element[] {e1}, new BigInteger[] {BigInteger.ZERO}), e1);
    Assert.assertEquals(gStarMod.multiSelfApply(new Element[] {e1}, new BigInteger[] {BigInteger.TEN}), e1);
    Assert.assertEquals(gStarMod.multiSelfApply(new Element[] {e1, e4}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(2)}), e16);
    Assert.assertEquals(gStarMod.multiSelfApply(new Element[] {e1, e4, e6}, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(2), BigInteger.ONE}), e21);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException1() {
    gStarMod.multiSelfApply(new Element[] {e1, e1}, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException2() {
    gStarMod.multiSelfApply(null, new BigInteger[] {BigInteger.TEN, BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException3() {
    gStarMod.multiSelfApply(null, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException4() {
    gStarMod.multiSelfApply(new Element[] {e1, e1, e4}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiSelfApplyElementArrayBigIntegerArrayException5() {
    gStarMod.multiSelfApply(new Element[] {OTHER_ELEMENT, e1}, new BigInteger[] {BigInteger.valueOf(8), BigInteger.ONE});
  }

  @Test
  public void testInvert() {
    for (MultiplicativeElement elt: elts) {
      Assert.assertEquals(elt.apply(elt.invert()), e1);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException1() {
    gStarMod.invert(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testInvertException2() {
    gStarMod.invert(OTHER_ELEMENT);
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(gStarMod.getOrder(), BigInteger.valueOf(10));
    // already tested in testGStarModGeneral()
  }

  @Test
  public void testGetMinOrder() {
    Assert.assertEquals(gStarMod.getMinOrder(), BigInteger.valueOf(10));  
    // already tested in testGStarModGeneral()
  }

  @Test
  public void testHasSameOrder() {
    Assert.assertTrue(gStarMod.hasSameOrder(gStarMod));
    Assert.assertTrue(gStarMod.hasSameOrder(new ZPlusModClass(BigInteger.valueOf(10))));
    Assert.assertTrue(gStarMod.hasSameOrder(g_11_10));
    Assert.assertTrue(gStarMod.hasSameOrder(new GStarMod(BigInteger.valueOf(11), true, BigInteger.valueOf(2), BigInteger.valueOf(5))));
    Assert.assertFalse(gStarMod.hasSameOrder(new GStarMod(BigInteger.valueOf(13), true)));
    Assert.assertFalse(gStarMod.hasSameOrder(SingletonGroup.getInstance()));
    Assert.assertFalse(gStarMod.hasSameOrder(BooleanGroup.getInstance()));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasSameOrderException() {
    gStarMod.hasSameOrder(null);  
  }

  @Test 
  public void testGetOrderGroup() {
    Assert.assertEquals(gStarMod.getOrderGroup(), new ZPlusModClass(BigInteger.valueOf(10)));
  }

  @Test
  public void testContainsElement() {
    for (MultiplicativeElement elt: elts) {
      Assert.assertTrue(gStarMod.contains(elt));
    }
    Assert.assertFalse(gStarMod.contains(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testContainsElementException() {
    gStarMod.contains(null);
  }

  @Test
  public void testIsIdentityElement() {
    Assert.assertTrue(gStarMod.isIdentity(gStarMod.getIdentityElement()));
    Assert.assertTrue(gStarMod.isIdentity(e1));
    Assert.assertFalse(gStarMod.isIdentity(e4));
    Assert.assertFalse(gStarMod.isIdentity(e24));
    Assert.assertFalse(gStarMod.isIdentity(OTHER_ELEMENT));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testIsIdentityElementException() {
    gStarMod.isIdentity(null);
  }

  @Test
  public void testAreEqualElements() {
    Assert.assertTrue(gStarMod.areEqual(MINUS_ONE, MINUS_ONE));
    Assert.assertTrue(gStarMod.areEqual(MINUS_ONE, e24));
    Assert.assertTrue(gStarMod.areEqual(e1, e1));
    Assert.assertTrue(gStarMod.areEqual(e4, e4));
    Assert.assertTrue(gStarMod.areEqual(OTHER_ELEMENT, OTHER_ELEMENT));
    Assert.assertFalse(gStarMod.areEqual(e1, e4));
    Assert.assertFalse(gStarMod.areEqual(e1, MINUS_ONE));
    Assert.assertFalse(gStarMod.areEqual(e1, OTHER_ELEMENT));
    Assert.assertFalse(gStarMod.areEqual(OTHER_ELEMENT, e4));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException1() {
    gStarMod.areEqual(null, e4);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException2() {
    gStarMod.areEqual(e4, null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testAreEqualElementsException3() {
    gStarMod.areEqual(null, null);
  }

  @Test
  public void testGetArity() {
    Assert.assertTrue(gStarMod.getArity()==1);
  }

  @Test
  public void testHasElement() {
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(-1)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(1)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(4)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(6)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(9)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(11)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(14)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(16)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(19)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(21)));
    Assert.assertTrue(gStarMod.contains(BigInteger.valueOf(24)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(-2)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(0)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(2)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(3)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(5)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(7)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(8)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(10)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(20)));
    Assert.assertFalse(gStarMod.contains(BigInteger.valueOf(30)));
  }

  @Test (expected=IllegalArgumentException.class)
  public void testHasElementException() {
    gStarMod.contains(null);
  }

  @Test
  public void testCreateElementBigInteger() {
    for (int i=-5; i<=50; i++) {
      if (gStarMod.contains(BigInteger.valueOf(i))) {
        Assert.assertEquals(gStarMod.getElement(BigInteger.valueOf(i)).getValue(), BigInteger.valueOf(i).mod(gStarMod.getModulus()));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementBigIntegerException() {
    gStarMod.getElement((BigInteger) null);
  }

  @Test
  public void testCreateElementInt() {
    for (int i=-5; i<=50; i++) {
      if (gStarMod.contains(BigInteger.valueOf(i))) {
        Assert.assertEquals(gStarMod.getElement(i).getValue(), BigInteger.valueOf(i).mod(gStarMod.getModulus()));
      }
    }
  }

  @Test
  public void testCreateElementAtomicElement() {
    for (int i=-5; i<=50; i++) {
      if (gStarMod.contains(BigInteger.valueOf(i))) {
        Assert.assertEquals(gStarMod.getElement(ZPlus.getInstance().getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(gStarMod.getModulus()));
        Assert.assertEquals(gStarMod.getElement(gStarMod.getElement(BigInteger.valueOf(i))).getValue(), BigInteger.valueOf(i).mod(gStarMod.getModulus()));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateElementAtomicElementException() {
    gStarMod.getElement((AtomicElement) null);
  }  

  @Test
  public void testGetBigInteger() {
    // has been tested in many test methods above
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException1() {
    gStarMod.getValue(null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testGetBigIntegerException2() {
    gStarMod.getValue(ZPlus.getInstance().getElement(5));
  }

  @Test
  public void testMultiplyElementElement() {
    for (MultiplicativeElement elt1: elts) {
      for (MultiplicativeElement elt2: elts) {
        Assert.assertEquals(gStarMod.multiply(elt1, elt2).getValue(), (elt1.getValue().multiply(elt2.getValue()).mod(gStarMod.getModulus())));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException1() {
    gStarMod.multiply((MultiplicativeElement)null, (MultiplicativeElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException2() {
    gStarMod.multiply(e11, (MultiplicativeElement)null);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException3() {
    gStarMod.multiply((MultiplicativeElement)null, e11);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException4() {
    gStarMod.multiply(OTHER_ELEMENT, e11);
  }
  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementElementException5() {
    gStarMod.multiply(e11, OTHER_ELEMENT);
  }

  @Test
  public void testMultiplyElementArray() {
    Assert.assertEquals(gStarMod.multiply(), e1);
    Assert.assertEquals(gStarMod.multiply(e1), e1);
    Assert.assertEquals(gStarMod.multiply(MINUS_ONE), e24);
    Assert.assertEquals(gStarMod.multiply(e1, e1, e1), e1);
    Assert.assertEquals(gStarMod.multiply(MINUS_ONE, e1, e1), e24);
    Assert.assertEquals(gStarMod.multiply(MINUS_ONE, e4, MINUS_ONE), e4);
    Assert.assertEquals(gStarMod.multiply(e1, e4, e6, e24), e1);
    Assert.assertEquals(gStarMod.multiply(new MultiplicativeElement[] {}), e1);
    Assert.assertEquals(gStarMod.multiply(new MultiplicativeElement[] {e1, e4, e6, e24}), e1);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException1() {
    gStarMod.multiply((MultiplicativeElement[])null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException2() {
    gStarMod.multiply(new MultiplicativeElement[] {null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException3() {
    gStarMod.multiply(new MultiplicativeElement[] {e1, e1, null});
  }

  @Test (expected=IllegalArgumentException.class)
  public void testMultiplyElementArrayException4() {
    gStarMod.multiply(new MultiplicativeElement[] {e1, e1, OTHER_ELEMENT});
  }

  @Test
  public void testPowerElementBigInteger() {
    Assert.assertEquals(gStarMod.power(e1, BigInteger.ZERO), e1);
    Assert.assertEquals(gStarMod.power(e9, BigInteger.ZERO), e1);
    Assert.assertEquals(gStarMod.power(e1, BigInteger.TEN), e1);
    Assert.assertEquals(gStarMod.power(MINUS_ONE, BigInteger.ONE), e24);
    Assert.assertEquals(gStarMod.power(MINUS_ONE, BigInteger.valueOf(10)), e1);
    Assert.assertEquals(gStarMod.power(e6, BigInteger.valueOf(2)), e11);
    Assert.assertEquals(gStarMod.power(e4, BigInteger.valueOf(-8)), e16);
    Assert.assertEquals(gStarMod.power(e4, BigInteger.valueOf(10)), e1);
    BigInteger amount;
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        amount = BigInteger.valueOf(j);
        Assert.assertEquals(gStarMod.power(elt, amount).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException1() {
    gStarMod.power(e1, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException2() {
    gStarMod.power(null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException3() {
    gStarMod.power(null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementBigIntegerException4() {
    gStarMod.power(OTHER_ELEMENT, BigInteger.TEN);
  }

  @Test
  public void testPowerElementAtomicElement() {
    AtomicElement amount;
    ZPlus zPlus = ZPlus.getInstance();
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        amount = zPlus.getElement(j);
        Assert.assertEquals(gStarMod.power(elt, amount).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException1() {
    gStarMod.power(e1, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException2() {
    gStarMod.power(null, e9);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException3() {
    gStarMod.power(null, (AtomicElement)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementAtomicElementException4() {
    gStarMod.power(OTHER_ELEMENT, e9);
  }

  @Test
  public void testPowerElementInt() {
    Assert.assertEquals(gStarMod.selfApply(e1, 0), e1);
    Assert.assertEquals(gStarMod.selfApply(e9, 0), e1);
    Assert.assertEquals(gStarMod.selfApply(e1, 10), e1);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, 1), e24);
    Assert.assertEquals(gStarMod.selfApply(MINUS_ONE, 10), e1);
    Assert.assertEquals(gStarMod.selfApply(e6, 2), e11);
    Assert.assertEquals(gStarMod.selfApply(e4, -8), e16);
    Assert.assertEquals(gStarMod.selfApply(e4, 10), e1);
    for (MultiplicativeElement elt: elts) {
      for (int j=0; j<gStarMod.getOrder().intValue();j++) {
        Assert.assertEquals(gStarMod.power(elt, j).getValue(),
            elt.getValue().modPow(BigInteger.valueOf(j), gStarMod.getModulus()));        
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementIntException1() {
    gStarMod.power(null, 10);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testPowerElementIntException2() {
    gStarMod.power(OTHER_ELEMENT, 10);
  }    

  @Test
  public void testHashCode() {
    Assert.assertTrue(gStarMod.hashCode() == gStarMod.hashCode());
    Assert.assertTrue(gStarMod.hashCode() == new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2), BigInteger.valueOf(5)).hashCode());
    Assert.assertTrue(gStarMod.hashCode() == new GStarMod(BigInteger.valueOf(5), 2, false, new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(5)}, new int[]{1, 1}).hashCode());
    Assert.assertFalse(gStarMod.hashCode() == new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(5)).hashCode());
    Assert.assertFalse(gStarMod.hashCode() == new GStarMod(BigInteger.valueOf(11), false).hashCode());
    Assert.assertFalse(gStarMod.hashCode() == new ZStarMod(BigInteger.valueOf(25)).hashCode());
    Assert.assertFalse(gStarMod.hashCode() == SingletonGroup.getInstance().hashCode());
    Assert.assertFalse(gStarMod.hashCode() == BooleanGroup.getInstance().hashCode());
    Assert.assertFalse(gStarMod.hashCode() == e1.hashCode());
    Assert.assertFalse(gStarMod.hashCode() == BigInteger.ZERO.hashCode());
    Assert.assertFalse(gStarMod.hashCode() == "".hashCode());    
  }

  @Test
  public void testEqualsObject() {
    Assert.assertTrue(gStarMod.equals(gStarMod));
    Assert.assertTrue(gStarMod.equals(new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(2), BigInteger.valueOf(5))));
    Assert.assertTrue(gStarMod.equals(new GStarMod(BigInteger.valueOf(5), 2, false, new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(5)}, new int[]{1, 1})));
    Assert.assertFalse(gStarMod.equals(new GStarMod(BigInteger.valueOf(5), 2, false, BigInteger.valueOf(5))));
    Assert.assertFalse(gStarMod.equals(new GStarMod(BigInteger.valueOf(11), false)));
    Assert.assertFalse(gStarMod.equals(new ZStarMod(BigInteger.valueOf(25))));
    Assert.assertFalse(gStarMod.equals(ZPlus.getInstance()));
    Assert.assertFalse(gStarMod.equals(SingletonGroup.getInstance()));
    Assert.assertFalse(gStarMod.equals(BooleanGroup.getInstance()));
    Assert.assertFalse(gStarMod.equals(new ZPlusModClass(BigInteger.ONE)));
    Assert.assertFalse(gStarMod.equals(null));
    Assert.assertFalse(gStarMod.equals(e1));
    Assert.assertFalse(gStarMod.equals(BigInteger.ZERO));
    Assert.assertFalse(gStarMod.equals(""));    
  }

  @Test
  public void testToString() {
    Assert.assertEquals(gStarMod.toString(), "GStarModClass[modulo=25,order=10]");
  }

}
