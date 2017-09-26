/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.math;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.prime.Factorization;
import com.squareup.jnagmp.Gmp;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

/**
 * This is a helper class with some static methods for various mathematical functions. By contract, the methods of this
 * class do not check the validity of the parameters.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public final class MathUtil {

	private static final int NUMBER_OF_PRIME_TESTS = 40;

	public static final BigInteger ZERO = BigInteger.valueOf(0);
	public static final BigInteger ONE = BigInteger.valueOf(1);
	public static final BigInteger TWO = BigInteger.valueOf(2);
	public static final BigInteger THREE = BigInteger.valueOf(3);
	public static final BigInteger FOUR = BigInteger.valueOf(4);
	public static final BigInteger FIVE = BigInteger.valueOf(5);
	public static final BigInteger SIX = BigInteger.valueOf(6);
	public static final BigInteger SEVEN = BigInteger.valueOf(7);
	public static final BigInteger EIGHT = BigInteger.valueOf(8);

	private static final byte[] BIT_MASKS = new byte[Byte.SIZE];
	private static final byte[] BIT_MASKS_INV = new byte[Byte.SIZE];

	static {
		for (int i = 0; i < Byte.SIZE; i++) {
			BIT_MASKS[i] = (byte) (1 << i);
			BIT_MASKS_INV[i] = (byte) ~(1 << i);
		}
	}

	/**
	 * GMP wrapper method for computing the greatest common divisor (gcd) of two integers {@code x} and {@code y}.
	 *
	 * @param x First integer
	 * @param y Second integer
	 * @return Greatest common divisor of {@code x} and {@code y}
	 */
	public static BigInteger gcd(BigInteger x, BigInteger y) {
		return Gmp.gcd(x, y);
	}

	/**
	 * GMP wrapper method for computing modular exponentiations for base {@code b>=0}, exponent {@code e>=0}, and
	 * modulus {@code m>0}.
	 *
	 * @param b The base
	 * @param e The exponent
	 * @param m The modulus
	 * @return {@code b^e mod m}
	 */
	public static BigInteger modExp(BigInteger b, BigInteger e, BigInteger m) {
		if (m.testBit(0)) {
			return Gmp.modPowSecure(b, e, m);
		} else {
			return Gmp.modPowInsecure(b, e, m); // Gmp.modPowSecure requires modulus to be odd
		}
	}

	/**
	 * GMP wrapper method for computing modular inverses of a positive integer {@code x>0} and a positive modulus
	 * {@code m>0}.
	 *
	 * @param x The value
	 * @param m The modulus
	 * @return {@code x^{.1} mod m}
	 */
	public static BigInteger modInv(BigInteger x, BigInteger m) {
		return Gmp.modInverse(x, m);
	}

	/**
	 * GMP wrapper method for computing the Legendre (Kronecker) symbol {@code (x/n)} of a non-negative integer
	 * {@code x>=0} and a prime number {@code p>2}.
	 * <p>
	 * @param x The given integer
	 * @param p The given odd prime number
	 * @return The Legendre symbol {@code (x/n)}
	 */
	public static int legendreSymbol(BigInteger x, BigInteger p) {
		return Gmp.kronecker(x, p);
	}

	/**
	 * Checks if a given integer value {@code x} is a quadratic residue modulo a given prime number {@code p}, for
	 * {@code 0<x<p}. In that case, {@code x} has corresponding square roots (modulo {@code p}).
	 * <p>
	 * @param x The integer value
	 * @param p The prime number
	 * @return {@code true} if {@code x} is a quadratic residue (modulo {@code p}), {@code false} otherwise
	 */
	public static boolean isQuadraticResidue(BigInteger x, BigInteger p) {
		return MathUtil.legendreSymbol(x, p) == 1;
	}

	/**
	 * Returns the value obtained from applying the Euler totient function to a positive integer {@code n}. For
	 * computing the result efficiently, the factorization of {@code n} must be specified.
	 * <p>
	 * @param factorization The factorization of {@code n}
	 * @return The result of applying the Euler totient function to {@code n}
	 * @see "Handbook of Applied Cryptography, Fact 2.101 (iii)"
	 */
	public static BigInteger eulerFunction(final Factorization factorization) {
		BigInteger product1 = ONE;
		BigInteger product2 = ONE;
		for (final BigInteger primeFactor : factorization.getPrimeFactors()) {
			product1 = product1.multiply(primeFactor);
			product2 = product2.multiply(primeFactor.subtract(ONE));
		}
		return factorization.getValue().multiply(product2).divide(product1);
	}

	/**
	 * Tests if a given integer value is a prime factor of another (positive) integer value.
	 * <p>
	 * @param value  The given integer value
	 * @param factor The potential prime factor
	 * @return {@code true} if {@code factor} is a prime factor of {@code value}, {@code false} otherwise
	 */
	public static boolean isPrimeFactor(final BigInteger value, final BigInteger factor) {
		return isPrime(factor) && value.mod(factor).equals(ZERO);
	}

	/**
	 * Tests if some given positive integer values are all prime factors of another (positive) integer value. The given
	 * list of prime factors needs not to be complete.
	 * <p>
	 * @param value   The given integer value
	 * @param factors The potential prime factors
	 * @return {@code true} if all values are prime factors of {@code value}, {@code false} otherwise
	 */
	public static boolean arePrimeFactors(final BigInteger value, final BigInteger... factors) {
		for (BigInteger factor : factors) {
			if (!isPrimeFactor(value, factor)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests if a given integer value is a prime number. This is a convenience method for
	 * {@link MathUtil#isPrime(java.math.BigInteger)}.
	 * <p>
	 * @param value A potential prime number
	 * @return {@code true} if {@code value} is prime, {@code false} otherwise
	 */
	public static boolean isPrime(final long value) {
		return MathUtil.isPrime(BigInteger.valueOf(value));
	}

	/**
	 * Tests if a given integer value is a prime number.
	 * <p>
	 * @param value A potential prime number
	 * @return {@code true} if {@code value} is prime, {@code false} otherwise
	 */
	public static boolean isPrime(final BigInteger value) {
		// BigInteger.isProbablePrime considers "negative primes" as primes
		return value.signum() > 0 && value.isProbablePrime(MathUtil.NUMBER_OF_PRIME_TESTS);
	}

	/**
	 * Tests if some given integers are all prime numbers.
	 * <p>
	 * @param values The potential prime numbers
	 * @return {@code true} if all values are prime numbers, {@code false} otherwise
	 */
	public static boolean arePrime(final BigInteger... values) {
		for (BigInteger value : values) {
			if (!isPrime(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests if a given integer value is a safe prime. This is a convenience method for
	 * {@link MathUtil#isSafePrime(java.math.BigInteger)}.
	 * <p>
	 * @param value A potential safe prime
	 * @return {@code true} if {@code value} is a safe prime, {@code false} otherwise
	 */
	public static boolean isSafePrime(final long value) {
		return MathUtil.isSafePrime(BigInteger.valueOf(value));
	}

	/**
	 * Tests if a given BigInteger value is a save prime.
	 * <p>
	 * @param value A potential safe prime
	 * @return {@code true} if {@code value} is a safe prime, {@code false} otherwise
	 */
	public static boolean isSafePrime(final BigInteger value) {
		return isPrime(value) && isPrime(value.subtract(ONE).divide(TWO));
	}

	/**
	 * Tests if two (positive) integer values are relatively prime.
	 * <p>
	 * @param value1 The first integer value
	 * @param value2 The second integer value
	 * @return {@code true} if the input values are relatively prime, {@code false} otherwise
	 */
	public static boolean areRelativelyPrime(BigInteger value1, BigInteger value2) {
		if (value1.signum() != 1 || value2.signum() != 1) {
			return false;
		}
		return MathUtil.gcd(value1, value2).equals(ONE);
	}

	/**
	 * Tests if some given (positive) integer values are pairwise relatively prime.
	 * <p>
	 * @param values The given integer values
	 * @return {@code true} if the input values are pairwise relatively prime, {@code false} otherwise
	 */
	public static boolean areRelativelyPrime(BigInteger... values) {
		for (int i = 0; i < values.length; i++) {
			for (int j = i + 1; j < values.length; j++) {
				if (!MathUtil.areRelativelyPrime(values[i], values[j])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Removes duplicates from a BigInteger array
	 * <p>
	 * @param values An array of BigInteger values
	 * @return The same array of BigInteger values without duplicates
	 */
	public static BigInteger[] removeDuplicates(final BigInteger... values) {
		final HashSet<BigInteger> hashSet = new HashSet<>(Arrays.asList(values));
		return hashSet.toArray(new BigInteger[hashSet.size()]);
	}

	/**
	 * Computes the factorial of some (non-negative) integer value. Returns 1 for input 0.
	 * <p>
	 * @param x The input value
	 * @return The factorial of {@code x}
	 */
	public static BigInteger factorial(final int x) {
		BigInteger result = ONE;
		for (int i = 1; i <= x; i++) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return result;
	}

	/**
	 * Computes the elegant pairing function for two non-negative BigInteger values. The mapping can be inverted using
	 * {@link MathUtil#unpair(java.math.BigInteger)}.
	 * <p>
	 * @see MathUtil#unpair(java.math.BigInteger)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param value1 The first value
	 * @param value2 The second value
	 * @return The result of applying the elegant pairing function
	 */
	public static BigInteger pair(BigInteger value1, BigInteger value2) {
		if (value1.compareTo(value2) < 0) {
			return value2.multiply(value2).add(value1);
		}
		return value1.multiply(value1).add(value1).add(value2);
	}

	/**
	 * Returns 0 for the special case of an empty list.
	 * <p>
	 * @return 0
	 */
	public static BigInteger pair() {
		return MathUtil.ZERO;
	}

	/**
	 * Computes the elegant pairing function for a given list of non-negative {@code long} values. This is a convenience
	 * method for {@link MathUtil#pair(java.math.BigInteger...)}.
	 * <p>
	 * @param values The given values
	 * @return The result of applying the elegant pairing function
	 * @see MathUtil#pair(java.math.BigInteger...)
	 */
	public static BigInteger pair(long... values) {
		BigInteger[] bigIntegers = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			bigIntegers[i] = BigInteger.valueOf(values[i]);
		}
		return MathUtil.pair(bigIntegers);
	}

	/**
	 * Computes the elegant pairing function for a given list of non-negative BigInteger values. The order in which the
	 * binary pairing function is applied is recursively from left to right. The mapping can be inverted using
	 * {@link MathUtil#unpair(java.math.BigInteger, int)}.
	 * <p>
	 * @see MathUtil#unpair(java.math.BigInteger, int)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param values The given values
	 * @return The result of applying the elegant pairing function
	 */
	public static BigInteger pair(BigInteger... values) {
		int length = values.length;
		if (length == 0) {
			return ZERO;
		}
		if (length == 1) {
			return values[0];
		}
		BigInteger[] newValues = new BigInteger[divideUp(length, 2)];
		for (int i = 0; i < length / 2; i++) {
			newValues[i] = pair(values[2 * i], values[2 * i + 1]);
		}
		if (length % 2 == 1) {
			newValues[length / 2] = values[length - 1];
		}
		return pair(newValues);
	}

	/**
	 * Returns 0 for the special case of an empty list.
	 * <p>
	 * @return 0
	 */
	public static BigInteger pairWithSize() {
		return MathUtil.ZERO;
	}

	/**
	 * Computes the elegant pairing function for a given list of non-negative {@code long} values. This is a convenience
	 * method for {@link MathUtil#pairWithSize(java.math.BigInteger...)}.
	 * <p>
	 * @param values The given values
	 * @return The result of applying the elegant pairing function
	 * @see MathUtil#pair(java.math.BigInteger...)
	 */
	public static BigInteger pairWithSize(long... values) {
		BigInteger[] bigIntegers = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			bigIntegers[i] = BigInteger.valueOf(values[i]);
		}
		return MathUtil.pairWithSize(bigIntegers);
	}

	/**
	 * Computes the elegant pairing function for a given list of non-negative BigInteger values. The size of the given
	 * input list is taken as an additional top-level input value. The value 0 is reserved for an empty list. The
	 * mapping can be inverted using {@link MathUtil#unpairWithSize(java.math.BigInteger)}.
	 * <p>
	 * @see MathUtil#unpairWithSize(java.math.BigInteger)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param values The given values
	 * @return The result of applying the elegant pairing function
	 */
	public static BigInteger pairWithSize(BigInteger... values) {
		if (values.length == 0) {
			return ZERO;
		}
		return pair(pair(values), BigInteger.valueOf(values.length - 1)).add(ONE);
	}

	/**
	 * Computes the inverse of the binary elegant pairing function for a given non-negative BigInteger value.
	 * <p>
	 * @see MathUtil#pair(java.math.BigInteger, java.math.BigInteger)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param value The input value
	 * @return An array containing the two resulting values
	 */
	public static BigInteger[] unpair(BigInteger value) {
		BigInteger x1 = sqrt(value);
		BigInteger x2 = value.subtract(x1.multiply(x1));
		if (x1.compareTo(x2) > 0) {
			return new BigInteger[]{x2, x1};
		}
		return new BigInteger[]{x1, x2.subtract(x1)};
	}

	/**
	 * Computes the inverse of the n-ary elegant pairing function for a given non-negative BigInteger value.
	 * <p>
	 * @see MathUtil#pair(java.math.BigInteger...)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param value The input value
	 * @param n     The number of resulting values
	 * @return An array containing the resulting values
	 */
	public static BigInteger[] unpair(BigInteger value, int n) {
		BigInteger[] result = new BigInteger[n];
		if (n > 0) {
			MathUtil.unpair(value, n, 0, result);
		}
		return result;
	}

	// This is a private helper method for doing the recursion
	private static void unpair(BigInteger value, int size, int start, BigInteger[] result) {
		if (size == 1) {
			result[start] = value;
		} else {
			BigInteger[] values = MathUtil.unpair(value);
			int powerOfTwo = 1 << BigInteger.valueOf(size - 1).bitLength() - 1;
			MathUtil.unpair(values[0], powerOfTwo, start, result);
			MathUtil.unpair(values[1], size - powerOfTwo, start + powerOfTwo, result);
		}
	}

	/**
	 * Computes the inverse of the n-ary elegant pairing function for a given non-negative BigInteger value, where the
	 * size is included as an additional top-level input value.
	 * <p>
	 * @see MathUtil#pairWithSize(java.math.BigInteger...)
	 * @see <a href="http://szudzik.com/ElegantPairing.pdf">ElegantPairing.pdf</a>
	 * @param value The input value
	 * @return An array containing the resulting values
	 */
	public static BigInteger[] unpairWithSize(BigInteger value) {
		if (value.equals(ZERO)) {
			return new BigInteger[0];
		}
		BigInteger[] values = unpair(value.subtract(ONE));
		return unpair(values[0], values[1].intValue() + 1);
	}

	/**
	 * Applies the folding function to a given value of type {@code long}. This is a convenience method for
	 * {@link MathUtil#fold(java.math.BigInteger)}.
	 * <p>
	 * @param value The given long value
	 * @return The result of applying the folding function
	 */
	public static BigInteger fold(long value) {
		return fold(BigInteger.valueOf(value));
	}

	/**
	 * Applies the folding function to a given integer value. The result is a unique non-negative integer. The mapping
	 * can be inverted using {@link MathUtil#unfold(java.math.BigInteger)}.
	 * <p>
	 * @see MathUtil#unfold(java.math.BigInteger)
	 * @param value The given integer value
	 * @return The result of applying the folding function
	 */
	public static BigInteger fold(BigInteger value) {
		if (value.signum() >= 0) {
			return value.shiftLeft(1);
		}
		return value.negate().shiftLeft(1).subtract(ONE);
	}

	/**
	 * Applies the inverse of the folding function to a (non-negative) integer value.
	 * <p>
	 * @see MathUtil#fold(java.math.BigInteger)
	 * @param value The given integer value
	 * @return The result of applying the unfolding function
	 */
	public static BigInteger unfold(BigInteger value) {
		if (value.mod(TWO).equals(ZERO)) {
			return value.shiftRight(1);
		}
		return value.add(ONE).shiftRight(1).negate();
	}

	/**
	 * Computes the pairing of a list of arbitrary (positive and negative) integer values. It is a combination of
	 * {@link MathUtil#fold(java.math.BigInteger)} and {@link MathUtil#pair(java.math.BigInteger...)}. The mapping can
	 * be inverted using {@link MathUtil#unpairAndUnfold(java.math.BigInteger, int)}.
	 * <p>
	 * @see MathUtil#unpairAndUnfold(java.math.BigInteger, int)
	 * @param values The integer values
	 * @return The result of applying the pairing function
	 */
	public static BigInteger foldAndPair(BigInteger... values) {
		BigInteger[] foldedValues = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			foldedValues[i] = fold(values[i]);
		}
		return pair(foldedValues);
	}

	/**
	 * Computes the inverse of the binary pairing function for a arbitrary (positive and negative) integer values.
	 * <p>
	 * @param value The integer value
	 * @return An array containing the resulting two values
	 */
	public static BigInteger[] unpairAndUnfold(BigInteger value) {
		return unpairAndUnfold(value, 2);
	}

	/**
	 * Computes the inverse of the n-ary pairing function for a arbitrary (positive and negative) integer values.
	 * <p>
	 * @see MathUtil#foldAndPair(java.math.BigInteger...)
	 * @param value The integer value
	 * @param n     The number of resulting values
	 * @return An array containing the resulting values
	 */
	public static BigInteger[] unpairAndUnfold(BigInteger value, int n) {
		BigInteger[] result = new BigInteger[n];
		BigInteger[] values = unpair(value, n);
		for (int i = 0; i < n; i++) {
			result[i] = unfold(values[i]);
		}
		return result;
	}

	/**
	 * Computes {@code 2^e}.
	 * <p>
	 * @param e The given exponent
	 * @return {@code 2^e}
	 */
	public static BigInteger powerOfTwo(int e) {
		return ONE.shiftLeft(e);
	}

	/**
	 * Computes the integer square root of a (non-negative) integer value using Newton's method.
	 * <p>
	 * @param x The integer value
	 * @return The integer square root {@code x}
	 */
	public static BigInteger sqrt(BigInteger x) {
		// special case
		if (x.signum() == 0) {
			return ZERO;
		}
		// first guess
		BigInteger current = powerOfTwo(x.bitLength() / 2 + 1);
		BigInteger last;
		do {
			last = current;
			current = last.add(x.divide(last)).shiftRight(1);
		} while (last.compareTo(current) > 0);
		return last;
	}

	/**
	 * Computes one of the two square roots of a given integer value {@code x} modulo a prime number {@code p}, using
	 * the Tonelli-Shanks algorithm, for {@code 0<x<p}. It is assumed that such square roots exist, i.e., that {@code x}
	 * is a quadratic residue (modulo {@code p}). For a sqaure root {@code r}, the second square root is {@code p-r}.
	 * <p>
	 * @param x The integer value
	 * @param p The prime modulo
	 * @return One of the two square roots of {@code x} (modulo {@code p})
	 */
	public static BigInteger sqrtModPrime(BigInteger x, BigInteger p) {

		// trivial cases
		if (p.equals(TWO)) {
			return ONE;
		}
		if (p.mod(FOUR).equals(THREE)) {
			return MathUtil.modExp(x, p.add(ONE).divide(FOUR), p);
		}

		// compute z, which must be a quadratic non-residue
		BigInteger z = TWO;

		while (isQuadraticResidue(z, p)) {
			z = z.add(ONE);
		}
		BigInteger s = ONE;
		BigInteger q = p.subtract(ONE).divide(TWO);

		// finding q
		while (q.mod(TWO).equals(ZERO)) {
			q = q.divide(TWO);
			s = s.add(ONE);
		}

		BigInteger c = MathUtil.modExp(z, q, p);
		BigInteger r = MathUtil.modExp(x, q.add(ONE).divide(TWO), p);
		BigInteger t = MathUtil.modExp(x, q, p);
		BigInteger m = s;

		// loop until t=1
		while (!t.equals(ONE)) {
			BigInteger i = ZERO;
			while (!ONE.equals(MathUtil.modExp(t, MathUtil.modExp(TWO, i, p), p))) {
				i = i.add(ONE);
			}

			BigInteger b = MathUtil.modExp(c, MathUtil.modExp(TWO, m.subtract(i).subtract(ONE), p), p);
			r = r.multiply(b).mod(p);
			t = t.multiply(b.pow(2)).mod(p);
			c = MathUtil.modExp(b, TWO, p);
			m = i;
		}

		return r;
	}

	/**
	 * Converts the 8 rightmost bits of an integer into a byte.
	 * <p>
	 * @param integer The given integer
	 * @return The corresponding byte
	 */
	public static byte getByte(int integer) {
		return (byte) (integer & 0xFF);
	}

	/**
	 * Converts an integer into a byte array in big-endian byte order.
	 * <p>
	 * @param integer The given integer
	 * @return The corresponding byte array
	 */
	public static ByteArray getByteArray(int integer) {
		byte byte0 = MathUtil.getByte(integer);
		byte byte1 = MathUtil.getByte(integer >>> Byte.SIZE);
		byte byte2 = MathUtil.getByte(integer >>> 2 * Byte.SIZE);
		byte byte3 = MathUtil.getByte(integer >>> 3 * Byte.SIZE);
		return ByteArray.getInstance(byte3, byte2, byte1, byte0);
	}

	/**
	 * Returns the {@code i}-th bit of a byte.
	 * <p>
	 * @param b The given byte
	 * @param i The index
	 * @return The bit at index {@code i}
	 */
	public static boolean getBit(byte b, int i) {
		return and(b, BIT_MASKS[i]) != 0;
	}

	/**
	 * Sets the {@code i}-th bit of a byte to 1.
	 * <p>
	 * @param b The given byte
	 * @param i The index
	 * @return The resulting byte
	 */
	public static byte setBit(byte b, int i) {
		return or(b, BIT_MASKS[i]);
	}

	/**
	 * Sets the {@code i}-th bit of a byte to 0.
	 * <p>
	 * @param b The given byte
	 * @param i The index
	 * @return The resulting byte
	 */
	public static byte clearBit(byte b, int i) {
		return and(b, BIT_MASKS_INV[i]);
	}

	/**
	 * Replaces the {@code i}-th bit of a byte.
	 * <p>
	 * @param b   The given byte
	 * @param i   The index
	 * @param bit The new bit
	 * @return The resulting byte
	 */
	public static byte replaceBit(byte b, int i, boolean bit) {
		if (bit) {
			return setBit(b, i);
		} else {
			return clearBit(b, i);
		}
	}

	/**
	 * Reverses the bit order of a given byte
	 * <p>
	 * @param b The given byte
	 * @return The resulting byte
	 */
	public static byte reverse(byte b) {
		return (byte) (Integer.reverse(b & 0xFF) >>> (Integer.SIZE - Byte.SIZE));
	}

	/**
	 * Shifts the bits of a given byte to the left ({@code n} positions).
	 * <p>
	 * @param b The given byte
	 * @param n The number of positions to shift
	 * @return The resulting byte
	 */
	public static byte shiftLeft(byte b, int n) {
		return (byte) ((b & 0xFF) << n);
	}

	/**
	 * Shifts the bits of a given byte to the right ({@code n} positions).
	 * <p>
	 * @param b The given byte
	 * @param n The number of positions to shift
	 * @return The resulting byte
	 */
	public static byte shiftRight(byte b, int n) {
		return (byte) ((b & 0xFF) >>> n);
	}

	/**
	 * Applies the logical XOR operation to two bytes.
	 * <p>
	 * @param b1 The first byte
	 * @param b2 The second byte
	 * @return The resulting byte
	 */
	public static byte xor(byte b1, byte b2) {
		return (byte) ((b1 & 0xFF) ^ (b2 & 0xFF));
	}

	/**
	 * Applies the logical AND operation to two bytes.
	 * <p>
	 * @param b1 The first byte
	 * @param b2 The second byte
	 * @return The resulting byte
	 */
	public static byte and(byte b1, byte b2) {
		return (byte) ((b1 & 0xFF) & (b2 & 0xFF));
	}

	/**
	 * Applies the logical OR operation to two bytes.
	 * <p>
	 * @param b1 The first byte
	 * @param b2 The second byte
	 * @return The resulting byte
	 */
	public static byte or(byte b1, byte b2) {
		return (byte) ((b1 & 0xFF) | (b2 & 0xFF));
	}

	/**
	 * Applies the logical NOT operation to a given byte.
	 * <p>
	 * @param b The given byte
	 * @return The resulting byte
	 */
	public static byte not(byte b) {
		return (byte) ~(b & 0xFF);
	}

	/**
	 * Computes the remainder of the Euclidean division applied to two (positive or negative) integers.
	 * <p>
	 * @param x The given dividend
	 * @param y The given divisor
	 * @return The remainder of the Euclidean division
	 */
	public static int modulo(int x, int y) {
		y = Math.abs(y);
		return (x % y + y) % y;
	}

	/**
	 * Computes the quotient of the Euclidean division applied to two (positive or negative) integers.
	 * <p>
	 * @param x The given dividend
	 * @param y The given divisor
	 * @return The quotient of the Euclidean division
	 */
	public static int divide(int x, int y) {
		return (x - modulo(x, y)) / y;
	}

	/**
	 * Computes the quotient of the Euclidean division applied to two (positive or negative) BigIntger values.
	 * <p>
	 * @param x The given dividend
	 * @param y The given divisor
	 * @return The quotient of the Euclidean division
	 */
	public static BigInteger divide(BigInteger x, BigInteger y) {
		return x.subtract(x.mod(y)).divide(y);
	}

	/**
	 * Computes the quotient of two (positive or negative) integers and rounds up the result to the next integer.
	 * <p>
	 * @param x The given dividend
	 * @param y The given divisor
	 * @return The quotient rounded up to the next integer
	 */
	public static int divideUp(int x, int y) {
		if (y < 0) {
			return divideUp(-x, -y);
		}
		return divide(x + y - 1, y);
	}

	/**
	 * Computes the quotient of two (positive or negative) BigInteger values and rounds up the result to the next
	 * MathUtil. value.
	 * <p>
	 * @param x The given dividend
	 * @param y The given divisor
	 * @return The quotient rounded up to the next integer
	 */
	public static BigInteger divideUp(BigInteger x, BigInteger y) {
		if (y.signum() < 0) {
			return divideUp(x.negate(), y.negate());
		}
		return divide(x.add(y).subtract(ONE), y);
	}

}
