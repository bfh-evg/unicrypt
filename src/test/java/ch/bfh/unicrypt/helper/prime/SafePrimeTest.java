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
public class SafePrimeTest {

	@Test
	public void testPrecomputedPrimes() {
		for (int bits : new int[]{128, 160, 192, 224, 256, 384, 512, 768, 1024, 2048, 3072, 4096}) {

			Prime prime = SafePrime.getSmallestInstance(bits);
			assertEquals(bits, prime.getValue().bitLength());
			assertTrue(MathUtil.isSafePrime(prime.getValue()));

			prime = SafePrime.getLargestInstance(bits);
			assertEquals(bits, prime.getValue().bitLength());
			assertTrue(MathUtil.isSafePrime(prime.getValue()));
		}
	}

	@Test
	public void generalTest() {
		for (int i = 0; i < 100; i++) {
			if (MathUtil.isSafePrime(i)) {
				SafePrime p = SafePrime.getInstance(i);
				assertTrue(p.isPrime());
				assertEquals(i, p.getValue().intValue());
				assertEquals(i, p.getPrimeFactor().intValue());
				assertEquals(i, p.getPrimeFactors().getAt(0).intValue());
				assertEquals(1, p.getPrimeFactors().getLength());
				assertEquals(1, p.getExponent());
				assertEquals((Integer) 1, p.getExponents().getAt(0));
				assertEquals(1, p.getExponents().getLength());
				assertEquals(p, SafePrime.getInstance(BigInteger.valueOf(i)));
			} else {
				try {
					SafePrime p = SafePrime.getInstance(i);
					fail();
				} catch (Exception e) {
				}
			}
		}
	}

	@Test
	public void testGetRandomInstance_int() {
		try {
			SafePrime p = SafePrime.getRandomInstance(-1);
			fail();
		} catch (Exception e) {
		}
		try {
			SafePrime p = SafePrime.getRandomInstance(0);
			fail();
		} catch (Exception e) {
		}
		try {
			SafePrime p = SafePrime.getRandomInstance(1);
			fail();
		} catch (Exception e) {
		}
		try {
			SafePrime p = SafePrime.getRandomInstance(2);
			fail();
		} catch (Exception e) {
		}
		for (int i = 1; i < 10; i++) {
			SafePrime p = SafePrime.getRandomInstance(3);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 5 || p.getValue().intValue() == 7);
		}
		for (int i = 1; i < 10; i++) {
			SafePrime p = SafePrime.getRandomInstance(4);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 11);
		}
		for (int i = 1; i < 10; i++) {
			SafePrime p = SafePrime.getRandomInstance(5);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 23);
		}
		for (int i = 1; i < 10; i++) {
			SafePrime p = SafePrime.getRandomInstance(6);
			assertTrue(p.isPrime());
			assertTrue(p.getValue().intValue() == 47 || p.getValue().intValue() == 59);
		}
	}

	@Test
	public void testGetSmallelstInstance_int() {
		try {
			SafePrime p = SafePrime.getSmallestInstance(1);
			fail();
		} catch (Exception e) {
		}
		try {
			SafePrime p = SafePrime.getSmallestInstance(2);
			fail();
		} catch (Exception e) {
		}
		SafePrime p;
		p = SafePrime.getSmallestInstance(3);
		assertEquals(5, p.getValue().intValue());

		p = SafePrime.getSmallestInstance(4);
		assertEquals(11, p.getValue().intValue());

		p = SafePrime.getSmallestInstance(5);
		assertEquals(23, p.getValue().intValue());

		p = SafePrime.getSmallestInstance(6);
		assertEquals(47, p.getValue().intValue());

		p = SafePrime.getSmallestInstance(7);
		assertEquals(83, p.getValue().intValue());
	}

	@Test
	public void testGetLargestInstance_int() {
		try {
			SafePrime p = SafePrime.getLargestInstance(1);
			fail();
		} catch (Exception e) {
		}
		try {
			SafePrime p = SafePrime.getLargestInstance(2);
			fail();
		} catch (Exception e) {
		}
		SafePrime p;
		p = SafePrime.getLargestInstance(3);
		assertEquals(7, p.getValue().intValue());

		p = SafePrime.getLargestInstance(4);
		assertEquals(11, p.getValue().intValue());

		p = SafePrime.getLargestInstance(5);
		assertEquals(23, p.getValue().intValue());

		p = SafePrime.getLargestInstance(6);
		assertEquals(59, p.getValue().intValue());

		p = SafePrime.getLargestInstance(7);
		assertEquals(107, p.getValue().intValue());
	}

}
