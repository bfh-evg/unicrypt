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
package ch.bfh.unicrypt.helper.map;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * This class provides a LRU (Least Recently Used) hash map, which discards the least recently used elements first when
 * the maximal size of the map is reached.
 *
 * @author R. Haenni
 * @version 2.0
 * @param <K> The type of the keys
 * @param <V> The type of the values stored in the map
 */
public class LRUHashMap< K, V>
	   extends LinkedHashMap< K, V> {

	// same default values as in LinkedHashMap
	private static final int INITIAL_CAPACITY = 16;
	private static final float LOAD_FACTOR = 0.75F;
	private static final int DEFAULT_MAX_SIZE = 100;

	// maximum number of elements in the map
	private final int maxSize;

	private LRUHashMap(int maxSize) {
		// accessOrder = true for 'last recently used'
		super(INITIAL_CAPACITY, LOAD_FACTOR, true);
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Entry<K, V> entry) {
		return size() > this.maxSize;
	}

	/**
	 * Returns a new instance of this class, an initially empty LRU hash map of size {@code DEFAULT_MAX_SIZE}.
	 * <p>
	 * @param <K> The type of the first key
	 * @param <V> The type of the values stored in the map
	 * @return The new LRU hash map
	 */
	public static <K, V> LRUHashMap<K, V> getInstance() {
		return new LRUHashMap<>(DEFAULT_MAX_SIZE);
	}

	/**
	 * Returns a new instance of this class, an initially empty LRU hash map of size {@code maxSize}.
	 * <p>
	 * @param <K>     The type of the first key
	 * @param <V>     The type of the values stored in the map
	 * @param maxSize The maximal size of the hash map
	 * @return The new LRU hash map
	 */
	public static <K, V> LRUHashMap<K, V> getInstance(int maxSize) {
		return new LRUHashMap<>(maxSize);
	}

}
