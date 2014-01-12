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
import java.util.ArrayList;

import ch.bfh.unicrypt.math.group.classes.GStarModClass;
import ch.bfh.unicrypt.math.group.classes.GStarPrimeClass;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZStarModClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarMod;
import ch.bfh.unicrypt.math.group.interfaces.ZStarMod;

public class OrderExample {

  public static void main(final String[] args) {

    final ArrayList<ZStarMod> groups = new ArrayList<ZStarMod>();

    // BigInteger zero = BigInteger.ZERO;
    // BigInteger one = BigInteger.ONE;
    final BigInteger two = BigInteger.valueOf(2);
    final BigInteger three = BigInteger.valueOf(3);
    final BigInteger five = BigInteger.valueOf(5);
    final BigInteger ten = BigInteger.valueOf(10);

    groups.add(new ZStarModClass(ten));
    groups.add(new ZStarModClass(three, five));
    groups.add(new ZStarModClass(new BigInteger[] { three, five }, new int[] { 2, 1 }));

    groups.add(new GStarModClass(two));
    groups.add(new GStarModClass(two, 1));
    groups.add(new GStarModClass(two, 1, false));
    groups.add(new GStarModClass(two, 1, true));
    groups.add(new GStarModClass(two, 1, true, two));
    groups.add(new GStarModClass(three, 2, false));
    groups.add(new GStarModClass(three, 2, false, two));
    groups.add(new GStarModClass(three, 2, false, three));
    groups.add(new GStarModClass(three, 2, false, two, three));
    groups.add(new GStarModClass(three, 2, true));
    groups.add(new GStarModClass(three, 2, true, two));
    groups.add(new GStarModClass(three, 2, true, three));
    groups.add(new GStarModClass(three, 2, true, two, three));
    groups.add(new GStarModClass(three, 3, true));
    groups.add(new GStarModClass(three, 3, true, two));
    groups.add(new GStarModClass(three, 3, true, three));
    groups.add(new GStarModClass(three, 3, true, two, three));
    groups.add(new GStarModClass(three, 4, true, two, three));
    groups.add(new GStarModClass(five, 4, false));
    groups.add(new GStarModClass(five, 4, false, two));
    groups.add(new GStarModClass(five, 4, false, five));
    groups.add(new GStarModClass(five, 4, false, two, five));

    groups.add(new GStarPrimeClass(two));
    groups.add(new GStarPrimeClass(three));
    groups.add(new GStarPrimeClass(three, two));
    groups.add(new GStarPrimeClass(five));
    groups.add(new GStarPrimeClass(five, two));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7)));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), two));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), three));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), two, three));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(11)));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(13)));

    groups.add(new GStarSaveClass(BigInteger.valueOf(5)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(7)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(11)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23), true));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23), false));

    for (final ZStarMod group : groups) {
      if (group == null) {
        break;
      }
      System.out.print("Mod=" + group.getModulus() + " Order=" + group.getOrder());
      if (group instanceof GStarMod) {
        System.out.print(" GroupOrder=" + ((GStarModClass) group).getGroupOrder());
        System.out.print(" Generator=" + ((GStarModClass) group).getDefaultGenerator().getBigInteger());
      }
      System.out.println();
    }

  }

}
