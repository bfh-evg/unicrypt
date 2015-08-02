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

	public static final BigInteger INFINITE = BigInteger.valueOf(-1);
	public static final BigInteger UNKNOWN = BigInteger.valueOf(-2);

	// the length of the sequence
	private BigInteger length;

	public final boolean isEmpty() {
		return this.getLength().signum() == 0;
	}

	public final BigInteger getLength() {
		if (this.length == null) {
			this.length = this.abstractGetLength();
			if (this.length.equals(Sequence.UNKNOWN) && !this.iterator().hasNext()) {
				this.length = MathUtil.ZERO;
			}
			return this.length;
		}
		return this.length;
	}

	public final long count() {
		if (this.getLength().equals(Sequence.INFINITE)) {
			throw new UnsupportedOperationException();
		}
		if (this.getLength().equals(Sequence.UNKNOWN)) {
			long counter = 0;
			for (V value : this) {
				counter++;
			}
			this.length = BigInteger.valueOf(counter);
		}
		return this.length.longValue();
	}

	public final long count(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		if (this.getLength().equals(Sequence.INFINITE)) {
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

	public final boolean check(Predicate<? super V> predicate) {
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

	public final V reduce(Operation<V> operation) {
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

	public final V reduce(Operation<V> operation, V identity) {
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
		return new Sequence<W>() {

			@Override
			protected BigInteger abstractGetLength() {
				return source.getLength();
			}

			@Override
			public Iterator<W> iterator() {
				return new Iterator<W>() {

					private final Iterator<V> iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public W next() {
						return mapping.map(this.iterator.next());
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
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				if (source.isEmpty()) {
					return MathUtil.ZERO;
				}
				return Sequence.UNKNOWN;
			}

			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {

					private final Iterator<V> iterator = source.iterator();
					private V nextValue = source.getNextValue(this.iterator, predicate);

					@Override
					public boolean hasNext() {
						return this.nextValue != null;
					}

					@Override
					public V next() {
						V result = this.nextValue;
						this.nextValue = source.getNextValue(this.iterator, predicate);
						return result;
					}

				};
			}
		};

	}

	// private helper method for filter
	private V getNextValue(Iterator<V> iterator, Predicate<? super V> predicate) {
		V nextValue = null;
		while (nextValue == null && iterator.hasNext()) {
			V value = iterator.next();
			if (predicate.test(value)) {
				nextValue = value;
			}
		}
		return nextValue;
	}

	public final Sequence<V> limit(final long maxLength) {
		return this.limit(BigInteger.valueOf(maxLength));
	}

	public final Sequence<V> limit(final BigInteger maxLength) {
		if (maxLength == null || maxLength.signum() < 0) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				BigInteger length = source.getLength();
				if (length.equals(Sequence.INFINITE) || maxLength.signum() == 0) {
					return maxLength;
				}
				if (length.equals(Sequence.UNKNOWN)) {
					return Sequence.UNKNOWN;
				}
				return length.min(maxLength);
			}

			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {

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

	public Sequence<V> skip() {
		return this.skip(1);
	}

	public Sequence<V> skip(final long n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				BigInteger length = source.getLength();
				if (length.equals(Sequence.INFINITE)) {
					return Sequence.INFINITE;
				}
				if (length.equals(Sequence.UNKNOWN)) {
					return Sequence.UNKNOWN;
				}
				return source.getLength().subtract(BigInteger.valueOf(n)).max(MathUtil.ZERO);
			}

			@Override
			public Iterator<V> iterator() {
				Iterator<V> iterator = source.iterator();
				long i = n;
				while (i > 0 && iterator.hasNext()) {
					iterator.next();
					i--;
				}
				return iterator;
			}
		};
	}

	public final Sequence<DenseArray<V>> group(final long groupLength) {
		if (groupLength < 1) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<DenseArray<V>>() {

			@Override
			protected BigInteger abstractGetLength() {
				BigInteger length = source.getLength();
				if (length.equals(Sequence.INFINITE)) {
					return Sequence.INFINITE;
				}
				if (length.equals(Sequence.UNKNOWN)) {
					return Sequence.UNKNOWN;
				}
				return MathUtil.divideUp(length, BigInteger.valueOf(groupLength));
			}

			@Override
			public Iterator<DenseArray<V>> iterator() {
				return new Iterator<DenseArray<V>>() {

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

	protected abstract BigInteger abstractGetLength();

	public static <V> Sequence<V> getInstance(final V... source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				return BigInteger.valueOf(source.length);
			}

			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {
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

	public static <V> Sequence<V> getInstance(final ImmutableArray<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				return BigInteger.valueOf(source.getLength());
			}

			@Override
			public Iterator<V> iterator() {
				return source.iterator();
			}
		};
	}

	public static <V> Sequence<V> getInstance(final Collection<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>() {

			@Override
			protected BigInteger abstractGetLength() {
				return BigInteger.valueOf(source.size());
			}

			@Override
			public Iterator<V> iterator() {
				return source.iterator();
			}
		};
	}

}
