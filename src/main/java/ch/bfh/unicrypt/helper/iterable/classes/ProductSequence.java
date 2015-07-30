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
package ch.bfh.unicrypt.helper.iterable.classes;

import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.iterable.abstracts.AbstractSequence;
import ch.bfh.unicrypt.helper.iterable.interfaces.Sequence;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

/**
 * This generic class generates an iterable collection of all possible combinations of elements of the given iterable
 * collections of elements.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the iterable collection
 */
public class ProductSequence<V>
	   extends AbstractSequence<DenseArray<V>> {

	private final Iterable<V> values;
	private final ProductSequence<V> productSequence;

	// the base case of the recursion
	protected ProductSequence() {
		super(MathUtil.ONE);
		this.values = null;
		this.productSequence = null;
	}

	// the general case of the recursion
	protected ProductSequence(Iterable<V> values, ProductSequence<V> productSequence) {
		super(computeLength(Sequence.UNKNOWN, productSequence.getLength()));
		this.values = values;
		this.productSequence = productSequence;
	}

	// the general case of the recursion for a collection
	protected ProductSequence(Collection<V> collection, ProductSequence<V> productSequence) {
		super(computeLength(BigInteger.valueOf(collection.size()), productSequence.getLength()));
		this.values = collection;
		this.productSequence = productSequence;
	}

	// the general case of the recursion for a collection
	protected ProductSequence(Sequence<V> sequence, ProductSequence<V> productSequence) {
		super(computeLength(sequence.getLength(), productSequence.getLength()));
		this.values = sequence;
		this.productSequence = productSequence;
	}

	private static BigInteger computeLength(BigInteger length1, BigInteger length2) {
		if (length1.equals(Sequence.INFINITE) || length2.equals(Sequence.INFINITE)) {
			return Sequence.INFINITE;
		}
		if (length1.equals(Sequence.UNKNOWN) || length2.equals(Sequence.UNKNOWN)) {
			return Sequence.UNKNOWN;
		}
		return length1.multiply(length2);
	}

	@Override
	public Iterator<DenseArray<V>> iterator() {

		if (this.values == null && this.productSequence == null) {
			// the base case of the recursion
			return new Iterator() {

				private boolean next = true;

				@Override
				public boolean hasNext() {
					return this.next;
				}

				@Override
				public Object next() {
					this.next = false;
					return DenseArray.<V>getInstance();
				}

			};
		}
		// the general case of the recursion
		return new Iterator() {

			private final Iterator<V> iterator1 = values.iterator();
			private Iterator<DenseArray<V>> iterator2 = productSequence.iterator();
			private V item = iterator1.hasNext() ? iterator1.next() : null;

			@Override
			public boolean hasNext() {
				return item != null && this.iterator2.hasNext();
			}

			@Override
			public DenseArray<V> next() {
				DenseArray<V> items = this.iterator2.next();
				DenseArray<V> result = items.insertAt(0, item);
				if (!this.iterator2.hasNext() && this.iterator1.hasNext()) {
					this.item = this.iterator1.next();
					this.iterator2 = productSequence.iterator();
				}
				return result;
			}

		};
	}

	/**
	 * Creates and returns an iterable collection of all combinations of elements in the input collections of elements.
	 * <p>
	 * @param <V>   The generic type of the given iterable collections of elements
	 * @param array The given iterable collections of elements
	 * @return The new iterable collection of all combinations of elements
	 */
	public static <V> ProductSequence<V> getInstance(Iterable<V>... array) {
		if (array == null) {
			throw new IllegalArgumentException();
		}
		for (Iterable<V> iterable : array) {
			if (iterable == null) {
				throw new IllegalArgumentException();
			}
		}
		return ProductSequence.getInstance(ArraySequence.getInstance(array));
	}

	/**
	 * Creates and returns an iterable collection of all combinations of elements in the input collections of elements.
	 * <p>
	 * @param <V>       The generic type of the given iterable collections of elements
	 * @param iterables The given iterable collections of elements
	 * @return The new iterable collection of all combinations of elements
	 */
	public static <V> ProductSequence<V> getInstance(Iterable<? extends Iterable<V>> iterables) {
		if (iterables == null) {
			throw new IllegalArgumentException();
		}
		ImmutableArray<? extends Iterable<V>> denseArray = DenseArray.getInstance(iterables);
		if (denseArray.isEmpty()) {
			return new ProductSequence<>();
		}
		Iterable<V> values = denseArray.getFirst();
		return new ProductSequence<>(values, ProductSequence.getInstance(denseArray.removeFirst()));
	}

}
