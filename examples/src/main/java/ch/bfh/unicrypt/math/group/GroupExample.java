/* 
 * UniCrypt
 * 
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 * 
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 * 
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 * 
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 * 
 *
 * Redistributions of files must retain the above copyright notice.
 */
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
