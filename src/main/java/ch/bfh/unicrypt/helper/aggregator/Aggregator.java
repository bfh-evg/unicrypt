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
package ch.bfh.unicrypt.helper.aggregator;

import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.iterable.IterableArray;
import java.util.Collection;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public abstract class Aggregator<V> {

	public final V aggregate(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractAggregate(value);
	}

	public final V aggregate(V... values) {
		return this.aggregate(IterableArray.getInstance(values));
	}

	public final V aggregate(Iterable<V> values) {
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
		return this.abstractAggregate(values, length);
	}

	public final SingleOrMultiple<V> disaggregate(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if (this.abstractIsSingle(value)) {
			return SingleOrMultiple.getInstance(this.abstractDisaggregateSingle(value));
		} else {
			return SingleOrMultiple.getInstance(this.abstractDisaggregateMultiple(value));
		}
	}

	protected abstract V abstractAggregate(V value);

	protected abstract V abstractAggregate(Iterable<V> values, int size);

	protected abstract boolean abstractIsSingle(V value);

	protected abstract V abstractDisaggregateSingle(V value);

	protected abstract Iterable<V> abstractDisaggregateMultiple(V value);

}
