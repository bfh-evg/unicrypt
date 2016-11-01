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
package ch.bfh.unicrypt.helper.array.interfaces;

import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * This interface represents immutable instances of arrays. The type of the values in an array is generic. After
 * creating an immutable array, it can no longer be modified. This interface specifies useful methods for accessing the
 * array values and for creating new arrays from existing ones. The number of values in an immutable array is finite.
 * The values of an array of length {@code n} are indexed from {@code 0} to {@code n-1}. Immutable arrays are
 * {@link Iterable}. There are multiple optimized implementations for different needs.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the immutable array
 */
public interface ImmutableArray<V>
	   extends Iterable<V> {

	/**
	 * Returns the length of the immutable array, i.e., the number of values.
	 * <p>
	 * @return The length of the immutable array
	 */
	public int getLength();

	/**
	 * Checks if the immutable array is empty. The length of an empty array is 0.
	 * <p>
	 * @return {@code true} if the array is empty, {@code false} otherwise
	 */
	public boolean isEmpty();

	/**
	 * Checks if all values of an immutable array are identical. All arrays of length 0 or 1 are uniform.
	 * <p>
	 * @return {@code true} if all values are identical, {@code false} otherwise
	 */
	public boolean isUniform();

	/**
	 * Returns the sequence of all array indices from 0 to n-1.
	 * <p>
	 * @return The sequence of all array indices
	 * @see ImmutableArray#getIndices(java.lang.Object)
	 * @see ImmutableArray#getIndicesExcept(java.lang.Object)
	 */
	public Sequence<Integer> getAllIndices();

	/**
	 * Returns the sequence of all array indices for which the stored value matches with the given value.
	 * <p>
	 * @param value The given value
	 * @return The sequence of array indices
	 * @see ImmutableArray#getAllIndices()
	 * @see ImmutableArray#getIndicesExcept(java.lang.Object)
	 */
	public Sequence<Integer> getIndices(V value);

	/**
	 * Returns the sequence of all array indices for which the stored values does not match with the given value.
	 * <p>
	 * @param value The given value
	 * @return The sequence of array indices.
	 * @see ImmutableArray#getAllIndices()
	 * @see ImmutableArray#getIndices(java.lang.Object)
	 */
	public Sequence<Integer> getIndicesExcept(V value);

	/**
	 * Counts the number of occurrences of a given value in the array.
	 * <p>
	 * @param value The given value
	 * @return The number of occurrences
	 * @see ImmutableArray#countExcept(java.lang.Object)
	 * @see ImmutableArray#countPrefix(java.lang.Object)
	 * @see ImmutableArray#countSuffix(java.lang.Object)
	 */
	public int count(V value);

	/**
	 * Counts the number of values in the array that are different from a given value.
	 * <p>
	 * @param value The given value
	 * @return The number of occurrences
	 * @see ImmutableArray#count(java.lang.Object)
	 */
	public int countExcept(V value);

	/**
	 * Counts the number of consecutive occurrences of a given value in the array prefix.
	 * <p>
	 * @param value The given value
	 * @return The number of occurrences
	 * @see ImmutableArray#count(java.lang.Object)
	 * @see ImmutableArray#countSuffix(java.lang.Object)
	 */
	public int countPrefix(V value);

	/**
	 * Counts the number of consecutive occurrences of a given value in the array suffix.
	 * <p>
	 * @param value The given value
	 * @return The number of occurrences
	 * @see ImmutableArray#count(java.lang.Object)
	 * @see ImmutableArray#countPrefix(java.lang.Object)
	 */
	public int countSuffix(V value);

	/**
	 * Returns the value stored at the given index. Valid indices range from 0 to n-1.
	 * <p>
	 * @param index The given index
	 * @return The corresponding value
	 */
	public V getAt(int index);

	/**
	 * Returns the first value of the array. This method is equivalent to {@code getAt(0)}.
	 * <p>
	 * @return The first value
	 * @see ImmutableArray#getAt(int)
	 */
	public V getFirst();

	/**
	 * Returns the last value of the array. This method is equivalent to {@code getAt(n-1)}.
	 * <p>
	 * @return The last value
	 * @see ImmutableArray#getAt(int)
	 */
	public V getLast();

	/**
	 * Extracts and returns a consecutive sub-array of a given length. The first value in the sub-array corresponds to
	 * {@code getAt(fromIndex)}.
	 * <p>
	 * @param fromIndex The index of the first extracted value
	 * @param length    The given length
	 * @return The extracted sub-array
	 * @see ImmutableArray#extractPrefix(int)
	 * @see ImmutableArray#extractSuffix(int)
	 * @see ImmutableArray#extractRange(int, int)
	 */
	public ImmutableArray<V> extract(int fromIndex, int length);

	/**
	 * Extracts and returns a prefix sub-array of a given length. This is equivalent to {@code extract(0, length)}.
	 * <p>
	 * @param length The given length
	 * @return The extracted sub-array
	 * @see ImmutableArray#extract(int, int)
	 * @see ImmutableArray#extractSuffix(int)
	 * @see ImmutableArray#extractRange(int, int)
	 */
	public ImmutableArray<V> extractPrefix(int length);

	/**
	 * Extracts and returns a suffix sub-array of a given length. This is equivalent to
	 * {@code extract(n-length, length)}.
	 * <p>
	 * @param length The given length
	 * @return The extracted sub-array
	 * @see ImmutableArray#extract(int, int)
	 * @see ImmutableArray#extractPrefix(int)
	 * @see ImmutableArray#extractRange(int, int)
	 */
	public ImmutableArray<V> extractSuffix(int length);

	/**
	 * Extracts and returns a consecutive sub-array. The first value in the sub-array corresponds to
	 * {@code getAt(fromIndex)} and the last to {@code getAt(toIndex)}. This method is equivalent to
	 * {@code extract(fromIndex, toIndex-fromIndex+1)}.
	 * <p>
	 * @param fromIndex The index of the first extracted value
	 * @param toIndex   The index of the last extracted value
	 * @return The extracted sub-array
	 * @see ImmutableArray#extract(int, int)
	 * @see ImmutableArray#extractPrefix(int)
	 * @see ImmutableArray#extractSuffix(int)
	 */
	public ImmutableArray<V> extractRange(int fromIndex, int toIndex);

	/**
	 * Returns a new immutable array obtained by removing a range of values of a given length from the existing
	 * immutable array.
	 * <p>
	 * @param fromIndex The index of the first removed value
	 * @param length    The number of removed values
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#removePrefix(int)
	 * @see ImmutableArray#removeSuffix(int)
	 * @see ImmutableArray#removeRange(int, int)
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> remove(int fromIndex, int length);

	/**
	 * Returns a new immutable array obtained by removing a prefix of a given length from the existing immutable array.
	 * This is equivalent to {@code remove(0, length)}.
	 * <p>
	 * @param length The number of removed values
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#remove(int, int)
	 * @see ImmutableArray#removeSuffix(int)
	 * @see ImmutableArray#removeRange(int, int)
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> removePrefix(int length);

	/**
	 * Returns a new immutable array obtained by removing a suffix of a given length from the existing immutable array.
	 * This is equivalent to {@code remove(n-length, length)}.
	 * <p>
	 * @param length The number of removed values
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#remove(int, int)
	 * @see ImmutableArray#removePrefix(int)
	 * @see ImmutableArray#removeRange(int, int)
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> removeSuffix(int length);

	/**
	 * Returns a new immutable array obtained by removing a range of values. The first removed value corresponds to
	 * {@code getAt(fromIndex)} and the last to {@code getAt(toIndex)}. This method is equivalent to
	 * {@code remove(fromIndex, toIndex-fromIndex+1)}.
	 * <p>
	 *
	 * @param fromIndex The index of the first removed value
	 * @param toIndex   The index of the last extracted value
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#remove(int, int)
	 * @see ImmutableArray#removePrefix(int)
	 * @see ImmutableArray#removeSuffix(int)
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> removeRange(int fromIndex, int toIndex);

	/**
	 * Returns a new immutable array obtained by removing a single value. The removed value corresponds to
	 * {@code getAt(index)}. This method is equivalent to {@code remove(index, 1)}.
	 * <p>
	 * @param index The index of the removed value
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#remove(int, int)
	 * @see ImmutableArray#removePrefix(int)
	 * @see ImmutableArray#removeSuffix(int)
	 * @see ImmutableArray#removeRange(int, int)
	 */
	public ImmutableArray<V> removeAt(int index);

	/**
	 * Returns a new immutable array obtained by removing the first value. This method is equivalent to
	 * {@code removeAt(0)}.
	 * <p>
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> removeFirst();

	/**
	 * Returns a new immutable array obtained by removing the last value. This method is equivalent to
	 * {@code removeAt(n-0)}.
	 * <p>
	 * @return The new immutable array with the remaining values
	 * @see ImmutableArray#removeAt(int)
	 */
	public ImmutableArray<V> removeLast();

	/**
	 * Returns a new immutable array with an additional value inserted at the given index. The length is therefore
	 * increased by 1.
	 * <p>
	 * @param index The index of the inserted value
	 * @param value The new value to be inserted
	 * @return The new immutable array with the inserted value
	 * @see ImmutableArray#add(java.lang.Object)
	 */
	public ImmutableArray<V> insertAt(int index, V value);

	/**
	 * Returns a new immutable array with an additional value inserted at the front. This is equivalent to
	 * {@code insertAt(0, value)}.
	 * <p>
	 * @param value The new value to be inserted
	 * @return The new immutable array with the inserted value
	 * @see ImmutableArray#insertAt(int, java.lang.Object)
	 */
	public ImmutableArray<V> insert(V value);

	/**
	 * Returns a new immutable array with an additional value inserted at its end. This is equivalent to
	 * {@code insertAt(n, value)}.
	 * <p>
	 * @param value The new value to be inserted
	 * @return The new immutable array with the inserted value
	 * @see ImmutableArray#insertAt(int, java.lang.Object)
	 */
	public ImmutableArray<V> add(V value);

	/**
	 * Returns a new immutable array with a single value replaced by a new one. The length of the array remains
	 * unchanged.
	 * <p>
	 * @param index The index of the value to be replaced
	 * @param value The new value replacing the old one
	 * @return The new immutable array with the replaced value
	 */
	public ImmutableArray<V> replaceAt(int index, V value);

	/**
	 * Returns a new immutable array by appending two existing immutable arrays. The length of the new immutable array
	 * is the sum of the two corresponding lengths.
	 * <p>
	 * @param other The second array
	 * @return The new immutable array obtained from appending the existing ones.
	 */
	public ImmutableArray<V> append(ImmutableArray<V> other);

	/**
	 * Returns a new immutable array with the same values arranged in reverse order.
	 * <p>
	 * @return The reversed immutable array
	 */
	public ImmutableArray<V> reverse();

	/**
	 * Breaks an immutable array into multiple immutable arrays. The break point a specified by a series of increasing
	 * indices. The number of newly created immutable arrays corresponds to the number of given indices plus 1.
	 * <p>
	 * @param indices The series of indices specifying the break points.
	 * @return The resulting immutable arrays
	 */
	public ImmutableArray<V>[] split(int... indices);

	/**
	 * Creates and returns a sequence containing all values from the immutable array.
	 * <p>
	 * @return The sequence of values
	 */
	public Sequence<V> getSequence();

}
