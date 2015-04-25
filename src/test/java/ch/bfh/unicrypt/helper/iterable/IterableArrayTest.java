/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
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
package ch.bfh.unicrypt.helper.iterable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class IterableArrayTest {

	@Test
	public void generalTest() {
		IterableArray<Integer> ia0 = IterableArray.getInstance();
		IterableArray<Integer> ia1 = IterableArray.getInstance(new Integer[]{});
		IterableArray<Integer> ia2 = IterableArray.getInstance(2);
		IterableArray<Integer> ia3 = IterableArray.getInstance(2, 3, 4, 5);
		IterableArray<Integer> ia4 = IterableArray.getInstance(3, 4, 5, 6);
		assertEquals(0, ia0.getLength());
		assertEquals(0, ia1.getLength());
		assertEquals(1, ia2.getLength());
		assertEquals(4, ia3.getLength());
		assertEquals(4, ia4.getLength());
		assertEquals(ia0, ia1);
		assertEquals(ia2, ia2);
		assertFalse(ia2.equals(ia3));
		assertFalse(ia2.equals(ia4));
		assertFalse(ia0.iterator().hasNext());
		assertFalse(ia1.iterator().hasNext());
		assertTrue(ia2.iterator().hasNext());
		assertTrue(ia3.iterator().hasNext());
		assertTrue(ia4.iterator().hasNext());
		int j = 2;
		for (int i : ia3) {
			assertEquals(i, j);
			j++;
		}
		try {
			IterableArray.getInstance((Integer[]) null);
			fail();
		} catch (Exception e) {
		}
	}

}
