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
package ch.bfh.unicrypt.helper.array;

import ch.bfh.unicrypt.helper.UniCrypt;
import java.lang.reflect.Array;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <A>
 */
abstract public class AbstractArray<A extends AbstractArray<A>>
	   extends UniCrypt {

	protected int length;
	protected int offset;
	protected boolean reverse;

	protected AbstractArray(int length, int offset, boolean reverse) {
		this.length = length;
		this.offset = offset;
		this.reverse = reverse;
	}

	public int getLength() {
		return this.length;
	}

	public boolean isEmpty() {
		return this.length == 0;
	}

	public A extract(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > this.length) {
			throw new IllegalArgumentException();
		}
		if (offset == 0 && length == this.length) {
			return (A) this;
		}
		return abstractExtract(offset, length);
	}

	public A extractPrefix(int length) {
		return this.extract(0, length);
	}

	public A extractSuffix(int length) {
		return this.extract(this.length - length, length);
	}

	public A extractRange(int fromIndex, int toIndex) {
		return this.extract(fromIndex, toIndex - fromIndex + 1);
	}

	public A remove(int offset, int length) {
		A prefix = this.extractPrefix(offset);
		A suffix = this.extractSuffix(this.length - offset - length);
		return prefix.append(suffix);
	}

	// prefix here means the lowest indices
	public A removePrefix(int length) {
		return this.remove(0, length);
	}

	// trailing here means the highest indices
	public A removeSuffix(int length) {
		return this.remove(this.length - length, length);
	}

	public A removeRange(int fromIndex, int toIndex) {
		return this.remove(fromIndex, toIndex - fromIndex + 1);
	}

	public A removeAt(int index) {
		return this.removeRange(index, index);
	}

	public A append(A other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		if (this.length == 0) {
			return other;
		}
		if (other.length == 0) {
			return (A) this;
		}
		return this.abstractAppend(other);
	}

	public A[] split(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		A[] result = (A[]) Array.newInstance(this.getArrayClass(), indices.length + 1);
		int lastIndex = 0;
		for (int i = 0; i < indices.length; i++) {
			int currentIndex = indices[i];
			if (currentIndex < lastIndex || currentIndex > this.length) {
				throw new IllegalArgumentException();
			}
			result[i] = this.extract(lastIndex, currentIndex - lastIndex);
			lastIndex = currentIndex;
		}
		result[indices.length] = this.extract(lastIndex, this.length - lastIndex);
		return result;
	}

	public A reverse() {
		return this.abstractReverse();
	}

	abstract protected A abstractExtract(int offset, int length);

	abstract protected A abstractAppend(A other);

	abstract protected A abstractReverse();

	abstract protected Class getArrayClass();

}
