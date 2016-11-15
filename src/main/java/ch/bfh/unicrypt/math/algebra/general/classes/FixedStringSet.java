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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class FixedStringSet
	   extends FiniteStringSet {

	private static final long serialVersionUID = 1L;

	private FixedStringSet(Alphabet alphabet, int length) {
		super(alphabet, length, length);
	}

	public int getLength() {
		return this.getMinLength();
	}

	public static FixedStringSet getInstance(final int length) {
		return FixedStringSet.getInstance(Alphabet.getInstance(), length);
	}

	public static FixedStringSet getInstance(final Alphabet alphabet, final int length) {
		if (alphabet == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (length < 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_LENGTH, length);
		}
		return new FixedStringSet(alphabet, length);
	}

	public static FixedStringSet getInstance(final BigInteger minOrder) {
		return FixedStringSet.getInstance(Alphabet.getInstance(), minOrder);
	}

	public static FixedStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder) {
		if (alphabet == null || minOrder == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, alphabet, minOrder);
		}
		if (minOrder.signum() < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE, minOrder);
		}
		int length = 0;
		BigInteger size = BigInteger.valueOf(alphabet.getSize());
		BigInteger order = MathUtil.ONE;
		while (order.compareTo(minOrder) < 0) {
			order = order.multiply(size);
			length++;
		}
		return FixedStringSet.getInstance(alphabet, length);
	}

}
