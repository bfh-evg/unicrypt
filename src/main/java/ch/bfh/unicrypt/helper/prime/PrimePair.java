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
package ch.bfh.unicrypt.helper.prime;

import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimePair;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrimePair;
import java.math.BigInteger;

/**
 * Instances of this class represent factorizations of the form {@code n=pq}, where {@code p} and {@code q} are distinct
 * prime factors. Such factorizations are needed to construct instances of {@link ZModPrimePair} and
 * {@link ZStarModPrimePair}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @see ZModPrimePair
 * @see ZStarModPrimePair
 */
public class PrimePair
	   extends Factorization {

	private static final long serialVersionUID = 1L;

	protected PrimePair(BigInteger prime1, BigInteger prime2) {
		// the smaller prime factor is stored at index 0, the larger at index 1
		super(prime1.multiply(prime2), new BigInteger[]{prime1.min(prime2), prime1.max(prime2)}, new Integer[]{1, 1});
	}

	/**
	 * Returns the smaller of the two prime factors.
	 * <p>
	 * @return The smaller prime factor
	 */
	public BigInteger getSmallerPrimeFactor() {
		return this.primeFactors.getAt(0);
	}

	/**
	 * Returns the larger of the two prime factors.
	 * <p>
	 * @return The larger prime factor
	 */
	public BigInteger getLargerPrimeFactor() {
		return this.primeFactors.getAt(1);
	}

	/**
	 * Creates a new prime pair from two given integer values of type {@code long}. This method is a convenience method
	 * for {@link PrimePair#getInstance(BigInteger, BigInteger)}. Throws an exception if one of the given integers is
	 * not prime or if they are equal.
	 * <p>
	 * @param prime1 The first integer value
	 * @param prime2 The second integer value
	 * @return The new prime pair
	 */
	public static PrimePair getInstance(long prime1, long prime2) {
		return PrimePair.getInstance(Prime.getInstance(prime1), Prime.getInstance(prime2));
	}

	/**
	 * Creates a new prime pair from two given integer values of type {@code BigInteger}. This method is a convenience
	 * method for {@link PrimePair#getInstance(Prime, Prime)}. Throws an exception if one of the given integers is not
	 * prime or if they are equal.
	 * <p>
	 * @param prime1 The first integer value
	 * @param prime2 The second integer value
	 * @return The new prime pair
	 */
	public static PrimePair getInstance(BigInteger prime1, BigInteger prime2) {
		return PrimePair.getInstance(Prime.getInstance(prime1), Prime.getInstance(prime2));
	}

	/**
	 * Creates a new prime pair from two distinct primes. Throws an exception if they are equal.
	 * <p>
	 * @param prime1 The first prime
	 * @param prime2 The second prime
	 * @return The new prime pair
	 */
	public static PrimePair getInstance(Prime prime1, Prime prime2) {
		if (prime1 == null || prime2 == null || prime1.equals(prime2)) {
			throw new IllegalArgumentException();
		}
		return new PrimePair(prime1.getValue(), prime2.getValue());
	}

	/**
	 * Creates a new random prime pair, each prime of a given bit length, using the library's default source of
	 * randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new prime pair
	 */
	public static PrimePair getRandomInstance(int bitLength) {
		return PrimePair.getRandomInstance(bitLength, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Creates a new random prime pair, each prime of a given bit length, using a given source of randomness.
	 * <p>
	 * @param bitLength          The bit length
	 * @param randomByteSequence The given source of randomness
	 * @return The new prime pair
	 */
	public static PrimePair getRandomInstance(int bitLength, RandomByteSequence randomByteSequence) {
		Prime prime1 = Prime.getRandomInstance(bitLength, randomByteSequence);
		Prime prime2;
		do {
			prime2 = Prime.getRandomInstance(bitLength, randomByteSequence);
		} while (prime1.equals(prime2));
		return new PrimePair(prime1.getValue(), prime2.getValue());
	}

}
