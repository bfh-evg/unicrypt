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
package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an {@link Element} of a multiplicatively written semigroup. No functionality is added. Some
 * return types are updated.
 * <p>
 * @param <V> The generic type of values stored in this element
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public interface MultiplicativeElement<V>
	   extends Element<V> {

	/**
	 * This method is a synonym for {@link Element#apply(Element)}. It multiplies this element with the given element.
	 * <p>
	 * @param element The given element
	 * @return The result of multiplying the two elements
	 * @see Element#apply(Element)
	 */
	public MultiplicativeElement<V> multiply(Element element);

	/**
	 * This method is a synonym for {@link Element#selfApply(long)}. It raises the element to the power of the given
	 * exponent. This is a convenient method for {@link MultiplicativeElement#power(BigInteger)}.
	 * <p>
	 * @param exponent The given exponent
	 * @return The element raised to the power of the exponent
	 * @see Element#selfApply(long)
	 */
	public MultiplicativeElement<V> power(long exponent);

	/**
	 * This method is a synonym for {@link Element#selfApply(BigInteger)}. It raises the element to the power of the
	 * given exponent.
	 * <p>
	 * @param exponent The given exponent
	 * @return The element raised to the power of the exponent
	 * @see Element#selfApply(BigInteger)
	 */
	public MultiplicativeElement<V> power(BigInteger exponent);

	/**
	 * This method is a synonym for {@link Element#selfApply(Element)} and the same as
	 * {@link MultiplicativeElement#power(BigInteger)}, except that the exponent is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} exponent can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param exponent The given exponent
	 * @return The element raised to the power of the exponent
	 * @see Element#selfApply(Element)
	 */
	public MultiplicativeElement<V> power(Element<BigInteger> exponent);

	/**
	 * This method is a synonym for {@link Element#selfApply()}. It computes the square of this element.
	 * <p>
	 * @return The square of this element
	 * @see Element#selfApply()
	 */
	public MultiplicativeElement<V> square();

	/**
	 * This method is a synonym for {@link Element#isIdentity()}. Returns {@code true} if the element is the identity
	 * element of the monoid. Throws an exception if the method is called for an element not belonging to a monoid.
	 * <p>
	 * @return {@code true} if the element is the identity element, {@code false} otherwise
	 * @see Element#isIdentity()
	 */
	public boolean isOne();

	/**
	 * This method is a synonym for {@link Element#invert()}. It computes the multiplicative inverse of the element.
	 * Throws an exception if the method is called for an element not belonging to a group.
	 * <p>
	 * @return The multiplicative inverse of the element
	 * @see Element#invert()
	 */
	public MultiplicativeElement<V> oneOver();

	/**
	 * This method is a synonym for {@link Element#applyInverse(Element)}. It divides this element over the given
	 * element. Throws an exception if the method is called for an element not belonging to a group.
	 * <p>
	 * @param element The given element
	 * @return This element divided over the given element
	 * @see Element#applyInverse(Element)
	 */
	public MultiplicativeElement<V> divide(Element element);

	public MultiplicativeElement<V> nthRoot(long n);

	public MultiplicativeElement<V> nthRoot(BigInteger n);

	public MultiplicativeElement<V> nthRoot(Element<BigInteger> n);

	public MultiplicativeElement<V> squareRoot();

	@Override
	public MultiplicativeSemiGroup<V> getSet();

	@Override
	public MultiplicativeElement<V> apply(Element element);

	@Override
	public MultiplicativeElement<V> selfApply(long exponent);

	@Override
	public MultiplicativeElement<V> selfApply(BigInteger exponent);

	@Override
	public MultiplicativeElement<V> selfApply(Element<BigInteger> exponent);

	@Override
	public MultiplicativeElement<V> selfApply();

	@Override
	public MultiplicativeElement<V> invertSelfApply(long amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply(BigInteger amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply(Element<BigInteger> amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply();

	@Override
	public MultiplicativeElement<V> invert();

	@Override
	public MultiplicativeElement<V> applyInverse(Element element);

}
