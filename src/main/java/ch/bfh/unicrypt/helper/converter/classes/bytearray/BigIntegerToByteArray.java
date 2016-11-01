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
package ch.bfh.unicrypt.helper.converter.classes.bytearray;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractByteArrayConverter;
import java.math.BigInteger;
import java.nio.ByteOrder;

/**
 * Instances of this class convert {@code BigInteger} values into {@code ByteArray} values of {@code length>0} using the
 * standard Java conversion method implemented in {@link BigInteger#toByteArray()} and
 * {@link BigInteger#BigInteger(byte[])}. The {@code BigInteger} values can be positive or negative. There are two modes
 * of operation: the default mode using big-endian byte order and the alternative mode using little-endian byte order.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BigIntegerToByteArray
	   extends AbstractByteArrayConverter<BigInteger> {

	private static final long serialVersionUID = 1L;

	private final ByteOrder byteOrder;

	protected BigIntegerToByteArray(ByteOrder byteOrder) {
		super(BigInteger.class);
		this.byteOrder = byteOrder;
	}

	/**
	 * Returns the default instance of this class using big-endian byte order.
	 * <p>
	 * @return The default instance
	 */
	public static BigIntegerToByteArray getInstance() {
		return BigIntegerToByteArray.getInstance(ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Returns a new instance of this class for a given byte order.
	 * <p>
	 * @param byteOrder The given byte order
	 * @return The resulting instance
	 */
	public static BigIntegerToByteArray getInstance(ByteOrder byteOrder) {
		if (byteOrder == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerToByteArray(byteOrder);
	}

	/**
	 * This is a convenience method to allow inputs of type {@code long}.
	 * <p>
	 * @param value The given value
	 * @return The resulting byte array
	 */
	public ByteArray convert(long value) {
		return this.convert(BigInteger.valueOf(value));
	}

	@Override
	protected boolean defaultIsValidOutput(ByteArray byteArray) {
		return byteArray.getLength() > 0;
	}

	@Override
	public ByteArray abstractConvert(BigInteger value) {
		ByteArray result = new SafeByteArray(value.toByteArray());
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			return result.reverse();
		}
		return result;
	}

	@Override
	public BigInteger abstractReconvert(ByteArray byteArray) {
		byte[] bytes;
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			bytes = byteArray.reverse().getBytes();
		} else {
			bytes = byteArray.getBytes();
		}
		return new BigInteger(bytes);
	}

}
