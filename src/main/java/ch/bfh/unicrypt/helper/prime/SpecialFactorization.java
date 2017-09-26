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

import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import java.math.BigInteger;

/**
 * Instances of this class represent factorizations of the form {@code n=2}, {@code n=4}, {@code n=p^k}, or
 * {@code n=2p^k}, where {@code p>2} is prime and {@code k>=1}. These are the values, for which the group of integers
 * modulo {@code n} is cyclic. Such factorizations are needed to construct instances of {@link GStarMod}, which
 * represent such groups and corresponding sub-groups.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @see "Handbook of Applied Cryptography, Fact 2.132 (i)"
 * @see GStarMod
 * @see Factorization
 */
public class SpecialFactorization
	   extends Factorization {

	private static final long serialVersionUID = 1L;

	protected SpecialFactorization(BigInteger value, BigInteger[] primeFactors, Integer[] exponents) {
		super(value, primeFactors, exponents);
	}

	/**
	 * Returns a new special factorization for a single prime factor {@code p>=2}. The return type is {@link Prime}, a
	 * sub-class of {@link SpecialFactorization}.
	 * <p>
	 * @param primeFactor The single prime factor
	 * @return The new special factorization
	 */
	public static Prime getInstance(BigInteger primeFactor) {
		return Prime.getInstance(primeFactor);
	}

	/**
	 * Returns a new special factorization of the form {@code n=2}, {@code n=4}, {@code n=p^k} for a single prime factor
	 * {@code p>2} and {@code k>=1}. For {code k=1}, the type of the returned result is {@link Prime}.
	 * <p>
	 * @param primeFactor The single prime factor
	 * @param exponent    The exponent
	 * @return The new special factorization
	 */
	public static SpecialFactorization getInstance(BigInteger primeFactor, int exponent) {
		return SpecialFactorization.getInstance(primeFactor, exponent, false);
	}

	/**
	 * Returns a new special factorization of the form {@code n=p} or {@code n=2p} for a prime factor {@code p>=2}. In
	 * the first case, the type of the returned result is {@link Prime}.
	 * <p>
	 * @param primeFactor The prime factor
	 * @param timesTwo    A flag indicating whether {@code 2} is a second prime factor.
	 * @return The new special factorization
	 */
	public static SpecialFactorization getInstance(BigInteger primeFactor, boolean timesTwo) {
		return SpecialFactorization.getInstance(primeFactor, 1, timesTwo);
	}

	/**
	 * This method covers the general case consisting of {@code n=2}, {@code n=4}, {@code n=p^k}, or {@code n=2p^k},
	 * where {@code p>2} is prime and {@code k>=1}. If {@code n} is prime (first case, third case with {@code k=1}), the
	 * type of the returned result is {@link Prime}.
	 * <p>
	 * @param primeFactor The prime factor
	 * @param exponent    The exponent
	 * @param timesTwo    A flag indicating whether {@code 2} is a second prime factor.
	 * @return The new special factorization
	 */
	public static SpecialFactorization getInstance(BigInteger primeFactor, int exponent, boolean timesTwo) {
		if (primeFactor == null || !MathUtil.isPrime(primeFactor) || exponent < 1) {
			throw new IllegalArgumentException();
		}
		if (primeFactor.equals(MathUtil.TWO)) {
			if (timesTwo) {
				exponent++;
				timesTwo = false;
			}
			if (exponent > 2) {
				throw new IllegalArgumentException();
			}
		}
		if (exponent == 1 && !timesTwo) {
			if (MathUtil.isPrime(primeFactor.subtract(MathUtil.ONE).divide(MathUtil.TWO))) {
				return new SafePrime(primeFactor);
			}
			return new Prime(primeFactor);
		}
		BigInteger[] primeFactors;
		Integer[] exponents;
		BigInteger value = primeFactor.pow(exponent);
		if (timesTwo) {
			primeFactors = new BigInteger[]{primeFactor, MathUtil.TWO};
			exponents = new Integer[]{exponent, 1};
			value = value.multiply(MathUtil.TWO);
		} else {
			primeFactors = new BigInteger[]{primeFactor};
			exponents = new Integer[]{exponent};
		}
		return new SpecialFactorization(value, primeFactors, exponents);
	}

	/**
	 * Returns the prime factor.
	 * <p>
	 * @return The prime factor
	 */
	public BigInteger getPrimeFactor() {
		return this.primeFactors.getAt(0);
	}

	/**
	 * Returns the exponent of the prime factor.
	 * <p>
	 * @return The exponent
	 */
	public int getExponent() {
		return this.exponents.getAt(0);
	}

	/**
	 * Returns the flag indicating whether the 2 is a second prime factor.
	 * <p>
	 * @return The flag
	 */
	public boolean timesTwo() {
		return this.primeFactors.getLength() == 2;
	}

}
