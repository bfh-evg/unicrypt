/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
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
package ch.bfh.unicrypt.helper.aggregator.abstracts;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.iterable.IterableArray;
import java.util.Collection;

/**
 * This abstract class serves as a base implementation for the {@link Aggregator} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values precessed by the aggregator
 */
public abstract class AbstractAggregator<V>
	   implements Aggregator<V> {

	@Override
	public final V aggregateLeaf(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractAggregateLeaf(value);
	}

	@Override
	public final V aggregateNode(V... values) {
		return this.aggregateNode(IterableArray.getInstance(values));
	}

	@Override
	public final V aggregateNode(Iterable<V> values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		int length;
		if (values instanceof ImmutableArray) {
			length = ((ImmutableArray) values).getLength();
		} else if (values instanceof Collection) {
			length = ((Collection) values).size();
		} else {
			length = 0;
			for (V value : values) {
				if (value == null) {
					throw new IllegalArgumentException();
				}
				length++;
			}
		}
		return this.abstractAggregateNode(values, length);
	}

	protected abstract V abstractAggregateLeaf(V value);

	protected abstract V abstractAggregateNode(Iterable<V> values, int size);

}
