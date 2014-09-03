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
package ch.bfh.unicrypt.helper;

import ch.bfh.unicrypt.helper.array.ByteArray;
import java.util.HashMap;
import static org.junit.Assert.assertArrayEquals;
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
		p0 = Polynomial.getInstance(new Integer[]{}, 0, 1);
		p1 = Polynomial.getInstance(new Integer[]{1, 2, 0, 3}, 0, 1);
		p2 = Polynomial.getInstance(new HashMap(), 0, 1);
		HashMap map = new HashMap();
		map.put(0, 1);
		map.put(1, 2);
		map.put(3, 3);
		p3 = Polynomial.getInstance(map, 0, 1);

		p4 = Polynomial.getInstance(ByteArray.getInstance(), 0, 1);
		p5 = Polynomial.getInstance(ByteArray.getInstance(0x0b), 0, 1);  // 0000 1011 -> 1+x+x^3
		p6 = Polynomial.getInstance(ByteArray.getInstance(0xa8, 0xc4), 0, 1);  // 1100 0100 1010 1000
		p7 = Polynomial.getInstance(new Integer[]{1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0}, 0, 1);
	}

	@Test
	public void testToString() {
		//System.out.println(p0);
		assertEquals("Polynomial[f(x)=0]", p0.toString());
		//System.out.println(p1);
		assertEquals("Polynomial[f(x)=1+2X+3X^3]", p1.toString());
		//System.out.println(p2);
		assertEquals("Polynomial[f(x)=0]", p2.toString());
		//System.out.println(p3);
		assertEquals("Polynomial[f(x)=1+2X+3X^3]", p3.toString());
		//System.out.println(p4);
		assertEquals("Polynomial[f(x)=0]", p4.toString());
		//System.out.println(p5);
		assertEquals("Polynomial[f(x)=1+X+X^3]", p5.toString());
		//System.out.println(p6);
		assertEquals("Polynomial[f(x)=X^3+X^5+X^7+X^10+X^14+X^15]", p6.toString());

		//System.out.println(p7);
		assertEquals("Polynomial[f(x)=1+X^2+X^3+X^6+X^8+X^9+X^10+X^12]", p7.toString());

		//System.out.println(Polynomial.getInstance(ByteArray.getInstance(0x01)));
		assertEquals("Polynomial[f(x)=1]", Polynomial.getInstance(ByteArray.getInstance(0x01), 0, 1).toString());
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

		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, Polynomial.getInstance(ByteArray.getInstance(0x00), 0, 1).getDegree());
		assertEquals(Polynomial.ZERO_POLYNOMIAL_DEGREE, Polynomial.getInstance(new Integer[]{0}, 0, 1).getDegree());
		assertEquals(0, Polynomial.getInstance(new Integer[]{4}, 0, 1).getDegree());
		assertEquals(0, Polynomial.getInstance(ByteArray.getInstance(0x01), 0, 1).getDegree());
		assertEquals(1, Polynomial.getInstance(ByteArray.getInstance(0x02), 0, 1).getDegree());
		assertEquals(7, Polynomial.getInstance(ByteArray.getInstance(0xb4), 0, 1).getDegree());
		assertEquals(8, Polynomial.getInstance(ByteArray.getInstance(0xb4, 0x01), 0, 1).getDegree());
		assertEquals(15, Polynomial.getInstance(ByteArray.getInstance(0x01, 0xb4), 0, 1).getDegree());
	}

	@Test
	public void testIsBinary() {
		assertTrue(p0.isBinary());
		assertFalse(p1.isBinary());
		assertTrue(p2.isBinary());
		assertFalse(p3.isBinary());
		assertTrue(p4.isBinary());
		assertTrue(p5.isBinary());
		assertTrue(p6.isBinary());
		assertTrue(p7.isBinary());
	}

	@Test
	public void testIsZeroPolynomial() {
		assertTrue(p0.isZeroPolynomial());
		assertFalse(p1.isZeroPolynomial());
		assertTrue(p2.isZeroPolynomial());
		assertFalse(p3.isZeroPolynomial());
		assertTrue(p4.isZeroPolynomial());
		assertFalse(p5.isZeroPolynomial());
		assertFalse(p6.isZeroPolynomial());
		assertFalse(p7.isZeroPolynomial());
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
		} catch (UnsupportedOperationException e) {
		}
		assertArrayEquals(ByteArray.getInstance().getBytes(), p4.getCoefficients().getBytes());
		assertArrayEquals(ByteArray.getInstance(0x0b).getBytes(), p5.getCoefficients().getBytes());
		assertArrayEquals(ByteArray.getInstance(0x4d, 0x17).getBytes(), p7.getCoefficients().getBytes());

	}

	@Test
	public void testGetIndices() {
		assertArrayEquals(new Integer[]{}, p0.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{0, 1, 3}, p1.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{}, p2.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{0, 1, 3}, p3.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{}, p4.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{0, 1, 3}, p5.getIndices().getAll().toArray());
		assertArrayEquals(new Integer[]{3, 5, 7, 10, 14, 15}, p6.getIndices().getAll().toArray());
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
		assertTrue(p6.equals(Polynomial.getInstance(ByteArray.getInstance(0xa8, 0xc4), 0, 1)));
		assertFalse(p6.equals(Polynomial.getInstance(ByteArray.getInstance(0xa8, 0xc3), 0, 1)));
	}

}
