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
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import java.math.BigInteger;

/**
 * This interface represents the mathematical concept of a group. A group is a monoid for which inverse elements exist
 * for all elements in the group. It is therefore implemented as a specialization of {@link Monoid}.
 * <p>
 *
 * @param <V> The generic type of the values representing the elements of a group
 * @see "Handbook of Applied Cryptography, Definition 2.162"
 * @see Element
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Group<V>
	   extends Monoid<V> {

	/**
	 * Computes and returns the inverse of the given group element.
	 * <p>
	 * @param element The given element
	 * @return The inverse of the given element
	 */
	public Element<V> invert(Element element);

	/**
	 * Applies the binary operation to the first and the inverse of the second element.
	 * <p>
	 * @param element1 The first element
	 * @param element2 The second element
	 * @return The result of applying the group operation to the the first and the inverse of the second element
	 */
	public Element<V> applyInverse(Element element1, Element element2);

	/**
	 * Inverts applying the binary operation repeatedly to {@code amount} many instances of a given input element. To
	 * perform this operation, the group order must be known and {@code amount} must be relatively prime to the order.
	 * This is a convenient method for {@link Group#invertSelfApply(Element, BigInteger)}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of inverting the application of the operation multiple times to the input element
	 */
	public Element<V> invertSelfApply(Element element, long amount);

	/**
	 * Inverts applying the binary operation repeatedly to {@code amount} many instances of a given input element. To
	 * perform this operation, the group order must be known and {@code amount} must be relatively prime to the order.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of inverting the application of the operation multiple times to the input element
	 */
	public Element<V> invertSelfApply(Element element, BigInteger amount);

	/**
	 * Same as {@link Group#invertSelfApply(Element, BigInteger)}, except that the amount is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} value can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of inverting the application of the operation multiple times to the input element
	 */
	public Element<V> invertSelfApply(Element element, Element<BigInteger> amount);

	/**
	 * Inverts applying the binary operation to two instances of a given element. This is equivalent to
	 * {@link Group#invertSelfApply(Element, long)} for {@code amount=2}.
	 * <p>
	 * @param element The given input element
	 * @return The result of inverting the application of the operation to two instances of the input element
	 */
	public Element<V> invertSelfApply(Element element);

}
