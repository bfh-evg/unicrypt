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

import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.math.BigInteger;

/**
 * This interface represents the mathematical concept of a semigroup. It defines a set of elements and an associative
 * (but not necessarily commutative) binary operation. It is implemented as a specialization of {@link Set}.
 * <p>
 * @param <V> The generic type of the values representing the elements of a semigroup
 * @see Element
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface SemiGroup<V>
	   extends Set<V> {

	/**
	 * Applies the binary operation to two input elements (in the given order).
	 * <p>
	 * @param element1 The first input element
	 * @param element2 The second input element
	 * @return The result of applying the binary operation to the two input elements
	 */
	public Element<V> apply(Element element1, Element element2);

	/**
	 * Applies the binary group operation sequentially to multiple input elements (in the given order). The elements are
	 * given as an array.
	 * <p>
	 * @param elements The given array of input elements
	 * @return The result of applying the operation to the input elements
	 */
	public Element<V> apply(Element... elements);

	/**
	 * Applies the binary group operation sequentially to multiple input elements (in the given order). The elements are
	 * given as an immutable array.
	 * <p>
	 * @param elements The given immutable array of input elements
	 * @return The result of applying the operation to the input elements
	 */
	public Element<V> apply(ImmutableArray<Element> elements);

	/**
	 * Applies the binary group operation sequentially to multiple input elements (in the given order). The elements are
	 * given as a sequence.
	 * <p>
	 * @param elements The given sequence of input elements
	 * @return The result of applying the operation to the input elements
	 */
	public Element<V> apply(Sequence<Element> elements);

	/**
	 * Applies the binary operation repeatedly to {@code amount} many instances of a given input element. This is a is a
	 * convenient method for {@link SemiGroup#selfApply(Element, BigInteger)}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying the operation multiple times to the input element
	 */
	public Element<V> selfApply(Element element, long amount);

	/**
	 * Applies the binary operation repeatedly to {@code amount} many instances of a given input element.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying the operation multiple times to the input element
	 */
	public Element<V> selfApply(Element element, BigInteger amount);

	/**
	 * Same as {@link SemiGroup#selfApply(Element, BigInteger)}, except that the amount is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} value can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying the operation multiple times to the input element
	 */
	public Element<V> selfApply(Element element, Element<BigInteger> amount);

	/**
	 * Applies the binary operation to two instances of a given element. This is equivalent to
	 * {@link SemiGroup#selfApply(Element, long)} for {@code amount=2}.
	 * <p>
	 * @param element The given input element
	 * @return The result of applying the operation to two instances of the input element
	 */
	public Element<V> selfApply(Element element);

	/**
	 * Applies the binary operation sequentially to the results of computing {@link #selfApply(Element, BigInteger)}
	 * multiple times. This operation is sometimes called 'weighed sum' or 'product-of-powers', depending on whether the
	 * operation is an addition or multiplication. If the two input arrays are not of equal length, an exception is
	 * thrown.
	 * <p>
	 * @param elements The given array of elements
	 * @param amounts  Corresponding amounts
	 * @return The result of this operation
	 */
	public Element<V> multiSelfApply(Element[] elements, BigInteger[] amounts);

}
