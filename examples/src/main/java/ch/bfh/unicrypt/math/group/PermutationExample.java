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

public class PermutationExample {

  public static void main(final String[] args) {

    // PermutationGroup group0 = new PermutationGroupClass(0);
    // PermutationElement p0 = group0.getIdentityElement();
    // System.out.println(p0.getString());
    // System.out.println();
    //
    // PermutationGroup group = new PermutationGroupClass(6);
    // PermutationElement p1 = group.getIdentityElement();
    // System.out.println(p1.getString());
    // System.out.println(p1.invert().getString());
    // System.out.println();
    //
    // PermutationElement p2 = group.createRandomElement();
    // System.out.println(p2.getString());
    // System.out.println(p2.invert().getString());
    // System.out.println(p2.apply(p2.invert()).getString());
    // System.out.println(p2.invert().apply(p2).invert().getString());
    // System.out.println(p2.apply(p2).apply(p2).getString());
    // System.out.println(p2.selfApply(3).getString());
    // System.out.println();
    //
    // System.out.println(group.invert(p2).getString());
    // System.out.println(group.apply(p2, p2.invert()).getString());
    // System.out.println(group.apply(p2.invert(), p2).getString());
    // System.out.println();
    //
    // PermutationElement p3 = group.createElement(3, 4, 0, 2, 5, 1);
    // System.out.println(p3.getString());
    // PermutationElement p4 = group.createElement(1, 5, 4, 2, 3, 0);
    // System.out.println(p4.getString());
    // System.out.println(p3.apply(p4).getString());
    // System.out.println(group.apply(p3,p4).getString());
    // System.out.println(p4.apply(p3).getString());
    // System.out.println(group.apply(p4,p3).getString());
    // System.out.println();
    //
    // System.out.println(group.getOrder());
  }

}
