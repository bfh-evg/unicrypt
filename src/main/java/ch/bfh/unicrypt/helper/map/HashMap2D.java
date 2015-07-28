/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.UniCrypt;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a {@link HashMap} based implementation of the {@link Map2D} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <K1> The type of the first key
 * @param <K2> The type of the second key
 * @param <V>  The type of the values stored in the map
 */
public class HashMap2D<K1, K2, V>
	   extends UniCrypt
	   implements Map2D<K1, K2, V> {

	private final Map<K1, Map<K2, V>> hashMaps;

	private HashMap2D() {
		this.hashMaps = new HashMap<K1, Map<K2, V>>();
	}

	@Override
	public V get(K1 key1, K2 key2) {
		Map<K2, V> hashMap = this.hashMaps.get(key1);
		if (hashMap == null) {
			return null;
		}
		return hashMap.get(key2);
	}

	@Override
	public void put(K1 key1, K2 key2, V value) {
		Map<K2, V> hashMap = this.hashMaps.get(key1);
		if (hashMap == null) {
			hashMap = new HashMap<K2, V>();
			this.hashMaps.put(key1, hashMap);
		}
		hashMap.put(key2, value);
	}

	/**
	 * Returns a new instance of this class, an initially empty 2-dimensional hash map.
	 * <p>
	 * @param <K1> The type of the first key
	 * @param <K2> The type of the second key
	 * @param <V>  The type of the values stored in the map
	 * @return The new 2-dimensional hash map
	 */
	public static <K1, K2, V> HashMap2D<K1, K2, V> getInstance() {
		return new HashMap2D<K1, K2, V>();
	}

}
