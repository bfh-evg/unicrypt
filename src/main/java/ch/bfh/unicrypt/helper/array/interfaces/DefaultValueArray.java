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
 * For some immutable arrays, it is natural to define a default value. The existence of a default value allows to extend
 * the {@link ImmutableArray} interface. This interface is therfore an extension of {@link ImmutableArray} with some
 * additional convenience methods. The existence of a default value may also help to make more efficient implementations
 * of immutable arrays.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the immutable array
 */
public interface DefaultValueArray<V>
	   extends ImmutableArray<V> {

	/**
	 * Returns the default value of the immutable array.
	 * <p>
	 * @return The default value
	 */
	public V getDefault();

	/**
	 * Returns the sequence of all array indices for which the stored value matches with the default value.
	 * <p>
	 * @return The sequence of array indices
	 * @see ImmutableArray#getIndices(java.lang.Object)
	 */
	public Sequence<Integer> getIndices();

	/**
	 * Returns the sequence of all array indices for which the stored value differs from the default value.
	 * <p>
	 * @return The sequence of array indices
	 * @see ImmutableArray#getIndicesExcept(java.lang.Object)
	 */
	public Sequence<Integer> getIndicesExcept();

	/**
	 * Counts the number of occurrences of the default value in the array.
	 * <p>
	 * @return The number of occurrences
	 * @see ImmutableArray#count(java.lang.Object)
	 */
	public int count();

	/**
	 * Counts the number of element in the array different from the default value.
	 * <p>
	 * @return The number of occurrences
	 * @see ImmutableArray#count(java.lang.Object)
	 */
	public int countExcept();

	/**
	 * Counts the number of consecutive occurrences of the default value in the array prefix.
	 * <p>
	 * @return The number of occurrences
	 * @see ImmutableArray#countPrefix(java.lang.Object)
	 */
	public int countPrefix();

	/**
	 * Counts the number of consecutive occurrences of the default value in the array suffix.
	 * <p>
	 * @return The number of occurrences
	 * @see ImmutableArray#countSuffix(java.lang.Object)
	 */
	public int countSuffix();

	/**
	 * Returns a new immutable array with an additional default value inserted at the given index. The length is
	 * therefore increased by 1.
	 * <p>
	 * @param index The index of the inserted default value
	 * @return The new immutable array with the inserted default value
	 * @see ImmutableArray#insertAt(int, java.lang.Object)
	 */
	public DefaultValueArray<V> insertAt(int index);

	/**
	 * Returns a new immutable array with an additional default value inserted at the front. This is equivalent to
	 * {@code insertAt(0)}.
	 * <p>
	 * @return The new immutable array with the inserted default value
	 * @see ImmutableArray#add(java.lang.Object)
	 * @see DefaultValueArray#insertAt(int)
	 */
	public DefaultValueArray<V> insert();

	/**
	 * Returns a new immutable array with an additional default value inserted at its end. This is equivalent to
	 * {@code insertAt(n)}.
	 * <p>
	 * @return The new immutable array with the inserted default value
	 * @see ImmutableArray#add(java.lang.Object)
	 * @see DefaultValueArray#insertAt(int)
	 */
	public DefaultValueArray<V> add();

	/**
	 * Returns a new immutable array with a single value replaced by a default value. The length of the array remains
	 * unchanged.
	 * <p>
	 * @param index The index of the value to be replaced
	 * @return The new immutable array with the replaced value
	 * @see ImmutableArray#replaceAt(int, java.lang.Object)
	 */
	public DefaultValueArray<V> replaceAt(int index);

	/**
	 * Returns a new immutable array by adding a given number of default values as prefix to an existing immutable
	 * array.
	 * <p>
	 * @param length The number of added default values
	 * @return The new immutable array with the added prefix
	 * @see DefaultValueArray#addSuffix(int)
	 * @see DefaultValueArray#addPrefixAndSuffix(int, int)
	 */
	public DefaultValueArray<V> addPrefix(int length);

	/**
	 * Returns a new immutable array by adding a given number of default values as suffix to an existing immutable
	 * array.
	 * <p>
	 * @param length The number of added default values
	 * @return The new immutable array with the added suffix
	 * @see DefaultValueArray#addPrefix(int)
	 * @see DefaultValueArray#addPrefixAndSuffix(int, int)
	 */
	public DefaultValueArray<V> addSuffix(int length);

	/**
	 * Returns a new immutable array by adding a given number of default values as prefix and suffix to an existing
	 * immutable array. This method is a combination of {@link DefaultValueArray#addPrefix(int)} and
	 * {@link DefaultValueArray#addSuffix(int)}.
	 * <p>
	 * @param prefixLength The number of added default values as prefix
	 * @param suffixLength The number of added default values as suffix
	 * @return The new immutable array with the added prefix and suffix
	 * @see DefaultValueArray#addPrefix(int)
	 * @see DefaultValueArray#addSuffix(int)
	 */
	public DefaultValueArray<V> addPrefixAndSuffix(int prefixLength, int suffixLength);

	/**
	 * Returns a new immutable array obtained from removing all prefix default values.
	 * <p>
	 * @return The new immutable array
	 * @see DefaultValueArray#removeSuffix()
	 * @see DefaultValueArray#removePrefixAndSuffix()
	 */
	public DefaultValueArray<V> removePrefix();

	/**
	 * Returns a new immutable array obtained from removing all suffix default values.
	 * <p>
	 * @return The new immutable array
	 * @see DefaultValueArray#removePrefix()
	 * @see DefaultValueArray#removePrefixAndSuffix()
	 */
	public DefaultValueArray<V> removeSuffix();

	/**
	 * Returns a new immutable array obtained from removing all prefix and suffix default values. This method is a
	 * combination of {@link DefaultValueArray#removePrefix()} and {@link DefaultValueArray#removeSuffix()}.
	 * <p>
	 * @return The new immutable array
	 * @see DefaultValueArray#removePrefix()
	 * @see DefaultValueArray#removeSuffix()
	 */
	public DefaultValueArray<V> removePrefixAndSuffix();

	/**
	 * Returns a new immutable array obtained by shifting the values of a given array to the 'left'. This makes the
	 * array smaller. This method is equivalent to {@code shiftRight(-n)}.
	 * <p>
	 * @param n The size of the shift
	 * @return The new immutable array
	 * @see DefaultValueArray#shiftRight(int)
	 */
	public DefaultValueArray<V> shiftLeft(int n);

	/**
	 * Returns a new immutable array obtained by shifting the values of a given array to the 'right'. This makes the
	 * array bigger (by filling it up with default values). This method is equivalent to both {@code shiftRight(-n)} and
	 * {@code addPrefix(n)}.
	 * <p>
	 * @param n The size of the shift
	 * @return The new immutable array
	 * @see DefaultValueArray#shiftLeft(int)
	 */
	public DefaultValueArray<V> shiftRight(int n);

}
