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
package ch.bfh.unicrypt.helper.sequence.functions;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rolfhaenni
 */
public class PredicateTest {

	@Test
	public void test() {

		Predicate<Integer> pred1 = new Predicate<Integer>() {

			@Override
			public boolean test(Integer value) {
				return value > 0 && value < 10;
			}
		};
		Predicate<Integer> pred2 = new Predicate<Integer>() {

			@Override
			public boolean test(Integer value) {
				return value % 2 == 0;
			}
		};
		Predicate<Integer> predAND = pred1.and(pred2);
		Predicate<Integer> predOR = pred1.or(pred2);
		Predicate<Integer> predNOT = pred1.not();

		assertTrue(predAND.test(2));
		assertTrue(predAND.test(4));
		assertTrue(predAND.test(6));
		assertTrue(predAND.test(8));
		assertFalse(predAND.test(0));
		assertFalse(predAND.test(1));
		assertFalse(predAND.test(3));
		assertFalse(predAND.test(5));
		assertFalse(predAND.test(7));
		assertFalse(predAND.test(9));

		assertTrue(predOR.test(2));
		assertTrue(predOR.test(4));
		assertTrue(predOR.test(6));
		assertTrue(predOR.test(8));
		assertTrue(predOR.test(0));
		assertTrue(predOR.test(1));
		assertTrue(predOR.test(3));
		assertTrue(predOR.test(5));
		assertTrue(predOR.test(7));
		assertTrue(predOR.test(9));
		assertFalse(predOR.test(11));
		assertTrue(predNOT.test(0));
		assertTrue(predNOT.test(10));
		assertFalse(predNOT.test(3));

	}

}
