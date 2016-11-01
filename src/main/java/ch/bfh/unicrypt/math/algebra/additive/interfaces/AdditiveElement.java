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
package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an {@link Element} of a additively written semigroup. No functionality is added. Some
 * return types are updated.
 * <p>
 * @param <V> The generic type of values stored in the element
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public interface AdditiveElement<V>
	   extends Element<V> {

	/**
	 * This method is a synonym for {@link Element#apply(Element)}. It computes the sum of this element and the given
	 * element.
	 * <p>
	 * @param element The given element
	 * @return The sum of the two elements
	 * @see Element#apply(Element)
	 */
	public AdditiveElement<V> add(Element element);

	/**
	 * This method is a synonym for {@link Element#selfApply(long)}. It multiplies this element by some factor. This is
	 * a convenient method for {@link AdditiveElement#times(BigInteger)}.
	 * <p>
	 * @param factor The given factor
	 * @return The element multiplied by the factor
	 * @see Element#selfApply(long)
	 */
	public AdditiveElement<V> times(long factor);

	/**
	 * This method is a synonym for {@link Element#selfApply(long)}. It multiplies this element by some factor.
	 * <p>
	 * @param factor The given factor
	 * @return The element multiplied by the factor
	 * @see Element#selfApply(long)
	 */
	public AdditiveElement<V> times(BigInteger factor);

	/**
	 * This method is a synonym for {@link Element#selfApply(Element)} and the same as
	 * {@link AdditiveElement#times(BigInteger)}, except that the exponent is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} exponent can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param factor The given factor
	 * @return The element multiplied by the factor
	 * @see Element#selfApply(Element)
	 */
	public AdditiveElement<V> times(Element<BigInteger> factor);

	/**
	 * This method is a synonym for {@link Element#selfApply()}. It multiplies this element by 2.
	 * <p>
	 * @return The element multiplied by 2
	 * @see Element#selfApply()
	 */
	public AdditiveElement<V> timesTwo();

	/**
	 * This method is a synonym for {@link Element#isIdentity()}. Returns {@code true} if the element is the identity
	 * element of the monoid. Throws an exception if the method is called for an element not belonging to a monoid.
	 * <p>
	 * @return {@code true} if the element is the identity element, {@code false} otherwise
	 * @see Element#isIdentity()
	 */
	public boolean isZero();

	/**
	 * This method is a synonym for {@link Element#invert()}. It computes the additive inverse of the given element.
	 * Throws an exception if the method is called for an element not belonging to a group.
	 * <p>
	 * @return The additive inverse of the element
	 * @see Element#invert()
	 */
	public AdditiveElement<V> negate();

	/**
	 * This method is a synonym for {@link Element#applyInverse(Element)}. It subtracts the given element from the this
	 * element.Throws an exception if the method is called for an element not belonging to a group.
	 * <p>
	 * @param element The given element
	 * @return The given element subtracted from this element
	 * @see Element#applyInverse(Element)
	 */
	public AdditiveElement<V> subtract(Element element);

	public AdditiveElement<V> divide(long divisor);

	public AdditiveElement<V> divide(BigInteger divisor);

	public AdditiveElement<V> halve();

	@Override
	public AdditiveSemiGroup<V> getSet();

	@Override
	public AdditiveElement<V> apply(Element element);

	@Override
	public AdditiveElement<V> selfApply(BigInteger factor);

	@Override
	public AdditiveElement<V> selfApply(Element<BigInteger> factor);

	@Override
	public AdditiveElement<V> selfApply(long factor);

	@Override
	public AdditiveElement<V> selfApply();

	@Override
	public AdditiveElement<V> invertSelfApply(long amount);

	@Override
	public AdditiveElement<V> invertSelfApply(BigInteger amount);

	@Override
	public AdditiveElement<V> invertSelfApply(Element<BigInteger> amount);

	@Override
	public AdditiveElement<V> invertSelfApply();

	@Override
	public AdditiveElement<V> invert();

	@Override
	public AdditiveElement<V> applyInverse(Element element);

}
