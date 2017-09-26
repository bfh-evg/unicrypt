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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class PrimePairTest {

	@Test
	public void generalTest() {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				if (MathUtil.isPrime(i) && MathUtil.isPrime(j) && i != j) {
					PrimePair p = PrimePair.getInstance(i, j);
					assertFalse(p.isPrime());
					assertEquals(i * j, p.getValue().intValue());
					assertEquals(Math.min(i, j), p.getSmallerPrimeFactor().intValue());
					assertEquals(Math.max(i, j), p.getLargerPrimeFactor().intValue());
					assertEquals(2, p.getPrimeFactors().getLength());
					assertEquals((Integer) 1, p.getExponents().getAt(0));
					assertEquals((Integer) 1, p.getExponents().getAt(1));
					assertEquals(2, p.getExponents().getLength());
					assertEquals(p, PrimePair.getInstance(BigInteger.valueOf(i), BigInteger.valueOf(j)));
				} else {
					try {
						PrimePair p = PrimePair.getInstance(i, j);
						fail();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	@Test
	public void testGetRandomInstance_int() {
		try {
			PrimePair p = PrimePair.getRandomInstance(-1);
			fail();
		} catch (Exception e) {
		}
		try {
			PrimePair p = PrimePair.getRandomInstance(0);
			fail();
		} catch (Exception e) {
		}
		try {
			PrimePair p = PrimePair.getRandomInstance(1);
			fail();
		} catch (Exception e) {
		}
		for (int i = 1; i < 10; i++) {
			PrimePair p = PrimePair.getRandomInstance(2);
			assertFalse(p.isPrime());
			assertTrue(p.getValue().intValue() == 6);
		}
		for (int i = 1; i < 10; i++) {
			PrimePair p = PrimePair.getRandomInstance(3);
			assertFalse(p.isPrime());
			assertTrue(p.getValue().intValue() == 35);
		}
		for (int i = 1; i < 10; i++) {
			PrimePair p = PrimePair.getRandomInstance(4);
			assertFalse(p.isPrime());
			assertTrue(p.getValue().intValue() == 143);
		}
		for (int i = 1; i < 100; i++) {
			PrimePair p = PrimePair.getRandomInstance(5);
			assertFalse(p.isPrime());
			int v = p.getValue().intValue();
			assertTrue(v == 17 * 19 || v == 17 * 23 || v == 17 * 29 || v == 17 * 31 || v == 19 * 23 || v == 19 * 29 || v == 19 * 31 || v == 23 * 29 || v == 23 * 31 || v == 29 * 31);
		}
	}

}
