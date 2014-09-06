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
	   extends AbstractArray<SparseArray<T>, T> {

	private final T defaultObject;
	private final Map<Integer, T> map;

	public SparseArray(T defaultObject, Map<Integer, T> map, int length, int offset, boolean reverse) {
		super(length, offset, reverse);
		this.defaultObject = defaultObject;
		this.map = map;
	}

	// collects the indices of the objects different from the default
	public List<Integer> getIndices() {
		List<Integer> result = new LinkedList<Integer>();
		for (Integer i : this.map.keySet()) {
			if (!this.map.get(i).equals(this.defaultObject)) {
				result.add(this.reverse ? i - this.offset : this.length - i - 1 + offset);
			}
		}
		return result;
	}

	// collects the indices a given object
	@Override
	public List<Integer> getIndices(T object) {
		List<Integer> result = new LinkedList<Integer>();
		if (object.equals(this.defaultObject)) {
			for (int i = 0; i < this.length; i++) {
				if (this.abstractGetAt(i).equals(object)) {
					result.add(i);
				}
			}
		} else {
			for (Integer i : this.map.keySet()) {
				if (this.map.get(i).equals(object)) {
					result.add(this.reverse ? i - this.offset : this.length - i - 1 + offset);
				}
			}
		}
		return result;
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
		return SparseArray.getInstance(defaultObject, maxIndex);
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
		return new SparseArray<T>(defaultObject, map, length, 0, false);
	}

	@Override
	protected T abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		return this.map.getOrDefault(this.offset + index, this.defaultObject);
	}

	@Override
	protected SparseArray<T> abstractExtract(int fromIndex, int length) {
		if (this.reverse) {
			fromIndex = this.length - fromIndex - length;
		}
		return new SparseArray<T>(this.defaultObject, this.map, this.offset + fromIndex, length, this.reverse);
	}

	@Override
	protected SparseArray<T> abstractAppend(SparseArray<T> other) {
		if (this.defaultObject.equals(other.defaultObject)) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
		return null;
		// TODO
	}

	@Override
	protected SparseArray<T> abstractInsertAt(int index, T newObject) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		Map<Integer, T> newMap = new HashMap<Integer, T>();
		for (int i : this.getIndices()) {
			if (i < index) {
				newMap.put(i, this.abstractGetAt(index));
			} else {
				newMap.put(i + 1, this.abstractGetAt(index));
			}
		}
		newMap.put(index, newObject);
		return new SparseArray<T>(this.defaultObject, newMap, this.length + 1, 0, this.reverse);
	}

	@Override
	protected SparseArray<T> abstractReplaceAt(int index, T newObject) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		Map<Integer, T> newMap = new HashMap<Integer, T>();
		for (int i : this.getIndices()) {
			if (i != index) {
				newMap.put(i, this.abstractGetAt(index));
			}
		}
		newMap.put(index, newObject);
		return new SparseArray<T>(this.defaultObject, newMap, this.length, 0, this.reverse);
	}

	@Override
	protected SparseArray<T> abstractReverse() {
		return new SparseArray<T>(this.defaultObject, this.map, this.offset, this.length, !this.reverse);
	}

	@Override
	protected Class getArrayClass() {
		return SparseArray.class;
	}

}
