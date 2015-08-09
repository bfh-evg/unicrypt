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
import java.math.BigInteger;
import java.util.Iterator;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class MultiSequence<V>
	   extends Sequence<Sequence<V>> {

	Sequence<Sequence<V>> sequences;

	protected MultiSequence(Sequence<Sequence<V>> sequences) {
		super(sequences.getLength());
		this.sequences = sequences;
	}

	@Override
	public SequenceIterator<Sequence<V>> iterator() {
		return this.sequences.iterator();
	}

	public Sequence<V> flatten() {
		if (this.sequences.isEmpty()) {
			return Sequence.getInstance();
		}
		final Sequence<V> first = sequences.find();
		final Sequence<V> rest = MultiSequence.getInstance(sequences.skip()).flatten();
		BigInteger newLength;
		if (this.length.equals(Sequence.INFINITE) || rest.length.equals(Sequence.INFINITE)) {
			newLength = Sequence.INFINITE;
		} else if (this.length.equals(Sequence.UNKNOWN) || rest.length.equals(Sequence.UNKNOWN)) {
			newLength = Sequence.UNKNOWN;
		} else {
			newLength = this.length.add(rest.length);
		}
		return new Sequence<V>(newLength) {

			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {

					private final Iterator<V> iterator1 = first.iterator();
					private final Iterator<V> iterator2 = rest.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator1.hasNext() || this.iterator2.hasNext();
					}

					@Override
					public V abstractNext() {
						if (this.iterator1.hasNext()) {
							return this.iterator1.next();
						}
						return this.iterator2.next();
					}
				};
			}
		};
	}

	public Sequence<DenseArray<V>> combine() {
		if (this.sequences.isEmpty()) {
			return new Sequence<DenseArray<V>>(Sequence.INFINITE) {

				@Override
				public SequenceIterator iterator() {
					return new SequenceIterator<DenseArray<V>>() {

						@Override
						public boolean hasNext() {
							return true;
						}

						@Override
						public DenseArray<V> abstractNext() {
							return DenseArray.<V>getInstance();
						}

					};
				}

			};
		}
		final Sequence<V> first = sequences.find();
		final Sequence<DenseArray<V>> rest = MultiSequence.getInstance(sequences.skip()).combine();
		BigInteger newLength;
		if (first.length.equals(Sequence.UNKNOWN) || rest.length.equals(Sequence.UNKNOWN)) {
			newLength = Sequence.INFINITE;
		} else if (first.length.equals(Sequence.INFINITE)) {
			newLength = rest.length;
		} else if (rest.length.equals(Sequence.INFINITE)) {
			newLength = first.length;
		} else {
			newLength = first.length.min(rest.length);
		}
		return new Sequence<DenseArray<V>>(newLength) {

			@Override
			public SequenceIterator<DenseArray<V>> iterator() {
				return new SequenceIterator<DenseArray<V>>() {

					private final Iterator<V> firstIterator = first.iterator();
					private final Iterator<DenseArray<V>> restIterator = rest.iterator();

					@Override
					public boolean hasNext() {
						return firstIterator.hasNext() && restIterator.hasNext();
					}

					@Override
					public DenseArray<V> abstractNext() {
						return restIterator.next().insert(firstIterator.next());
					}
				};
			}
		};
	}

	public Sequence<DenseArray<V>> join() {
		if (this.sequences.isEmpty()) {
			return Sequence.<DenseArray<V>>getInstance(DenseArray.<V>getInstance());
		}
		final Sequence<V> first = sequences.find();
		final Sequence<DenseArray<V>> rest = MultiSequence.getInstance(sequences.skip()).join();
		BigInteger newLength;
		if (first.length.equals(Sequence.INFINITE) || rest.length.equals(Sequence.INFINITE)) {
			newLength = Sequence.INFINITE;
		} else if (first.length.equals(Sequence.UNKNOWN) || rest.length.equals(Sequence.UNKNOWN)) {
			newLength = Sequence.UNKNOWN;
		} else {
			newLength = first.length.multiply(rest.length);
		}
		return new Sequence<DenseArray<V>>(newLength) {

			@Override
			public SequenceIterator<DenseArray<V>> iterator() {
				return new SequenceIterator() {

					private Iterator<V> firstIterator = first.iterator();
					private final Iterator<DenseArray<V>> restIterator = rest.iterator();
					private DenseArray<V> restItem = restIterator.hasNext() ? restIterator.next() : null;

					@Override
					public boolean hasNext() {
						return restItem != null && this.firstIterator.hasNext();
					}

					@Override
					public DenseArray<V> abstractNext() {
						DenseArray<V> result = restItem.insert(this.firstIterator.next());
						if (!this.firstIterator.hasNext() && this.restIterator.hasNext()) {
							this.restItem = this.restIterator.next();
							this.firstIterator = first.iterator();
						}
						return result;
					}

				};
			}
		};
	}

	public static <V> MultiSequence<V> getInstance(Sequence<V>... sequences) {
		return MultiSequence.getInstance(Sequence.getInstance(sequences));
	}

	public static <V> MultiSequence<V> getInstance(Sequence<Sequence<V>> sequences) {
		return new MultiSequence<>(sequences.filter(Predicate.NOT_NULL));
	}

}
