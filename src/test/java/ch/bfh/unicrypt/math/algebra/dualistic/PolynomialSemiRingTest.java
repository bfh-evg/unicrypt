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

import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.helper.numerical.WholeNumber;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import java.math.BigInteger;
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

	private static PolynomialSemiRing<WholeNumber> ring0;  // Z
	private static PolynomialSemiRing<ResidueClass> ring1;  // ZMod
	private static PolynomialSemiRing<ResidueClass> ring2;  // ZMod Binary

	public PolynomialSemiRingTest() {
		ring0 = PolynomialSemiRing.getInstance(Z.getInstance());
		ring1 = PolynomialSemiRing.getInstance(ZMod.getInstance(7));
		ring2 = PolynomialSemiRing.getInstance(ZMod.getInstance(2));
	}

	@Test
	public void testIsBinary() {
		assertFalse(ring0.isBinray());
		assertFalse(ring1.isBinray());
		assertTrue(ring2.isBinray());
	}

	@Test
	public void testGetElementBigInteger() {
		PolynomialElement<WholeNumber> p0 = ring0.getElement(BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(2));
		//System.out.println(p0);
		assertEquals(1, p0.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(-2, p0.getValue().getCoefficient(1).getValue().getBigInteger().intValue());

		PolynomialElement<ResidueClass> p1 = ring1.getElement(BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(2));
		//System.out.println(p1);
		assertEquals(2, p1.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(3, p1.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
	}

	@Test
	public void testGetElementTuple() {
		try {
			PolynomialElement p = ring1.getElement(Tuple.getInstance(Z.getInstance().getElement(2), ZMod.getInstance(7).getElement(3)));
			fail();
		} catch (IllegalArgumentException e) {
		}

		Z z = Z.getInstance();
		PolynomialElement<WholeNumber> p = ring0.getElement(Tuple.getInstance(z.getElement(0), z.getElement(1), z.getElement(2), z.getElement(3)));
		assertFalse(p.getValue().isBinary());
		assertEquals(0, p.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(1, p.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(2, p.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(3, p.getValue().getCoefficient(3).getValue().getBigInteger().intValue());

		ZMod zmod = (ZMod) ring2.getSemiRing();
		PolynomialElement<ResidueClass> p2 = ring2.getElement(Tuple.getInstance(zmod.getElement(0), zmod.getElement(1), zmod.getElement(1), zmod.getElement(1)));
		assertTrue(p2.getValue().isBinary());
		assertEquals(0, p2.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(1, p2.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(1, p2.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(1, p2.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
	}

	@Test
	public void testContains() {
		Z z = Z.getInstance();
		Polynomial<ZElement> poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
															z.getIdentityElement(), z.getOneElement());
		assertTrue(ring0.contains(poly1));
		assertFalse(ring1.contains(poly1));

		Polynomial<WholeNumber> poly2 = Polynomial.getInstance(new WholeNumber[]{WholeNumber.getInstance(2), WholeNumber.getInstance(6), WholeNumber.getInstance(4)},
															   WholeNumber.getInstance(0), WholeNumber.getInstance(1));
		assertFalse(ring0.contains(poly2));
	}

	@Test
	public void testAbstractApply() {
		// Z
		Z z = Z.getInstance();
		Polynomial poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
												  z.getIdentityElement(), z.getOneElement());
		Polynomial poly2 = Polynomial.getInstance(new ZElement[]{z.getElement(4), z.getElement(7), z.getElement(4), z.getElement(9)},
												  z.getIdentityElement(), z.getOneElement());
		PolynomialElement<WholeNumber> e1 = ring0.getElement(poly1);
		PolynomialElement<WholeNumber> e2 = ring0.getElement(poly2);
		PolynomialElement<WholeNumber> e3 = (PolynomialElement) e1.apply(e2);

		assertEquals(5, e3.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(7, e3.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(6, e3.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(9, e3.getValue().getCoefficient(3).getValue().getBigInteger().intValue());

		// ZMod
		ZMod zmod = (ZMod) ring1.getSemiRing();
		poly1 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(5), zmod.getElement(0), zmod.getElement(2)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(4), zmod.getElement(7), zmod.getElement(4), zmod.getElement(5)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		PolynomialElement<ResidueClass> e4 = ring1.getElement(poly1);
		PolynomialElement<ResidueClass> e5 = ring1.getElement(poly2);
		PolynomialElement<ResidueClass> e6 = (PolynomialElement) e4.apply(e5);

		assertEquals(2, e6.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(6, e6.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(5, e6.getValue().getCoefficient(3).getValue().getBigInteger().intValue());

		// ZMod Binary
		zmod = (ZMod) ring2.getSemiRing();
		poly1 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(0), zmod.getElement(1), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(0), zmod.getElement(0), zmod.getElement(1), zmod.getElement(0), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = (PolynomialElement) e4.apply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().getBigInteger().intValue());
	}

	@Test
	public void testAbstractMultiply() {
		// Z
		Z z = Z.getInstance();
		Polynomial poly1 = Polynomial.getInstance(new ZElement[]{z.getElement(1), z.getElement(0), z.getElement(2)},
												  z.getIdentityElement(), z.getOneElement());
		Polynomial poly2 = Polynomial.getInstance(new ZElement[]{z.getElement(4), z.getElement(7), z.getElement(4), z.getElement(9)},
												  z.getIdentityElement(), z.getOneElement());
		PolynomialElement<WholeNumber> e1 = ring0.getElement(poly1);
		PolynomialElement<WholeNumber> e2 = ring0.getElement(poly2);
		PolynomialElement<WholeNumber> e3 = (PolynomialElement) e1.multiply(e2);

		assertEquals(4, e3.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(7, e3.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(12, e3.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(23, e3.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
		assertEquals(8, e3.getValue().getCoefficient(4).getValue().getBigInteger().intValue());
		assertEquals(18, e3.getValue().getCoefficient(5).getValue().getBigInteger().intValue());

		// ZMod
		ZMod zmod = (ZMod) ring1.getSemiRing();
		poly1 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(1), zmod.getElement(0), zmod.getElement(2)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(4), zmod.getElement(7), zmod.getElement(4), zmod.getElement(9)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		PolynomialElement<ResidueClass> e4 = ring1.getElement(poly1);
		PolynomialElement<ResidueClass> e5 = ring1.getElement(poly2);
		PolynomialElement<ResidueClass> e6 = (PolynomialElement) e4.multiply(e5);

		assertEquals(4, e6.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(5, e6.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(2, e6.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().getBigInteger().intValue());
		assertEquals(4, e6.getValue().getCoefficient(5).getValue().getBigInteger().intValue());

		// ZMod Binary
		zmod = (ZMod) ring2.getSemiRing();
		poly1 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(0), zmod.getElement(1), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(0), zmod.getElement(1), zmod.getElement(1), zmod.getElement(0), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = (PolynomialElement) e4.multiply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(4).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(5).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(6).getValue().getBigInteger().intValue());

		zmod = (ZMod) ring2.getSemiRing();
		poly1 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(1), zmod.getElement(1), zmod.getElement(0), zmod.getElement(0), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		poly2 = Polynomial.getInstance(new ZModElement[]{zmod.getElement(0), zmod.getElement(0), zmod.getElement(0), zmod.getElement(0), zmod.getElement(0), zmod.getElement(0), zmod.getElement(1)},
									   zmod.getIdentityElement(), zmod.getOneElement());
		e4 = ring2.getElement(poly1);
		e5 = ring2.getElement(poly2);
		e6 = (PolynomialElement) e4.multiply(e5);

		assertEquals(0, e6.getValue().getCoefficient(0).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(1).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(2).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(3).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(4).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(5).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(6).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(7).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(8).getValue().getBigInteger().intValue());
		assertEquals(0, e6.getValue().getCoefficient(9).getValue().getBigInteger().intValue());
		assertEquals(1, e6.getValue().getCoefficient(10).getValue().getBigInteger().intValue());
	}

}
