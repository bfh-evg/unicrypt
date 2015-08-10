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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class IntegerSequenceTest {

	@Test
	public void generalTest() {
		IntegerSequence s0 = IntegerSequence.getInstance(0, -3);
		IntegerSequence s1 = IntegerSequence.getInstance(3, 2);
		IntegerSequence s2 = IntegerSequence.getInstance(2, 2);
		IntegerSequence s3 = IntegerSequence.getInstance(2, 5);
		IntegerSequence s4 = IntegerSequence.getInstance(3, 6);
		assertEquals(0, s0.getLength().intValue());
		assertEquals(0, s1.getLength().intValue());
		assertEquals(1, s2.getLength().intValue());
		assertEquals(4, s3.getLength().intValue());
		assertEquals(4, s4.getLength().intValue());

		assertEquals(s0, s1);
		assertEquals(s2, s2);
		assertFalse(s2.equals(s3));
		assertFalse(s2.equals(s4));
		assertFalse(s0.iterator().hasNext());
		assertFalse(s1.iterator().hasNext());
		assertTrue(s2.iterator().hasNext());
		assertTrue(s3.iterator().hasNext());
		assertTrue(s4.iterator().hasNext());
		Integer j = 2;
		for (Integer i : s3) {
			assertEquals(j, i);
			j++;
		}
	}

}
