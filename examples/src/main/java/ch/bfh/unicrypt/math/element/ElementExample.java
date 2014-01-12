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
package ch.bfh.unicrypt.math.element;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;

public class ElementExample {

  public static void main(final String[] args) {

    final AdditiveElement e0 = ZPlusClass.getInstance().createElement(BigInteger.valueOf(55));
    System.out.println(e0.getBigInteger());

    // TODO
    // CONCAT
    // final AdditiveElement e1 =
    // ZPlusClass.getInstance().createElement(BigInteger.valueOf(55),
    // BigInteger.valueOf(66));
    // System.out.println(e1.getBigInteger());

    final AdditiveElement e2 = ZPlusClass.getInstance().createElement(new BigInteger("ABC".getBytes()));
    System.out.println(new String(e2.getBigInteger().toByteArray()));

    // TODO
    // Concat
    // final AdditiveElement e3 = ZPlusClass.getInstance().createElement("ABC",
    // "DEF");
    // System.out.println(new String(e3.getBigInteger().toByteArray()));

    // TODO
    // Concat
    // final AdditiveElement e4 = ZPlusClass.getInstance().createElement(new
    // BigInteger("ABC".getBytes()), new BigInteger("DEF".getBytes()),
    // BigInteger.valueOf(55));
    // System.out.println(new String(e4.getBigInteger().toByteArray()));

    // final ProductGroup group = new PowerGroupClass(ZPlusClass.getInstance(),
    // 3);
    // final TupleElement e5 =
    // group.createElement(ZPlusClass.getInstance().createElement(new
    // BigInteger("ABC".getBytes())), ZPlusClass.getInstance().createElement(new
    // BigInteger("DEF".getBytes())),
    // ZPlusClass.getInstance().createElement(BigInteger.valueOf(55)));
    // TODO
    // Concat
    // System.out.println(e5.getString());
    // System.out.println(group.getString(e5)); // alternative call

    // final AdditiveElement e6 = ZPlusClass.getInstance().createElement(e5, e5,
    // e5);
    // System.out.println(new String(e6.getBigInteger().toByteArray()));

  }

}
