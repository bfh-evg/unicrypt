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
import java.util.function.Predicate;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class FilteredSequenceTest {

	public FilteredSequenceTest() {
	}

	@Test
	public void test() {

		Sequence<Integer> seq = IntegerSequence.getInstance(1, 10);

		Predicate<Integer> primePredicate = value -> MathUtil.isPrime(value);

		Predicate<Integer> oddPredicate = value -> value % 2 == 1;

		{
			Sequence<Integer> filteredSeq = seq.filter(primePredicate);

			Assert.assertEquals(4, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(4, i);
			Assert.assertEquals(4, filteredSeq.count(primePredicate));
			Assert.assertEquals(4, filteredSeq.getLength().intValue());
		}
		{
			Sequence<Integer> filteredSeq = seq.filter(primePredicate).filter(oddPredicate);

			Assert.assertEquals(3, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(3, i);
			Assert.assertEquals(3, filteredSeq.getLength().intValue());
		}
		{
			Sequence<Integer> filteredSeq = seq.filter(oddPredicate).filter(primePredicate);

			Assert.assertEquals(3, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(3, i);
			Assert.assertEquals(3, filteredSeq.getLength().intValue());

		}
		{
			Sequence<Integer> filteredSeq = Sequence.getInstance(3).filter(oddPredicate).filter(primePredicate);

			Assert.assertEquals(1, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(1, i);
			Assert.assertEquals(1, filteredSeq.getLength().intValue());
		}
		{
			Sequence<Integer> filteredSeq = Sequence.getInstance(4).filter(oddPredicate).filter(primePredicate);

			Assert.assertEquals(0, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(0, i);
			Assert.assertEquals(0, filteredSeq.getLength().intValue());
		}
		{
			Sequence<Integer> filteredSeq = IntegerSequence.getInstance(4, 3).filter(oddPredicate).filter(primePredicate);

			Assert.assertEquals(0, filteredSeq.getLength().intValue());
			int i = 0;
			for (Integer prime : filteredSeq) {
				i++;
			}
			Assert.assertEquals(0, i);
			Assert.assertEquals(0, filteredSeq.getLength().intValue());
		}
	}

}
