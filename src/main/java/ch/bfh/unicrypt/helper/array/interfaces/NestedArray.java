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

/**
 * This interface extends the {@link ImmutableArray} interface with a single additional method for accessing the values
 * of a nested array. In a nested array, each value is either atomic or another array of the same generic type. As such,
 * a nested array can be regarded as a tree.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the nested array
 */
public interface NestedArray<V>
	   extends ImmutableArray<V> {

	/**
	 * Returns the value from the given nested array that corresponds to the given list of indices. For example, a list
	 * of indices (2,3,0) returns the first (index 0) value of the fourth (index 3) array of the third (index 2) array
	 * of the given nested array. The structure of the given nested array determines the the maximal length of the list
	 * of indices and their ranges.
	 * <p>
	 * @param indices The given list of indices
	 * @return The corresponding value
	 */
	public V getAt(int... indices);

}
