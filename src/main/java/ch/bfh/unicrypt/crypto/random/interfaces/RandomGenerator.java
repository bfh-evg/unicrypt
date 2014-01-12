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
package ch.bfh.unicrypt.crypto.random.interfaces;

import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public interface RandomGenerator {

	/**
	 * Generates a random boolean value.
	 * <p>
	 * @return The random boolean value
	 */
	public boolean nextBoolean();

	public byte nextByte();

	public byte[] nextBytes(int length);

	public int nextInteger();

	/**
	 * Generates a random integer between 0 and {@literal maxValue} (inclusive).
	 * <p>
	 * @param maxValue The maximal value
	 * @return The random integer
	 * @throws IllegalArgumentException if {@literal maxValue < 0}
	 */
	public int nextInteger(final int maxValue);

	/**
	 * Generates a random integer between {@literal minValue} (inclusive) and {@literal maxValue} (inclusive).
	 * <p>
	 * @param minValue The minimal value
	 * @param maxValue The maximal value
	 * @return The random integer
	 * @throws IllegalArgumentException if {@literal maxValue < minValue}
	 */
	public int nextInteger(final int minValue, final int maxValue);

	/**
	 * Generates a random BigInteger value of a certain bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal bitLength < 0}
	 */
	public BigInteger nextBigInteger(final int bitLength);

	/**
	 * Generates a random BigInteger between 0 and {@literal maxValue} (inclusive).
	 * <p>
	 * @param maxValue The maximal value
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal maxValue} is null or if {@literal maxValue < 0}
	 */
	public BigInteger nextBigInteger(final BigInteger maxValue);

	/**
	 * Generates a random BigInteger value between {@literal minValue} (inclusive) and {@literal maxValue} (inclusive).
	 * <p>
	 * @param minValue The minimal value
	 * @param maxValue The maximal value
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal minValue} or {@literal maxValue} is null, or if
	 *                                  {@literal maxValue < minValue}
	 */
	public BigInteger nextBigInteger(final BigInteger minValue, final BigInteger maxValue);

	/**
	 * Generates a random BigInteger value of a certain bit length that is probably prime with high certainty.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The random BigInteger prime number
	 * @throws IllegalArgumentException if {@literal bitLength < 2}
	 */
	public BigInteger nextPrime(final int bitLength);

	/**
	 * Generates a random BigInteger value of a certain bit length that is a save prime with high certainty.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The random BigInteger save prime
	 * @throws IllegalArgumentException if {@literal bitLength < 3}
	 * @see "Handbook of Applied Cryptography, Algorithm 4.86"
	 */
	public BigInteger nextSavePrime(final int bitLength);

	/**
	 * Generates a pair of distinct random BigInteger values of respective bit lengths such that both values are probably
	 * prime with high certainty and such that the second divides the first minus one.
	 * <p>
	 * @param bitLength1 The bit length of the first random prime
	 * @param bitLength2 The bit length of the second random prime
	 * @return A BigInteger array containing the two primes
	 * @throws IllegalArgumentException if {@literal bitLength1 <= bitLength2} or {@literal bitLengh2<2}
	 */
	public BigInteger[] nextPrimePair(final int bitLength1, final int bitLength2);

}
