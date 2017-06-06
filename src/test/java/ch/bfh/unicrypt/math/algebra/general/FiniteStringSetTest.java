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
package ch.bfh.unicrypt.math.algebra.general;

import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringSet;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class FiniteStringSetTest {

//	@Test
//	public void testIteration() {
//		FiniteStringSet set = FiniteStringSet.getInstance(Alphabet.OCTAL, 2, true);
//		for (Element element : set) {
//			System.out.println(element);
//		}
//		System.out.println(set.getOrder());
//	}
	@Test
	public void testGetValue() {
		FiniteStringSet set = FiniteStringSet.getInstance(Alphabet.BINARY, 3, 5);
//		System.out.println(set.getElement("000").getValue());
//		System.out.println(set.getElement("001").getValue());
//		System.out.println(set.getElement("111").getValue());
//		System.out.println(set.getElement("0000").getValue());
//		System.out.println(set.getElement("0001").getValue());
//		System.out.println(set.getElement("0010").getValue());
//		System.out.println(set.getElement("0011").getValue());
//		System.out.println(set.getElement("0100").getValue());
//		System.out.println(set.getElement("0101").getValue());
//		System.out.println(set.getElement("0110").getValue());
//		System.out.println(set.getElement("0111").getValue());
//		System.out.println(set.getElement("1111").getValue());
//		System.out.println(set.getElement("00000").getValue());
//		for (BigInteger i = MathUtil.ZERO; i.compareTo(BigInteger.valueOf(24)) <= 0; i = i.add(MathUtil.ONE)) {
//			System.out.println(set.getElement(i));
//		}
//		System.out.println(set.getOrder());
//		System.out.println(FiniteStringSet.getInstance(Alphabet.BINARY, BigInteger.valueOf(54), 3).getOrder());
	}

}
