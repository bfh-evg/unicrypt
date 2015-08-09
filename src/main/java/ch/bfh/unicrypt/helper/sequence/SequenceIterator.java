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

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public abstract class SequenceIterator<V>
	   extends UniCrypt
	   implements Iterator<V> {

	public ImmutableArray<V> next(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		List<V> values = new LinkedList<>();
		while (n > 0 && this.hasNext()) {
			values.add(this.abstractNext());
			n--;
		}
		this.defaultUpdate();
		return DenseArray.getInstance(values);
	}

	public final void skip(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		while (n > 0 && this.hasNext()) {
			this.abstractNext();
			n--;
		}
		this.defaultUpdate();
	}

	public final V find(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		while (this.hasNext()) {
			V value = this.abstractNext();
			if (predicate.test(value)) {
				this.defaultUpdate();
				return value;
			}
		}
		this.defaultUpdate();
		return null;
	}

	@Override
	public final V next() {
		V result = this.abstractNext();
		this.defaultUpdate();
		return result;
	}

	protected abstract V abstractNext();

	// update operation is called after each call to next() or next(n)
	protected void defaultUpdate() {
	}

	public static <V> SequenceIterator<V> getInstance(final Iterator<V> iterator) {
		if (iterator == null) {
			throw new IllegalArgumentException();
		}
		return new SequenceIterator<V>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public V abstractNext() {
				return iterator.next();
			}
		};
	}

}