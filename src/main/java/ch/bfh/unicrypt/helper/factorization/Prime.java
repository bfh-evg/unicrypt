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
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
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
	 * Creates a new prime from a given integer value of type {@code int}. This method is a convenience method for
	 * {@link Prime#getInstance(java.math.BigInteger)}. Throws an exception if the given integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(int prime) {
		return Prime.getInstance(BigInteger.valueOf(prime));
	}

	/**
	 * Creates a new prime from a given integer value of type {@link BigInteger}. Throws an exception if the given
	 * integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(BigInteger prime) {
		if (prime == null || !MathUtil.isPrime(prime)) {
			throw new IllegalArgumentException();
		}
		if (MathUtil.isPrime(prime.subtract(BigInteger.ONE).divide(MathUtil.TWO))) {
			return new SafePrime(prime);
		}
		return new Prime(prime);
	}

	/**
	 * Returns the smallest prime greater or equal to {@code lowerBound}.
	 * <p>
	 * @param lowerBound The lower bound
	 * @return The new prime
	 */
	public static Prime getNextInstance(BigInteger lowerBound) {
		if (lowerBound == null) {
			throw new IllegalArgumentException();
		}
		BigInteger prime;
		if (lowerBound.compareTo(BigInteger.valueOf(2)) <= 0) {
			prime = MathUtil.TWO;
		} else {
			prime = lowerBound;
			if (prime.mod(MathUtil.TWO).equals(MathUtil.ZERO)) {
				prime = prime.add(MathUtil.ONE);
			}
		}
		while (!MathUtil.isPrime(prime)) {
			prime = prime.add(MathUtil.TWO);
		}
		return new Prime(prime);
	}

	/**
	 * Returns the smallest prime of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new prime
	 */
	public static Prime getNextInstance(int bitLength) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		return Prime.getNextInstance(MathUtil.powerOfTwo(bitLength - 1));
	}

	/**
	 * Creates a new random prime of a given bit length using the library's default source of randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength) {
		return Prime.getRandomInstance(bitLength, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Creates a new random prime of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength          The bit length
	 * @param randomByteSequence The given source of randomness
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength, RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return Prime.getInstance(randomByteSequence.getRandomNumberGenerator().nextPrime(bitLength));
	}

}
