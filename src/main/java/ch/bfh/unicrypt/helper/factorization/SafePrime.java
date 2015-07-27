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
package ch.bfh.unicrypt.helper.factorization;

import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.random.classes.RandomNumberGenerator;
import java.math.BigInteger;

/**
 * Each instance of this class represents a safe prime number, i.e., a value {@code p} such that {@code (p-1)/2} is
 * prime. This class is a specialization of {@link Prime}.
 * <p>
 * @see Prime
 * @author R. Haenni
 * @version 2.0
 */
public class SafePrime
	   extends Prime {

	private static final long serialVersionUID = 1L;

	protected SafePrime(BigInteger safePrime) {
		super(safePrime);
	}

	/**
	 * Creates a new safe prime from a given integer value of type {@code int}. This method is a convenience method for
	 * {@link SafePrime#getInstance(java.math.BigInteger)}. Throws an exception if the given integer is not a safe
	 * prime.
	 * <p>
	 * @param safePrime The given integer value
	 * @return The new safe prime
	 */
	public static SafePrime getInstance(int safePrime) {
		return SafePrime.getInstance(BigInteger.valueOf(safePrime));
	}

	/**
	 * Creates a new safe prime from a given integer value of type {@link BigInteger}. Throws an exception if the given
	 * integer is not a safe prime.
	 * <p>
	 * @param safePrime The given integer value
	 * @return The new safe prime
	 */
	public static SafePrime getInstance(BigInteger safePrime) {
		if (safePrime == null || !MathUtil.isSafePrime(safePrime)) {
			throw new IllegalArgumentException();
		}
		return new SafePrime(safePrime);
	}

	/**
	 * Returns the smallest safe prime greater or equal to {@code lowerBound}.
	 * <p>
	 * @param lowerBound The lower bound
	 * @return The new safe prime
	 */
	public static SafePrime getFirstInstance(BigInteger lowerBound) {
		if (lowerBound == null) {
			throw new IllegalArgumentException();
		}
		BigInteger twelve = BigInteger.valueOf(12);
		BigInteger safePrime;
		if (lowerBound.compareTo(BigInteger.valueOf(5)) <= 0) {
			safePrime = BigInteger.valueOf(5);
		} else if (lowerBound.compareTo(BigInteger.valueOf(7)) <= 0) {
			safePrime = BigInteger.valueOf(7);
		} else {
			safePrime = lowerBound.add(twelve.subtract(lowerBound.mod(twelve))).subtract(BigInteger.ONE);
			while (!MathUtil.isSafePrime(safePrime)) {
				safePrime = safePrime.add(twelve);
			}
		}
		return new SafePrime(safePrime);
	}

	/**
	 * Returns the smallest safe prime of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new safe prime
	 */
	public static SafePrime getFirstInstance(int bitLength) {
		if (bitLength < 3) {
			throw new IllegalArgumentException();
		}
		return SafePrime.getFirstInstance(MathUtil.powerOfTwo(bitLength - 1));
	}

	/**
	 * Creates a new random safe prime of a given bit length using the library's default source of randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength) {
		return SafePrime.getRandomInstance(bitLength, RandomNumberGenerator.getInstance().getInstance());
	}

	/**
	 * Creates a new random safe prime of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength             The bit length
	 * @param randomNumberGenerator The given random number generator
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength, RandomNumberGenerator randomNumberGenerator) {
		if (bitLength < 3 || randomNumberGenerator == null) {
			throw new IllegalArgumentException();
		}
		// Special case with safe primes p=5 or p=7 not satisfying p mod 12 = 11
		if (bitLength == 3) {
			if (randomNumberGenerator.nextBoolean()) {
				return new SafePrime(BigInteger.valueOf(5));
			} else {
				return new SafePrime(BigInteger.valueOf(7));
			}
		}
		BigInteger prime;
		BigInteger safePrime;
		do {
			prime = randomNumberGenerator.nextBigInteger(bitLength - 1);
			safePrime = prime.shiftLeft(1).add(BigInteger.ONE);
		} while (!safePrime.mod(BigInteger.valueOf(12)).equals(BigInteger.valueOf(11)) || !MathUtil.isPrime(prime) || !MathUtil.isPrime(safePrime));
		return new SafePrime(safePrime);
	}

}
