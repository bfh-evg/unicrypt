/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.prime;

import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class PrimeTest {

	@Test
	public void testPrecomputedPrimes() {
		for (int bits : new int[]{128, 160, 192, 224, 256, 384, 512, 768, 1024, 2048, 3072, 4096}) {

			Prime prime = Prime.getSmallestInstance(bits);
			assertEquals(bits, prime.getValue().bitLength());
			assertTrue(MathUtil.isPrime(prime.getValue()));
			
			prime = Prime.getLargestInstance(bits);
			assertEquals(bits, prime.getValue().bitLength());
			assertTrue(MathUtil.isPrime(prime.getValue()));
		}
	}

	@Test
	public void generalTest() {
		for (int i = 0; i < 100; i++) {
			if (MathUtil.isPrime(i)) {
				Prime p = Prime.getInstance(i);
				assertTrue(p.isPrime());
				assertEquals(i, p.getValue().intValue());
				assertEquals(i, p.getPrimeFactor().intValue());
				assertEquals(i, p.getPrimeFactors().getAt(0).intValue());
				assertEquals(1, p.getPrimeFactors().getLength());
				assertEquals(1, p.getExponent());
				assertEquals((Integer) 1, p.getExponents().getAt(0));
				assertEquals(1, p.getExponents().getLength());
				assertEquals(p, Prime.getInstance(BigInteger.valueOf(i)));
			} else {
				try {
					Prime p = Prime.getInstance(i);
					fail();
				} catch (Exception e) {
				}
			}
		}
	}

	@Test
	public void testGetRandomInstance_int() {
		try {
			Prime p = Prime.getRandomInstance(-1);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime p = Prime.getRandomInstance(0);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime p = Prime.getRandomInstance(1);
			fail();
		} catch (Exception e) {
		}
		for (int i = 1; i < 10; i++) {
			Prime p = Prime.getRandomInstance(2);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 2 || p.getValue().intValue() == 3);
		}
		for (int i = 1; i < 10; i++) {
			Prime p = Prime.getRandomInstance(3);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 5 || p.getValue().intValue() == 7);
		}
		for (int i = 1; i < 10; i++) {
			Prime p = Prime.getRandomInstance(4);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 11 || p.getValue().intValue() == 13);
		}
		for (int i = 1; i < 20; i++) {
			Prime p = Prime.getRandomInstance(5);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 17 || p.getValue().intValue() == 19 || p.getValue().intValue() == 23 || p.getValue().intValue() == 29 || p.getValue().intValue() == 31);
		}
	}

	@Test
	public void testGetSmallestInstance_int() {
		try {
			Prime.getSmallestInstance(1);
			fail();
		} catch (Exception e) {
		}
		assertEquals(2, Prime.getSmallestInstance(2).getValue().intValue());
		assertEquals(5, Prime.getSmallestInstance(3).getValue().intValue());
		assertEquals(11, Prime.getSmallestInstance(4).getValue().intValue());
		assertEquals(17, Prime.getSmallestInstance(5).getValue().intValue());
		assertEquals(37, Prime.getSmallestInstance(6).getValue().intValue());
		assertEquals(67, Prime.getSmallestInstance(7).getValue().intValue());
	}

	@Test
	public void testGetSmallestInstance_int_BigInteger() {
		try {
			Prime.getSmallestInstance(1, MathUtil.TWO);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getSmallestInstance(2, MathUtil.ONE);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getSmallestInstance(2, MathUtil.THREE);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getSmallestInstance(3, MathUtil.FIVE);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getSmallestInstance(4, MathUtil.SEVEN);
			fail();
		} catch (Exception e) {
		}
		assertEquals(3, Prime.getSmallestInstance(2, MathUtil.TWO).getValue().intValue());
		assertEquals(5, Prime.getSmallestInstance(3, MathUtil.TWO).getValue().intValue());
		assertEquals(7, Prime.getSmallestInstance(3, MathUtil.THREE).getValue().intValue());
		assertEquals(11, Prime.getSmallestInstance(4, MathUtil.TWO).getValue().intValue());
		assertEquals(13, Prime.getSmallestInstance(4, MathUtil.THREE).getValue().intValue());
		assertEquals(11, Prime.getSmallestInstance(4, MathUtil.FIVE).getValue().intValue());
		assertEquals(17, Prime.getSmallestInstance(5, MathUtil.TWO).getValue().intValue());
		assertEquals(31, Prime.getSmallestInstance(5, MathUtil.FIVE).getValue().intValue());
		assertEquals(23, Prime.getSmallestInstance(5, BigInteger.valueOf(11)).getValue().intValue());
		assertEquals(37, Prime.getSmallestInstance(6, MathUtil.TWO).getValue().intValue());
		assertEquals(41, Prime.getSmallestInstance(6, MathUtil.FIVE).getValue().intValue());
		try {
			Prime.getSmallestInstance(6, BigInteger.valueOf(11));
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getSmallestInstance(6, BigInteger.valueOf(17));
			fail();
		} catch (Exception e) {
		}
		assertEquals(67, Prime.getSmallestInstance(7, MathUtil.TWO).getValue().intValue());
		assertEquals(71, Prime.getSmallestInstance(7, MathUtil.FIVE).getValue().intValue());
		assertEquals(67, Prime.getSmallestInstance(7, BigInteger.valueOf(11)).getValue().intValue());
		assertEquals(103, Prime.getSmallestInstance(7, BigInteger.valueOf(17)).getValue().intValue());
		try {
			Prime.getSmallestInstance(7, BigInteger.valueOf(37));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetLargestInstance_int() {
		try {
			Prime.getSmallestInstance(1);
			fail();
		} catch (Exception e) {
		}
		assertEquals(3, Prime.getLargestInstance(2).getValue().intValue());
		assertEquals(7, Prime.getLargestInstance(3).getValue().intValue());
		assertEquals(13, Prime.getLargestInstance(4).getValue().intValue());
		assertEquals(31, Prime.getLargestInstance(5).getValue().intValue());
		assertEquals(61, Prime.getLargestInstance(6).getValue().intValue());
		assertEquals(127, Prime.getLargestInstance(7).getValue().intValue());
	}}
