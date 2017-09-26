/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This abstract class provides more powerful iterators with additional methods for retrieving multiple values, skipping
 * values, or finding values. In addition, two auxiliary methods {@code updateBefore()} and {@code updateAfter()} are
 * called before respectively after processing a call of one of the main methods. These auxiliary methods are initially
 * empty, but may be overridden in sub-classes.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the iterator
 */
public abstract class SequenceIterator<V>
	   extends UniCrypt
	   implements Iterator<V> {

	/**
	 * Returns an array consisting of the next {@code n} values from the iterator. The result may be shorter than
	 * {@code n}, if there are not enough values available.
	 * <p>
	 * @param n The number of values to return
	 * @return The resulting array
	 */
	public ImmutableArray<V> next(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		this.updateBefore();
		List<V> values = new LinkedList<>();
		while (n > 0 && this.hasNext()) {
			values.add(this.abstractNext());
			n--;
		}
		this.updateAfter();
		return DenseArray.getInstance(values);
	}

	/**
	 * Advances the iterator by {@code n} positions without returning anything.
	 * <p>
	 * @param n The number of values to skip
	 */
	public final void skip(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		this.updateBefore();
		while (n > 0 && this.hasNext()) {
			this.abstractNext();
			n--;
		}
		this.updateAfter();
	}

	/**
	 * Finds and returns the next value from the iterator that satisfies the given predicate. Values not satisfying the
	 * predicate are skipped. If none of the values satisfies the predicate, {@code null} is returned.
	 * <p>
	 * @param predicate The given predicate
	 * @return The next value satisfying the predicate, or {@code null} if no such value exists
	 */
	public final V find(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		this.updateBefore();
		while (this.hasNext()) {
			V value = this.abstractNext();
			if (predicate.test(value)) {
				this.updateAfter();
				return value;
			}
		}
		this.updateAfter();
		return null;
	}

	@Override
	public final V next() {
		if (!this.hasNext()) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_METHOD_CALL, this);
		}
		this.updateBefore();
		V result = this.abstractNext();
		this.updateAfter();
		return result;
	}

	/**
	 * Returns the next value from the iterator. This method is only called when if {@link SequenceIterator#hasNext()}
	 * returns {@code true}.
	 * <p>
	 * @return The next value
	 */
	protected abstract V abstractNext();

	/**
	 * This method is called before processing each call to {@link #next()}, {@link #next(int)}, {@link #skip(int)}, or
	 * {@link #find(Predicate)}. The default behavior is to do nothing. It can be overridden to change the state of the
	 * iterator.
	 */
	protected void updateBefore() {
	}

	/**
	 * This method is called after processing each call to {@link #next()}, {@link #next(int)}, {@link #skip(int)}, or
	 * {@link #find(Predicate)}. The default behavior is to do nothing. It can be overridden to change the state of the
	 * iterator.
	 */
	protected void updateAfter() {
	}

	/**
	 * Returns a wrapper {@link SequenceIterator} instance from a given {@link Iterator} instance.
	 * <p>
	 * @param <V>      The generic type of the iterators
	 * @param iterator The given {@link Iterator} instance
	 * @return The new {@link SequenceIterator} instance
	 */
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
