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
package ch.bfh.unicrypt.helper.math;

import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import java.util.Arrays;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialTest {

	private static Polynomial<Integer> p0;
	private static Polynomial<Integer> p1;
	private static Polynomial<Integer> p2;
	private static Polynomial<Integer> p3;
	private static Polynomial<Integer> p4;
	private static Polynomial<Integer> p5;
	private static Polynomial<Integer> p6;
	private static Polynomial<Integer> p7;

	public PolynomialTest() {
		try {
			p0 = Polynomial.getInstance(new Integer[]{}, 0, 0);
			fail();
		} catch (Exception e) {
		}
		p0 = Polynomial.getInstance(new Integer[]{}, 0, 1);
		p1 = Polynomial.getInstance(new Integer[]{1, 2, 0, 3}, 0, 1);
		p2 = Polynomial.getInstance(new Integer[]{0, 0, 0}, 0, 1);

		HashMap map = new HashMap();
		map.put(0, 1);
		map.put(1, 2);
		map.put(3, 3);
		p3 = Polynomial.getInstance(map, 0, 1);
		map.put(-3, 4);
		try {
			p3 = Polynomial.getInstance(map, 0, 1);
			fail();
		} catch (Exception e) {
		}

		p4 = Polynomial.getInstance(BitArray.getInstance(), 0, 1); // p(x) = 0
		p5 = Polynomial.getInstance(BitArray.getInstance("1101"), 0, 1);  // p(x) = 1+x+x^3
		p6 = Polynomial.getInstance(BitArray.getInstance("0001010100100011"), 0, 1); // p(x) = x^3+x^5+x^7+x^10+x^14+x^15
		p7 = Polynomial.getInstance(new Integer[]{1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0}, 0, 1);
	}

	@Test
	public void testToString() {
		//System.out.println(p0);
		assertEquals("Polynomial[f(x)=0]", p0.toString());
		//System.out.println(p1);
		assertEquals("Polynomial[f(x)=1+2x+3x^3]", p1.toString());
		//System.out.println(p2);
		assertEquals("Polynomial[f(x)=0]", p2.toString());
		//System.out.println(p3);
		assertEquals("Polynomial[f(x)=1+2x+3x^3]", p3.toString());
		//System.out.println(p4);
		assertEquals("Polynomial[f(x)=0]", p4.toString());
		//System.out.println(p5);
		assertEquals("Polynomial[f(x)=1+x+x^3]", p5.toString());
		//System.out.println(p6);
		assertEquals("Polynomial[f(x)=x^3+x^5+x^7+x^10+x^14+x^15]", p6.toString());
		//System.out.println(p7);
		assertEquals("Polynomial[f(x)=1+x^2+x^3+x^6+x^8+x^9+x^10+x^12]", p7.toString());
		//System.out.println(Polynomial.getInstance(ByteArray.getInstance(0x01)));
		assertEquals("Polynomial[f(x)=1]", Polynomial.getInstance(BitArray.getInstance("1"), 0, 1).toString());
		//System.out.println(Polynomial.getInstance(new Integer[]{6}, 0, 1));
		assertEquals("Polynomial[f(x)=6]", Polynomial.getInstance(new Integer[]{6}, 0, 1).toString());
	}

	@Test
	public void testDegree() {
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, p0.getDegree());
		assertEquals(3, p1.getDegree());
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, p2.getDegree());
		assertEquals(3, p3.getDegree());
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, p4.getDegree());
		assertEquals(3, p5.getDegree());
		assertEquals(15, p6.getDegree());
		assertEquals(12, p7.getDegree());

		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, Polynomial.getInstance(BitArray.getInstance(""), 0, 1).getDegree());
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, Polynomial.getInstance(BitArray.getInstance("0"), 0, 1).getDegree());
		assertEquals(3, Polynomial.getInstance(BitArray.getInstance("001100"), 0, 1).getDegree());
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, Polynomial.getInstance(new Integer[]{0}, 0, 1).getDegree());
		assertEquals(0, Polynomial.getInstance(new Integer[]{4}, 0, 1).getDegree());
		assertEquals(0, Polynomial.getInstance(BitArray.getInstance("1"), 0, 1).getDegree());
		assertEquals(1, Polynomial.getInstance(BitArray.getInstance("01"), 0, 1).getDegree());
		assertEquals(7, Polynomial.getInstance(BitArray.getInstance("11100001"), 0, 1).getDegree());
		assertEquals(8, Polynomial.getInstance(BitArray.getInstance("110000011"), 0, 1).getDegree());
		assertEquals(13, Polynomial.getInstance(BitArray.getInstance("00110101001101"), 0, 1).getDegree());
		assertEquals(13, Polynomial.getInstance(BitArray.getInstance("0011010100110100000"), 0, 1).getDegree());
	}

	@Test
	public void testIsZeroPolynomial() {
		assertTrue(p0.isZero());
		assertFalse(p1.isZero());
		assertTrue(p2.isZero());
		assertFalse(p3.isZero());
		assertTrue(p4.isZero());
		assertFalse(p5.isZero());
		assertFalse(p6.isZero());
		assertFalse(p7.isZero());
	}

	@Test
	public void testIsMonic() {
		assertFalse(p0.isMonic());
		assertFalse(p1.isMonic());
		assertFalse(p2.isMonic());
		assertFalse(p3.isMonic());
		assertFalse(p4.isMonic());
		assertTrue(p5.isMonic());
		assertTrue(p6.isMonic());
		assertTrue(p7.isMonic());
	}

	@Test
	public void testGetCoefficient() {
		assertTrue(0 == p0.getCoefficient(0));

		assertTrue(1 == p1.getCoefficient(0));
		assertTrue(2 == p1.getCoefficient(1));
		assertTrue(0 == p1.getCoefficient(2));
		assertTrue(3 == p1.getCoefficient(3));
		assertTrue(0 == p1.getCoefficient(4));

		assertTrue(0 == p2.getCoefficient(0));

		assertTrue(1 == p3.getCoefficient(0));
		assertTrue(2 == p3.getCoefficient(1));
		assertTrue(0 == p3.getCoefficient(2));
		assertTrue(3 == p3.getCoefficient(3));
		assertTrue(0 == p3.getCoefficient(4));

		assertTrue(0 == p4.getCoefficient(0));

		assertTrue(1 == p5.getCoefficient(0));
		assertTrue(1 == p5.getCoefficient(1));
		assertFalse(1 == p5.getCoefficient(2));
		assertTrue(1 == p5.getCoefficient(3));
		assertFalse(1 == p5.getCoefficient(4));

		assertTrue(1 == p6.getCoefficient(7));
		assertFalse(1 == p6.getCoefficient(8));

		assertTrue(1 == p7.getCoefficient(0));
		assertTrue(0 == p7.getCoefficient(11));
		assertTrue(1 == p7.getCoefficient(12));
	}

	@Test
	public void getCoefficients() {
		try {
			p1.getCoefficients();
			fail();
		} catch (UniCryptRuntimeException e) {
		}
		assertEquals(BitArray.getInstance(), p4.getCoefficients());
		assertEquals(BitArray.getInstance("1101"), p5.getCoefficients());
		assertEquals(BitArray.getInstance("1011001011101"), p7.getCoefficients());
	}

	@Test
	public void testGetIndices() {
		for (int i : p0.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{}, i) == -1);
		}
		for (int i : p1.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{0, 1, 3}, i) == -1);
		}
		for (int i : p2.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{}, i) == -1);
		}
		for (int i : p3.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{0, 1, 3}, i) == -1);
		}
		for (int i : p4.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{}, i) == -1);
		}
		for (int i : p5.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{0, 1, 3}, i) == -1);
		}
		for (int i : p6.getCoefficientIndices()) {
			assertFalse(Arrays.binarySearch(new Integer[]{3, 5, 7, 10, 14, 15}, i) == -1);
		}
	}

	@Test
	public void testEquals() {
		assertTrue(p0.equals(p2));
		assertFalse(p0.equals(p1));
		assertTrue(p1.equals(p3));
		assertTrue(p0.equals(p4));
		assertFalse(p1.equals(p5));
		assertFalse(p4.equals(Polynomial.getInstance(new Integer[]{1}, 0, 1)));
		assertTrue(p5.equals(Polynomial.getInstance(new Integer[]{1, 1, 0, 1}, 0, 1)));
		assertFalse(p5.equals(Polynomial.getInstance(new Integer[]{1, 1, 0, 1, 1}, 0, 1)));
		assertFalse(p5.equals(Polynomial.getInstance(new Integer[]{1, 1}, 0, 1)));
		assertFalse(p6.equals(p5));
		assertTrue(p6.equals(Polynomial.getInstance(BitArray.getInstance("0001010100100011"), 0, 1)));
		assertTrue(p6.equals(Polynomial.getInstance(BitArray.getInstance("00010101001000110000"), 0, 1)));
		assertFalse(p6.equals(Polynomial.getInstance(BitArray.getInstance("0001010100100001"), 0, 1)));
	}

}
