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
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class MathUtilTest {

	private static final BigInteger MINUS_THREE = BigInteger.valueOf(-3);
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
	private static final BigInteger SIXTEEN = BigInteger.valueOf(16);
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
		Assert.assertEquals(3, MathUtil.removeDuplicates(TEN, TWO, SEVEN).length);
		Assert.assertEquals(3, MathUtil.removeDuplicates(TEN, TWO, SEVEN, SEVEN).length);
		Assert.assertEquals(1, MathUtil.removeDuplicates(SEVEN, SEVEN, SEVEN, SEVEN, SEVEN).length);
		Assert.assertEquals(2, MathUtil.removeDuplicates(SEVEN, TWO, SEVEN, SEVEN, SEVEN, TWO, SEVEN).length);
		Assert.assertEquals(1, MathUtil.removeDuplicates(NULL_VALUE, NULL_VALUE, NULL_VALUE).length);
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

	@Test
	public void testSqrt() {
		for (int i = 0; i < 1000; i++) {
			BigInteger n = BigInteger.valueOf(i);

			BigInteger s = MathUtil.sqrt(n);
			BigInteger sSquare = s.multiply(s);
			Assert.assertTrue(sSquare.compareTo(n) <= 0);

			BigInteger sPlusOne = s.add(ONE);
			BigInteger sPlusOneSquare = sPlusOne.multiply(sPlusOne);
			Assert.assertTrue(sPlusOneSquare.compareTo(n) > 0);
		}

		for (int i = 0; i < 1000; i++) {
			BigInteger n = BigInteger.valueOf(i);
			BigInteger nSquare = n.multiply(n);
			BigInteger result = MathUtil.sqrt(nSquare);
			Assert.assertEquals(n, result);
		}
	}

	@Test
	public void testAreRelativelyPrime_BigInteger_BigInteger() {
	}

	@Test
	public void testAreRelativelyPrime_BigIntegerArr() {
	}

	@Test
	public void testArePositive_BigIntegerArr() {
	}

	@Test
	public void testIsPositive() {
	}

	@Test
	public void testArePositive_intArr() {
	}

	@Test
	public void testMaxValue() {
	}

	@Test
	public void testPairUnpair() {
		for (int i = 0; i <= 5; i++) {
			BigInteger bi = BigInteger.valueOf(i);
			Assert.assertEquals(bi, MathUtil.unpair(MathUtil.pair(bi), 1)[0]);
			for (int j = 0; j <= 5; j++) {
				BigInteger bj = BigInteger.valueOf(j);
				Assert.assertEquals(bi, MathUtil.unpair(MathUtil.pair(bi, bj))[0]);
				Assert.assertEquals(bj, MathUtil.unpair(MathUtil.pair(bi, bj))[1]);
				Assert.assertEquals(bi, MathUtil.unpair(MathUtil.pair(bi, bj), 2)[0]);
				Assert.assertEquals(bj, MathUtil.unpair(MathUtil.pair(bi, bj), 2)[1]);
				for (int k = 0; k <= 5; k++) {
					BigInteger bk = BigInteger.valueOf(k);
					Assert.assertEquals(bi, MathUtil.unpair(MathUtil.pair(bi, bj, bk), 3)[0]);
					Assert.assertEquals(bj, MathUtil.unpair(MathUtil.pair(bi, bj, bk), 3)[1]);
					Assert.assertEquals(bk, MathUtil.unpair(MathUtil.pair(bi, bj, bk), 3)[2]);
				}
			}
		}
	}

	@Test
	public void testFoldPairUnpairUnfold() {
		for (int i = -5; i <= 5; i++) {
			BigInteger bi = BigInteger.valueOf(i);
			Assert.assertEquals(bi, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi), 1)[0]);
			for (int j = -5; j <= 5; j++) {
				BigInteger bj = BigInteger.valueOf(j);
				Assert.assertEquals(bi, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj))[0]);
				Assert.assertEquals(bj, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj))[1]);
				Assert.assertEquals(bi, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj), 2)[0]);
				Assert.assertEquals(bj, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj), 2)[1]);
				for (int k = -5; k <= 5; k++) {
					BigInteger bk = BigInteger.valueOf(k);
					Assert.assertEquals(bi, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj, bk), 3)[0]);
					Assert.assertEquals(bj, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj, bk), 3)[1]);
					Assert.assertEquals(bk, MathUtil.unpairAndUnfold(MathUtil.foldAndPair(bi, bj, bk), 3)[2]);
				}
			}
		}
	}

	@Test
	public void testPairUnPairWithSize() {
		for (int i = 0; i <= 5; i++) {
			BigInteger bi = BigInteger.valueOf(i);
			Assert.assertEquals(bi, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi))[0]);
			for (int j = 0; j <= 5; j++) {
				BigInteger bj = BigInteger.valueOf(j);
				Assert.assertEquals(bi, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi, bj))[0]);
				Assert.assertEquals(bj, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi, bj))[1]);
				for (int k = 0; k <= 5; k++) {
					BigInteger bk = BigInteger.valueOf(k);
					Assert.assertEquals(bi, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi, bj, bk))[0]);
					Assert.assertEquals(bj, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi, bj, bk))[1]);
					Assert.assertEquals(bk, MathUtil.unpairWithSize(MathUtil.pairWithSize(bi, bj, bk))[2]);
				}
			}
		}
	}

	@Test
	public void testFold() {
		Assert.assertEquals(ZERO, MathUtil.fold(ZERO));
		Assert.assertEquals(TWO, MathUtil.fold(ONE));
		Assert.assertEquals(FOUR, MathUtil.fold(TWO));
		Assert.assertEquals(SIX, MathUtil.fold(THREE));
		Assert.assertEquals(ONE, MathUtil.fold(MINUS_ONE));
		Assert.assertEquals(THREE, MathUtil.fold(MINUS_TWO));
		Assert.assertEquals(FIVE, MathUtil.fold(MINUS_THREE));
	}

	@Test
	public void testUnfold() {
		Assert.assertEquals(ZERO, MathUtil.unfold(ZERO));
		Assert.assertEquals(MINUS_ONE, MathUtil.unfold(ONE));
		Assert.assertEquals(ONE, MathUtil.unfold(TWO));
		Assert.assertEquals(MINUS_TWO, MathUtil.unfold(THREE));
		Assert.assertEquals(TWO, MathUtil.unfold(FOUR));
		Assert.assertEquals(MINUS_THREE, MathUtil.unfold(FIVE));
		Assert.assertEquals(THREE, MathUtil.unfold(SIX));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnfoldException() {
		MathUtil.unfold(MINUS_ONE);
	}

	@Test
	public void testFoldAndPair() {
	}

	@Test
	public void testUnpairAndUnfold_BigInteger() {
	}

	@Test
	public void testUnpairAndUnfold_BigInteger_int() {
	}

	@Test
	public void testPowerOfTwo() {
		Assert.assertEquals(ONE, MathUtil.powerOfTwo(0));
		Assert.assertEquals(TWO, MathUtil.powerOfTwo(1));
		Assert.assertEquals(FOUR, MathUtil.powerOfTwo(2));
		Assert.assertEquals(EIGHT, MathUtil.powerOfTwo(3));
		Assert.assertEquals(SIXTEEN, MathUtil.powerOfTwo(4));
		Assert.assertEquals(ONE.shiftLeft(100), MathUtil.powerOfTwo(100));

	}

	@Test
	public void testSqrtModPrime() {
		for (BigInteger p : new BigInteger[]{THREE, FIVE, SEVEN, BigInteger.valueOf(97)}) {
			for (int i = 1; i < p.intValue(); i++) {
				BigInteger bi = BigInteger.valueOf(i);
				BigInteger bi_square = bi.modPow(TWO, p);
				BigInteger bi_square_sqrt = MathUtil.sqrtModPrime(bi_square, p);
				Assert.assertEquals(bi_square, bi_square_sqrt.modPow(TWO, p));
			}
		}
	}

	@Test
	public void testHasSqrtModPrime() {
		for (BigInteger p : new BigInteger[]{THREE, FIVE, SEVEN, BigInteger.valueOf(97)}) {
			Set squares = new HashSet<BigInteger>();
			for (int i = 1; i < p.intValue(); i++) {
				BigInteger bi = BigInteger.valueOf(i);
				squares.add(bi.modPow(TWO, p));
			}
			for (int i = 1; i < p.intValue(); i++) {
				BigInteger bi = BigInteger.valueOf(i);
				Assert.assertEquals(squares.contains(bi), MathUtil.hasSqrtModPrime(bi, p));
			}
		}
	}

	@Test
	public void testGetByte() {
		long l = 0x0123456789ABCDEFL;
		byte[] bytes = {(byte) 0xEF, (byte) 0xCD, (byte) 0xAB, (byte) 0x89, (byte) 0x67, (byte) 0x45, (byte) 0x23, (byte) 0x01};
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(bytes[i], MathUtil.getByte(l, i));
		}
	}

	@Test
	public void testSetByte() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals((byte) 0xFF, MathUtil.getByte(MathUtil.setByte(l, i), i));
		}
	}

	@Test
	public void testClearByte() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals((byte) 0, MathUtil.getByte(MathUtil.clearByte(l, i), i));
		}
	}

	@Test
	public void testReplaceByte() {
		long l = 0x0123456789ABCDEFL;
		byte b = (byte) 0x5A;
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(b, MathUtil.getByte(MathUtil.replaceByte(l, i, b), i));
		}
	}

	@Test
	public void testFillWithByte() {
		byte b = (byte) 0x5A;
		long l = MathUtil.fillWithByte(b);
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(b, MathUtil.getByte(l, i));
		}
	}

	@Test
	public void testShiftBytesLeft() {
		long l = 0x0123456789ABCDEFL;
		Assert.assertEquals(0x0123456789ABCDEFL, MathUtil.shiftBytesLeft(l, 0));
		Assert.assertEquals(0x23456789ABCDEF00L, MathUtil.shiftBytesLeft(l, 1));
		Assert.assertEquals(0x456789ABCDEF0000L, MathUtil.shiftBytesLeft(l, 2));
		Assert.assertEquals(0x6789ABCDEF000000L, MathUtil.shiftBytesLeft(l, 3));
		Assert.assertEquals(0x89ABCDEF00000000L, MathUtil.shiftBytesLeft(l, 4));
		Assert.assertEquals(0xABCDEF0000000000L, MathUtil.shiftBytesLeft(l, 5));
		Assert.assertEquals(0xCDEF000000000000L, MathUtil.shiftBytesLeft(l, 6));
		Assert.assertEquals(0xEF00000000000000L, MathUtil.shiftBytesLeft(l, 7));
	}

	@Test
	public void testShiftBytesRight() {
		long l = 0x0123456789ABCDEFL;
		Assert.assertEquals(0x0123456789ABCDEFL, MathUtil.shiftBytesRight(l, 0));
		Assert.assertEquals(0x0123456789ABCDL, MathUtil.shiftBytesRight(l, 1));
		Assert.assertEquals(0x0123456789ABL, MathUtil.shiftBytesRight(l, 2));
		Assert.assertEquals(0x0123456789L, MathUtil.shiftBytesRight(l, 3));
		Assert.assertEquals(0x01234567L, MathUtil.shiftBytesRight(l, 4));
		Assert.assertEquals(0x012345L, MathUtil.shiftBytesRight(l, 5));
		Assert.assertEquals(0x0123L, MathUtil.shiftBytesRight(l, 6));
		Assert.assertEquals(0x01L, MathUtil.shiftBytesRight(l, 7));
	}

	@Test
	public void testByteMask() {
		for (int i = 0; i < 8; i++) {
			Assert.assertEquals(0xFFL << (i * 8), MathUtil.byteMask(i));
		}
	}

	@Test
	public void testGetBit() {
		long l0 = 0L;
		long l1 = 0xFFFFFFFFFFFFFFFFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(false, MathUtil.getBit(l0, i));
			Assert.assertEquals(true, MathUtil.getBit(l1, i));
		}
	}

	@Test
	public void testSetBit() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(true, MathUtil.getBit(MathUtil.setBit(l, i), i));
		}
	}

	@Test
	public void testClearBit() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(false, MathUtil.getBit(MathUtil.clearBit(l, i), i));
		}
	}

	@Test
	public void testReplaceBit() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(false, MathUtil.getBit(MathUtil.replaceBit(l, i, false), i));
			Assert.assertEquals(true, MathUtil.getBit(MathUtil.replaceBit(l, i, true), i));
		}
	}

	@Test
	public void testFillWithBit() {
		long l0 = MathUtil.fillWithBit(false);
		long l1 = MathUtil.fillWithBit(true);
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(false, MathUtil.getBit(l0, i));
			Assert.assertEquals(true, MathUtil.getBit(l1, i));
		}
	}

	@Test
	public void testShiftBitsLeft() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(l, MathUtil.shiftBitsLeft(0x0123456789ABCDEFL, i));
			l = l * 2;
		}
	}

	@Test
	public void testShiftBitsRight() {
		long l = 0x0123456789ABCDEFL;
		for (int i = 0; i < 64; i++) {
			Assert.assertEquals(l, MathUtil.shiftBitsRight(0x0123456789ABCDEFL, i));
			l = l / 2;
		}
	}

	@Test
	public void testBitMask() {
		Assert.assertEquals(0x1L, MathUtil.bitMask(0));
		Assert.assertEquals(0x2L, MathUtil.bitMask(1));
		Assert.assertEquals(0x4L, MathUtil.bitMask(2));
		Assert.assertEquals(0x8000000000000000L, MathUtil.bitMask(63));
	}

	@Test
	public void testXor() {
		long lp = 0x0123456789ABCDEFL;
		long ln = 0xFEDCBA9876543210L;
		long l0 = MathUtil.fillWithBit(false);
		long l1 = MathUtil.fillWithBit(true);
		for (long l : new long[]{lp, ln, l0, l1}) {
			Assert.assertEquals(l0, MathUtil.xor(l, l));
			Assert.assertEquals(l, MathUtil.xor(l, l0));
			Assert.assertEquals(l, MathUtil.xor(l0, l));
			Assert.assertEquals(MathUtil.not(l), MathUtil.xor(l, l1));
			Assert.assertEquals(MathUtil.not(l), MathUtil.xor(l1, l));
		}
		Assert.assertEquals(l1, MathUtil.xor(lp, ln));
		Assert.assertEquals(l1, MathUtil.xor(ln, lp));
	}

	@Test
	public void testAnd() {
		long lp = 0x0123456789ABCDEFL;
		long ln = 0xFEDCBA9876543210L;
		long l0 = MathUtil.fillWithBit(false);
		long l1 = MathUtil.fillWithBit(true);
		for (long l : new long[]{lp, ln, l0, l1}) {
			Assert.assertEquals(l, MathUtil.and(l, l));
			Assert.assertEquals(l0, MathUtil.and(l, l0));
			Assert.assertEquals(l0, MathUtil.and(l0, l));
			Assert.assertEquals(l, MathUtil.and(l, l1));
			Assert.assertEquals(l, MathUtil.and(l1, l));
		}
		Assert.assertEquals(l0, MathUtil.and(lp, ln));
		Assert.assertEquals(l0, MathUtil.and(ln, lp));
	}

	@Test
	public void testOr() {
		long lp = 0x0123456789ABCDEFL;
		long ln = 0xFEDCBA9876543210L;
		long l0 = MathUtil.fillWithBit(false);
		long l1 = MathUtil.fillWithBit(true);
		for (long l : new long[]{lp, ln, l0, l1}) {
			Assert.assertEquals(l, MathUtil.or(l, l));
			Assert.assertEquals(l, MathUtil.or(l, l0));
			Assert.assertEquals(l, MathUtil.or(l0, l));
			Assert.assertEquals(l1, MathUtil.or(l, l1));
			Assert.assertEquals(l1, MathUtil.or(l1, l));
		}
		Assert.assertEquals(l1, MathUtil.or(lp, ln));
		Assert.assertEquals(l1, MathUtil.or(ln, lp));
	}

	@Test
	public void testNot() {
		long lp = 0x0123456789ABCDEFL;
		long ln = 0xFEDCBA9876543210L;
		long l0 = MathUtil.fillWithBit(false);
		long l1 = MathUtil.fillWithBit(true);
		Assert.assertEquals(ln, MathUtil.not(lp));
		Assert.assertEquals(lp, MathUtil.not(ln));
		Assert.assertEquals(l1, MathUtil.not(l0));
		Assert.assertEquals(l0, MathUtil.not(l1));
	}

	@Test
	public void testModulo() {
		int n = 1;
		Assert.assertEquals(0, MathUtil.modulo(-5, n));
		Assert.assertEquals(0, MathUtil.modulo(-4, n));
		Assert.assertEquals(0, MathUtil.modulo(-3, n));
		Assert.assertEquals(0, MathUtil.modulo(-2, n));
		Assert.assertEquals(0, MathUtil.modulo(-1, n));
		Assert.assertEquals(0, MathUtil.modulo(0, n));
		Assert.assertEquals(0, MathUtil.modulo(1, n));
		Assert.assertEquals(0, MathUtil.modulo(2, n));
		Assert.assertEquals(0, MathUtil.modulo(3, n));
		Assert.assertEquals(0, MathUtil.modulo(4, n));
		Assert.assertEquals(0, MathUtil.modulo(5, n));
		n = 2;
		Assert.assertEquals(1, MathUtil.modulo(-5, n));
		Assert.assertEquals(0, MathUtil.modulo(-4, n));
		Assert.assertEquals(1, MathUtil.modulo(-3, n));
		Assert.assertEquals(0, MathUtil.modulo(-2, n));
		Assert.assertEquals(1, MathUtil.modulo(-1, n));
		Assert.assertEquals(0, MathUtil.modulo(0, n));
		Assert.assertEquals(1, MathUtil.modulo(1, n));
		Assert.assertEquals(0, MathUtil.modulo(2, n));
		Assert.assertEquals(1, MathUtil.modulo(3, n));
		Assert.assertEquals(0, MathUtil.modulo(4, n));
		Assert.assertEquals(1, MathUtil.modulo(5, n));
		n = 3;
		Assert.assertEquals(1, MathUtil.modulo(-5, n));
		Assert.assertEquals(2, MathUtil.modulo(-4, n));
		Assert.assertEquals(0, MathUtil.modulo(-3, n));
		Assert.assertEquals(1, MathUtil.modulo(-2, n));
		Assert.assertEquals(2, MathUtil.modulo(-1, n));
		Assert.assertEquals(0, MathUtil.modulo(0, n));
		Assert.assertEquals(1, MathUtil.modulo(1, n));
		Assert.assertEquals(2, MathUtil.modulo(2, n));
		Assert.assertEquals(0, MathUtil.modulo(3, n));
		Assert.assertEquals(1, MathUtil.modulo(4, n));
		Assert.assertEquals(2, MathUtil.modulo(5, n));
		n = 4;
		Assert.assertEquals(3, MathUtil.modulo(-5, n));
		Assert.assertEquals(0, MathUtil.modulo(-4, n));
		Assert.assertEquals(1, MathUtil.modulo(-3, n));
		Assert.assertEquals(2, MathUtil.modulo(-2, n));
		Assert.assertEquals(3, MathUtil.modulo(-1, n));
		Assert.assertEquals(0, MathUtil.modulo(0, n));
		Assert.assertEquals(1, MathUtil.modulo(1, n));
		Assert.assertEquals(2, MathUtil.modulo(2, n));
		Assert.assertEquals(3, MathUtil.modulo(3, n));
		Assert.assertEquals(0, MathUtil.modulo(4, n));
		Assert.assertEquals(1, MathUtil.modulo(5, n));
		n = 5;
		Assert.assertEquals(0, MathUtil.modulo(-5, n));
		Assert.assertEquals(1, MathUtil.modulo(-4, n));
		Assert.assertEquals(2, MathUtil.modulo(-3, n));
		Assert.assertEquals(3, MathUtil.modulo(-2, n));
		Assert.assertEquals(4, MathUtil.modulo(-1, n));
		Assert.assertEquals(0, MathUtil.modulo(0, n));
		Assert.assertEquals(1, MathUtil.modulo(1, n));
		Assert.assertEquals(2, MathUtil.modulo(2, n));
		Assert.assertEquals(3, MathUtil.modulo(3, n));
		Assert.assertEquals(4, MathUtil.modulo(4, n));
		Assert.assertEquals(0, MathUtil.modulo(5, n));
	}

	@Test
	public void testDivide() {
		int n = 1;
		Assert.assertEquals(-5, MathUtil.divide(-5, n));
		Assert.assertEquals(-4, MathUtil.divide(-4, n));
		Assert.assertEquals(-3, MathUtil.divide(-3, n));
		Assert.assertEquals(-2, MathUtil.divide(-2, n));
		Assert.assertEquals(-1, MathUtil.divide(-1, n));
		Assert.assertEquals(0, MathUtil.divide(0, n));
		Assert.assertEquals(1, MathUtil.divide(1, n));
		Assert.assertEquals(2, MathUtil.divide(2, n));
		Assert.assertEquals(3, MathUtil.divide(3, n));
		Assert.assertEquals(4, MathUtil.divide(4, n));
		Assert.assertEquals(5, MathUtil.divide(5, n));
		n = 2;
		Assert.assertEquals(-3, MathUtil.divide(-5, n));
		Assert.assertEquals(-2, MathUtil.divide(-4, n));
		Assert.assertEquals(-2, MathUtil.divide(-3, n));
		Assert.assertEquals(-1, MathUtil.divide(-2, n));
		Assert.assertEquals(-1, MathUtil.divide(-1, n));
		Assert.assertEquals(0, MathUtil.divide(0, n));
		Assert.assertEquals(0, MathUtil.divide(1, n));
		Assert.assertEquals(1, MathUtil.divide(2, n));
		Assert.assertEquals(1, MathUtil.divide(3, n));
		Assert.assertEquals(2, MathUtil.divide(4, n));
		Assert.assertEquals(2, MathUtil.divide(5, n));
		n = 3;
		Assert.assertEquals(-2, MathUtil.divide(-5, n));
		Assert.assertEquals(-2, MathUtil.divide(-4, n));
		Assert.assertEquals(-1, MathUtil.divide(-3, n));
		Assert.assertEquals(-1, MathUtil.divide(-2, n));
		Assert.assertEquals(-1, MathUtil.divide(-1, n));
		Assert.assertEquals(0, MathUtil.divide(0, n));
		Assert.assertEquals(0, MathUtil.divide(1, n));
		Assert.assertEquals(0, MathUtil.divide(2, n));
		Assert.assertEquals(1, MathUtil.divide(3, n));
		Assert.assertEquals(1, MathUtil.divide(4, n));
		Assert.assertEquals(1, MathUtil.divide(5, n));
		n = 4;
		Assert.assertEquals(-2, MathUtil.divide(-5, n));
		Assert.assertEquals(-1, MathUtil.divide(-4, n));
		Assert.assertEquals(-1, MathUtil.divide(-3, n));
		Assert.assertEquals(-1, MathUtil.divide(-2, n));
		Assert.assertEquals(-1, MathUtil.divide(-1, n));
		Assert.assertEquals(0, MathUtil.divide(0, n));
		Assert.assertEquals(0, MathUtil.divide(1, n));
		Assert.assertEquals(0, MathUtil.divide(2, n));
		Assert.assertEquals(0, MathUtil.divide(3, n));
		Assert.assertEquals(1, MathUtil.divide(4, n));
		Assert.assertEquals(1, MathUtil.divide(5, n));
		n = 5;
		Assert.assertEquals(-1, MathUtil.divide(-5, n));
		Assert.assertEquals(-1, MathUtil.divide(-4, n));
		Assert.assertEquals(-1, MathUtil.divide(-3, n));
		Assert.assertEquals(-1, MathUtil.divide(-2, n));
		Assert.assertEquals(-1, MathUtil.divide(-1, n));
		Assert.assertEquals(0, MathUtil.divide(0, n));
		Assert.assertEquals(0, MathUtil.divide(1, n));
		Assert.assertEquals(0, MathUtil.divide(2, n));
		Assert.assertEquals(0, MathUtil.divide(3, n));
		Assert.assertEquals(0, MathUtil.divide(4, n));
		Assert.assertEquals(1, MathUtil.divide(5, n));
	}

}
