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
package ch.bfh.unicrypt.helper.sequence;

import java.util.function.BinaryOperator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ReducedSequenceTest {

	@Test
	public void generalTest() {

		Sequence<Integer> sequence1 = Sequence.getInstance();
		Sequence<Integer> sequence2 = Sequence.getInstance(1);
		Sequence<Integer> sequence3 = Sequence.getInstance(1, 2, 3, 4);

		BinaryOperator<Integer> plus = (value1, value2) -> value1 + value2;

		BinaryOperator<Integer> times = (value1, value2) -> value1 * value2;;

		try {
			sequence1.reduce(plus);
			fail();
		} catch (Exception e) {
		}
		assertEquals(1, (long) sequence2.reduce(plus));
		assertEquals(10, (long) sequence3.reduce(plus));

		assertEquals(0, (long) sequence1.reduce(plus, 0));
		assertEquals(1, (long) sequence2.reduce(plus, 0));
		assertEquals(10, (long) sequence3.reduce(plus, 0));

		try {
			sequence1.reduce(plus);
			fail();
		} catch (Exception e) {
		}
		assertEquals(1, (long) sequence2.reduce(times));
		assertEquals(24, (long) sequence3.reduce(times));

		assertEquals(1, (long) sequence1.reduce(times, 1));
		assertEquals(1, (long) sequence2.reduce(times, 1));
		assertEquals(24, (long) sequence3.reduce(times, 1));
	}

}
