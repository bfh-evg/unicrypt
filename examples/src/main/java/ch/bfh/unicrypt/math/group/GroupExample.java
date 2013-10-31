package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;

public class GroupExample {

  public static void main(final String[] args) {

    AdditiveElement a1;
    AdditiveElement a2;
    MultiplicativeElement m1;
    MultiplicativeElement m2;

    // TEST ZPLUS

    final ZPlus group1 = ZPlusClass.getInstance();
    System.out.println(group1.getIdentityElement());
    System.out.println(group1.getOrder());
    System.out.println(group1.getArity());
    System.out.println(group1.getDefaultGenerator());
    for (int i = 1; i <= 3; i++) {
      System.out.println(group1.createRandomGenerator());
    }
    a1 = group1.createElement(BigInteger.valueOf(3));
    a2 = group1.createElement(BigInteger.valueOf(-5));
    System.out.println(group1.invert(a1));
    System.out.println(group1.invert(a2));
    System.out.println(group1.apply(a1, a2));
    System.out.println(group1.add(a1, a2));
    System.out.println(group1.selfApply(a1, a2));
    System.out.println(group1.times(a1, a2));
    System.out.println(group1.apply(a1, a1, a1, a1));
    System.out.println();

    // TEST ZPLUSMOD

    final ZPlusMod group2 = new ZPlusModClass(BigInteger.valueOf(10));
    System.out.println(group2.getIdentityElement());
    System.out.println(group2.getOrder());
    System.out.println(group2.getArity());
    System.out.println(group2.getDefaultGenerator());
    for (int i = 1; i <= 3; i++) {
      System.out.println(group2.createRandomGenerator());
    }
    a1 = group2.createElement(BigInteger.valueOf(3));
    a2 = group2.createElement(BigInteger.valueOf(-5));
    System.out.println(group2.invert(a1));
    System.out.println(group2.invert(a2));
    System.out.println(group2.apply(a1, a2));
    System.out.println(group2.add(a1, a2));
    System.out.println(group2.selfApply(a1, a2));
    System.out.println(group2.times(a1, a2));
    System.out.println(group2.apply(a1, a1, a1, a1));
    System.out.println();

    // TEST ZSTARMOD

    // 1) unknown order

    final ZStarMod group3 = new ZStarModClass(BigInteger.valueOf(10));
    System.out.println(group3.getIdentityElement());
    System.out.println(group3.getOrder());
    System.out.println(group3.getArity());
    m1 = group3.createElement(BigInteger.valueOf(3));
    m2 = group3.createElement(BigInteger.valueOf(7));
    System.out.println(group3.invert(m1));
    System.out.println(group3.invert(m2));
    System.out.println(group3.apply(m1, m2));
    System.out.println(group3.multiply(m1, m2));
    System.out.println(group3.selfApply(m1, m2));
    System.out.println(group3.power(m1, m2));
    System.out.println(group3.apply(m1, m1, m1, m1));
    System.out.println();

    // 2) known order

    final ZStarMod group4 = new ZStarModClass(BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(5));
    System.out.println(group4.getIdentityElement());
    System.out.println(group4.getOrder()); // 8
    System.out.println(group4.getArity()); // 1
    m1 = group4.createElement(BigInteger.valueOf(3));
    m2 = group4.createElement(BigInteger.valueOf(11));
    System.out.println(group4.invert(m1));
    System.out.println(group4.invert(m2));
    System.out.println(group4.apply(m1, m2));
    System.out.println(group4.multiply(m1, m2));
    System.out.println(group4.selfApply(m1, m2));
    System.out.println(group4.power(m1, m2));
    System.out.println(group4.apply(m1, m1, m1, m1));
    System.out.println();

    // TEST MULTISELFAPPLY IN ADDITIVE GROUP

    final Element e1 = group1.createElement(2);
    final Element e2 = group1.createElement(3);
    final Element e3 = group1.createElement(5);
    final BigInteger i1 = BigInteger.valueOf(3);
    final BigInteger i2 = BigInteger.valueOf(2);
    final BigInteger i3 = BigInteger.valueOf(1);
    System.out.println(group1.multiSelfApply(new Element[] { e1, e2, e3 }, new BigInteger[] { i1, i2, i3 }));

  }

}
