/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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

/**
 * This interface represents immutable instances of arrays. The type
 * of the elements in an array is generic. After creating an immutable array, it 
 * can no longer be modified. This interface specifies a large set of useful methods
 * for accessing the array values and for creating new arrays from existing ones.
 * The number of values in an immutable array is finite. The values of an array
 * of length n are indexed from 0 to n-1. Immutable arrays are {@link Iterable}.
 *
 * @author Rolf Haenni
 * @version 2.0
 * <p>
 * @param <V> The generic type of the values in the array
 */
public interface ImmutableArray<V extends Object>
	   extends Iterable<V> {

	/**
	 * Returns the length of the immutable array, i.e., the number of values.
	 * @return The length of the immutable array
	 */
	public int getLength();

	/**
	 * Checks if the immutable array is empty. The length of an empty array is 0.
	 * @return {@code true} if the array is empty, {@code false} otherwise
	 */
	public boolean isEmpty();

	/**
	 * Checks if all values of an immutable array are identical. All arrays of
	 * length 0 or 1 are uniform.
	 * @return {@code true} if all values are identical, {@code false} otherwise
	 */
	public boolean isUniform();

	/**
	 * Returns an iterable collection of all array indices from 0 to n-1.
	 * @return The iterable collection of all array indices.
	 * @see getIndices(V value)
	 * @see getIndicesExcept(V value)
	 */
	public Iterable<Integer> getAllIndices();

	/**
	 * Returns an iterable collection of all array indices for which the stored
	 * value matches with the given value.
	 * @param value The given value
	 * @return An iterable collection of array indices.
	 * @see getAllIndices()
	 * @see getIndicesExcept(V value)
	 */
	public Iterable<Integer> getIndices(V value);

	/**
	 * Returns an iterable collection of all array indices for which the stored
	 * values does not mathc with the given value.
	 * @param value The given value
	 * @return An iterable collection of array indices.
	 * @see getAllIndices()
	 * @see getIndices(V value)
	 */
	public Iterable<Integer> getIndicesExcept(V value);

	/**
	 * Counts the number of occurrences of a given value in the array.
	 * @param value The given value
	 * @return The number of occurrences
	 * @see countPrefix(V value)
	 * @see countSuffix(V value)
	 */
	public int count(V value);

	/**
	 * Counts the number of consecutive occurrences of a given value
	 * in the array prefix.
	 * @param value The given value
	 * @return The number of occurrences
	 * @see count(V value)
	 * @see countSuffix(V value)
	 */
	public int countPrefix(V value);

	/**
	 * Counts the number of consecutive occurrences of a given value
	 * in the array suffix.
	 * @param value The given value
	 * @return The number of occurrences
	 * @see count(V value)
	 * @see countPrefix(V value)
	 */
	public int countSuffix(V value);

	/**
	 * Returns the value stored at the given index. Valid indixes range
	 * from 0 to n-1.
	 * @param index The given index
	 * @return The corresponding value
	 */
	public V getAt(int index);

	/**
	 * Returns the first value of the array. This method is equivalent to {@code getAt(0)}.
	 * @return The first value
	 * @see getAt(int index)
	 */
	public V getFirst();

	/**
	 * Returns the last value of the array. This method is equivalent to {@code getAt(n-1)}.
	 * @return The last value
	 * @see getAt(int index)
	 */
	public V getLast();

	/**
	 * Extracts and returns a consecutive sub-array of a given length. The first value in 
	 * the sub-array corresponds to {@code getAt(fromIndex)}.
	 * @param fromIndex The index of the first extracted value
	 * @param length The given length
	 * @return The extracted sub-array
	 * @see extractPrefix(int length)
	 * @see extractSuffix(int length)
	 * @see extractRange(int fromIndex, int toIndex)
	 */
	public ImmutableArray<V> extract(int fromIndex, int length);

	/**
	 * Extracts and returns a prefix sub-array of a given length. This is equivalent
	 * to {@code extract(0, length)}.
	 * @param length The given length
	 * @return The extracted sub-array
	 * @see extract(int fromIndex, int length)
	 * @see extractSuffix(int length)
	 * @see extractRange(int fromIndex, int toIndex)
	 */
	public ImmutableArray<V> extractPrefix(int length);

	/**
	 * Extracts and returns a suffix sub-array of a given length. This is equivalent
	 * to {@code extract(n-length, length)}.
	 * @param length The given length
	 * @return The extracted sub-array
	 * @see extract(int fromIndex, int length)
	 * @see extractPrefix(int length)
	 * @see extractRange(int fromIndex, int toIndex)
	 */
	public ImmutableArray<V> extractSuffix(int length);

	/**
	 * Extracts and returns a consecutive sub-array. The first value in 
	 * the sub-array corresponds to {@code getAt(fromIndex)} and the last
	 * to {@code getAt(toIndex)}. This method is equivalent to 
	 * {@code extract(fromIndex, toIndex-fromIndex+1)}.
	 * @param fromIndex The index of the first extracted value
	 * @param toIndex The index of the last extracted value
	 * @return The extracted sub-array
	 * @see extract(int fromIndex, int length)
	 * @see extractPrefix(int length)
	 * @see extractSuffix(int length)
	 */
	public ImmutableArray<V> extractRange(int fromIndex, int toIndex);

	/**
	 * Returns a new array obtained by removing a range of values of a given length
	 * from the existing array.
	 * @param fromIndex The index of the first removed value
	 * @param length The number of removed values
	 * @return The new array with the remaining values
	 */
	public ImmutableArray<V> remove(int fromIndex, int length);

	/**
	 *
	 * @param n
	 * @return
	 */
	public ImmutableArray<V> removePrefix(int length);

	/**
	 *
	 * @param n
	 * @return
	 */
	public ImmutableArray<V> removeSuffix(int length);

	/**
	 *
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public ImmutableArray<V> removeRange(int fromIndex, int toIndex);

	/**
	 *
	 * @param index
	 * @return
	 */
	public ImmutableArray<V> removeAt(int index);

	/**
	 *
	 * @param index
	 * @param value
	 * @return
	 */
	public ImmutableArray<V> insertAt(final int index, final V value);

	/**
	 *
	 * @param index
	 * @param value
	 * @return
	 */
	public ImmutableArray<V> replaceAt(int index, V value);

	/**
	 *
	 * @param value
	 * @return
	 */
	public ImmutableArray<V> add(V value);

	/**
	 *
	 * @param other
	 * @return
	 */
	public ImmutableArray<V> append(ImmutableArray<V> other);

	/**
	 *
	 * @return
	 */
	public ImmutableArray<V> reverse();

	/**
	 *
	 * @param indices
	 * @return
	 */
	public ImmutableArray<V>[] split(int... indices);

}
