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
package ch.bfh.unicrypt.helper.array.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.sequence.IntegerSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * This abstract class serves as a base implementation of the {@link ImmutableArray} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <A> The type of a potential non-generic sub-class
 * @param <V> The generic type of the values in the immutable array
 */
abstract public class AbstractImmutableArray<A extends AbstractImmutableArray<A, V>, V>
	   extends UniCrypt
	   implements ImmutableArray<V> {

	private static final long serialVersionUID = 1L;

	protected Class valueClass;
	protected int length;
	protected int rangeOffset;
	protected boolean reverse;

	// A flag indicating whether all elements in the array are identical. The wrapper class Boolean is used
	// to allow the value null for the case that the flag has not yet been computed.
	protected Boolean uniform = null;

	protected AbstractImmutableArray(Class valueClass, int length, int rangeOffset, boolean reverse) {
		this.valueClass = valueClass;
		this.length = length;
		this.rangeOffset = rangeOffset;
		this.reverse = reverse;
		if (length <= 1) {
			this.uniform = true;
		}
	}

	@Override
	public final int getLength() {
		return this.length;
	}

	@Override
	public final boolean isEmpty() {
		return this.length == 0;
	}

	@Override
	public final boolean isUniform() {
		if (this.uniform == null) {
			this.uniform = true;
			if (this.length > 1) {
				V first = this.abstractGetAt(0);
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
	public final Sequence<Integer> getAllIndices() {
		return IntegerSequence.getInstance(0, this.length - 1);
	}

	@Override
	public final Sequence<Integer> getIndices(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return defaultGetIndices(value);
	}

	protected Sequence<Integer> defaultGetIndices(final V value) {
		return this.getAllIndices().filter(index -> abstractGetAt(index).equals(value));
	}

	@Override
	public final Sequence<Integer> getIndicesExcept(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return defaultGetIndicesExcept(value);
	}

	protected Sequence<Integer> defaultGetIndicesExcept(final V value) {
		return this.getAllIndices().filter(index -> !abstractGetAt(index).equals(value));
	}

	@Override
	public final int count(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return (int) this.getAllIndices().count(index -> abstractGetAt(index).equals(value));
	}

	@Override
	public final int countExcept(V value) {
		return this.getLength() - this.count(value);
	}

	@Override
	public final int countPrefix(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (int i : this.getAllIndices()) {
			if (this.abstractGetAt(i).equals(value)) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}

	@Override
	public final int countSuffix(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.abstractGetAt(i).equals(value)) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}

	@Override
	public final V getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
		}
		return this.abstractGetAt(index);
	}

	@Override
	public final V getFirst() {
		return this.getAt(0);
	}

	@Override
	public final V getLast() {
		return this.getAt(this.length - 1);
	}

	@Override
	public final A extract(int index, int length) {
		if (index < 0 || length < 0 || index + length > this.length) {
			throw new IllegalArgumentException();
		}
		if (index == 0 && length == this.length) {
			return (A) this;
		}
		return abstractExtract(index, length);
	}

	@Override
	public final A extractPrefix(int length) {
		return this.extract(0, length);
	}

	@Override
	public final A extractSuffix(int length) {
		return this.extract(this.length - length, length);
	}

	@Override
	public final A extractRange(int fromIndex, int toIndex) {
		return this.extract(fromIndex, toIndex - fromIndex + 1);
	}

	@Override
	public final A remove(int index, int length) {
		if (index < 0 || length < 0 || index + length > this.length) {
			throw new IllegalArgumentException();
		}
		if (length == 0) {
			return (A) this;
		}
		A prefix = this.extractPrefix(index);
		A suffix = this.extractSuffix(this.length - index - length);
		if (prefix.length == 0) {
			return suffix;
		}
		if (suffix.length == 0) {
			return prefix;
		}
		return prefix.append(suffix);
	}

	// prefix here means the lowest indices
	@Override
	public final A removePrefix(int length) {
		return this.remove(0, length);
	}

	// trailing here means the highest indices
	@Override
	public final A removeSuffix(int length) {
		return this.remove(this.length - length, length);
	}

	@Override
	public final A removeRange(int fromIndex, int toIndex) {
		return this.remove(fromIndex, toIndex - fromIndex + 1);
	}

	@Override
	public final A removeAt(int index) {
		return this.removeRange(index, index);
	}

	@Override
	public final A removeFirst() {
		return this.removeAt(0);
	}

	@Override
	public final A removeLast() {
		return this.removeAt(this.length - 1);
	}

	@Override
	public final A append(ImmutableArray<V> other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		if (other.getLength() == 0) {
			return (A) this;
		}
		return this.abstractAppend(other);
	}

	@Override
	public final A insertAt(int index, V value) {
		if (index < 0 || index > this.length) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
		}
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractInsertAt(index, value);
	}

	@Override
	public final A insert(V value) {
		return this.insertAt(0, value);
	}

	@Override
	public final A add(V value) {
		return this.insertAt(this.length, value);
	}

	@Override
	public final A replaceAt(int index, V value) {
		if (index < 0 || index >= this.length) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
		}
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if (this.getAt(index).equals(value)) {
			return (A) this;
		}
		return this.abstractReplaceAt(index, value);
	}

	@Override
	public final A[] split(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		A[] result = (A[]) Array.newInstance(this.valueClass, indices.length + 1);
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
	public final A reverse() {
		return this.abstractReverse();
	}

	@Override
	public final Sequence<V> getSequence() {
		return Sequence.getInstance(this);
	}

	@Override
	public final Iterator<V> iterator() {
		return this.getSequence().iterator();
	}

	@Override
	protected final String defaultToStringType() {
		return "";
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + this.length;
		for (V value : this) {
			hash = 43 * hash + value.hashCode();
		}
		return hash;
	}

	@Override
	public boolean equals(Object value) {
		if (this == value) {
			return true;
		}
		if (value == null || !(value instanceof AbstractImmutableArray)) {
			return false;
		}
		final AbstractImmutableArray other = (AbstractImmutableArray) value;
		if (this.length != other.length) {
			return false;
		}
		for (int i : this.getAllIndices()) {
			if (!this.abstractGetAt(i).equals(other.abstractGetAt(i))) {
				return false;
			}
		}
		return true;
	}

	abstract protected V abstractGetAt(int index);

	abstract protected A abstractExtract(int index, int length);

	abstract protected A abstractInsertAt(int index, V value);

	abstract protected A abstractReplaceAt(int index, V value);

	abstract protected A abstractAppend(ImmutableArray<V> other);

	abstract protected A abstractReverse();

}
