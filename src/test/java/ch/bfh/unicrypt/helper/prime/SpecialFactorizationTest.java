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
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class SpecialFactorizationTest {

	@Test
	public void generalTest() {
		for (int i = 0; i <= 100; i++) {
			for (int e = 0; e <= 5; e++) {
				if (MathUtil.isPrime(i) && e >= 1 && (i != 2 || e <= 2)) {
					SpecialFactorization f = SpecialFactorization.getInstance(BigInteger.valueOf(i), e);
					assertEquals(BigInteger.valueOf(i).pow(e), f.getValue());
					assertEquals(BigInteger.valueOf(i), f.getPrimeFactor());
					assertEquals(e, f.getExponent());
					assertEquals(false, f.timesTwo());
					assertEquals(1, f.getPrimeFactors().getLength());
					assertEquals(1, f.getExponents().getLength());
					assertEquals(SpecialFactorization.getInstance(BigInteger.valueOf(i), e, false), f);
					if (MathUtil.isSafePrime(f.getValue())) {
						assertEquals(SafePrime.class, f.getClass());
					} else if (MathUtil.isPrime(f.getValue())) {
						assertEquals(Prime.class, f.getClass());
					}
				} else {
					try {
						SpecialFactorization f = SpecialFactorization.getInstance(BigInteger.valueOf(i), e);
						fail();
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	@Test
	public void generalTest2() {
		for (int i = 0; i <= 100; i++) {
			for (int e = 0; e <= 5; e++) {
				if (MathUtil.isPrime(i) && e >= 1 && (i != 2 || e <= 1)) {
					SpecialFactorization f = SpecialFactorization.getInstance(BigInteger.valueOf(i), e, true);
					assertEquals(BigInteger.valueOf(i).pow(e).multiply(BigInteger.valueOf(2)), f.getValue());
					assertEquals(BigInteger.valueOf(i), f.getPrimeFactor());
					if (i == 2) {
						assertEquals(false, f.timesTwo());
						assertEquals(e + 1, f.getExponent());
						assertEquals(1, f.getPrimeFactors().getLength());
						assertEquals(1, f.getExponents().getLength());
					} else {
						assertEquals(true, f.timesTwo());
						assertEquals(e, f.getExponent());
						assertEquals(2, f.getPrimeFactors().getLength());
						assertEquals(2, f.getExponents().getLength());
					}
					assertEquals(SpecialFactorization.getInstance(BigInteger.valueOf(i), e, true), f);
				} else {
					try {
						SpecialFactorization f = SpecialFactorization.getInstance(BigInteger.valueOf(i), e, true);
						fail();
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	@Test
	public void testGetInstance_BigInteger_int() {
	}

	@Test
	public void testGetInstance_BigInteger_boolean() {
	}

	@Test
	public void testGetInstance_3args() {
	}

	@Test
	public void testGetPrimeFactor() {
	}

	@Test
	public void testGetExponent() {
	}

	@Test
	public void testTimesTwo() {
	}

}
