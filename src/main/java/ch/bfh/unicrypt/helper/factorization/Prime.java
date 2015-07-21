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

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.random.classes.RandomNumberGenerator;
import java.math.BigInteger;

/**
 * Each instance of this class represents a prime number. This class is a specialization of {@link SpecialFactorization}
 * for the borderline case of a single prime factor {@code p} with exponent {@code 0}.
 * <p>
 * @see SpecialFactorization
 * @author R. Haenni
 * @version 2.0
 */
public class Prime
	   extends SpecialFactorization {

	private static final long serialVersionUID = 1L;

	protected Prime(BigInteger prime) {
		super(prime, new BigInteger[]{prime}, new int[]{1});
	}

	/**
	 * Creates a new prime number from a given integer value of type {@code int}. This method is a convenience method
	 * for {@link Prime#getInstance(BigInteger)}. Throws an exception if the given integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(int prime) {
		return Prime.getInstance(BigInteger.valueOf(prime));
	}

	/**
	 * Creates a new prime number from a given integer value of type {@link BigInteger}. Throws an exception if the
	 * given integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(BigInteger prime) {
		if (prime == null || !MathUtil.isPrime(prime)) {
			throw new IllegalArgumentException();
		}
		return Prime.privateGetInstance(prime);
	}

	/**
	 * Returns the smallest prime number of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new prime
	 */
	public static Prime getFirstInstance(int bitLength) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		return Prime.getFirstInstance(MathUtil.powerOfTwo(bitLength - 1));
	}

	/**
	 * Returns the smallest prime number greater or equal to {@code lowerBound}.
	 * <p>
	 * @param lowerBound The lower bound
	 * @return The new prime
	 */
	public static Prime getFirstInstance(BigInteger lowerBound) {
		if (lowerBound == null) {
			throw new IllegalArgumentException();
		}
		BigInteger candidate;
		if (lowerBound.compareTo(BigInteger.valueOf(2)) <= 0) {
			candidate = MathUtil.TWO;
		} else {
			candidate = lowerBound;
			if (candidate.mod(MathUtil.TWO).equals(MathUtil.ZERO)) {
				candidate = candidate.add(MathUtil.ONE);
			}
		}
		while (!MathUtil.isPrime(candidate)) {
			candidate = candidate.add(MathUtil.TWO);
		}
		return Prime.privateGetInstance(candidate);
	}

	/**
	 * Returns the smallest prime number of a given bit length such that {@code divisor} divides the prime number minus
	 * one.
	 * <p>
	 * @param bitLength The given bit length
	 * @param divisor   The given divisor
	 * @return The new prime
	 */
	public static Prime getFirstInstance(int bitLength, BigInteger divisor) {
		return Prime.getFirstInstance(bitLength, Prime.getInstance(divisor));
	}

	/**
	 * Returns the smallest prime number of a given bit length such that {@code divisor} divides the prime number minus
	 * one.
	 * <p>
	 * @param bitLength The given bit length
	 * @param divisor   The given divisor
	 * @return The new prime
	 */
	public static Prime getFirstInstance(int bitLength, Prime divisor) {
		if (divisor == null) {
			throw new IllegalArgumentException();
		}
		Prime prime = Prime.getFirstInstance(MathUtil.powerOfTwo(bitLength - 1), divisor);
		if (prime.getValue().bitLength() > bitLength) {
			throw new IllegalArgumentException();
		}
		return prime;
	}

	/**
	 * Returns the smallest prime number greater or equal to {@code lowerBound} such that {@code divisor} divides the
	 * prime number minus one.
	 * <p>
	 * @param lowerBound The lower bound
	 * @param divisor    The given divisor
	 * @return The new prime
	 */
	public static Prime getFirstInstance(BigInteger lowerBound, BigInteger divisor) {
		return Prime.getFirstInstance(lowerBound, Prime.getInstance(divisor));
	}

	/**
	 * Returns the smallest prime number greater or equal to {@code lowerBound} such that {@code divisor} divides the
	 * prime number minus one.
	 * <p>
	 * @param lowerBound The lower bound
	 * @param divisor    The given divisor
	 * @return The new prime
	 */
	public static Prime getFirstInstance(BigInteger lowerBound, Prime divisor) {
		if (lowerBound == null || divisor == null) {
			throw new IllegalArgumentException();
		}
		BigInteger primeDivisor = divisor.getValue();
		if (primeDivisor.equals(MathUtil.TWO)) {
			return Prime.getFirstInstance(lowerBound.max(MathUtil.THREE));
		}
		BigInteger doublePrimeDivisor = primeDivisor.shiftLeft(1);
		// the smallest possible value is 2*divisor+1
		lowerBound = lowerBound.max(doublePrimeDivisor.add(MathUtil.ONE));
		// compute the smallest possible candidate
		BigInteger candidate;
		if (lowerBound.mod(doublePrimeDivisor).equals(MathUtil.ONE)) {
			candidate = lowerBound;
		} else {
			candidate = lowerBound.subtract(MathUtil.ONE).divide(doublePrimeDivisor).add(MathUtil.ONE).multiply(doublePrimeDivisor).add(MathUtil.ONE);
		}
		while (!MathUtil.isPrime(candidate)) {
			candidate = candidate.add(doublePrimeDivisor);
		}
		return Prime.privateGetInstance(candidate);
	}

	/**
	 * Creates a new random prime number of a given bit length using the library's default source of randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength) {
		return Prime.getRandomInstance(bitLength, RandomNumberGenerator.getInstance());
	}

	/**
	 * Creates a new random prime number of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength             The bit length
	 * @param randomNumberGenerator The given source of randomness
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength, RandomNumberGenerator randomNumberGenerator) {
		if (bitLength < 2 || randomNumberGenerator == null) {
			throw new IllegalArgumentException();
		}
		BigInteger candidate;
		do {
			candidate = randomNumberGenerator.nextBigInteger(bitLength);
		} while (!MathUtil.isPrime(candidate));
		return Prime.privateGetInstance(candidate);
	}

	private static Prime privateGetInstance(BigInteger prime) {
		if (MathUtil.isPrime(prime.subtract(BigInteger.ONE).divide(MathUtil.TWO))) {
			return new SafePrime(prime);
		}
		return new Prime(prime);
	}

}
