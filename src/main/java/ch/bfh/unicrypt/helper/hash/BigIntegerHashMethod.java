/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import java.math.BigInteger;

/**
 * This class instantiates the generic class {@link HashMethod} for {@link BigInteger} values. It is a convenience class
 * which does nothing more than selecting the default {@link BigIntegerToByteArray} converter.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BigIntegerHashMethod
	   extends HashMethod<BigInteger> {

	// Case 1: recursive hashing
	protected BigIntegerHashMethod(HashAlgorithm hashAlgorithm) {
		super(hashAlgorithm, BigIntegerToByteArray.getInstance());
	}

	// Case 2: hashing after aggregation
	protected BigIntegerHashMethod(HashAlgorithm hashAlgorithm, Aggregator<BigInteger> aggregator) {
		super(hashAlgorithm, aggregator, BigIntegerToByteArray.getInstance());
	}

	/**
	 * Returns a new hash method which computes the hash values recursively using the default hash algorithm.
	 * <p>
	 * @return The new hash method
	 */
	public static BigIntegerHashMethod getInstance() {
		return BigIntegerHashMethod.getInstance(HashAlgorithm.getInstance());
	}

	/**
	 * Returns a new hash method which computes the hash values recursively. The hash algorithm is given as parameter.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new hash method
	 */
	public static BigIntegerHashMethod getInstance(HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerHashMethod(hashAlgorithm);
	}

	/**
	 * Returns a new hash method which computes the hash values by first applying an aggregator to the given tree. The
	 * aggregator is given as parameter. The resulting hash method uses the default hash algorithm.
	 * <p>
	 * @param aggregator The given aggregator
	 * @return The new hash method
	 */
	public static BigIntegerHashMethod getInstance(Aggregator<BigInteger> aggregator) {
		return BigIntegerHashMethod.getInstance(HashAlgorithm.getInstance(), aggregator);
	}

	/**
	 * Returns a new hash method which computes the hash values by first applying an aggregator to the given tree. The
	 * hash algorithm and the aggregator are given as parameters.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param aggregator    The given aggregator
	 * @return The new hash method
	 */
	public static BigIntegerHashMethod getInstance(HashAlgorithm hashAlgorithm, Aggregator<BigInteger> aggregator) {
		if (hashAlgorithm == null || aggregator == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerHashMethod(hashAlgorithm, aggregator);
	}

}
