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
 * For some immutable arrays, it is natural to define a default value. The existence of
 * a default value allows to extend the {@link ImmutableArray} interface. This 
 * interface is therfore an extension of {@link ImmutableArray}
 * with some additional convenience methods. The existence of a default value may also
 * help to make more efficient implementations of immutable arrays. 
 * @author Rolf Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the immutable array
 */
public interface DefaultValueArray<V extends Object>
	   extends ImmutableArray<V> {

	/**
	 * Returns the default value of the immutable array.
	 * @return The default value
	 */
	public V getDefault();

	/**
	 * Returns an iterable collection of all array indices for which the stored
	 * value matches with the default value.
	 * @return An iterable collection of array indices
	 * @see ImmutableArray#getIndices(java.lang.Object) 
	 */
	public Iterable<Integer> getIndices();

	/**
	 *
	 * @return
	 * @see ImmutableArray#getIndicesExcept(java.lang.Object) 
	 */
	public Iterable<Integer> getIndicesExcept();

	/**
	 *
	 * @return
	 * @see ImmutableArray#count(java.lang.Object)  
	 */
	public int count();

	/**
	 *
	 * @return
	 * @see ImmutableArray#countPrefix(java.lang.Object)  
	 */
	public int countPrefix();

	/**
	 *
	 * @return
	 * @see ImmutableArray#countSuffix(java.lang.Object)  
	 */
	public int countSuffix();

	/**
	 *
	 * @param index
	 * @return
	 * @see ImmutableArray#insertAt(int, java.lang.Object)
	 */
	public ImmutableArray<V> insertAt(final int index);

	/**
	 *
	 * @param index
	 * @return
	 * @see ImmutableArray#replaceAt(int, java.lang.Object)
	 */
	public ImmutableArray<V> replaceAt(int index);

	/**
	 *
	 * @return
	 * @see ImmutableArray#add(int, java.lang.Object)
	 */
	public ImmutableArray<V> add();

	/**
	 *
	 * @param length
	 * @return
	 */
	public ImmutableArray<V> appendPrefix(int length);

	/**
	 *
	 * @param length
	 * @return
	 */
	public ImmutableArray<V> appendSuffix(int length);

	/**
	 *
	 * @param prefixLength
	 * @param suffixLength
	 * @return
	 */
	public ImmutableArray<V> appendPrefixAndSuffix(int prefixLength, int suffixLength);

	/**
	 *
	 * @return
	 */
	public ImmutableArray<V> removePrefix();

	/**
	 *
	 * @return
	 */
	public ImmutableArray<V> removeSuffix();

	// left here means making the array smaller

	/**
	 *
	 * @param n
	 * @return
	 */
	public ImmutableArray<V> shiftLeft(int n);

	// right here means making the array larger and fill up with default values

	/**
	 *
	 * @param n
	 * @return
	 */
	public ImmutableArray<V> shiftRight(int n);

}
