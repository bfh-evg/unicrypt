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
import java.nio.ByteOrder;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class HashMethod
	   extends UniCrypt {

	private final HashAlgorithm hashAlgorithm;
	private final ByteOrder byteOrder;
	private final boolean recursive;

	protected HashMethod(HashAlgorithm hashAlgorithm, ByteOrder byteOrder, boolean recursive) {
		this.hashAlgorithm = hashAlgorithm;
		this.byteOrder = byteOrder;
		this.recursive = recursive;
	}

	public HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	public ByteOrder getByteOrder() {
		return this.byteOrder;
	}

	public boolean isRecursive() {
		return this.recursive;
	}

	@Override
	public String defaultToStringValue() {
		String str = this.hashAlgorithm.toString() + "," + this.byteOrder;
		if (this.isRecursive()) {
			str = str + ",recursive";
		}
		return str;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + this.hashAlgorithm.hashCode();
		hash = 97 * hash + this.byteOrder.hashCode();
		hash = 97 * hash + (this.recursive ? 1 : 0);
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
		return this.hashAlgorithm.equals(other.hashAlgorithm) && this.byteOrder.equals(other.byteOrder) && this.recursive == other.recursive;
	}

	public static HashMethod getInstance() {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), ByteOrder.BIG_ENDIAN, true);
	}

	public static HashMethod getInstance(HashAlgorithm hashAlgorithm) {
		return HashMethod.getInstance(hashAlgorithm, ByteOrder.BIG_ENDIAN, true);
	}

	public static HashMethod getInstance(HashAlgorithm hashAlgorithm, ByteOrder byteOrder, boolean recursive) {
		if (hashAlgorithm == null || byteOrder == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, byteOrder, recursive);
	}

}
