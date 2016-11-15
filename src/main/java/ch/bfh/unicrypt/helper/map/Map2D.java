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

/**
 * This interface specifies a generic 2-dimensional map for storing values with two keys.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <K1> The type of the first key
 * @param <K2> The type of the second key
 * @param <V>  The type of the values stored in the map
 */
public interface Map2D<K1, K2, V> {

	/**
	 * Retrieves a the value stored in the map for two given key. If no value exists for the given key, {@code null} is
	 * returned.
	 * <p>
	 * @param key1 The first key
	 * @param key2 The second key
	 * @return The value stored in the map, or {@code null} if no such value exists
	 */
	public V get(K1 key1, K2 key2);

	/**
	 * Stores a new value in the map for two given keys. If a value already exists for the given key, it is overridden.
	 * {@code null} is accepted as key and as value.
	 * <p>
	 * @param key1  The first key
	 * @param key2  The second key
	 * @param value The value to be stored in the map
	 */
	public void put(K1 key1, K2 key2, V value);

}
