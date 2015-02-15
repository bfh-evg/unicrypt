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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.array.abstracts.AbstractDefaultValueArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <V>
 */
public class SparseArray<V extends Object>
	   extends AbstractDefaultValueArray<SparseArray<V>, V> {

	private final Map<Integer, V> map;

	private SparseArray(Map<Integer, V> map, int length, V defaultValue) {
		this(map, length, 0, false, defaultValue, 0, 0, length);
	}

	private SparseArray(Map<Integer, V> map, int length, int rangeOffset, boolean reverse, V defaultValue, int trailer, int header, int rangeLength) {
		super(SparseArray.class, defaultValue, length, rangeOffset, rangeLength, trailer, header, reverse);
		this.map = map;
		if (map.isEmpty() || (map.size() == 1 && length == rangeLength)) {
			this.uniform = true;
		}
	}

	// this method is more efficient than its predecessor
	@Override
	protected Iterable<Integer> defaultGetIndices(V value) {
		if (this.defaultValue.equals(value)) {
			return super.defaultGetIndices(value);
		}
		List<Integer> result = new LinkedList<Integer>();
		for (Integer i : this.map.keySet()) {
			if (i >= this.rangeOffset && i < this.rangeOffset + this.rangeLength) {
				if (this.map.get(i).equals(value)) {
					result.add(this.getIndex(i));
				}
			}
		}
		return result;
	}

	// this method is more efficient than its predecessor
	@Override
	protected Iterable<Integer> defaultGetIndicesExcept(V value) {
		if (!this.defaultValue.equals(value)) {
			return super.defaultGetIndicesExcept(value);
		}
		List<Integer> result = new LinkedList<Integer>();
		for (Integer i : this.map.keySet()) {
			if (i >= this.rangeOffset && i < this.rangeOffset + this.length - this.header - this.trailer) {
				if (!this.map.get(i).equals(this.defaultValue)) {
					result.add(this.getIndex(i));
				}
			}
		}
		return result;
	}

	@Override
	protected String defaultToStringValue() {
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

	public static <T> SparseArray<T> getInstance(T defaultValue, int length) {
		return SparseArray.getInstance(defaultValue, new HashMap<Integer, T>(), length);
	}

	public static <T> SparseArray<T> getInstance(T defaultValue, Map<Integer, T> map) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		int maxIndex = 0;
		for (Integer i : map.keySet()) {
			maxIndex = Math.max(maxIndex, i);
		}
		return SparseArray.getInstance(defaultValue, map, maxIndex + 1);
	}

	public static <T> SparseArray<T> getInstance(T defaultValue, Map<Integer, T> map, int rangeLength) {
		if (defaultValue == null || map == null || rangeLength < 0) {
			throw new IllegalArgumentException();
		}
		Map<Integer, T> newMap = new HashMap();
		for (Integer i : map.keySet()) {
			T value = map.get(i);
			if (value == null || i >= rangeLength) {
				throw new IllegalArgumentException();
			}
			if (!value.equals(defaultValue)) {
				newMap.put(i, value);
			}
		}
		return new SparseArray<T>(newMap, rangeLength, defaultValue);
	}

	public static <T> SparseArray<T> getInstance(T defaultValue, T... values) {
		if (defaultValue == null || values == null) {
			throw new IllegalArgumentException();
		}
		Map<Integer, T> map = new HashMap<Integer, T>();
		int i = 0;
		for (T value : values) {
			if (value == null) {
				throw new IllegalArgumentException();
			}
			if (!value.equals(defaultValue)) {
				map.put(i, value);
			}
			i++;
		}
		return new SparseArray<T>(map, values.length, defaultValue);
	}

	public static <T> SparseArray<T> getInstance(T defaultValue, ImmutableArray<T> immutableArray) {
		if (defaultValue == null || immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof SparseArray) {
			return (SparseArray) immutableArray; // defaultValue is ignored
		}
		Map<Integer, T> map = new HashMap<Integer, T>();
		int i = 0;
		for (T value : immutableArray) {
			if (!value.equals(defaultValue)) {
				map.put(i++, value);
			}
		}
		return new SparseArray<T>(map, immutableArray.getLength(), defaultValue);
	}

	@Override
	protected V abstractGetValueAt(int index) {
		// JAVA 8: return this.map.getOrDefault(this.rangeOffset + index, this.defaultValue);
		V result = this.map.get(index);
		if (result == null) {
			return this.defaultValue;
		}
		return result;
	}

	@Override
	protected SparseArray abstractAppend(ImmutableArray<V> other) {
		Map<Integer, V> newMap = new HashMap<Integer, V>();
		for (int i : this.getIndicesExcept()) {
			newMap.put(i, this.abstractGetAt(i));
		}
		for (int i : other.getIndicesExcept(this.defaultValue)) {
			newMap.put(this.length + i, other.getAt(i));
		}
		return new SparseArray<V>(newMap, this.length + other.getLength(), this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractInsertAt(int index, V newObject) {
		Map<Integer, V> newMap = new HashMap<Integer, V>();
		for (int i : this.getIndicesExcept()) {
			if (i < index) {
				newMap.put(i, this.abstractGetAt(i));
			} else {
				newMap.put(i + 1, this.abstractGetAt(i));
			}
		}
		if (!newObject.equals(this.defaultValue)) {
			newMap.put(index, newObject);
		}
		return new SparseArray<V>(newMap, this.length + 1, this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractReplaceAt(int index, V newObject) {
		Map<Integer, V> newMap = new HashMap<Integer, V>();
		for (int i : this.getIndicesExcept()) {
			if (i != index) {
				newMap.put(i, this.abstractGetAt(i));
			}
		}
		if (!newObject.equals(this.defaultValue)) {
			newMap.put(index, newObject);
		}
		return new SparseArray<V>(newMap, this.length, this.defaultValue);
	}

	@Override
	protected SparseArray<V> abstractReverse() {
		// switch trailer and header
		return new SparseArray<V>(this.map, this.length, this.rangeOffset, !this.reverse, this.defaultValue, this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected SparseArray<V> abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new SparseArray<V>(this.map, length, rangeOffset, this.reverse, this.defaultValue, trailer, header, rangeLength);
	}

	private int getIndex(int rangeIndex) {
		if (this.reverse) {
			return this.rangeLength - rangeIndex - 1 + this.rangeOffset + this.trailer;
		}
		return rangeIndex - this.rangeOffset + this.trailer;
	}

}
