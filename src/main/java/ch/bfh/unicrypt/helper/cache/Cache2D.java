/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2017 Bern University of Applied Sciences (BFH), Research Institute for
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

/**
 * This class provides a thread-safe 2-dimensional cache based on a LRU (Least Recently Used) hash map. Each value in
 * the cache is associated with two keys. The cache discards the least recently used entries first when the maximal size
 * of the cache is reached.
 *
 * @param <K1> The type of the first keys
 * @param <K2> The type of the second keys
 * @param <V>  The type of the values stored in the cache
 */
public class Cache2D<K1, K2, V>
	   extends Cache<Cache2D.KeyPair, V> {

	/**
	 * Returns a new medium-sized instance of this class, an initially empty 2-dimensional cache of size
	 * {@link Cache#SIZE_M}.
	 */
	public Cache2D() {
		super();
	}

	/**
	 * Returns a new instance of this class, an initially empty 2-dimensional cache of size {@code maxSize}.
	 *
	 * @param maxSize The maximal size of the cache
	 */
	public Cache2D(int maxSize) {
		super(maxSize);
	}

	/**
	 * Returns the value associated to the given pair of keys in the cache. Return {@code null} if the key pair does not
	 * exist.
	 *
	 * @param key1 The first key whose associated value is to be returned
	 * @param key2 The second key whose associated value is to be returned
	 * @return The value associated to the pair of keys, or {@code null} the keys do not exist
	 */
	public V get(K1 key1, K2 key2) {
		if (key1 == null || key2 == null) {
			throw new IllegalArgumentException();
		}
		KeyPair key = new KeyPair(key1, key2);
		return this.get(key);
	}

	/**
	 * Adds a new entry to the cache. If the pair of keys already exists, the value is replaced.
	 *
	 * @param key1  The first key with which the value is to be associated
	 * @param key2  The second key with which the value is to be associated
	 * @param value The value to be associated with the key
	 */
	public void put(K1 key1, K2 key2, V value) {
		if (key1 == null || key2 == null) {
			throw new IllegalArgumentException();
		}
		KeyPair key = new KeyPair(key1, key2);
		this.put(key, value);
	}

	protected class KeyPair {

		private final K1 first;
		private final K2 second;

		protected KeyPair(K1 first, K2 second) {
			this.first = first;
			this.second = second;
		}

		public K1 getFirst() {
			return this.first;
		}

		public K2 getSecond() {
			return this.second;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 29 * hash + this.first.hashCode();
			hash = 29 * hash + this.second.hashCode();
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final KeyPair other = (KeyPair) obj;
			return this.first.equals(other.first) && this.second.equals(other.second);
		}

	}

}
