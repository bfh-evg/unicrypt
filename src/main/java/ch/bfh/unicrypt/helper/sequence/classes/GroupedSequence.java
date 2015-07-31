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
package ch.bfh.unicrypt.helper.sequence.classes;

import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.abstracts.AbstractSequence;
import ch.bfh.unicrypt.helper.sequence.interfaces.Sequence;
import java.math.BigInteger;
import java.util.Iterator;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class GroupedSequence<V>
	   extends AbstractSequence<DenseArray<V>> {

	private final Iterable<V> values;
	private final int groupLength;

	protected GroupedSequence(Iterable<V> values, int groupLength) {
		this.values = values;
		this.groupLength = groupLength;
	}

	public static <V> GroupedSequence<V> getInstance(Iterable<V> values, int groupLength) {
		if (values == null || groupLength < 1) {
			throw new IllegalArgumentException();
		}
		return new GroupedSequence<>(values, groupLength);
	}

	@Override
	public Iterator<DenseArray<V>> iterator() {
		return new Iterator<DenseArray<V>>() {

			private final Iterator<V> iterator = values.iterator();

			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public DenseArray<V> next() {
				int i = 0;
				DenseArray<V> result = DenseArray.getInstance();
				while (i < groupLength && this.iterator.hasNext()) {
					result = result.add(this.iterator.next());
					i++;
				}
				return result;
			}

		};
	}

	@Override
	protected BigInteger abstractGetLength() {
		BigInteger length = AbstractSequence.getLength(this.values);
		if (length.equals(Sequence.INFINITE)) {
			return Sequence.INFINITE;
		}
		if (length.equals(Sequence.UNKNOWN)) {
			return Sequence.UNKNOWN;
		}
		return MathUtil.divideUp(length, BigInteger.valueOf(this.groupLength));
	}

}
