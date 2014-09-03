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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <A>
 * @param <T>
 */
abstract public class AbstractArray<A extends AbstractArray<A, T>, T extends Object>
	   extends UniCrypt
	   implements ch.bfh.unicrypt.helper.array.Array<A, T> {

	protected int length;
	protected int offset;
	protected boolean reverse;
	protected Boolean uniform = null;

	protected AbstractArray(int length, int offset, boolean reverse) {
		this.length = length;
		this.offset = offset;
		this.reverse = reverse;
	}

	@Override
	public int getLength() {
		return this.length;
	}

	@Override
	public boolean isEmpty() {
		return this.length == 0;
	}

	@Override
	public boolean isUniform() {
		if (this.uniform == null) {
			this.uniform = true;
			if (this.length > 1) {
				T first = this.abstractGetAt(0);
				for (int i = 1; i < this.length; i++) {
					if (!first.equals(this.abstractGetAt(i))) {
						this.uniform = false;
						break;
					}
				}
			}
		}
		return this.uniform;
	}

	@Override
	public int count(T object) {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (this.abstractGetAt(i).equals(object)) {
				result++;
			}
		}
		return result;
	}

	@Override
	public int countPrefix(T object) {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (this.abstractGetAt(i).equals(object)) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}

	@Override
	public int countSuffix(T object) {
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.abstractGetAt(i).equals(object)) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}

	@Override
	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < this.length; i++) {
			result.add(this.abstractGetAt(i));
		}
		return result;
	}

	@Override
	public T getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.abstractGetAt(index);
	}

	@Override
	public T getFirst() {
		return this.getAt(0);
	}

	@Override
	public T getLast() {
		return this.getAt(this.length - 1);
	}

	@Override
	public A extract(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > this.length) {
			throw new IllegalArgumentException();
		}
		if (offset == 0 && length == this.length) {
			return (A) this;
		}
		return abstractExtract(offset, length);
	}

	@Override
	public A extractPrefix(int length) {
		return this.extract(0, length);
	}

	@Override
	public A extractSuffix(int length) {
		return this.extract(this.length - length, length);
	}

	@Override
	public A extractRange(int fromIndex, int toIndex) {
		return this.extract(fromIndex, toIndex - fromIndex + 1);
	}

	@Override
	public A remove(int offset, int length) {
		A prefix = this.extractPrefix(offset);
		A suffix = this.extractSuffix(this.length - offset - length);
		return prefix.append(suffix);
	}

	// prefix here means the lowest indices
	@Override
	public A removePrefix(int length) {
		return this.remove(0, length);
	}

	// trailing here means the highest indices
	@Override
	public A removeSuffix(int length) {
		return this.remove(this.length - length, length);
	}

	@Override
	public A removeRange(int fromIndex, int toIndex) {
		return this.remove(fromIndex, toIndex - fromIndex + 1);
	}

	@Override
	public A removeAt(int index) {
		return this.removeRange(index, index);
	}

	@Override
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

	@Override
	public A add(T object) {
		return this.insertAt(this.length, object);
	}

	@Override
	public A insertAt(int index, T newObject) {
		if (index < 0 || index > this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (newObject == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractInsertAt(index, newObject);
	}

	@Override
	public A replaceAt(int index, T newObject) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (newObject == null) {
			throw new IllegalArgumentException();
		}
		if (this.getAt(index).equals(newObject)) {
			return (A) this;
		}
		return this.abstractReplaceAt(index, newObject);
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

	@Override
	public A reverse() {
		return this.abstractReverse();
	}

	abstract protected T abstractGetAt(int index);

	abstract protected A abstractExtract(int offset, int length);

	abstract protected A abstractAppend(A other);

	abstract protected A abstractInsertAt(int offset, T newObject);

	abstract protected A abstractReplaceAt(int offset, T newObject);

	abstract protected A abstractReverse();

	abstract protected Class getArrayClass();

}
