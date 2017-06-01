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

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Each instance of this class represents a prime factorization {@code n=p1^e1*...*pN^eN} of a positive integer
 * {@code x>0}. The prime factors and exponents must be given when creating the instance. Specialized classes
 * {@link SpecialFactorization}, {@link PrimePair}, {@link Prime}, and {@link SafePrime} exist for several borderline
 * case..
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class Factorization
	   extends UniCrypt {

	private static final long serialVersionUID = 1L;

	protected final BigInteger value;
	protected final ImmutableArray<BigInteger> primeFactors;
	protected final ImmutableArray<Integer> exponents;

	protected Factorization(BigInteger value, BigInteger[] primeFactors, Integer[] exponents) {
		this.value = value;
		this.primeFactors = DenseArray.getInstance(primeFactors);
		this.exponents = DenseArray.getInstance(exponents);
	}

	/**
	 * Creates a new prime factorization {@code n=p1*...*pN}.
	 * <p>
	 * @param primeFactors The prime factors
	 * @return The new factorization
	 */
	public static Factorization getInstance(BigInteger... primeFactors) {
		if (primeFactors == null) {
			throw new IllegalArgumentException();
		}
		int[] exponents = new int[primeFactors.length];
		Arrays.fill(exponents, 1);
		return Factorization.getInstance(primeFactors, exponents);
	}

	/**
	 * Create a new prime factorization {@code n=p1^e1*...*pN^eN} based on the prime numbers {@code p1,...,pN} and
	 * corresponding exponents {@code e1,...,eN}. This is the general factor method for this class.
	 * <p>
	 * @param primeFactors The prime factors
	 * @param exponents    The corresponding exponents
	 * @return The new factorization
	 */
	public static Factorization getInstance(BigInteger[] primeFactors, int[] exponents) {
		if (primeFactors == null || exponents == null || primeFactors.length != exponents.length) {
			throw new IllegalArgumentException();
		}
		BigInteger value = MathUtil.ONE;
		for (int i = 0; i < primeFactors.length; i++) {
			if (primeFactors[i] == null || !MathUtil.isPrime(primeFactors[i]) || exponents[i] < 1) {
				throw new IllegalArgumentException();
			}
			value = value.multiply(primeFactors[i].pow(exponents[i]));
		}
		BigInteger[] newPrimeFactors = MathUtil.removeDuplicates(primeFactors);
		int newLength = newPrimeFactors.length;
		Integer[] newExponents = new Integer[newLength];
		Arrays.fill(newExponents, 0);
		for (int i = 0; i < newLength; i++) {
			for (int j = 0; j < primeFactors.length; j++) {
				if (newPrimeFactors[i].equals(primeFactors[j])) {
					newExponents[i] = newExponents[i] + exponents[j];
				}
			}
		}
		if (newLength == 1 && newExponents[0] == 1) {
			return new Prime(newPrimeFactors[0]);
		}
		if (newLength == 2 && newExponents[0] == 1 && newExponents[1] == 1) {
			return new PrimePair(newPrimeFactors[0], newPrimeFactors[1]);
		}
		return new Factorization(value, newPrimeFactors, newExponents);
	}

	/**
	 * Returns the value that corresponds to the prime factorization.
	 * <p>
	 * @return The value
	 */
	public BigInteger getValue() {
		return this.value;
	}

	/**
	 * Checks if the factorization consists of a single prime number.
	 *
	 * @return {@code true} if the factorization consists of a single prime number, {@code false} otherwise
	 */
	public boolean isPrime() {
		return this.primeFactors.getLength() == 1 && this.exponents.getAt(0) == 1;
	}

	/**
	 * Returns an array containing the prime factors.
	 * <p>
	 * @return The prime factors
	 */
	public ImmutableArray<BigInteger> getPrimeFactors() {
		return this.primeFactors;
	}

	/**
	 * Returns an array containing the exponents.
	 * <p>
	 * @return The exponents
	 */
	public ImmutableArray<Integer> getExponents() {
		return this.exponents;
	}

	@Override
	protected String defaultToStringContent() {
		return "" + this.getValue();
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + this.value.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Factorization) {
			final Factorization other = (Factorization) obj;
			return this.value.equals(other.value);
		}
		return false;
	}

}
