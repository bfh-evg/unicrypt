/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 ???
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
 *   a written agreement between you and ???.
 *
 *
 *   For further information contact ???
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.helper;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArray {

	private final byte[] bytes;

	private ByteArray(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return this.bytes.clone();
	}

	public int getLength() {
		return this.bytes.length;
	}

	public ByteArray concatenate(ByteArray other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		byte[] result = new byte[this.getLength() + other.getLength()];
		System.arraycopy(this.bytes, 0, result, 0, this.getLength());
		System.arraycopy(other.bytes, 0, result, this.getLength(), other.getLength());
		return new ByteArray(result);
	}

	public static ByteArray getInstance(int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(new byte[length]);
	}

	public static ByteArray getInstance(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(bytes.clone());
	}

}
