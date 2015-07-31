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
package ch.bfh.unicrypt.helper.sequence;

import ch.bfh.unicrypt.helper.sequence.classes.ArraySequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class ArraySequenceTest {

	@Test
	public void generalTest() {
		ArraySequence<Integer> ia0 = ArraySequence.getInstance();
		ArraySequence<Integer> ia1 = ArraySequence.getInstance(new Integer[]{});
		ArraySequence<Integer> ia2 = ArraySequence.getInstance(2);
		ArraySequence<Integer> ia3 = ArraySequence.getInstance(2, 3, 4, 5);
		ArraySequence<Integer> ia4 = ArraySequence.getInstance(3, 4, 5, 6);
		assertEquals(ia0, ia1);
		assertEquals(ia2, ia2);
		assertFalse(ia2.equals(ia3));
		assertFalse(ia2.equals(ia4));
		assertFalse(ia0.iterator().hasNext());
		assertFalse(ia1.iterator().hasNext());
		assertTrue(ia2.iterator().hasNext());
		assertTrue(ia3.iterator().hasNext());
		assertTrue(ia4.iterator().hasNext());

		assertTrue(ia0.isEmpty());
		assertTrue(ia1.isEmpty());
		assertFalse(ia2.isEmpty());
		assertFalse(ia3.isEmpty());
		assertFalse(ia4.isEmpty());

		assertEquals(0, ia0.getLength().intValue());
		assertEquals(0, ia1.getLength().intValue());
		assertEquals(1, ia2.getLength().intValue());
		assertEquals(4, ia3.getLength().intValue());
		assertEquals(4, ia4.getLength().intValue());

		int j = 2;
		for (int i : ia3) {
			assertEquals(i, j);
			j++;
		}
		try {
			ArraySequence.getInstance((Integer[]) null);
			fail();
		} catch (Exception e) {
		}
	}

}
