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
package ch.bfh.unicrypt.helper.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class Cache2DTest {

	@Test
	public void generalTest() {

		Cache2D<Integer, String, Double> cache = new Cache2D<>();
		cache.put(1, "one", 1.0);
		cache.put(1, "two", 2.0);
		cache.put(2, "one", 2.0);
		cache.put(2, "two", 4.0);
		cache.put(3, "two", 6.0);
		cache.put(3, "three", 9.0);
		try {
			cache.put(null, "", 0.0);
			fail();
		} catch (Exception e) {
		}
		try {
			cache.put(2, null, 0.0);
			fail();
		} catch (Exception e) {
		}
		try {
			cache.put(3, "", null);
			fail();
		} catch (Exception e) {
		}
		assertEquals((Double) 1.0, cache.get(1, "one"));
		assertEquals((Double) 2.0, cache.get(1, "two"));
		assertEquals((Double) 2.0, cache.get(2, "one"));
		assertEquals((Double) 4.0, cache.get(2, "two"));
		assertEquals((Double) 6.0, cache.get(3, "two"));
		assertEquals((Double) 9.0, cache.get(3, "three"));
		assertEquals(null, cache.get(4, "two"));
		assertEquals(null, cache.get(3, "four"));
		assertEquals(null, cache.get(3, "null"));
		try {
			cache.get(3, null);
			fail();
		} catch (Exception e) {
		}
		try {
			cache.get(null, "");
			fail();
		} catch (Exception e) {
		}
		try {
			cache.get(null, null);
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void sizeTest() {

		Cache2D<Integer, Integer, Integer> cache = new Cache2D<>(100);
		assertEquals(0, cache.getSize());
		cache.put(0, 0, 0);
		assertEquals(1, cache.getSize());
		assertEquals((Integer) 0, cache.get(0, 0));
		assertEquals(null, cache.get(0, 1));
		assertEquals(null, cache.get(1, 1));

		for (int i = 1; i <= 150; i++) {
			cache.put(i, i, i);
		}
		assertEquals(100, cache.getSize());
		assertNull(cache.get(0, 0));
		assertNull(cache.get(50, 50));
		assertNotNull(cache.get(51, 51));
		assertNull(cache.get(51, 0));

		for (int i = 51; i <= 150; i++) {
			cache.put(i, i + 1, i);
		}

		assertEquals(100, cache.getSize());

		cache.put(151, 151, 151);
		assertEquals(100, cache.getSize());

	}

}
