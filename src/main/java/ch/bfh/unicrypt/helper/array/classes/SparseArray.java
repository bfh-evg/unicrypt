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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.array.abstracts.AbstractDefaultValueArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is a default implementation of the {@link ImmutableArray} interface. It is optimized for sparse arrays
 * with a designated default value. Internally, the values are stored in a hash map. The implementation is optimized to
 * provide O(1) running times for most operations. Compared to {@link DenseArray}, if consumes less memory if the
 * spareness of the array is high.
 * <p>
 * @see DenseArray
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the immutable array
 */
public class SparseArray<V>
	   extends AbstractDefaultValueArray<SparseArray<V>, V> {

	private static final long serialVersionUID = 1L;

	private final Map<Integer, V> map;

	private SparseArray(Map<Integer, V> map, int length, V defaultValue) {
		this(map, length, 0, false, defaultValue, 0, 0, length);
	}

	private SparseArray(Map<Integer, V> map, int length, int rangeOffset, boolean reverse, V defaultValue, int trailer,
		   int header, int rangeLength) {
		super(SparseArray.class, defaultValue, length, rangeOffset, rangeLength, trailer, header, reverse);
		this.map = map;
		if (map.isEmpty() || (map.size() == 1 && length == rangeLength)) {
			this.uniform = true;
		}
	}

	/**
	 * Creates a new sparse array of a given length. All its values are identical to the given default value. This
	 * method is a special case of {@link SparseArray#getInstance(java.lang.Object, java.util.Map, int)} with a n empty
	 * map.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param length       The length of the new array
	 * @return The new sparse array
	 * @see SparseArray#getInstance(java.lang.Object, java.util.Map, int)
	 * @see DenseArray#getInstance(java.lang.Object, int)
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, int length) {
		return SparseArray.getInstance(defaultValue, new HashMap<Integer, V>(), length);
	}

	/**
	 * Creates a new sparse array from a given map of (index,value) pairs. The length of the resulting array corresponds
	 * to the maximum index in the map plus 1. For example, a map of pairs (3,v1), (12,v2), (25,v3) leads to an array of
	 * length 26 with v1 at index 3, v2 at index 12, and v3 at index 25. All other values with another index correspond
	 * to the default value. This method is a special case of
	 * {@link SparseArray#getInstance(java.lang.Object, java.util.Map, int)} with the length set to the maximal index
	 * plus 1.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param map          The given map of (index,value) pairs
	 * @return The new sparse array
	 * @see SparseArray#getInstance(java.lang.Object, java.util.Map, int)
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, Map<Integer, V> map) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		int maxIndex = 0;
		for (Integer i : map.keySet()) {
			maxIndex = Math.max(maxIndex, i);
		}
		return SparseArray.getInstance(defaultValue, map, maxIndex + 1);
	}

	/**
	 * Creates a new sparse array from a given map of (index,value) pairs and a given total length. For example, a map
	 * of pairs (3,v1), (12,v2), (25,v3) and a given length 30 leads to an array of length 30 with v1 at index 3, v2 at
	 * index 12, and v3 at index 25. All values with another index correspond to the default value.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param map          The given map of (index,value) pairs
	 * @param length       The length of the new array
	 * @return The new sparse array
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, Map<Integer, V> map, int length) {
		if (defaultValue == null || map == null || length < 0) {
			throw new IllegalArgumentException();
		}
		Map<Integer, V> newMap = new HashMap();
		for (Entry<Integer, V> entry : map.entrySet()) {
			int i = entry.getKey();
			V value = entry.getValue();
			if (value == null || i < 0 || i >= length) {
				throw new IllegalArgumentException();
			}
			if (!value.equals(defaultValue)) {
				newMap.put(i, value);
			}
		}
		return new SparseArray<>(newMap, length, defaultValue);
	}

	/**
	 * Creates a new sparse array with a single non-default value at some index.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param index        The index of the value
	 * @param value        The given value
	 * @return The new sparse array
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, int index, V value) {
		Map<Integer, V> map = new HashMap<>(1);
		map.put(index, value);
		return SparseArray.getInstance(defaultValue, map);
	}

	/**
	 * Creates a new sparse array from a given Java array of values. The Java array is transformed into a map for
	 * internal storage. The length and the indices of the values of the resulting sparse array correspond to the given
	 * Java array.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param values       The Java array of values
	 * @return The new sparse array
	 * @see DenseArray#getInstance(Object...)
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, V... values) {
		return SparseArray.getInstance(defaultValue, Sequence.getInstance(values));
	}

	/**
	 * Creates a new sparse array from a given sequence of values. The sequence is transformed into a map for internal
	 * storage. Null values are eliminated.
	 * <p>
	 * @param <V>          The generic type of the new array
	 * @param defaultValue The default value of the new array
	 * @param values       The given sequence of values
	 * @return The new sparse array
	 */
	public static <V> SparseArray<V> getInstance(V defaultValue, Sequence<V> values) {
		if (defaultValue == null || values == null || values.isInfinite()) {
			throw new IllegalArgumentException();
		}
		Map<Integer, V> map = new HashMap<>();
		int i = 0;
		for (V value : values.filter(Sequence.NOT_NULL)) {
			if (!value.equals(defaultValue)) {
				map.put(i, value);
			}
			i++;
		}
		return new SparseArray<>(map, i, defaultValue);
	}

	@Override
	// this method is more efficient than its predecessor
	protected Sequence<Integer> defaultGetIndices(V value) {
		if (this.defaultValue.equals(value)) {
			return super.defaultGetIndices(value);
		}
		List<Integer> result = new LinkedList<>();
		for (Entry<Integer, V> entry : this.map.entrySet()) {
			int i = entry.getKey();
			if (i >= this.rangeOffset && i < this.rangeOffset + this.rangeLength && entry.getValue().equals(value)) {
				result.add(this.getIndex(i));
			}
		}
		return Sequence.getInstance(result);
	}

	@Override
	// this method is more efficient than its predecessor
	protected Sequence<Integer> defaultGetIndicesExcept(V value) {
		if (!this.defaultValue.equals(value)) {
			return super.defaultGetIndicesExcept(value);
		}
		List<Integer> result = new LinkedList<>();
		for (Entry<Integer, V> entry : this.map.entrySet()) {
			int i = entry.getKey();
			if (i >= this.rangeOffset && i < this.rangeOffset + this.length - this.header - this.trailer
				   && !entry.getValue().equals(this.defaultValue)) {
				result.add(this.getIndex(i));
			}
		}
		return Sequence.getInstance(result);
	}

	@Override
	protected String defaultToStringContent() {
		if (this.isEmpty()) {
			return "[]";
		}
		String str = "";
		String delimiter = "";
		for (int i : this.getIndicesExcept()) {
			str = str + delimiter + i + "->" + this.getAt(i);
			delimiter = ", ";
		}
		return "[" + this.getLength() + ": " + str + "]";
	}

	@Override
	protected V abstractGetValueAt(int index) {
		return this.map.getOrDefault(index, this.defaultValue);
	}

	@Override
	protected SparseArray abstractAppend(ImmutableArray<V> other) {
		Map<Integer, V> newMap = new HashMap<>();
		for (int i : this.getIndicesExcept()) {
			newMap.put(i, this.abstractGetAt(i));
		}
		for (int i : other.getIndicesExcept(this.defaultValue)) {
			newMap.put(this.length + i, other.getAt(i));
		}
		return new SparseArray<>(newMap, this.length + other.getLength(), this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractInsertAt(int index, V value) {
		Map<Integer, V> newMap = new HashMap<>();
		for (int i : this.getIndicesExcept()) {
			if (i < index) {
				newMap.put(i, this.abstractGetAt(i));
			} else {
				newMap.put(i + 1, this.abstractGetAt(i));
			}
		}
		if (!value.equals(this.defaultValue)) {
			newMap.put(index, value);
		}
		return new SparseArray<>(newMap, this.length + 1, this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractReplaceAt(int index, V value) {
		Map<Integer, V> newMap = new HashMap<>();
		for (int i : this.getIndicesExcept()) {
			if (i != index) {
				newMap.put(i, this.abstractGetAt(i));
			}
		}
		if (!value.equals(this.defaultValue)) {
			newMap.put(index, value);
		}
		return new SparseArray<>(newMap, this.length, this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractReverse() {
		// switch trailer and header
		return new SparseArray<>(this.map, this.length, this.rangeOffset, !this.reverse, this.defaultValue,
								 this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected SparseArray<V> abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer,
		   int header) {
		return new SparseArray<>(this.map, length, rangeOffset, this.reverse, this.defaultValue, trailer, header,
								 rangeLength);
	}

	private int getIndex(int rangeIndex) {
		if (this.reverse) {
			return this.rangeLength - rangeIndex - 1 + this.rangeOffset + this.trailer;
		}
		return rangeIndex - this.rangeOffset + this.trailer;
	}

}
