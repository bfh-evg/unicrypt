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
package ch.bfh.unicrypt.math.helper.bytetree;

import ch.bfh.unicrypt.math.helper.ByteArray;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ByteTreeLeaf
	   extends ByteTree {

	public static final byte IDENTIFIER = 1;

	// either byteTree (from super class) or binaryData is initialized
	private byte[] binaryData = null;

	protected ByteTreeLeaf(byte[] binaryData) {
		this.binaryData = binaryData;
		this.length = LENGTH_OF_PREAMBLE + binaryData.length;
	}

	protected ByteTreeLeaf(ByteArray byteArray) {
		this.byteArray = byteArray;
		this.length = byteArray.getLength();
	}

	protected byte[] getBinaryData() {
		if (this.binaryData == null) {
			this.binaryData = this.byteArray.extract(LENGTH_OF_PREAMBLE, this.length - LENGTH_OF_PREAMBLE).getAll();
		}
		return this.binaryData;
	}

	public BigInteger convertToBigInteger() {
		return new BigInteger(this.getBinaryData());
	}

	public String convertToString() {
		return new String(this.getBinaryData());
	}

	public ByteArray convertToByteArray() {
		if (this.byteArray == null) {
			return ByteArray.getInstance(this.binaryData);
		}
		return this.byteArray.extract(LENGTH_OF_PREAMBLE, this.length - LENGTH_OF_PREAMBLE);
	}

	@Override
	public String defaultToStringValue() {
		return this.convertToByteArray().defaultToStringValue();
	}

	@Override
	protected void abstractGetByteArray(ByteBuffer buffer) {
		buffer.put(IDENTIFIER);
		buffer.putInt(this.binaryData.length);
		buffer.put(this.binaryData);
	}

}
