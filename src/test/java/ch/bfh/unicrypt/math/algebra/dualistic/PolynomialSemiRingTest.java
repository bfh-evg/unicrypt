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
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialSemiRingTest {

	private static final Z z = Z.getInstance();
	private static final ZModPrime zmod2 = ZModPrime.getInstance(2);
	private static final ZModPrime zmod7 = ZModPrime.getInstance(7);

	private static final PolynomialSemiRing ring0 = PolynomialSemiRing.getInstance(z);
	private static final PolynomialSemiRing ring2 = PolynomialSemiRing.getInstance(zmod2);
	private static final PolynomialSemiRing ring7 = PolynomialSemiRing.getInstance(zmod7);

	@Test
	public void testIsBinary() {
		assertFalse(ring0.isBinary());
		assertFalse(ring7.isBinary());
		assertTrue(ring2.isBinary());
	}

	@Test
	public void testGetElementBigInteger() {
		try {
			PolynomialElement p0 = ring0.getElementFrom(2, 3, 4, 2);
			assertEquals(1, p0.getValue().getCoefficient(0).getValue().intValue());
			assertEquals(-2, p0.getValue().getCoefficient(1).getValue().intValue());

			PolynomialElement p1 = ring7.getElementFrom(2, 3, 4, 2);
			assertEquals(2, p1.getValue().getCoefficient(0).getValue().intValue());
			assertEquals(3, p1.getValue().getCoefficient(1).getValue().intValue());
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testGetElementTuple() {
		try {
			PolynomialElement p = ring7.getElement(Tuple.getInstance(Z.getInstance().getElement(2), ZMod.getInstance(7).getElement(3)));
			fail();
		} catch (UniCryptRuntimeException e) {
		}

		PolynomialElement p = ring0.getElement(Tuple.getInstance(z.getElement(0), z.getElement(1), z.getElement(2), z.getElement(3)));
		assertEquals(0, p.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(1, p.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(2, p.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(3, p.getValue().getCoefficient(3).getValue().intValue());

		PolynomialElement p2 = ring2.getElement(Tuple.getInstance(zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(1)));
		assertEquals(0, p2.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(1, p2.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(1, p2.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(1, p2.getValue().getCoefficient(3).getValue().intValue());
	}

	@Test
	public void testGetElementByRoots() {
		PolynomialElement p = ring7.getElementByRoots(Tuple.getInstance(zmod7.getElement(3), zmod7.getElement(4)));

		assertTrue(p.getValue().getCoefficient(2).isEquivalent(zmod7.getOneElement()));
		assertTrue(p.getValue().getCoefficient(1).isEquivalent(zmod7.getElement(0)));
		assertTrue(p.getValue().getCoefficient(0).isEquivalent(zmod7.getElement(5)));

		assertTrue(p.evaluate(zmod7.getElement(3)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(p.evaluate(zmod7.getElement(4)).isEquivalent(zmod7.getZeroElement()));

		p = ring7.getElementByRoots(Tuple.getInstance(zmod7.getElement(2), zmod7.getElement(1), zmod7.getElement(5)));

		assertTrue(p.getValue().getCoefficient(3).isEquivalent(zmod7.getOneElement()));
		assertTrue(p.getValue().getCoefficient(0).isEquivalent(zmod7.getElement(4)));

		assertTrue(p.evaluate(zmod7.getElement(1)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(p.evaluate(zmod7.getElement(2)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(p.evaluate(zmod7.getElement(5)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(!p.evaluate(zmod7.getElement(0)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(!p.evaluate(zmod7.getElement(3)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(!p.evaluate(zmod7.getElement(4)).isEquivalent(zmod7.getZeroElement()));
		assertTrue(!p.evaluate(zmod7.getElement(6)).isEquivalent(zmod7.getZeroElement()));

	}

	@Test
	public void testContains() {
		Polynomial<ZElement> poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
															z.getIdentityElement(), z.getOneElement());
		assertTrue(ring0.contains(poly1));
		assertFalse(ring7.contains(poly1));

// This test cannot be executed anymore due to more precise generics
//		Polynomial poly2 = Polynomial.getInstance(new BigInteger[]{BigInteger.valueOf(2), BigInteger.valueOf(6), BigInteger.valueOf(4)},
//															  BigInteger.valueOf(0), BigInteger.valueOf(1));
//		assertFalse(ring0.contains(poly2));
	}

	@Test
	public void testAbstractApply() {
		Polynomial poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
												  z.getIdentityElement(), z.getOneElement());
		Polynomial poly2 = Polynomial.getInstance(new ZElement[]{z.getElement(4), z.getElement(7), z.getElement(4), z.getElement(9)},
												  z.getIdentityElement(), z.getOneElement());
		PolynomialElement e1 = ring0.getElement(poly1);
		PolynomialElement e2 = ring0.getElement(poly2);
		PolynomialElement e3 = e1.apply(e2);

		assertEquals(5, e3.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(7, e3.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(6, e3.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(9, e3.getValue().getCoefficient(3).getValue().intValue());

		poly1 = Polynomial.getInstance(new ZModElement[]{zmod7.getElement(5), zmod7.getElement(0), zmod7.getElement(2)},
									   zmod7.getIdentityElement(), zmod7.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod7.getElement(4), zmod7.getElement(7 % 7), zmod7.getElement(4), zmod7.getElement(5)},
									   zmod7.getIdentityElement(), zmod7.getOneElement());
		PolynomialElement e4 = ring7.getElement(poly1);
		PolynomialElement e5 = ring7.getElement(poly2);
		PolynomialElement e6 = e4.apply(e5);

		assertEquals(2, e6.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(6, e6.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(5, e6.getValue().getCoefficient(3).getValue().intValue());

		poly1 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(0), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = e4.apply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().intValue());
	}

	@Test
	public void testAbstractMultiply() {
		Polynomial poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
												  z.getIdentityElement(), z.getOneElement());
		Polynomial poly2 = Polynomial.getInstance(new ZElement[]{z.getElement(4), z.getElement(7), z.getElement(4), z.getElement(9)},
												  z.getIdentityElement(), z.getOneElement());
		PolynomialElement e1 = ring0.getElement(poly1);
		PolynomialElement e2 = ring0.getElement(poly2);
		PolynomialElement e3 = e1.multiply(e2);

		assertEquals(4, e3.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(7, e3.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(12, e3.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(23, e3.getValue().getCoefficient(3).getValue().intValue());
		assertEquals(8, e3.getValue().getCoefficient(4).getValue().intValue());
		assertEquals(18, e3.getValue().getCoefficient(5).getValue().intValue());

		poly1 = Polynomial.getInstance(new ZModElement[]{zmod7.getElement(1), zmod7.getElement(0), zmod7.getElement(2)},
									   zmod7.getIdentityElement(), zmod7.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod7.getElement(4), zmod7.getElement(7 % 7), zmod7.getElement(4), zmod7.getElement(9 % 7)},
									   zmod7.getIdentityElement(), zmod7.getOneElement());
		PolynomialElement e4 = ring7.getElement(poly1);
		PolynomialElement e5 = ring7.getElement(poly2);
		PolynomialElement e6 = e4.multiply(e5);

		assertEquals(4, e6.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(5, e6.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(2, e6.getValue().getCoefficient(3).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().intValue());
		assertEquals(4, e6.getValue().getCoefficient(5).getValue().intValue());

		poly1 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(0), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = e4.multiply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(5).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(6).getValue().intValue());

		poly1 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(0), zmod2.getElement(1)},
									   zmod2.getIdentityElement(), zmod2.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = e4.multiply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(2).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(4).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(5).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(6).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(7).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(8).getValue().intValue());
		assertEquals(0, e6.getValue().getCoefficient(9).getValue().intValue());
		assertEquals(1, e6.getValue().getCoefficient(10).getValue().intValue());
	}

}
