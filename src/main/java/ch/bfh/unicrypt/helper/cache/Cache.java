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
package ch.bfh.unicrypt.helper.cache;

import ch.bfh.unicrypt.UniCrypt;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class provides a thread-safe cache based on a LRU (Least Recently Used) hash map. The cache discards the least
 * recently used entries first when the maximal size of the cache is reached.
 *
 * @author R. Haenni
 * @version 2.0
 * @param <K> The type of the keys
 * @param <V> The type of the values stored in the cache
 */
public class Cache<K, V>
	   extends UniCrypt {

	public static final int SIZE_XS = 10;
	public static final int SIZE_S = 100;
	public static final int SIZE_M = 1000;
	public static final int SIZE_L = 1000;
	public static final int SIZE_XL = 10000;
	public static final int SIZE_XXL = 100000;

	// same default values as in LinkedHashMap
	private static final int INITIAL_CAPACITY = 16;
	private static final float LOAD_FACTOR = 0.75F;

	// internal linked hash map
	private final Map<K, V> map;

	/**
	 * Returns a new medium-sized instance of this class, an initially empty cache of size {@link Cache#SIZE_M}.
	 */
	public Cache() {
		this(SIZE_M);
	}

	/**
	 * Returns a new instance of this class, an initially empty cache of size {@code maxSize}.
	 *
	 * @param maxSize The maximal size of the cache
	 */
	public Cache(int maxSize) {
		// accessOrder = true for 'last recently used'
		this.map = Collections.synchronizedMap(new LinkedHashMap<K, V>(INITIAL_CAPACITY, LOAD_FACTOR, true) {
			@Override
			protected boolean removeEldestEntry(Entry<K, V> entry) {
				return this.size() > maxSize;
			}

		});
	}

	/**
	 * Returns the number of entries in the cache.
	 *
	 * @return The number of entries
	 */
	public int getSize() {
		return this.map.size();
	}

	/**
	 * Returns the value associated to the given key in the cache. Return {@code null} if the key does not exist.
	 *
	 * @param key The key whose associated value is to be returned
	 * @return The value associated to the key, or {@code null} the key does not exist
	 */
	public V get(K key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		return this.map.get(key);
	}

	/**
	 * Adds a new entry to the cache. If the key already exists, the value is replaced.
	 *
	 * @param key   The key with which the value is to be associated
	 * @param value The value to be associated with the key
	 */
	public void put(K key, V value) {
		if (key == null || value == null) {
			throw new IllegalArgumentException();
		}
		this.map.put(key, value);
	}

}
