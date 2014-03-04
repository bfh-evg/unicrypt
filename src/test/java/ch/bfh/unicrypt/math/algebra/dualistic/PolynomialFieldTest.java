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
package ch.bfh.unicrypt.math.algebra.dualistic;

import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialFieldTest {

	//private static PolynomialField<ResidueClass> field1;  // ZMod
	private static PolynomialRing<ResidueClass> ring2;  // ZMod Binary
	private static PolynomialField<ResidueClass> field2;  // ZMod Binary

	private static BigInteger zero = BigInteger.valueOf(0);
	private static BigInteger one = BigInteger.valueOf(1);

	public PolynomialFieldTest() {
		//field1 = PolynomialField.getInstance(ZModPrime.getInstance(7), 4);
		ZModPrime zmod2 = ZModPrime.getInstance(2);
		ring2 = PolynomialRing.getInstance(zmod2);
		// irr poly = 1 + x + x^4
		PolynomialElement<ResidueClass> irrPoly = ring2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(1));
		field2 = PolynomialField.getInstance(zmod2, irrPoly);
	}

	@Test
	public void testGetElement() {
		// 1 + x^2 + x^5 + x^6 => 1 + x + x^2 + x^3
		PolynomialElement<ResidueClass> p = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(1));
		//System.out.println(poly);
		ZMod zmod = (ZMod) field2.getSemiRing();
		assertEquals(zmod.getOneElement(), p.getValue().getCoefficient(0));
		assertEquals(zmod.getOneElement(), p.getValue().getCoefficient(1));
		assertEquals(zmod.getOneElement(), p.getValue().getCoefficient(2));
		assertEquals(zmod.getOneElement(), p.getValue().getCoefficient(3));
		assertEquals(zmod.getZeroElement(), p.getValue().getCoefficient(4));
	}

	@Test
	public void testMultiply() {
		// p1 = 1 + x^2 + x^3
		PolynomialElement<ResidueClass> p1 = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(1));
		// p2 = 1 + x^3
		PolynomialElement<ResidueClass> p2 = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(1));
		// p1 * p2 = 1 + x^2 + x^5 + x^6 => 1 + x + x^2 + x^3
		PolynomialElement<ResidueClass> p3 = p1.multiply(p2);
		//System.out.println(p3);
		ZMod zmod = (ZMod) field2.getSemiRing();
		assertEquals(zmod.getOneElement(), p3.getValue().getCoefficient(0));
		assertEquals(zmod.getOneElement(), p3.getValue().getCoefficient(1));
		assertEquals(zmod.getOneElement(), p3.getValue().getCoefficient(2));
		assertEquals(zmod.getOneElement(), p3.getValue().getCoefficient(3));
		assertEquals(zmod.getZeroElement(), p3.getValue().getCoefficient(4));

		// p1 = 1 + x + x^3
		p1 = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(1));
		// p2 = 1 + x^2
		p2 = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(1));
		// p1 * p2 = 1 + x^2 + x^5 => 1
		p3 = p1.multiply(p2);
		//System.out.println(p3);
		assertTrue(p3.isEquivalent(field2.getOneElement()));
		assertTrue(p3.getSet().isField());
	}

	@Test
	public void testOneOver() {
		// p1 = 1 + x + x^3
		PolynomialElement<ResidueClass> p1 = field2.getElement(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(1));
		PolynomialElement<ResidueClass> p2 = p1.oneOver();

		ZMod zmod = (ZMod) field2.getSemiRing();
		assertEquals(zmod.getOneElement(), p2.getValue().getCoefficient(0));
		assertEquals(zmod.getZeroElement(), p2.getValue().getCoefficient(1));
		assertEquals(zmod.getOneElement(), p2.getValue().getCoefficient(2));
		assertEquals(zmod.getZeroElement(), p2.getValue().getCoefficient(3));
		assertTrue(p2.getSet().isField());

		PolynomialElement<ResidueClass> p3 = p1.multiply(p2);
		assertTrue(p3.isEquivalent(field2.getOneElement()));
	}

	@Test
	public void testDivide() {
		// p1 = 1 + x^2 + x^3
		PolynomialElement<ResidueClass> p1 = field2.getElement(one, zero, one, one);
		// p2 = 1 + x + x^2 + x^3
		PolynomialElement<ResidueClass> p2 = field2.getElement(one, one, one, one);
		// p2 / p1 = 1 + x^3
		PolynomialElement<ResidueClass> p3 = p2.divide(p1);
		//System.out.println(p3);
		assertEquals(field2.getElement(one, zero, zero, one), p3);
		assertTrue(p3.getSet().isField());
	}

}
