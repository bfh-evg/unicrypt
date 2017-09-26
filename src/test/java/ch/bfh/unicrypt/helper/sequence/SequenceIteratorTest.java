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
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class SequenceIteratorTest {

	List<Integer> values = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

	@Test
	public void testNext_int() {
		SequenceIterator<Integer> iterator = SequenceIterator.getInstance(values.iterator());
		assertEquals(DenseArray.getInstance(1, 2, 3), iterator.next(3));
		assertEquals(DenseArray.getInstance(), iterator.next(0));
		assertEquals(DenseArray.getInstance(4, 5, 6, 7), iterator.next(4));
		assertEquals(DenseArray.getInstance(8, 9, 10), iterator.next(5));
	}

	@Test
	public void testSkip() {
		SequenceIterator<Integer> iterator = SequenceIterator.getInstance(values.iterator());
		iterator.skip(2);
		assertEquals(3, (long) iterator.next());
		iterator.skip(0);
		assertEquals(4, (long) iterator.next());
		iterator.skip(4);
		assertEquals(9, (long) iterator.next());
		iterator.skip(4);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testFind() {
		SequenceIterator<Integer> iterator = SequenceIterator.getInstance(values.iterator());
		Predicate<Integer> pred = value -> value % 2 == 0;
		assertEquals(2, (long) iterator.find(pred));
		assertEquals(4, (long) iterator.find(pred));
		assertEquals(6, (long) iterator.find(pred));
		assertEquals(8, (long) iterator.find(pred));
		assertEquals(10, (long) iterator.find(pred));
		assertEquals(null, iterator.find(pred));
	}

	@Test
	public void testNext() {
		SequenceIterator<Integer> iterator = SequenceIterator.getInstance(values.iterator());
		assertEquals(1, (long) iterator.next());
		assertEquals(2, (long) iterator.next());
		assertEquals(3, (long) iterator.next());
		assertEquals(4, (long) iterator.next());
		assertEquals(5, (long) iterator.next());
		assertEquals(6, (long) iterator.next());
		assertEquals(7, (long) iterator.next());
		assertEquals(8, (long) iterator.next());
		assertEquals(9, (long) iterator.next());
		assertEquals(10, (long) iterator.next());
		assertFalse(iterator.hasNext());
	}

}
