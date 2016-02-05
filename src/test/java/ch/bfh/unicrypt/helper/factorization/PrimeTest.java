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
package ch.bfh.unicrypt.helper.factorization;

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
	public void testGetNextInstance_int() {
		try {
			Prime.getFirstInstance(1);
			fail();
		} catch (Exception e) {
		}
		assertEquals(2, Prime.getFirstInstance(2).getValue().intValue());
		assertEquals(5, Prime.getFirstInstance(3).getValue().intValue());
		assertEquals(11, Prime.getFirstInstance(4).getValue().intValue());
		assertEquals(17, Prime.getFirstInstance(5).getValue().intValue());
		assertEquals(37, Prime.getFirstInstance(6).getValue().intValue());
		assertEquals(67, Prime.getFirstInstance(7).getValue().intValue());
	}

	@Test
	public void testGetNextInstance_int_int() {
		try {
			Prime.getFirstInstance(1, 1);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getFirstInstance(2, 1);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getFirstInstance(1, 2);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getFirstInstance(3, 3);
			fail();
		} catch (Exception e) {
		}
		assertEquals(3, Prime.getFirstInstance(2, 2).getValue().intValue());
		assertEquals(5, Prime.getFirstInstance(3, 2).getValue().intValue());
		assertEquals(11, Prime.getFirstInstance(4, 2).getValue().intValue());
		assertEquals(11, Prime.getFirstInstance(4, 3).getValue().intValue());
		assertEquals(17, Prime.getFirstInstance(5, 2).getValue().intValue());
		assertEquals(31, Prime.getFirstInstance(5, 3).getValue().intValue());
		assertEquals(23, Prime.getFirstInstance(5, 4).getValue().intValue());
		assertEquals(37, Prime.getFirstInstance(6, 2).getValue().intValue());
		assertEquals(41, Prime.getFirstInstance(6, 3).getValue().intValue());
		try {
			Prime.getFirstInstance(6, 4);
			fail();
		} catch (Exception e) {
		}
		try {
			Prime.getFirstInstance(6, 5);
			fail();
		} catch (Exception e) {
		}
		assertEquals(67, Prime.getFirstInstance(7, 2).getValue().intValue());
		assertEquals(71, Prime.getFirstInstance(7, 3).getValue().intValue());
		assertEquals(67, Prime.getFirstInstance(7, 4).getValue().intValue());
		assertEquals(103, Prime.getFirstInstance(7, 5).getValue().intValue());
		try {
			Prime.getFirstInstance(7, 6);
			fail();
		} catch (Exception e) {
		}
	}

}
