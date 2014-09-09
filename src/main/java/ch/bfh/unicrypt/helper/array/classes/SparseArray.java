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

import ch.bfh.unicrypt.helper.array.abstracts.AbstractArrayWithDefault;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class SparseArray<T extends Object>
	   extends AbstractArrayWithDefault<SparseArray<T>, T> {

	private final Map<Integer, T> map;

	private SparseArray(T defaultObject, Map<Integer, T> map, int length) {
		this(defaultObject, map, 0, length, 0, 0, false);
	}

	private SparseArray(T defaultObject, Map<Integer, T> map, int offset, int length, int trailer, int header, boolean reverse) {
		super(defaultObject, trailer, header, length, offset, reverse);
		this.map = map;
		if (map.isEmpty()) {
			this.uniform = true;
		}
	}

	// this method is more efficient than its predecessor
	@Override
	public Iterable<Integer> getIndices(T object) {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		if (this.defaultObject.equals(object)) {
			return super.getIndices(object);
		}
		List<Integer> result = new LinkedList<Integer>();
		for (Integer i : this.map.keySet()) {
			if (i >= this.offset && i < this.offset + this.length - this.header - this.trailer) {
				if (this.map.get(i).equals(object)) {
					result.add(this.getIndex(i));
				}
			}
		}
		return result;
	}

	// this method is more efficient than its predecessor
	@Override
	public Iterable<Integer> getIndicesExcept(T object) {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		if (!this.defaultObject.equals(object)) {
			return super.getIndicesExcept(object);
		}
		List<Integer> result = new LinkedList<Integer>();
		for (Integer i : this.map.keySet()) {
			if (i >= this.offset && i < this.offset + this.length - this.header - this.trailer) {
				if (!this.map.get(i).equals(this.defaultObject)) {
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

	public static <T> SparseArray<T> getInstance(T defaultObject, int length) {
		return SparseArray.getInstance(defaultObject, new HashMap<Integer, T>(), length);
	}

	public static <T> SparseArray<T> getInstance(T defaultObject, Map<Integer, T> map) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		int maxIndex = 0;
		for (Integer i : map.keySet()) {
			maxIndex = Math.max(maxIndex, i);
		}
		return SparseArray.getInstance(defaultObject, map, maxIndex + 1);
	}

	public static <T> SparseArray<T> getInstance(T defaultObject, Map<Integer, T> map, int length) {
		if (defaultObject == null || map == null || length < 0) {
			throw new IllegalArgumentException();
		}
		Map<Integer, T> newMap = new HashMap();
		for (Integer i : map.keySet()) {
			T object = map.get(i);
			if (object == null || i >= length) {
				throw new IllegalArgumentException();
			}
			if (!object.equals(defaultObject)) {
				newMap.put(i, object);
			}
		}
		return new SparseArray<T>(defaultObject, newMap, 0, length, 0, 0, false);
	}

	public static <T> SparseArray<T> getInstance(T defaultObject, T... objects) {
		if (defaultObject == null || objects == null) {
			throw new IllegalArgumentException();
		}
		Map<Integer, T> map = new HashMap<Integer, T>();
		int i = 0;
		for (T object : objects) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
			if (!object.equals(defaultObject)) {
				map.put(i, object);
			}
			i++;
		}
		return new SparseArray<T>(defaultObject, map, 0, objects.length, 0, 0, false);
	}

	@Override
	protected T abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		if (index < this.trailer || index >= this.length - this.header) {
			return this.defaultObject;
		}
		return this.map.getOrDefault(index - this.trailer + this.offset, this.defaultObject);
	}

	@Override
	protected SparseArray<T> abstractAppend(SparseArray<T> other) {
		Map<Integer, T> newMap = new HashMap<Integer, T>();
		for (int i : this.getIndicesExcept()) {
			newMap.put(i, this.abstractGetAt(i));
		}
		Iterable<Integer> indices;
		if (this.defaultObject.equals(other.defaultObject)) {
			indices = other.getIndicesExcept();
		} else {
			indices = other.getIndicesExcept(this.defaultObject);
		}
		for (int i : indices) {
			newMap.put(this.length + i, other.abstractGetAt(i));
		}
		return new SparseArray<T>(this.defaultObject, newMap, this.length + other.length);
	}

	@Override
	protected SparseArray<T> abstractInsertAt(int index, T newObject
	) {
		Map<Integer, T> newMap = new HashMap<Integer, T>();
		for (int i : this.getIndicesExcept()) {
			if (i < index) {
				newMap.put(i, this.abstractGetAt(i));
			} else {
				newMap.put(i + 1, this.abstractGetAt(i));
			}
		}
		if (!newObject.equals(this.defaultObject)) {
			newMap.put(index, newObject);
		}
		return new SparseArray<T>(this.defaultObject, newMap, this.length + 1);
	}

	@Override
	protected SparseArray<T> abstractReplaceAt(int index, T newObject
	) {
		Map<Integer, T> newMap = new HashMap<Integer, T>();
		for (int i : this.getIndicesExcept()) {
			if (i != index) {
				newMap.put(i, this.abstractGetAt(i));
			}
		}
		if (!newObject.equals(this.defaultObject)) {
			newMap.put(index, newObject);
		}
		return new SparseArray<T>(this.defaultObject, newMap, this.length);
	}

	@Override
	protected SparseArray<T> abstractGetInstance(int offset, int length, int trailer, int header, boolean reverse
	) {
		return new SparseArray<T>(this.defaultObject, this.map, offset, length, trailer, header, reverse);
	}

	@Override
	protected Class getArrayClass() {
		return SparseArray.class;
	}

	private int getIndex(int i) {
		if (this.reverse) {
			return this.length - i - 1 + this.offset - this.trailer;
		}
		return i - this.offset + this.trailer;
	}

}
