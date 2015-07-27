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
package ch.bfh.unicrypt.helper.iterable;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class IterablePrefixTest {

	@Test
	public void generalTest() {

		Iterable<Integer> ir = IterableRange.getInstance(1, 100);
		Iterable<Integer> ip0 = IterablePrefix.getInstance(ir, 0);
		Iterable<Integer> ip1 = IterablePrefix.getInstance(ir, 1);
		Iterable<Integer> ip10 = IterablePrefix.getInstance(ir, 10);
		Iterable<Integer> ip100 = IterablePrefix.getInstance(ir, 100);
		Iterable<Integer> ip200 = IterablePrefix.getInstance(ir, 200);
		Iterable<Integer> ip50 = IterablePrefix.getInstance(ip100, 50);
		Iterable<Integer> ip5 = IterablePrefix.getInstance(ip0, 5);

		{
			int counter = 0;
			for (Integer i : ip0) {
				counter++;
			}
			assertEquals(0, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip1) {
				counter++;
			}
			assertEquals(1, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip10) {
				counter++;
			}
			assertEquals(10, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip100) {
				counter++;
			}
			assertEquals(100, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip200) {
				counter++;
			}
			assertEquals(100, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip50) {
				counter++;
			}
			assertEquals(50, counter);
		}
		{
			int counter = 0;
			for (Integer i : ip5) {
				counter++;
			}
			assertEquals(0, counter);
		}

	}

}
