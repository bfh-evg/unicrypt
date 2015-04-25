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
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class IterableRangeTest {

	@Test
	public void generalTest() {
		IterableRange ir0 = IterableRange.getInstance(0, -3);
		IterableRange ir1 = IterableRange.getInstance(3, 2);
		IterableRange ir2 = IterableRange.getInstance(2, 2);
		IterableRange ir3 = IterableRange.getInstance(2, 5);
		IterableRange ir4 = IterableRange.getInstance(3, 6);
		assertEquals(0, ir0.getLength());
		assertEquals(0, ir1.getLength());
		assertEquals(1, ir2.getLength());
		assertEquals(4, ir3.getLength());
		assertEquals(4, ir4.getLength());
		assertEquals(ir0, ir1);
		assertEquals(ir2, ir2);
		assertFalse(ir2.equals(ir3));
		assertFalse(ir2.equals(ir4));
		assertFalse(ir0.iterator().hasNext());
		assertFalse(ir1.iterator().hasNext());
		assertTrue(ir2.iterator().hasNext());
		assertTrue(ir3.iterator().hasNext());
		assertTrue(ir4.iterator().hasNext());
		int j = 2;
		for (int i : ir3) {
			assertEquals(i, j);
			j++;
		}
	}

}
