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
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

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
	
	/**
	 * A predicate that checks that the value is not null.
	 */
	public static final Predicate<Object> NOT_NULL = value -> value != null;

	/**
	 * A constant value representing an infinite sequence length.
	 */
	public static final BigInteger INFINITE = BigInteger.valueOf(-1);

	/**
	 * A constant value representing an unknown sequence length.
	 */
	public static final BigInteger UNKNOWN = BigInteger.valueOf(-2);

	// the length of the sequence
	protected BigInteger length;

	protected Sequence() {
		this.length = Sequence.UNKNOWN;
	}

	protected Sequence(BigInteger length) {
		this.length = length;
	}

	@Override
	public abstract SequenceIterator<V> iterator();

	/**
	 * Checks if the sequence is empty. If this is the case, {@link Sequence#iterator()}{@code .hasNext()} returns
	 * {@code false}.
	 * <p>
	 * @return {@code true} if the sequence is empty, {@code false} otherwise
	 */
	public final boolean isEmpty() {
		return this.length.equals(MathUtil.ZERO) || (this.length.equals(Sequence.UNKNOWN)
			   && !this.iterator().hasNext());
	}

	/**
	 * Checks if the sequence is infinitely long. If this is the case, {@link Sequence#iterator()}{@code .hasNext()}
	 * returns {@code false}.
	 * <p>
	 * @return {@code true} if the sequence is empty, {@code false} otherwise
	 */
	public final boolean isInfinite() {
		return this.length.equals(Sequence.INFINITE);
	}

	/**
	 * Returns the length of this sequence, which could possibly be {@link Sequence#INFINITE}. If the length of the
	 * sequence is finite, but unknown at the moment of calling this method, it is computed by iterating through the
	 * sequence. In that case, calling this method is expensive.
	 * <p>
	 * @return The length of the sequence
	 */
	public final BigInteger getLength() {
		if (this.length.equals(Sequence.UNKNOWN)) {
			long counter = 0;
			for (Iterator<V> it = this.iterator(); it.hasNext();) {
				it.next();
				counter++;
			}
			this.length = BigInteger.valueOf(counter);
		}
		return this.length;
	}

	/**
	 * Counts the number of values in the sequence satisfying the given predicate. An exception is thrown if the
	 * sequence is infinite.
	 * <p>
	 * @param predicate The given predicate
	 * @return The number of values satisfying the predicate
	 */
	public final long count(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		if (this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		long counter = 0;
		for (V value : this) {
			if (predicate.test(value)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Returns the first value of the sequence, or {@code null} if the sequence is empty.
	 * <p>
	 * @return The first value
	 */
	public final V get() {
		return this.get(0);
	}

	/**
	 * Returns the {@code n}-th value of the sequence, or {@code null} if the sequence is shorter than {@code n}.
	 * <p>
	 * @param n The position of the value in the sequence
	 * @return The {@code n}-th value
	 */
	public final V get(long n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		for (V value : this) {
			if (n == 0) {
				return value;
			}
			n--;
		}
		return null;
	}

	/**
	 * Creates a new dense array from a given finite sequence of values. Null values are eliminated and the total length
	 * is restricted to {@link Integer#MAX_VALUE}.
	 * <p>
	 * @return The new dense array
	 */
	public DenseArray<V> getAll() {
		if (this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return DenseArray.<V>getInstance(this);
	}

	/**
	 * Returns the first value of the sequence satisfying the given predicate, or {@code null} if no such value exists.
	 * <p>
	 * @param predicate The given predicate
	 * @return The first value satisfying the predicate
	 */
	public final V find(Predicate<? super V> predicate) {
		return this.find(predicate, 0);
	}

	/**
	 * Returns the {@code n}-th value of the sequence satisfying the given predicate, or {@code null} if no such value
	 * exists.
	 * <p>
	 * @param predicate The given predicate
	 * @param n         The position in the sequence of values satisfying the predicate
	 * @return The {@code n}-th value satisfying the predicate
	 */
	public final V find(Predicate<? super V> predicate, long n) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		return this.filter(predicate).get(n);
	}

	/**
	 * Checks if all values in the sequence satisfy the given predicate. An exception is thrown if the sequence is
	 * infinite.
	 * <p>
	 * @param predicate The given predicate
	 * @return {@code true}, if the predicate matches for all values, {@code false} otherwise
	 */
	public final boolean matchAll(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		if (this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		for (V value : this) {
			if (!predicate.test(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if at least one value in the sequence satisfies the given predicate. An exception is thrown if the
	 * sequence is infinite.
	 * <p>
	 * @param predicate The given predicate
	 * @return {@code true}, if the predicate matches for at least one value, {@code false} otherwise
	 */
	public final boolean matchAny(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		if (this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		for (V value : this) {
			if (predicate.test(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Applies an associative operator to all values in the sequence. If the sequence contains a single value, the
	 * single value is returned. If the sequence is empty or infinite, an exception is thrown.
	 * <p>
	 * @param operator The associative operator
	 * @return The result of applying the operator to all values
	 */
	public final V reduce(BinaryOperator<V> operator) {
		if (operator == null) {
			throw new IllegalArgumentException();
		}
		if (this.isEmpty() || this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		Iterator<V> iterator = this.iterator();
		V result = iterator.next();
		while (iterator.hasNext()) {
			result = operator.apply(result, iterator.next());
		}
		return result;
	}

	/**
	 * Applies an associative operator to all values in the sequence. If the sequence contains a single value, the
	 * single value is returned. If the sequence is empty, the given identity value is returned. If the sequence is
	 * infinite, an exception is thrown.
	 * <p>
	 * @param operator The associative operator
	 * @param identity  The identity value
	 * @return The result of applying the operator to all values
	 */
	public final V reduce(BinaryOperator<V> operator, V identity) {
		if (operator == null || identity == null) {
			throw new IllegalArgumentException();
		}
		if (this.isInfinite()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		Iterator<V> iterator = this.iterator();
		V result = identity;
		while (iterator.hasNext()) {
			result = operator.apply(result, iterator.next());
		}
		return result;
	}

	/**
	 * Returns the sequence of values obtained from applying a mapping to all values of this sequence.
	 * <p>
	 * @param <W>     The type of the new sequence
	 * @param mapping The given mapping
	 * @return The new sequence
	 */
	public final <W> Sequence<W> map(final Function<? super V, ? extends W> mapping) {
		if (mapping == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<W>(source.length) {

			@Override
			public SequenceIterator<W> iterator() {
				return new SequenceIterator<W>() {

					private final Iterator<V> iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public W abstractNext() {
						return mapping.apply(this.iterator.next());
					}
				};
			}

		};
	}

	/**
	 * Returns the sequence of all values satisfying the given predicate.
	 * <p>
	 * @param predicate The given predicate
	 * @return The new sequence
	 */
	public final Sequence<V> filter(final Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>(Sequence.UNKNOWN) {

			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {

					private final SequenceIterator<V> iterator = source.iterator();
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
					public V abstractNext() {
						V result = this.nextValue;
						this.nextValue = null;
						return result;
					}

				};
			}
		};

	}

	/**
	 * Returns a sequence consisting of the values of this sequence, truncated to be no longer than {@code maxLength}.
	 * <p>
	 * @param maxLength The maximal length
	 * @return The truncated sequence
	 */
	public final Sequence<V> limit(final long maxLength) {
		return this.limit(BigInteger.valueOf(maxLength));
	}

	/**
	 * Returns a sequence consisting of the values of this sequence, truncated to be no longer than {@code maxLength}.
	 * <p>
	 * @param maxLength The maximal length
	 * @return The truncated sequence
	 */
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
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {

					private final Iterator<V> iterator = source.iterator();
					private BigInteger counter = MathUtil.ZERO;

					@Override
					public boolean hasNext() {
						return this.counter.compareTo(maxLength) < 0 && this.iterator.hasNext();
					}

					@Override
					public V abstractNext() {
						this.counter = this.counter.add(MathUtil.ONE);
						return this.iterator.next();
					}

				};
			}

		};
	}

	/**
	 * Returns a sequence consisting of the values of this sequence, truncated up to the first value satisfying the
	 * given predicate.
	 * <p>
	 * @param predicate The given predicate
	 * @return The truncated sequence
	 */
	public final Sequence<V> limit(final Predicate<V> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException();
		}
		final Sequence<V> source = this;
		return new Sequence<V>(Sequence.UNKNOWN) {

			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {

					private final SequenceIterator<V> iterator = source.iterator();
					private boolean found = false;

					@Override
					public boolean hasNext() {
						return !found && iterator.hasNext();
					}

					@Override
					public V abstractNext() {
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

	/**
	 * Returns a sequence consisting of the values of this sequence, except for the first one.
	 * <p>
	 * @return The new sequence with 1 skipped value
	 */
	public final Sequence<V> skip() {
		return this.skip(1);
	}

	/**
	 * Returns a sequence consisting of the values of this sequence, except for the first {@code n} values.
	 * <p>
	 * @param n The number of values to skip
	 * @return The new sequence with {@code n} skipped values
	 */
	public final Sequence<V> skip(final int n) {
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
			public SequenceIterator<V> iterator() {
				SequenceIterator<V> iterator = source.iterator();
				iterator.skip(n);
				return iterator;
			}
		};
	}

	/**
	 * Returns a new sequence consisting of the values grouped into groups of size {@code groupLength}. The groups of
	 * values are returned as immutable arrays. The last group may be shorter than {@code groupLength}.
	 * <p>
	 * @param groupLength The length of the groups
	 * @return The sequence of groups
	 */
	public Sequence<? extends ImmutableArray<V>> group(final int groupLength) {
		if (groupLength < 0) {
			throw new IllegalArgumentException();
		}
		BigInteger newLength;
		if (groupLength == 0) {
			newLength = Sequence.INFINITE;
		} else {
			if (this.length.equals(Sequence.UNKNOWN) || this.length.equals(Sequence.INFINITE)) {
				newLength = this.length;
			} else {
				newLength = MathUtil.divideUp(this.length, BigInteger.valueOf(groupLength));
			}
		}
		final Sequence<V> source = this;
		return new Sequence<ImmutableArray<V>>(newLength) {

			@Override
			public SequenceIterator<ImmutableArray<V>> iterator() {
				return new SequenceIterator<ImmutableArray<V>>() {

					private final SequenceIterator<V> iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public ImmutableArray<V> abstractNext() {
						return this.iterator.next(groupLength);
					}

				};

			}
		};

	}

	/**
	 * Returns the concatenation of this sequence with another sequence.
	 * <p>
	 * @param other The other sequence
	 * @return The concatenation of the two sequences
	 * @see MultiSequence#flatten()
	 */
	public final Sequence<V> conc(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).flatten();
	}

	/**
	 * Returns a sequence of pairs of values. The first value in each pair is taken from this sequence, and the second
	 * from the other sequence, one after another. The length of the new sequence corresponds to the minimal length of
	 * the two sequences.
	 * <p>
	 * @param other The other sequence
	 * @return The combination of the two sequences
	 * @see MultiSequence#combine()
	 */
	public final Sequence<DenseArray<V>> combine(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).combine();
	}

	/**
	 * Returns the sequence of all pairs of values by taking he first value from this sequence and the second from the
	 * other sequence. The length of the new sequence the product of the lengths of the two sequences.
	 * <p>
	 * @param other The other sequence
	 * @return The join of the two sequences
	 * @see MultiSequence#join()
	 */
	public final Sequence<DenseArray<V>> join(final Sequence<V> other) {
		return MultiSequence.getInstance(this, other).join();
	}

	/**
	 * Returns a new finite sequence consisting of the values contained in the input array. Using this method requires
	 * some care, since the input array is not checked for {@code null} values. If necessary, a filter using
	 * {@link Sequence#NOT_NULL} can be applied to the resulting sequence to eliminate {@code null} values. The
	 * resulting sequence is also not safe against modifications in the input array.
	 * <p>
	 * @param <V>    The type of the new sequence
	 * @param source The given array
	 * @return The new sequence
	 */
	public static <V> Sequence<V> getInstance(final V... source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(BigInteger.valueOf(source.length)) {

			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {
					private int pos = 0;

					@Override
					public boolean hasNext() {
						return this.pos < source.length;
					}

					@Override
					public V abstractNext() {
						return source[this.pos++];
					}

				};
			}
		};
	}

	/**
	 * Returns a new finite sequence consisting of the values contained in the given collection. Using this method
	 * requires some care, since the collection is not checked for {@code null} values. If necessary, a filter using
	 * {@link Sequence#NOT_NULL} can be applied to the resulting sequence to eliminate {@code null} values. The
	 * resulting sequence is also not safe against modifications in the given collection.
	 * <p>
	 * @param <V>    The type of the new sequence
	 * @param source The given collection
	 * @return The new sequence
	 */
	public static <V> Sequence<V> getInstance(final Collection<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(BigInteger.valueOf(source.size())) {

			@Override
			public SequenceIterator<V> iterator() {
				return SequenceIterator.getInstance(source.iterator());
			}
		};
	}

	/**
	 * Returns a new finite sequence consisting of the values contained in the given immutable array.
	 * <p>
	 * @param <V>    The type of the new sequence
	 * @param source The given immutable array
	 * @return The new sequence
	 */
	public static <V> Sequence<V> getInstance(final ImmutableArray<V> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>() {
			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {
					private int currentIndex = 0;

					@Override
					public boolean hasNext() {
						return this.currentIndex < source.getLength();
					}

					@Override
					public V abstractNext() {
						return source.getAt(this.currentIndex++);
					}
				};
			}
		};

	}

	/**
	 * Returns a new infinite sequence consisting of the values obtained from applying a mapping repeatedly to a given
	 * starting value. The first value in the sequence is the starting value.
	 * <p>
	 * @param <V>        The type of the new sequence
	 * @param startValue The starting value
	 * @param mapping    The mapping applied repeatedly to the starting value
	 * @return The new sequence
	 */
	public static <V> Sequence<V> getInstance(final V startValue, final Function<V, V> mapping) {
		if (startValue == null || mapping == null) {
			throw new IllegalArgumentException();
		}
		return new Sequence<V>(Sequence.INFINITE) {

			@Override
			public SequenceIterator<V> iterator() {
				return new SequenceIterator<V>() {

					private V currentValue = startValue;

					@Override
					public boolean hasNext() {
						return true;
					}

					@Override
					public V abstractNext() {
						V nextValue = this.currentValue;
						this.currentValue = mapping.apply(this.currentValue);
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
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Sequence)) {
			return false;
		}
		final Sequence<?> other = (Sequence<?>) obj;
		if (this.isInfinite() || other.isInfinite()) {
			return false;
		}
		Iterator<V> it1 = this.iterator();
		Iterator<?> it2 = other.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			if (!it1.next().equals(it2.next())) {
				return false;
			}
		}
		return !it1.hasNext() && !it2.hasNext();
	}

	@Override
	protected String defaultToStringContent() {
		if (this.isInfinite()) {
			return "INFINITE";
		}
		String str = "";
		String delimiter = "";
		for (V value : this) {
			str = str + delimiter + value;
			delimiter = ", ";
		}
		return "[" + str + "]";
	}

}
