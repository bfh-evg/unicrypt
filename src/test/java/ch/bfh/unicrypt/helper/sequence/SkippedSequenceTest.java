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

import ch.bfh.unicrypt.helper.math.MathUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class SkippedSequenceTest {

	@Test
	public void generalTest() {

		IntegerSequence sequence = IntegerSequence.getInstance(1, 10);

		Assert.assertFalse(sequence.skip(0).isEmpty());
		Assert.assertEquals(10, sequence.skip(0).getLength().intValue());
		Assert.assertEquals(10, sequence.skip(0).getLength().intValue());
		Assert.assertEquals(1, (long) sequence.skip(0).get());

		Assert.assertFalse(sequence.skip(1).isEmpty());
		Assert.assertEquals(9, sequence.skip(1).getLength().intValue());
		Assert.assertEquals(9, sequence.skip(1).getLength().intValue());
		Assert.assertEquals(2, (long) sequence.skip(1).get());

		Assert.assertFalse(sequence.skip(9).isEmpty());
		Assert.assertEquals(1, sequence.skip(9).getLength().intValue());
		Assert.assertEquals(1, sequence.skip(9).getLength().intValue());
		Assert.assertEquals(10, (long) sequence.skip(9).get());

		Assert.assertTrue(sequence.skip(10).isEmpty());
		Assert.assertEquals(0, sequence.skip(10).getLength().intValue());
		Assert.assertEquals(0, sequence.skip(10).getLength().intValue());
		Assert.assertEquals(null, sequence.skip(10).get());

		Assert.assertTrue(sequence.skip(11).isEmpty());
		Assert.assertEquals(0, sequence.skip(11).getLength().intValue());
		Assert.assertEquals(0, sequence.skip(11).getLength().intValue());
		Assert.assertEquals(null, sequence.skip(11).get());

		Assert.assertEquals(5, sequence.skip(2).skip(3).getLength().intValue());
		Assert.assertEquals(5, sequence.skip(2).skip(3).getLength().intValue());
		Assert.assertEquals(6, (long) sequence.skip(2).skip(3).get());

		Assert.assertEquals(2, sequence.skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).getLength().intValue());
		Assert.assertEquals(9, (long) sequence.skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).skip(1).get());

		// unknown length
		Sequence<Integer> seq2 = sequence.filter(value -> true);
		Assert.assertEquals(10, seq2.getLength().intValue());
		Assert.assertEquals(1, seq2.skip(9).getLength().intValue());
		Assert.assertEquals(MathUtil.ZERO, seq2.skip(10).getLength());

	}

}
