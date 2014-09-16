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
package ch.bfh.unicrypt.helper.converter.classes.biginteger;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToBigInteger
	   extends AbstractBigIntegerConverter<ByteArray> {

	private transient final ByteOrder byteOrder; //TODO not serializable

	protected ByteArrayToBigInteger(ByteOrder byteOrder) {
		super(ByteArray.class);
		this.byteOrder = byteOrder;
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	@Override
	public BigInteger abstractConvert(ByteArray byteArray) {
		byte[] bytes;
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			bytes = byteArray.reverse().getBytes();
		} else {
			bytes = byteArray.getBytes();
		}
		return new BigInteger(1, bytes);
	}

	@Override
	public ByteArray abstractReconvert(BigInteger posBigInteger) {
		// TODO: negative integers not allowed
		if (posBigInteger.signum() == -1) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = posBigInteger.toByteArray();
		// remove the sign bit if necessary
		if (bytes[0] == 0) {
			bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		}
		ByteArray result = ByteArray.getInstance(bytes);
		if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
			return result.reverse();
		}
		return result;
	}

	public static ByteArrayToBigInteger getInstance() {
		return ByteArrayToBigInteger.getInstance(ByteOrder.BIG_ENDIAN);
	}

	public static ByteArrayToBigInteger getInstance(ByteOrder byteOrder) {
		if (byteOrder == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToBigInteger(byteOrder);
	}

}
