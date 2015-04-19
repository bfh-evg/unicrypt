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

import java.math.BigInteger;
import java.util.Arrays;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class FactorizationTest {

	public FactorizationTest() {
	}

	@Test
	public void testGetInstance_BigInteger() {
		Factorization f1 = Factorization.getInstance(BigInteger.valueOf(7));
		Assert.assertEquals(BigInteger.valueOf(7), f1.getValue());
		Assert.assertEquals(BigInteger.valueOf(7), f1.getPrimeFactors()[0]);
		Assert.assertEquals(1, f1.getExponents()[0]);
		try {
			Factorization f2 = Factorization.getInstance(BigInteger.valueOf(8));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetInstance_BigInteger_int() {
		{
			Factorization f1 = Factorization.getInstance(BigInteger.valueOf(7), 1);
			Assert.assertEquals(BigInteger.valueOf(7), f1.getValue());
			Assert.assertEquals(BigInteger.valueOf(7), f1.getPrimeFactors()[0]);
			Assert.assertEquals(1, f1.getExponents()[0]);
		}
		{
			Factorization f1 = Factorization.getInstance(BigInteger.valueOf(7), 2);
			Assert.assertEquals(BigInteger.valueOf(49), f1.getValue());
			Assert.assertEquals(BigInteger.valueOf(7), f1.getPrimeFactors()[0]);
			Assert.assertEquals(2, f1.getExponents()[0]);
		}
		try {
			Factorization f2 = Factorization.getInstance(BigInteger.valueOf(8), 1);
			fail();
		} catch (Exception e) {
		}
		try {
			Factorization f2 = Factorization.getInstance(BigInteger.valueOf(7), 0);
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetInstance_BigIntegerArr() {
		{
			Factorization f1 = Factorization.getInstance(BigInteger.valueOf(7), BigInteger.valueOf(3));
			Assert.assertEquals(BigInteger.valueOf(21), f1.getValue());
			Assert.assertFalse(-1 == Arrays.binarySearch(f1.getPrimeFactors(), BigInteger.valueOf(7)));
			Assert.assertFalse(-1 == Arrays.binarySearch(f1.getPrimeFactors(), BigInteger.valueOf(3)));
			Assert.assertTrue(-1 == Arrays.binarySearch(f1.getPrimeFactors(), BigInteger.valueOf(2)));
		}
		try {
			Factorization f2 = Factorization.getInstance(BigInteger.valueOf(7), BigInteger.valueOf(8));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetInstance_BigIntegerArr_intArr() {
		{
			Factorization f1 = Factorization.getInstance(new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(7)}, new int[]{2, 1});
			Assert.assertEquals(BigInteger.valueOf(63), f1.getValue());
		}
	}

	@Test
	public void testEquals() {
		{
			Factorization f1 = Factorization.getInstance(BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(3));
			Factorization f2 = Factorization.getInstance(BigInteger.valueOf(3), 3);
			Assert.assertEquals(f1, f2);
		}
		{
			Factorization f1 = Factorization.getInstance(BigInteger.valueOf(3), BigInteger.valueOf(7), BigInteger.valueOf(3));
			Factorization f2 = Factorization.getInstance(new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(7)}, new int[]{2, 1});
			Assert.assertEquals(f1, f2);
		}
	}

}
