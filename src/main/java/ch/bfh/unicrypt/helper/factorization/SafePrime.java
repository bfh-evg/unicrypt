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
package ch.bfh.unicrypt.helper.factorization;

import ch.bfh.unicrypt.helper.cache.Cache;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
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

	// maps of smallest and largest safe prime numbers for common bit lenghts
	private static final Cache<Integer, SafePrime> CACHE1 = new Cache<>();
	private static final Cache<Integer, SafePrime> CACHE2 = new Cache<>();

	static {
		SafePrime.CACHE1.put(128, new SafePrime(new BigInteger("8000000000000000000000000000225F", 16)));
		SafePrime.CACHE1.put(160, new SafePrime(new BigInteger("8000000000000000000000000000000000000A5F", 16)));
		SafePrime.CACHE1.put(192, new SafePrime(new BigInteger("80000000000000000000000000000000000000000000127B", 16)));
		SafePrime.CACHE1.put(224, new SafePrime(new BigInteger("800000000000000000000000000000000000000000000000000000FF", 16)));
		SafePrime.CACHE1.put(256, new SafePrime(new BigInteger("800000000000000000000000000000000000000000000000000000000002FF7F", 16)));
		SafePrime.CACHE1.put(384, new SafePrime(new BigInteger("80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000427B", 16)));
		SafePrime.CACHE1.put(512, new SafePrime(new BigInteger("80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000513", 16)));
		SafePrime.CACHE1.put(768, new SafePrime(new BigInteger("80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000017DF9F", 16)));
		SafePrime.CACHE1.put(1024, new SafePrime(new BigInteger("80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001981BF", 16)));
		SafePrime.CACHE1.put(2048, new SafePrime(new BigInteger("800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000AD3AF", 16)));
		SafePrime.CACHE1.put(3072, new SafePrime(new BigInteger("8000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006119DF", 16)));

		SafePrime.CACHE2.put(128, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFC3A7", 16)));
		SafePrime.CACHE2.put(160, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFCA73", 16)));
		SafePrime.CACHE2.put(192, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5F03", 16)));
		SafePrime.CACHE2.put(224, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEE1F", 16)));
		SafePrime.CACHE2.put(256, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF72EF", 16)));
		SafePrime.CACHE2.put(384, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF194F", 16)));
		SafePrime.CACHE2.put(512, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF6B1B", 16)));
		SafePrime.CACHE2.put(768, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF8D887", 16)));
		SafePrime.CACHE2.put(1024, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEF5127", 16)));
		SafePrime.CACHE2.put(2048, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE25CEF", 16)));
		SafePrime.CACHE2.put(3072, new SafePrime(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEF289B", 16)));
	}

	protected SafePrime(BigInteger safePrime) {
		super(safePrime, safePrime.subtract(MathUtil.ONE).divide(MathUtil.TWO));
	}

	/**
	 * Creates a new safe prime from a given integer value of type {@code long}. This method is a convenience method for
	 * {@link SafePrime#getInstance(java.math.BigInteger)}. Throws an exception if the given integer is not a safe
	 * prime.
	 * <p>
	 * @param safePrime The given integer value
	 * @return The new safe prime
	 */
	public static SafePrime getInstance(long safePrime) {
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
	 * Returns the smallest safe prime of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new safe prime
	 */
	public static SafePrime getSmallestInstance(int bitLength) {
		if (bitLength < 3) {
			throw new IllegalArgumentException();
		}
		SafePrime safePrime = CACHE1.get(bitLength);
		if (safePrime == null) {
			if (bitLength == 3) {
				safePrime = new SafePrime(MathUtil.FIVE);
			} else {
				BigInteger increase = BigInteger.valueOf(12);
				BigInteger candidate = MathUtil.powerOfTwo(bitLength - 1).add(MathUtil.ONE);
				while (!candidate.mod(increase).add(MathUtil.ONE).equals(increase)) {
					candidate = candidate.add(MathUtil.TWO);
				}
				while (!MathUtil.isSafePrime(candidate)) {
					candidate = candidate.add(increase);
				}
				safePrime = new SafePrime(candidate);
			}
			CACHE1.put(bitLength, safePrime);
		}
		return safePrime;
	}

	/**
	 * Returns the largest safe prime of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new safe prime
	 */
	public static SafePrime getLargestInstance(int bitLength) {
		if (bitLength < 3) {
			throw new IllegalArgumentException();
		}
		SafePrime safePrime = CACHE2.get(bitLength);
		if (safePrime == null) {
			if (bitLength == 3) {
				safePrime = new SafePrime(MathUtil.SEVEN);
			} else {
				BigInteger increase = BigInteger.valueOf(12);
				BigInteger candidate = MathUtil.powerOfTwo(bitLength).subtract(MathUtil.ONE);
				while (!candidate.mod(increase).add(MathUtil.ONE).equals(increase)) {
					candidate = candidate.subtract(MathUtil.TWO);
				}
				while (!MathUtil.isSafePrime(candidate)) {
					candidate = candidate.subtract(increase);
				}
				safePrime = new SafePrime(candidate);
			}
			CACHE2.put(bitLength, safePrime);
		}
		return safePrime;
	}

	/**
	 * Creates a new random safe prime of a given bit length using the library's default random byte sequence.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength) {
		return SafePrime.getRandomInstance(bitLength, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Creates a new random safe prime of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength          The bit length
	 * @param randomByteSequence The given random random byte sequence
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength, RandomByteSequence randomByteSequence) {
		if (bitLength < 3 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		// Special case with safe primes p=5 or p=7 not satisfying p mod 12 = 11
		if (bitLength == 3) {
			if (randomByteSequence.getRandomBitSequence().get()) {
				return new SafePrime(MathUtil.FIVE);
			} else {
				return new SafePrime(MathUtil.SEVEN);
			}
		}
		return new SafePrime(randomByteSequence.getRandomBigIntegerSequence(bitLength - 1)
			   .filter(value -> MathUtil.isPrime(value))
			   .map(value -> value.shiftLeft(1).add(MathUtil.ONE))
			   .find(value -> value.mod(BigInteger.valueOf(12)).equals(BigInteger.valueOf(11)) && MathUtil.isPrime(value)));
	}

}
