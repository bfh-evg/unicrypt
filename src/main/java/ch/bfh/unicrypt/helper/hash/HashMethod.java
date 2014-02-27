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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.UniCrypt;
import ch.bfh.unicrypt.helper.array.ByteArrayConverter;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class HashMethod
	   extends UniCrypt {

	public enum Mode {

		BYTEARRAY, BYTETREE, RECURSIVE;

	};

	private final HashAlgorithm hashAlgorithm;
	private final ByteArrayConverter converter;
	private final Mode mode;

	protected HashMethod(HashAlgorithm hashAlgorithm, ByteArrayConverter converter, Mode mode) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.mode = mode;
	}

	public HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	public ByteArrayConverter getByteArrayConverter() {
		return this.converter;
	}

	public Mode getMode() {
		return this.mode;
	}

	@Override
	public String defaultToStringValue() {
		return this.hashAlgorithm.toString() + "," + this.converter.toString() + "," + this.mode.toString();
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + this.hashAlgorithm.hashCode();
		hash = 97 * hash + this.converter.hashCode();
		hash = 97 * hash + this.mode.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HashMethod other = (HashMethod) obj;
		return this.hashAlgorithm.equals(other.hashAlgorithm) && this.converter.equals(other.converter) && this.mode.equals(other.mode);
	}

	public static HashMethod getInstance() {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), ByteArrayConverter.getInstance(), Mode.RECURSIVE);
	}

	public static HashMethod getInstance(HashAlgorithm hashAlgorithm) {
		return HashMethod.getInstance(hashAlgorithm, ByteArrayConverter.getInstance(), Mode.RECURSIVE);
	}

	public static HashMethod getInstance(HashAlgorithm hashAlgorithm, ByteArrayConverter converter, Mode mode) {
		if (hashAlgorithm == null || converter == null || mode == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, converter, mode);
	}

}
