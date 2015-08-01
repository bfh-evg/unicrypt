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
import ch.bfh.unicrypt.helper.sequence.classes.ArraySequence;
import ch.bfh.unicrypt.helper.sequence.classes.IntegerSequence;
import ch.bfh.unicrypt.helper.sequence.classes.ProductSequence;
import ch.bfh.unicrypt.helper.sequence.interfaces.Sequence;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class ProductSequenceTest {

	@Test
	public void testGeneralTest() {
		Sequence<Integer> it0 = ArraySequence.getInstance();
		Sequence it1 = IntegerSequence.getInstance(0, 5);
		Sequence it2 = IntegerSequence.getInstance(0, 4);
		Sequence<Integer> it3 = ArraySequence.getInstance(0, 1);
		{
			ProductSequence<Integer> seq = ProductSequence.<Integer>getInstance();
			int counter = 0;
			for (DenseArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(1, counter);
			assertEquals(1, seq.getLength().intValue());
		}
		{
			ProductSequence<Integer> seq = ProductSequence.getInstance(it1, it2, it3);
			int counter = 0;
			for (DenseArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(60, counter);
			assertEquals(60, seq.getLength().intValue());
		}
		{
			ProductSequence<Integer> seq = ProductSequence.getInstance(it0);
			int counter = 0;
			for (DenseArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}
		{
			ProductSequence<Integer> seq = ProductSequence.getInstance(it1, it0, it2);
			int counter = 0;
			for (DenseArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}
		{
			ProductSequence<Integer> seq = ProductSequence.getInstance(it0, it0, it0);
			int counter = 0;
			for (DenseArray<Integer> i : seq) {
				counter++;
			}
			assertEquals(0, counter);
			assertEquals(0, seq.getLength().intValue());
		}

	}

}
