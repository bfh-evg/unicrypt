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
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialRingTest {

	private static final Z z = Z.getInstance();
	private static final ZModPrime zmod2 = ZModPrime.getInstance(2);
	private static final ZModPrime zmod5 = ZModPrime.getInstance(5);
	private static final ZModPrime zmod7 = ZModPrime.getInstance(7);

	private static final PolynomialRing ring0 = PolynomialRing.getInstance(z);   // Z
	private static final PolynomialRing ring2 = PolynomialRing.getInstance(zmod2);  // ZMod Binary
	private static final PolynomialRing ring5 = PolynomialRing.getInstance(zmod5);  // ZMod
	private static final PolynomialRing ring7 = PolynomialRing.getInstance(zmod7);  // ZMod

	private static final BigInteger zero = BigInteger.valueOf(0);
	private static final BigInteger one = BigInteger.valueOf(1);

	@Test
	public void testInvert() {

		PolynomialElement p = ring0.getElement(Tuple.getInstance(z.getElement(0), z.getElement(1), z.getElement(2), z.getElement(3)));
		assertEquals(ring0.getZeroElement(), p.add(p.invert()));

		ZMod zmod = (ZMod) ring7.getSemiRing();
		PolynomialElement p1 = ring7.getElement(Tuple.getInstance(zmod.getElement(5), zmod.getElement(0), zmod.getElement(6), zmod.getElement(3)));
		assertEquals(ring7.getZeroElement(), p1.add(p1.invert()));

		zmod = (ZMod) ring2.getSemiRing();
		PolynomialElement p2 = ring2.getElement(Tuple.getInstance(zmod.getElement(0), zmod.getElement(1), zmod.getElement(1), zmod.getElement(1)));
		assertEquals(ring2.getZeroElement(), p2.add(p2.invert()));
	}

	@Test
	public void testLongDivision() {
		PolynomialElement f = ring5.getElement(Tuple.getInstance(zmod5.getZeroElement(), zmod5.getZeroElement(), zmod5.getElement(3), zmod5.getElement(2), zmod5.getElement(4), zmod5.getOneElement()));
		PolynomialElement g = ring5.getElement(Tuple.getInstance(zmod5.getElement(3), zmod5.getZeroElement(), zmod5.getOneElement()));

		Pair div = ring5.longDivision(f, g);
		assertEquals(div.getFirst(), ring5.getElement(Tuple.getInstance(zmod5.getOneElement(), zmod5.getElement(4), zmod5.getElement(4), zmod5.getOneElement())));
		assertEquals(div.getSecond(), ring5.getElement(Tuple.getInstance(zmod5.getElement(2), zmod5.getElement(3))));
	}

	@Test
	public void testEuclidean() {
		try {
			PolynomialElement p1 = ring2.getElementFrom(one, zero, zero, zero, one, one, one, zero, one, one, one);
			PolynomialElement p2 = ring2.getElementFrom(one, zero, one, one, zero, one, one, zero, zero, one, zero);

			PolynomialElement gcd = ring2.euclidean(p1, p2);
			assertEquals(ring2.getElementFrom(one, one, zero, one), gcd);
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testExtendedEuclidean() {

		try {
			PolynomialElement p1 = ring2.getElementFrom(one, zero, zero, zero, one, one, one, zero, one, one, one);
			PolynomialElement p2 = ring2.getElementFrom(one, zero, one, one, zero, one, one, zero, zero, one, zero);

			Triple euclid = ring2.extendedEuclidean(p1, p2);
			PolynomialElement d = ring2.getElementFrom(one, one, zero, one);
			PolynomialElement s = ring2.getElementFrom(zero, zero, zero, zero, one);
			PolynomialElement t = ring2.getElementFrom(one, one, one, one, one, one);
			assertEquals(d, euclid.getFirst());
			assertEquals(s, euclid.getSecond());
			assertEquals(t, euclid.getThird());
			assertEquals(d, p1.multiply(s).add(p2.multiply(t)));

			// gcd(1+2x^2, 1+4x+x^2+x^3) = 4 => 1
			p1 = ring5.getElementFrom(one, zero, BigInteger.valueOf(2));
			p2 = ring5.getElementFrom(one, BigInteger.valueOf(4), one, one);
			euclid = ring5.extendedEuclidean(p1, p2);
			d = (PolynomialElement) euclid.getFirst();
			s = (PolynomialElement) euclid.getSecond();
			t = (PolynomialElement) euclid.getThird();
			assertTrue(d.isOne());
			assertEquals(d, p1.multiply(s).add(p2.multiply(t)));
		} catch (UniCryptException ex) {
			fail();
		}
	}

	@Test
	public void testIsIrreduciblePolynomial() {
		try {
			PolynomialElement p = ring2.getElementFrom(one, one, zero, zero, one);
			assertTrue(ring2.isIrreduciblePolynomial(p));

			ZModPrime zmod3 = ZModPrime.getInstance(3);
			PolynomialRing ring3 = PolynomialRing.getInstance(zmod3);
			p = ring3.getElementFrom(zero, one);
			assertTrue(p.isIrreducible());

			p = ring3.getElementFrom(one, zero, BigInteger.valueOf(2), one);
			assertTrue(p.isIrreducible());
			p = ring3.getElementFrom(one, BigInteger.valueOf(2), zero, one);
			assertTrue(p.isIrreducible());
			p = ring3.getElementFrom(one, one, BigInteger.valueOf(2), one);
			assertTrue(p.isIrreducible());
			p = ring3.getElementFrom(BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(2), one);
			assertTrue(p.isIrreducible());
		} catch (UniCryptException ex) {
			fail();
		}

	}

	@Test
	public void testFindIrreduciblePolynomial() {
		PolynomialElement p = ring2.findIrreduciblePolynomial(4);
		assertTrue(ring2.isIrreduciblePolynomial(p));
		assertEquals(4, p.getValue().getDegree());
	}

}
