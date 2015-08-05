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
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

/**
 * This interface represents the concept of an iterable sequence of values similar to streams in Java 8. No means are
 * provided to directly access or manipulate the values. Computational operations can be described declaratively as a
 * pipeline. The execution of the pipeline is lazy. The length of a sequence is either finite and known, finite but
 * unknown, or infinite.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values contained in the sequence
 */
public abstract class Sequence<V>
	   extends UniCrypt
	   implements Iterable<V> {

	protected static final BigInteger INFINITE = BigInteger.valueOf(-1);
	protected static final BigInteger UNKNOWN = BigInteger.valueOf(-2);

	// the length of the sequence
	protected BigInteger length;

	protected Sequence() {
		this.length = Sequence.UNKNOWN;
	}

	protected Sequence(BigInteger length) {
		this.length = length;
	}

	public final boolean isEmpty() {
		return this.length.equals(MathUtil.ZERO) || (this.length.equals(Sequence.UNKNOWN) && !this.iterator().hasNext());
	}

	public final boolean isInfinite() {
		return this.length.equals(Sequence.INFINITE);
	}

	// possibly expensive
	public final BigInteger getLength() {
		if (this.length.equals(Sequence.UNKNOWN)) {
			long counter = 0;
			for (V value : this) {
				counter++;
			}
			this.length = BigInteger.valueOf(counter);
		}
		return this.length;
	}

	public final long count(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		if (this.isInfinite()) {
			throw new UnsupportedOperationException();
		}
		long counter = 0;
		for (V value : this) {
			if (predicate.test(value)) {
				counter++;
			}
		}
		return counter;
	}

	public final V find() {
		return this.find(1);
	}

	public final V find(long n) {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		for (V value : this) {
			n--;
			if (n == 0) {
				return value;
			}
		}
		return null;

	}

	public final V find(Predicate<? super V> predicate) {
		return this.find(predicate, 1);
	}

	public final V find(Predicate<? super V> predicate, long n) {
		if (predicate == null || n < 1) {
			throw new IllegalArgumentException();
		}
		for (V value : this.filter(predicate)) {
			n--;
			if (n == 0) {
				return value;
			}
		}
		return null;
	}

	public final boolean matchAll(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		for (V value : this) {
			if (!predicate.test(value)) {
				return false;
			}
		}
		return true;
	}

	public final boolean matchAny(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		for (V value : this) {
			if (predicate.test(value)) {
				return true;
			}
		}
		return false;
	}

	public final V reduce(BinaryOperator<V> operation) {
		if (operation == null) {
			throw new IllegalArgumentException();
		}
		if (this.isEmpty()) {
			throw new UnsupportedOperationException();
		}
		Iterator<V> iterator = this.iterator();
		V result = iterator.next();
		while (iterator.hasNext()) {
			result = operation.apply(result, iterator.next());
		}
		return result;
	}

	public final V reduce(BinaryOperator<V> operation, V identity) {
		if (operation == null || identity == null) {
			throw new IllegalArgumentException();
		}
		Iterator<V> iterator = this.iterator();
		V result = identity;
		while (iterator.hasNext()) {
			result = operation.apply(result, iterator.next());
		}
		return result;
	}

	public final <W> Sequence<W> map(final Mapping<? super V, ? extends W> mapping) {
		if (mapping == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<W>(source.length) {

			@Override
			public ExtendedIterator<W> iterator() {
				return new ExtendedIterator<W>() {

					private final Iterator<V> iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public W next() {
						return mapping.apply(this.iterator.next());
					}
				};
			}

		};
	}

	public final Sequence<V> filter(final Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>(Sequence.UNKNOWN) {

			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {

					private final ExtendedIterator<V> iterator = source.iterator();
					private V nextValue = null;
					private boolean terminated = false;

					@Override
					public boolean hasNext() {
						if (this.terminated) {
							return false;
						}
						if (this.nextValue != null) {
							return true;
						}
						this.nextValue = this.iterator.find(predicate);
						if (this.nextValue == null) {
							this.terminated = true;
							return false;
						}
						return true;
					}

					@Override
					public V next() {
						V result = this.nextValue;
						this.nextValue = null;
						return result;
					}

				};
			}
		};

	}

	public final Sequence<V> limit(final long maxLength) {
		return this.limit(BigInteger.valueOf(maxLength));
	}

	public final Sequence<V> limit(final BigInteger maxLength) {
		if (maxLength == null || maxLength.signum() < 0) {
			throw new IllegalArgumentException();
		}
		BigInteger newLength;
		if (this.length.equals(Sequence.UNKNOWN)) {
			newLength = Sequence.UNKNOWN;
		} else if (this.length.equals(Sequence.INFINITE)) {
			newLength = maxLength;
		} else {
			newLength = this.length.min(maxLength);
		}
		final Sequence<V> source = this;
		return new Sequence<V>(newLength) {

			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {

					private final Iterator<V> iterator = source.iterator();
					private BigInteger counter = MathUtil.ZERO;

					@Override
					public boolean hasNext() {
						return this.counter.compareTo(maxLength) < 0 && this.iterator.hasNext();
					}

					@Override
					public V next() {
						this.counter = this.counter.add(MathUtil.ONE);
						return this.iterator.next();
					}

				};
			}

		};
	}

	public Sequence<V> limit(final Predicate<V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>(Sequence.UNKNOWN) {

			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {

					private final ExtendedIterator<V> iterator = source.iterator();
					private boolean found = false;

					@Override
					public boolean hasNext() {
						return !found && iterator.hasNext();
					}

					@Override
					public V next() {
						V value = iterator.next();
						if (predicate.test(value)) {
							this.found = true;
						}
						return value;
					}
				};
			}
		};
	}

	public Sequence<V> skip() {
		return this.skip(1);
	}

	public Sequence<V> skip(final long n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		BigInteger newLength;
		if (this.length.equals(Sequence.UNKNOWN) || this.length.equals(Sequence.INFINITE)) {
			newLength = this.length;
		} else {
			newLength = this.length.subtract(BigInteger.valueOf(n)).max(MathUtil.ZERO);
		}
		final Sequence<V> source = this;
		return new Sequence<V>(newLength) {

			@Override
			public ExtendedIterator<V> iterator() {
				ExtendedIterator<V> iterator = source.iterator();
				iterator.skip(n);
				return iterator;
			}
		};
	}

	public final Sequence<DenseArray<V>> group(final long groupLength) {
		if (groupLength < 1) {
			throw new IllegalArgumentException();
		}
		BigInteger newLength;
		if (this.length.equals(Sequence.UNKNOWN) || this.length.equals(Sequence.INFINITE)) {
			newLength = this.length;
		} else {
			newLength = MathUtil.divideUp(this.length, BigInteger.valueOf(groupLength));
		}
		final Sequence<V> source = this;
		return new Sequence<DenseArray<V>>(newLength) {

			@Override
			public ExtendedIterator<DenseArray<V>> iterator() {
				return new ExtendedIterator<DenseArray<V>>() {

					private final Iterator<V> iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public DenseArray<V> next() {
						long i = 0;
						DenseArray<V> result = DenseArray.getInstance();
						while (i < groupLength && this.iterator.hasNext()) {
							result = result.add(this.iterator.next());
							i++;
						}
						return result;
					}

				};

			}
		};

	}

	public Sequence<V> conc(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).flatten();
	}

	public Sequence<DenseArray<V>> combine(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).combine();
	}

	public Sequence<DenseArray<V>> join(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).join();
	}

	@Override
	public abstract ExtendedIterator<V> iterator();

	public static <V> Sequence<V> getInstance(final V... source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(BigInteger.valueOf(source.length)) {

			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {
					private int pos = 0;

					@Override
					public boolean hasNext() {
						return this.pos < source.length;
					}

					@Override
					public V next() {
						return source[this.pos++];
					}

				};
			}
		};
	}

	public static <V> Sequence<V> getInstance(final Collection<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(BigInteger.valueOf(source.size())) {

			@Override
			public ExtendedIterator<V> iterator() {
				return ExtendedIterator.getInstance(source.iterator());
			}
		};
	}

	public static <V> Sequence<V> getInstance(final ImmutableArray<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>() {
			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {
					private int currentIndex = 0;

					@Override
					public boolean hasNext() {
						return this.currentIndex < source.getLength();
					}

					@Override
					public V next() {
						return source.getAt(this.currentIndex++);
					}
				};
			}
		};

	}

	public static <V> Sequence<V> getInstance(final V startValue, final UnaryOperator<V> operator) {
		if (startValue == null || operator == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(Sequence.INFINITE) {

			@Override
			public ExtendedIterator<V> iterator() {
				return new ExtendedIterator<V>() {

					V currentValue = startValue;

					@Override
					public boolean hasNext() {
						return true;
					}

					@Override
					public V next() {
						V nextValue = this.currentValue;
						this.currentValue = operator.apply(this.currentValue);
						return nextValue;
					}
				};
			}
		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Sequence<?> other = (Sequence<?>) obj;
		Iterator<V> it1 = this.iterator();
		Iterator<?> it2 = other.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			if (!it1.next().equals(it2.next())) {
				return false;
			}
		}
		return !it1.hasNext() && !it2.hasNext();
	}

}
