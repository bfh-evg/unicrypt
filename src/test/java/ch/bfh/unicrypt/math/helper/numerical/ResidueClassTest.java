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
package ch.bfh.unicrypt.math.helper.numerical;

import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ResidueClassTest {

	private ResidueClass r1 = ResidueClass.getInstance(0, 1);

	private ResidueClass r23_1 = ResidueClass.getInstance(3, 23);
	private ResidueClass r23_2 = ResidueClass.getInstance(13, 23);
	private ResidueClass r23_3 = ResidueClass.getInstance(-5, 23);
	private ResidueClass r23_4 = ResidueClass.getInstance(0, 23);

	private ResidueClass r25_1 = ResidueClass.getInstance(3, 25);
	private ResidueClass r25_2 = ResidueClass.getInstance(9, 25);
	private ResidueClass r25_3 = ResidueClass.getInstance(-5, 25);

	public ResidueClassTest() {
	}

	@Test
	public void testGetModulus() {
		assertEquals(BigInteger.valueOf(1), r1.getModulus());
		assertEquals(BigInteger.valueOf(23), r23_1.getModulus());
		assertEquals(BigInteger.valueOf(25), r25_1.getModulus());
	}

	@Test
	public void testGetBigInteger() {
		assertEquals(0, r1.getBigInteger().intValue());
		assertEquals(3, r23_1.getBigInteger().intValue());
		assertEquals(13, r23_2.getBigInteger().intValue());
		assertEquals(18, r23_3.getBigInteger().intValue());
		assertEquals(0, r23_4.getBigInteger().intValue());
	}

	@Test
	public void testIsCompatible() {
		assertTrue(r1.isCompatible(r1));
		assertTrue(r23_1.isCompatible(r23_1));
		assertTrue(r23_1.isCompatible(r23_2));
		assertFalse(r23_1.isCompatible(r25_1));
	}

	@Test
	public void testAdd() {
		assertEquals(0, r1.add(r1).getBigInteger().intValue());
		assertEquals(6, r23_1.add(r23_1).getBigInteger().intValue());
		assertEquals(16, r23_1.add(r23_2).getBigInteger().intValue());
		assertEquals(3, r23_2.add(r23_2).getBigInteger().intValue());
	}

	@Test
	public void testMultiply() {
		assertEquals(0, r1.multiply(r1).getBigInteger().intValue());
		assertEquals(9, r23_1.multiply(r23_1).getBigInteger().intValue());
		assertEquals(16, r23_1.multiply(r23_2).getBigInteger().intValue());
		assertEquals(8, r23_2.multiply(r23_2).getBigInteger().intValue());
	}

	@Test
	public void testSqaure() {
		assertEquals(0, r1.square().getBigInteger().intValue());
		assertEquals(9, r23_1.square().getBigInteger().intValue());
		assertEquals(8, r23_2.square().getBigInteger().intValue());
		assertEquals(2, r23_3.square().getBigInteger().intValue());
		assertEquals(0, r23_4.square().getBigInteger().intValue());
	}

	@Test
	public void testSubtract() {
		assertEquals(0, r1.subtract(r1).getBigInteger().intValue());
		assertEquals(0, r23_1.subtract(r23_1).getBigInteger().intValue());
		assertEquals(13, r23_1.subtract(r23_2).getBigInteger().intValue());
		assertEquals(10, r23_2.subtract(r23_1).getBigInteger().intValue());
		assertEquals(0, r23_2.subtract(r23_2).getBigInteger().intValue());
		assertEquals(20, r23_4.subtract(r23_1).getBigInteger().intValue());
	}

	@Test
	public void testMinus() {
		assertEquals(0, r1.minus().getBigInteger().intValue());
		assertEquals(20, r23_1.minus().getBigInteger().intValue());
		assertEquals(10, r23_2.minus().getBigInteger().intValue());
		assertEquals(5, r23_3.minus().getBigInteger().intValue());
		assertEquals(0, r23_4.minus().getBigInteger().intValue());
	}

	@Test
	public void testPower() {
		assertEquals(0, r1.power(0).getBigInteger().intValue());
		assertEquals(0, r1.power(1).getBigInteger().intValue());
		assertEquals(0, r1.power(2).getBigInteger().intValue());
		assertEquals(1, r23_1.power(0).getBigInteger().intValue());
		assertEquals(3, r23_1.power(1).getBigInteger().intValue());
		assertEquals(9, r23_1.power(2).getBigInteger().intValue());
		assertEquals(4, r23_1.power(3).getBigInteger().intValue());
		assertEquals(4, r23_1.power(BigInteger.valueOf(3)).getBigInteger().intValue());
		assertEquals(4, r23_1.power(BigInteger.valueOf(3)).getBigInteger().intValue());
	}

	@Test
	public void testIsGreaterThan() {
	}

	@Test
	public void testIsSmallerThan() {
	}

	@Test
	public void testIsEquals() {
		assertTrue(r1.isEqual(r1));
		assertTrue(r23_1.isEqual(r23_1));
		assertTrue(r25_2.isEqual(r25_1.multiply(r25_1)));
		assertFalse(r23_1.isEqual(r23_2));
	}

	@Test
	public void testGetInstance() {
	}

}
