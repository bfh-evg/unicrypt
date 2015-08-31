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
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class GroupedSequenceTest {

	@Test
	public void generalTest() {

		Sequence<Integer> seq0 = IntegerSequence.getInstance(1, 0);
		Sequence<Integer> seq1 = IntegerSequence.getInstance(1, 1);
		Sequence<Integer> seq9 = IntegerSequence.getInstance(1, 9);
		Sequence<Integer> seq10 = IntegerSequence.getInstance(1, 10);
		Sequence<Integer> seq11 = IntegerSequence.getInstance(1, 11);
		Sequence<Integer> seq100 = IntegerSequence.getInstance(1, 100);

		try {
			seq100.group(-1);
			fail();
		} catch (Exception e) {
		}

		Assert.assertTrue(seq0.group(0).isInfinite());
		Assert.assertTrue(seq1.group(0).isInfinite());
		Assert.assertTrue(seq9.group(0).isInfinite());
		Assert.assertTrue(seq10.group(0).isInfinite());
		Assert.assertTrue(seq11.group(0).isInfinite());
		Assert.assertTrue(seq100.group(0).isInfinite());

		Assert.assertEquals(0, seq0.group(1).getLength().intValue());
		Assert.assertEquals(1, seq1.group(1).getLength().intValue());
		Assert.assertEquals(9, seq9.group(1).getLength().intValue());
		Assert.assertEquals(10, seq10.group(1).getLength().intValue());
		Assert.assertEquals(11, seq11.group(1).getLength().intValue());
		Assert.assertEquals(100, seq100.group(1).getLength().intValue());

		Assert.assertEquals(0, seq0.group(5).getLength().intValue());
		Assert.assertEquals(1, seq1.group(5).getLength().intValue());
		Assert.assertEquals(2, seq9.group(5).getLength().intValue());
		Assert.assertEquals(2, seq10.group(5).getLength().intValue());
		Assert.assertEquals(3, seq11.group(5).getLength().intValue());
		Assert.assertEquals(20, seq100.group(5).getLength().intValue());

		Assert.assertEquals(0, seq0.group(100).getLength().intValue());
		Assert.assertEquals(1, seq1.group(100).getLength().intValue());
		Assert.assertEquals(1, seq9.group(100).getLength().intValue());
		Assert.assertEquals(1, seq10.group(100).getLength().intValue());
		Assert.assertEquals(1, seq11.group(100).getLength().intValue());
		Assert.assertEquals(1, seq100.group(100).getLength().intValue());

		Assert.assertEquals(0, seq0.group(1).group(1).getLength().intValue());
		Assert.assertEquals(1, seq1.group(1).group(1).getLength().intValue());
		Assert.assertEquals(9, seq9.group(1).group(1).getLength().intValue());
		Assert.assertEquals(10, seq10.group(1).group(1).getLength().intValue());
		Assert.assertEquals(11, seq11.group(1).group(1).getLength().intValue());
		Assert.assertEquals(100, seq100.group(1).group(1).getLength().intValue());

		Assert.assertEquals(0, seq0.group(5).group(1).getLength().intValue());
		Assert.assertEquals(1, seq1.group(5).group(1).getLength().intValue());
		Assert.assertEquals(2, seq9.group(5).group(1).getLength().intValue());
		Assert.assertEquals(2, seq10.group(5).group(1).getLength().intValue());
		Assert.assertEquals(3, seq11.group(5).group(1).getLength().intValue());
		Assert.assertEquals(20, seq100.group(5).group(1).getLength().intValue());

		Assert.assertEquals(0, seq0.group(5).group(2).getLength().intValue());
		Assert.assertEquals(1, seq1.group(5).group(2).getLength().intValue());
		Assert.assertEquals(1, seq9.group(5).group(2).getLength().intValue());
		Assert.assertEquals(1, seq10.group(5).group(2).getLength().intValue());
		Assert.assertEquals(2, seq11.group(5).group(2).getLength().intValue());
		Assert.assertEquals(10, seq100.group(5).group(2).getLength().intValue());

		Assert.assertEquals(DenseArray.getInstance(1, 2, 3, 4, 5), seq11.group(5).group(2).iterator().next().getFirst());
	}

}
