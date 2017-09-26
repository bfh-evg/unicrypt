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

import java.util.function.Predicate;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class SequenceTest {

	@Test
	public void testIteration() {

		Sequence<Integer> seq = Sequence.getInstance(2, value -> value + 2);
		SequenceIterator<Integer> iterator = seq.iterator();
		Assert.assertEquals(2, (long) iterator.next());
		Assert.assertEquals(4, (long) iterator.next());
		Assert.assertEquals(6, (long) iterator.next());
		Assert.assertEquals(8, (long) iterator.next());
		Assert.assertTrue(seq.isInfinite());
	}

	@Test
	public void testCountFindGetMatch() {
		Predicate<Integer> pred = value -> value > 10 && value < 15;
		Sequence<Integer> seq = IntegerSequence.getInstance(1, 20);

		Assert.assertEquals(4, seq.count(pred));

		Assert.assertEquals(11, (long) seq.find(pred));
		Assert.assertEquals(14, (long) seq.find(pred, 3));
		Assert.assertEquals(null, seq.find(pred, 5));

		Assert.assertEquals(1, (long) seq.get());
		Assert.assertEquals(4, (long) seq.get(3));
		Assert.assertEquals(null, seq.find(pred, 25));

		assertFalse(seq.matchAll(pred));
		assertTrue(seq.matchAny(pred));
	}

}
