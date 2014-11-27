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

import java.math.BigInteger;
import junit.framework.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class MathUtilTest {

	private static final BigInteger MINUS_TWO = BigInteger.valueOf(-2);
	private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);
	private static final BigInteger ZERO = BigInteger.valueOf(0);
	private static final BigInteger ONE = BigInteger.valueOf(1);
	private static final BigInteger TWO = BigInteger.valueOf(2);
	private static final BigInteger THREE = BigInteger.valueOf(3);
	private static final BigInteger FOUR = BigInteger.valueOf(4);
	private static final BigInteger FIVE = BigInteger.valueOf(5);
	private static final BigInteger SIX = BigInteger.valueOf(6);
	private static final BigInteger SEVEN = BigInteger.valueOf(7);
	private static final BigInteger EIGHT = BigInteger.valueOf(8);
	private static final BigInteger NINE = BigInteger.valueOf(9);
	private static final BigInteger TEN = BigInteger.valueOf(10);
	private static final BigInteger[] EMPTY_ARRAY = new BigInteger[]{};
	private static final BigInteger[] NULL_ARRAY = null;
	private static final BigInteger NULL_VALUE = null;

	@Test
	public void testEulerFunction() {
		// first 10 values with correct prime factors
		Assert.assertEquals(MathUtil.eulerFunction(ONE, new BigInteger[]{}), ONE);
		Assert.assertEquals(MathUtil.eulerFunction(TWO, new BigInteger[]{TWO}), ONE);
		Assert.assertEquals(MathUtil.eulerFunction(THREE, new BigInteger[]{THREE}), TWO);
		Assert.assertEquals(MathUtil.eulerFunction(FOUR, new BigInteger[]{TWO}), TWO);
		Assert.assertEquals(MathUtil.eulerFunction(FIVE, new BigInteger[]{FIVE}), FOUR);
		Assert.assertEquals(MathUtil.eulerFunction(SIX, new BigInteger[]{TWO, THREE}), TWO);
		Assert.assertEquals(MathUtil.eulerFunction(SIX, TWO, THREE), TWO);
		Assert.assertEquals(MathUtil.eulerFunction(SEVEN, new BigInteger[]{SEVEN}), SIX);
		Assert.assertEquals(MathUtil.eulerFunction(EIGHT, new BigInteger[]{TWO}), FOUR);
		Assert.assertEquals(MathUtil.eulerFunction(NINE, new BigInteger[]{THREE}), SIX);
		Assert.assertEquals(MathUtil.eulerFunction(TEN, new BigInteger[]{TWO, FIVE}), FOUR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException1() {
		MathUtil.eulerFunction(NULL_VALUE, NULL_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException2() {
		MathUtil.eulerFunction(ONE, NULL_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException3() {
		MathUtil.eulerFunction(NULL_VALUE, EMPTY_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException4() {
		MathUtil.eulerFunction(ZERO, EMPTY_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException5() {
		MathUtil.eulerFunction(MINUS_ONE, EMPTY_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEulerFunctionException6() {
		MathUtil.eulerFunction(SIX, new BigInteger[]{THREE, NULL_VALUE, TWO});
	}

	@Test
	public void testArePrimeFactors() {
		Assert.assertTrue(MathUtil.arePrimeFactors(TEN));
		Assert.assertTrue(MathUtil.arePrimeFactors(TEN, EMPTY_ARRAY));
		Assert.assertTrue(MathUtil.arePrimeFactors(TEN, new BigInteger[]{TWO, FIVE}));
		Assert.assertTrue(MathUtil.arePrimeFactors(TEN, TWO, FIVE));
		Assert.assertTrue(MathUtil.arePrimeFactors(TEN, TWO, FIVE, FIVE));
		Assert.assertFalse(MathUtil.arePrimeFactors(TEN, TWO, SIX));
		Assert.assertFalse(MathUtil.arePrimeFactors(TEN, TWO, SEVEN));
		Assert.assertFalse(MathUtil.arePrimeFactors(NULL_VALUE, NULL_ARRAY));
		Assert.assertFalse(MathUtil.arePrimeFactors(NULL_VALUE, TEN));
		Assert.assertFalse(MathUtil.arePrimeFactors(TEN, NULL_ARRAY));
		Assert.assertFalse(MathUtil.arePrimeFactors(ZERO, EMPTY_ARRAY));
		Assert.assertFalse(MathUtil.arePrimeFactors(MINUS_ONE, EMPTY_ARRAY));
		Assert.assertFalse(MathUtil.arePrimeFactors(TEN, new BigInteger[]{TWO, NULL_VALUE, FIVE}));
	}

	@Test
	public void testArePrime() {
		Assert.assertTrue(MathUtil.arePrime());
		Assert.assertTrue(MathUtil.arePrime(EMPTY_ARRAY));
		Assert.assertTrue(MathUtil.arePrime(new BigInteger[]{TWO, FIVE}));
		Assert.assertTrue(MathUtil.arePrime(TWO, FIVE));
		Assert.assertTrue(MathUtil.arePrime(TWO, FIVE, FIVE));
		Assert.assertFalse(MathUtil.arePrime(TWO, SIX));
		Assert.assertFalse(MathUtil.arePrime(NULL_ARRAY));
		Assert.assertFalse(MathUtil.arePrime(new BigInteger[]{TWO, NULL_VALUE, FIVE}));
	}

	@Test
	public void testIsPrime() {
		Assert.assertFalse(MathUtil.isPrime(null));
		Assert.assertFalse(MathUtil.isPrime(ZERO));
		Assert.assertFalse(MathUtil.isPrime(ONE));
		Assert.assertTrue(MathUtil.isPrime(TWO));
		Assert.assertTrue(MathUtil.isPrime(THREE));
		Assert.assertTrue(MathUtil.isPrime(FIVE));
		Assert.assertFalse(MathUtil.isPrime(MINUS_ONE));
		Assert.assertFalse(MathUtil.isPrime(MINUS_TWO));
	}

	@Test
	public void testIsSavePrime() {
		Assert.assertFalse(MathUtil.isPrime(null));
		Assert.assertFalse(MathUtil.isSavePrime(ZERO));
		Assert.assertFalse(MathUtil.isSavePrime(ONE));
		Assert.assertFalse(MathUtil.isSavePrime(TWO));
		Assert.assertFalse(MathUtil.isSavePrime(THREE));
		Assert.assertFalse(MathUtil.isSavePrime(FOUR));
		Assert.assertTrue(MathUtil.isSavePrime(FIVE));
		Assert.assertFalse(MathUtil.isSavePrime(SIX));
		Assert.assertTrue(MathUtil.isSavePrime(SEVEN));
		Assert.assertFalse(MathUtil.isSavePrime(MINUS_ONE));
		Assert.assertFalse(MathUtil.isSavePrime(MINUS_TWO));
	}

	@Test
	public void testArePositiveBigInteger() {
		Assert.assertTrue(MathUtil.arePositive(EMPTY_ARRAY));
		Assert.assertTrue(MathUtil.arePositive(new BigInteger[]{TWO, FIVE}));
		Assert.assertTrue(MathUtil.arePositive(TWO, FIVE));
		Assert.assertTrue(MathUtil.arePositive(TWO, FIVE, FIVE));
		Assert.assertFalse(MathUtil.arePositive(ZERO));
		Assert.assertFalse(MathUtil.arePositive(MINUS_ONE));
		Assert.assertFalse(MathUtil.arePositive(NULL_ARRAY));
		Assert.assertFalse(MathUtil.arePositive(new BigInteger[]{TWO, NULL_VALUE, FIVE}));
	}

	@Test
	public void testPositive() {
		Assert.assertTrue(MathUtil.isPositive(ONE));
		Assert.assertTrue(MathUtil.isPositive(TWO));
		Assert.assertTrue(MathUtil.isPositive(THREE));
		Assert.assertFalse(MathUtil.isPositive(ZERO));
		Assert.assertFalse(MathUtil.isPositive(MINUS_ONE));
		Assert.assertFalse(MathUtil.isPositive(MINUS_TWO));
	}

	@Test
	public void testArePositiveInteger() {
		Assert.assertTrue(MathUtil.arePositive(new int[]{2, 5}));
		Assert.assertTrue(MathUtil.arePositive(2, 5));
		Assert.assertTrue(MathUtil.arePositive(2, 5, 5));
		Assert.assertFalse(MathUtil.arePositive(0));
		Assert.assertFalse(MathUtil.arePositive(-1));
	}

	@Test
	public void testRemoveDuplicates() {
		Assert.assertEquals(MathUtil.removeDuplicates(TEN, TWO, SEVEN).length, 3);
		Assert.assertEquals(MathUtil.removeDuplicates(TEN, TWO, SEVEN, SEVEN).length, 3);
		Assert.assertEquals(MathUtil.removeDuplicates(SEVEN, SEVEN, SEVEN, SEVEN, SEVEN).length, 1);
		Assert.assertEquals(MathUtil.removeDuplicates(SEVEN, TWO, SEVEN, SEVEN, SEVEN, TWO, SEVEN).length, 2);
		Assert.assertEquals(MathUtil.removeDuplicates(NULL_VALUE, NULL_VALUE, NULL_VALUE).length, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveDuplicatesException() {
		MathUtil.removeDuplicates(NULL_ARRAY);
	}

	@Test
	public void testFactorial() {
		Assert.assertEquals(MathUtil.factorial(0), ONE);
		Assert.assertEquals(MathUtil.factorial(1), ONE);
		Assert.assertEquals(MathUtil.factorial(2), TWO);
		Assert.assertEquals(MathUtil.factorial(3), SIX);
		Assert.assertEquals(MathUtil.factorial(4), BigInteger.valueOf(24));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactorialException() {
		MathUtil.factorial(-1);
	}

//  @Test
//  public void testIsPermutationVector() {
//    Assert.assertTrue(MathUtil.isPermutationVector());
//    Assert.assertTrue(MathUtil.isPermutationVector(0));
//    Assert.assertTrue(MathUtil.isPermutationVector(0,1,2,3,4,5));
//    Assert.assertTrue(MathUtil.isPermutationVector(5,2,1,0,3,4));
//    Assert.assertFalse(MathUtil.isPermutationVector(1));
//    Assert.assertFalse(MathUtil.isPermutationVector(1,1));
//    Assert.assertFalse(MathUtil.isPermutationVector(1,3));
//  }
//  @Test (expected=IllegalArgumentException.class)
//  public void testIsPermutationVectorException() {
//    MathUtil.isPermutationVector((int[]) null);
//  }
	@Test
	public void testMax() {
		Assert.assertEquals(MathUtil.maxValue(new BigInteger[]{ONE}), ONE);
		Assert.assertEquals(MathUtil.maxValue(new BigInteger[]{ONE, SEVEN, NINE, TWO}), NINE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxException1() {
		MathUtil.maxValue(NULL_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxException2() {
		MathUtil.maxValue(EMPTY_ARRAY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxException3() {
		MathUtil.maxValue(new BigInteger[]{ONE, NULL_VALUE, SEVEN});
	}

}
