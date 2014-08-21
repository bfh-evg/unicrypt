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
package ch.bfh.unicrypt.helper.converter;

import ch.bfh.unicrypt.helper.array.ByteArray;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class BigIntegerConverter
	   extends Converter<BigInteger> {

	private final ByteOrder byteOrder;
	private final int minLength;

	protected BigIntegerConverter(ByteOrder byteOrder, int minLength) {
		super(BigInteger.class.getName());
		this.byteOrder = byteOrder;
		this.minLength = minLength;
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	public int getMinLength() {
		return minLength;
	}

	public ByteArray convertToByteArray(int integer) {
		return this.abstractConvertToByteArray(BigInteger.valueOf(integer));
	}

	@Override
	public ByteArray abstractConvertToByteArray(BigInteger posBigInteger) {
		// TODO: negative integers not allowed
		if (posBigInteger.signum() == -1) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = posBigInteger.toByteArray();
		// remove the sign bit if necessary
		if (bytes[0] == 0) {
			bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		}
		int length = bytes.length;
		if (length < minLength) {
			byte[] expandedBytes = new byte[minLength];
			System.arraycopy(bytes, 0, expandedBytes, minLength - length, length);
			bytes = expandedBytes;
		}
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			BigIntegerConverter.reverseBytes(bytes);
		}
		return ByteArray.getInstance(bytes);
	}

	@Override
	public BigInteger abstractConvertFromByteArray(ByteArray byteArray) {
		byte[] bytes = byteArray.getAll();
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			BigIntegerConverter.reverseBytes(bytes);
		}
		return new BigInteger(1, bytes);
	}

	private static void reverseBytes(byte[] bytes) {
		int length = bytes.length;
		for (int i = 0; i < length / 2; i++) {
			byte b = bytes[i];
			bytes[i] = bytes[length - 1 - i];
			bytes[length - 1 - i] = b;
		}
	}

	public static BigIntegerConverter getInstance() {
		return BigIntegerConverter.getInstance(ByteOrder.BIG_ENDIAN, 0);
	}

	public static BigIntegerConverter getInstance(ByteOrder byteOrder) {
		return BigIntegerConverter.getInstance(byteOrder, 0);
	}

	public static BigIntegerConverter getInstance(int minLength) {
		return BigIntegerConverter.getInstance(ByteOrder.BIG_ENDIAN, minLength);
	}

	public static BigIntegerConverter getInstance(ByteOrder byteOrder, int minLength) {
		if (byteOrder == null || minLength < 0) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerConverter(byteOrder, minLength);
	}

}
