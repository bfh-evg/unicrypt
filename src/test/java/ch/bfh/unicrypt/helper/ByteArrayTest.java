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
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayTest {

	public static ByteArray a0 = ByteArray.getInstance();
	public static ByteArray a1 = ByteArray.getInstance(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	public static ByteArray a2 = ByteArray.getInstance(15, false);
	public static ByteArray a3 = ByteArray.getInstance(20, true);
	public static ByteArray a4 = ByteArray.getInstance(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
	public static ByteArray a012 = ByteArray.getInstance(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	public static ByteArray a34 = ByteArray.getInstance(255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);

	public ByteArrayTest() {
	}

	@Test
	public void testGetBitOperations() {
		ByteArray b = ByteArray.getInstance("12|34"); // 00010010|00110100
		assertTrue(b.getBitAt(1));
		assertTrue(b.getBitAt(4));
		assertTrue(b.getBitAt(10));
		assertTrue(b.getBitAt(12));
		assertTrue(b.getBitAt(13));
		assertFalse(b.getBitAt(0));
		assertFalse(b.getBitAt(2));
		assertFalse(b.getBitAt(3));
		assertFalse(b.getBitAt(5));
		assertFalse(b.getBitAt(6));
		assertFalse(b.getBitAt(7));
		assertFalse(b.getBitAt(8));
		assertFalse(b.getBitAt(9));
		assertFalse(b.getBitAt(11));
		assertFalse(b.getBitAt(14));
		assertFalse(b.getBitAt(15));
		assertEquals(1, b.countLeadingZeros());
		assertEquals(2, b.countTrailingZeros());
		assertEquals(5, b.countBits());
		assertEquals(0, a0.countBits());
		assertEquals(15, a1.countBits());
		assertEquals(0, a2.countBits());
		assertEquals(160, a3.countBits());
		assertEquals(15, a4.countBits());
	}

	@Test
	public void testGetLength() {
		assertEquals(0, a0.getLength());
		assertEquals(10, a1.getLength());
		assertEquals(15, a2.getLength());
		assertEquals(20, a3.getLength());
		assertEquals(10, a4.getLength());
	}

	@Test
	public void testGetAll() {
	}

	@Test
	public void testGetAt() {
	}

	@Test
	public void testExtract_int() {
	}

	@Test
	public void testExtract_int_int() {
	}

	@Test
	public void testSplit() {
	}

	@Test
	public void testConcatenate() {
		ByteArray instance = ByteArray.getInstance(new byte[]{1, 2, 3, 4, 5, 6});
		ByteArray other = ByteArray.getInstance(new byte[]{7, 8, 9, 10});
		ByteArray expResult = ByteArray.getInstance(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});;
		ByteArray result = instance.concatenate(other);
		assertEquals(expResult, result);
	}

	@Test
	public void testXor() {
		ByteArray one = ByteArray.getInstance(new byte[]{1, 1, 1});
		ByteArray two = ByteArray.getInstance(new byte[]{0, 0, 0});
		ByteArray xored = one.xor(two);
		Assert.assertEquals(one, xored);

		ByteArray three = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(three);
		Assert.assertEquals(two, xored);

		ByteArray four = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(four);
		Assert.assertEquals(two, xored);

		ByteArray five = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(two, five);
		Assert.assertEquals(two, xored);

		xored = one.xor(one, two, two);
		Assert.assertEquals(two, xored);

		xored = xored.xor(one);
		Assert.assertEquals(one, xored);
	}

	@Test
	public void testAnd() {
	}

	@Test
	public void testOr() {
	}

	@Test
	public void testNot() {
	}

	@Test
	public void testIterator() {
	}

	@Test
	public void testEquals() {
	}

	@Test
	public void testGetInstance_int() {
	}

	@Test
	public void testGetInstance_int_boolean() {
	}

	@Test
	public void testGetInstance_byteArr() {
	}

	@Test
	public void testGetInstance_byteArrayArr() {
		ByteArray b012 = ByteArray.getInstance(a0, a1, a2);
		assertEquals(a012, b012);

		ByteArray b34 = ByteArray.getInstance(a3, a4);
		assertEquals(a34, b34);

		assertEquals(ByteArray.getInstance(a012, a34), ByteArray.getInstance(a0, a1, a2, a3, a4));
	}

	@Test
	public void testGetRandomInstance_int() {
	}

	@Test
	public void testGetRandomInstance_int_RandomNumberGenerator() {
	}

	@Test
	public void testCountLeadingZeros() {
		ByteArray b = ByteArray.getInstance("12|34"); // 00010010|00110100
		assertEquals(1, b.countLeadingZeros());
		b = ByteArray.getInstance("00|04"); // 00000000|00000100
		assertEquals(10, b.countLeadingZeros());
	}

	@Test
	public void testCountTrailingZeros() {
		ByteArray b = ByteArray.getInstance("12|34"); // 00010010|00110100
		assertEquals(2, b.countTrailingZeros());
		b = ByteArray.getInstance("00|04"); // 00000000|00000100
		assertEquals(5, b.countTrailingZeros());
	}

	@Test
	public void testShiftRight() {
		ByteArray b = ByteArray.getInstance("12|34"); // 00010010|00110100
		assertEquals(ByteArray.getInstance("09|1a"), b.shiftRight(1));
		assertEquals(ByteArray.getInstance("04|0d"), b.shiftRight(2));
		assertEquals(ByteArray.getInstance("82|06"), b.shiftRight(3));
		assertEquals(ByteArray.getInstance("41|03"), b.shiftRight(4));
		assertEquals(ByteArray.getInstance("a0|01"), b.shiftRight(5));
		assertEquals(ByteArray.getInstance("d0"), b.shiftRight(6));
		assertEquals(ByteArray.getInstance("68"), b.shiftRight(7));
		assertEquals(ByteArray.getInstance("34"), b.shiftRight(8));
		assertEquals(ByteArray.getInstance("1a"), b.shiftRight(9));
		assertEquals(ByteArray.getInstance("0d"), b.shiftRight(10));
		assertEquals(ByteArray.getInstance("06"), b.shiftRight(11));
		assertEquals(ByteArray.getInstance("03"), b.shiftRight(12));
		assertEquals(ByteArray.getInstance("01"), b.shiftRight(13));
		assertEquals(ByteArray.getInstance(""), b.shiftRight(14));
		assertEquals(ByteArray.getInstance(""), b.shiftRight(15));
	}

	@Test
	public void testShiftLeft() {
		ByteArray b = ByteArray.getInstance("02");
		assertEquals(ByteArray.getInstance("04"), b.shiftLeft(1));
		assertEquals(ByteArray.getInstance("08"), b.shiftLeft(2));
		assertEquals(ByteArray.getInstance("10"), b.shiftLeft(3));
		assertEquals(ByteArray.getInstance("20"), b.shiftLeft(4));
		assertEquals(ByteArray.getInstance("40"), b.shiftLeft(5));
		assertEquals(ByteArray.getInstance("80"), b.shiftLeft(6));
		assertEquals(ByteArray.getInstance("00|01"), b.shiftLeft(7));
		assertEquals(ByteArray.getInstance("00|02"), b.shiftLeft(8));
		assertEquals(ByteArray.getInstance("00|04"), b.shiftLeft(9));
		assertEquals(ByteArray.getInstance("00|08"), b.shiftLeft(10));
		assertEquals(ByteArray.getInstance("00|10"), b.shiftLeft(11));
		assertEquals(ByteArray.getInstance("00|20"), b.shiftLeft(12));
		assertEquals(ByteArray.getInstance("00|40"), b.shiftLeft(13));
		assertEquals(ByteArray.getInstance("00|80"), b.shiftLeft(14));
		assertEquals(ByteArray.getInstance("00|00|01"), b.shiftLeft(15));

		b = ByteArray.getInstance("80");
		assertEquals(ByteArray.getInstance("00|01"), b.shiftLeft(1));
	}

}
