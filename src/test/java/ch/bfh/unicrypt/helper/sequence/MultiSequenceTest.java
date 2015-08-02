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

import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class MultiSequenceTest {

	Sequence<Integer> s1 = IntegerSequence.getInstance(1, 5);
	Sequence<Integer> s2 = IntegerSequence.getInstance(3, 5);
	Sequence<Integer> s3 = IntegerSequence.getInstance(4, 8);
	MultiSequence<Integer> ss = MultiSequence.getInstance(s1, s1, s2, s3);

	@Test
	public void testAppend() {
		for (Integer i : ss.append()) {
//			System.out.println(i);
		}
	}

	@Test
	public void testJoin() {
		for (DenseArray<Integer> i : ss.join()) {
//			System.out.println(i);
		}
		Sequence<Integer> it0 = Sequence.getInstance();
		Sequence<Integer> it1 = IntegerSequence.getInstance(0, 5);
		Sequence<Integer> it2 = IntegerSequence.getInstance(0, 4);
		Sequence<Integer> it3 = Sequence.getInstance(0, 1);
		{
			Sequence<DenseArray<Integer>> seq = MultiSequence.<Integer>getInstance().join();
			int counter = 0;
			for (ImmutableArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(1, counter);
			assertEquals(1, seq.getLength().intValue());
		}
		{
			Sequence<DenseArray<Integer>> seq = MultiSequence.getInstance(it1, it2, it3).join();
			int counter = 0;
			for (ImmutableArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(60, counter);
			assertEquals(60, seq.getLength().intValue());
		}
		{
			Sequence<DenseArray<Integer>> seq = MultiSequence.getInstance(it0).join();
			int counter = 0;
			for (ImmutableArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}
		{
			Sequence<DenseArray<Integer>> seq = MultiSequence.getInstance(it1, it0, it2).join();
			int counter = 0;
			for (ImmutableArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}
		{
			Sequence<DenseArray<Integer>> seq = MultiSequence.getInstance(it0, it0, it0).join();
			int counter = 0;
			for (ImmutableArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}

	}

	@Test
	public void testMerge() {
		for (DenseArray<Integer> i : ss.merge()) {
//			System.out.println(i);
		}
	}

}
