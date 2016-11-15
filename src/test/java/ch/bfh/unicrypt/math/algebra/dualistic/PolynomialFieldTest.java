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

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import java.math.BigInteger;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialFieldTest {

	private static final BigInteger zero = MathUtil.ZERO;
	private static final BigInteger one = MathUtil.ONE;
	private static final BigInteger two = MathUtil.TWO;
	private static final BigInteger three = MathUtil.THREE;
	private static final BigInteger four = MathUtil.FOUR;
	private static final BigInteger five = MathUtil.FIVE;

	private static final ZModPrime zmod2 = ZModPrime.getInstance(2);
	private static final ZModPrime zmod3 = ZModPrime.getInstance(3);
	private static final ZModPrime zmod5 = ZModPrime.getInstance(5);
	private static final ZModPrime zmod7 = ZModPrime.getInstance(7);

	private static final PolynomialRing ring2 = PolynomialRing.getInstance(zmod2);
	private static final ZModElement ZERO = zmod2.getZeroElement();
	private static final ZModElement ONE = zmod2.getOneElement();
	private static final PolynomialElement irrPoly = ring2.getElement(ONE, ONE, ZERO, ZERO, ONE);
	private static final PolynomialField field2_4 = PolynomialField.getInstance(zmod2, irrPoly);

	private static final PolynomialField field5_3 = PolynomialField.getInstance(zmod5, 3);
	private static final PolynomialField field7_4 = PolynomialField.getInstance(zmod7, 4);
	private static final PolynomialField field3_10 = PolynomialField.getInstance(zmod3, 10);

	@Test
	public void testGetElement() {
		try {
			PolynomialElement p;

//			// 1 + x^2 + x^5 + x^6 => 1 + x + x^2 + x^3
//			p = field2_4.getElementFrom(one, zero, one, zero, zero, one, one);
//
			// 1 + 4x^2 + x^3
			p = field7_4.getElementFrom(one, zero, four, one);
			assertTrue(4 > p.getValue().getDegree());
			assertEquals(zmod7.getOneElement(), p.getValue().getCoefficient(0));
			assertEquals(zmod7.getZeroElement(), p.getValue().getCoefficient(1));
			assertEquals(zmod7.getElement(4), p.getValue().getCoefficient(2));
			assertEquals(zmod7.getOneElement(), p.getValue().getCoefficient(3));
			assertEquals(zmod7.getZeroElement(), p.getValue().getCoefficient(4));

			HashMap map = new HashMap();
			map.put(4, zmod3.getElement(14 % 3));
			map.put(9, zmod3.getElement(13 % 3));
			//map.put(12, zmod3.getElementFrom(19 % 3));
			p = field3_10.getElement(Polynomial.<DualisticElement<BigInteger>>getInstance(map, zmod3.getZeroElement(), zmod3.getOneElement()));
			assertTrue(10 > p.getValue().getDegree());
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testMultiply() {
		try {
			// p1 = 1 + x^2 + x^3
			PolynomialElement p1 = field2_4.getElementFrom(one, zero, one, one);
			// p2 = 1 + x^3
			PolynomialElement p2 = field2_4.getElementFrom(one, zero, zero, one);
			// p1 * p2 = 1 + x^2 + x^5 + x^6 => 1 + x + x^2 + x^3
			PolynomialElement p3 = p1.multiply(p2);
			assertEquals(zmod2.getOneElement(), p3.getValue().getCoefficient(0));
			assertEquals(zmod2.getOneElement(), p3.getValue().getCoefficient(1));
			assertEquals(zmod2.getOneElement(), p3.getValue().getCoefficient(2));
			assertEquals(zmod2.getOneElement(), p3.getValue().getCoefficient(3));
			assertEquals(zmod2.getZeroElement(), p3.getValue().getCoefficient(4));

			// p1 = 1 + x + x^3
			p1 = field2_4.getElementFrom(one, one, zero, one);
			// p2 = 1 + x^2
			p2 = field2_4.getElementFrom(one, zero, one);
			// p1 * p2 = 1 + x^2 + x^5 => 1
			p3 = p1.multiply(p2);
			assertTrue(p3.isEquivalent(field2_4.getOneElement()));
			assertTrue(p3.getSet().isField());
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testOneOver() {
		try {
			// p1 = 1 + x + x^3
			PolynomialElement p1 = field2_4.getElementFrom(one, one, zero, one);
			PolynomialElement p2 = p1.oneOver();

			assertEquals(zmod2.getOneElement(), p2.getValue().getCoefficient(0));
			assertEquals(zmod2.getZeroElement(), p2.getValue().getCoefficient(1));
			assertEquals(zmod2.getOneElement(), p2.getValue().getCoefficient(2));
			assertEquals(zmod2.getZeroElement(), p2.getValue().getCoefficient(3));
			assertTrue(p2.getSet().isField());

			PolynomialElement p3 = p1.multiply(p2);
			assertTrue(p3.isEquivalent(field2_4.getOneElement()));

			p1 = field5_3.getElementFrom(one, zero, two);
			p2 = p1.oneOver();
			p3 = p1.multiply(p2);
			assertTrue(p3.isEquivalent(field5_3.getOneElement()));

			p1 = field7_4.getElementFrom(one, four, five, three);
			p2 = p1.oneOver();
			p3 = p1.multiply(p2);
			assertTrue(p3.isEquivalent(field7_4.getOneElement()));
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testDivide() {
		try {
			// p1 = 1 + x^2 + x^3
			PolynomialElement p1 = field2_4.getElementFrom(one, zero, one, one);
			// p2 = 1 + x + x^2 + x^3
			PolynomialElement p2 = field2_4.getElementFrom(one, one, one, one);
			// p2 / p1 = 1 + x^3
			PolynomialElement p3 = p2.divide(p1);
			assertEquals(field2_4.getElementFrom(one, zero, zero, one), p3);
			assertTrue(p3.getSet().isField());
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testAdd() {
		try {
			// p1 = 1 + x + x^3
			PolynomialElement p1 = field2_4.getElementFrom(one, one, zero, one);
			// p2 = 1 + x^3
			PolynomialElement p2 = field2_4.getElementFrom(one, zero, zero, one);
			// p1 + p2 = x
			PolynomialElement p3 = p1.add(p2);
			assertEquals(zmod2.getZeroElement(), p3.getValue().getCoefficient(0));
			assertEquals(zmod2.getOneElement(), p3.getValue().getCoefficient(1));
			assertEquals(zmod2.getZeroElement(), p3.getValue().getCoefficient(2));
			assertEquals(zmod2.getZeroElement(), p3.getValue().getCoefficient(3));
			assertEquals(zmod2.getZeroElement(), p3.getValue().getCoefficient(4));
			assertTrue(p3.getSet().isField());
		} catch (UniCryptException ex) {
			fail();
		}
	}

}
