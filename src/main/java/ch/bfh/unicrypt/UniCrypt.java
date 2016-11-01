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
package ch.bfh.unicrypt;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import java.io.Serializable;

/**
 * This is the base class for all classes in this library. Its main purpose is providing a consistent string
 * representations by pre-defining {@link Object#toString()}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class UniCrypt
	   implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public final String toString() {
		String str1 = this.defaultToStringType();
		String str2 = this.defaultToStringContent();
		if (str1.length() == 0) {
			return str2;
		}
		if (str2.length() == 0) {
			return str1;
		}
		return str1 + "[" + str2 + "]";
	}

	// default implementation for producing a string describing the type of the object
	protected String defaultToStringType() {
		return this.getClass().getSimpleName();
	}

	// default implementation for producing a string describing the content of the object
	protected String defaultToStringContent() {
		return "";
	}

	// this local class allows creating instances of ByteArray without copying the underlying byte array
	protected static class SafeByteArray
		   extends ByteArray {

		public SafeByteArray(byte[] bytes) {
			super(bytes);
		}

		@Override
		public byte[] getBytes() {
			return this.bytes;
		}

	}

}
