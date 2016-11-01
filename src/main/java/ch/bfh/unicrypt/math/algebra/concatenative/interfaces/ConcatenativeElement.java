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
package ch.bfh.unicrypt.math.algebra.concatenative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an {@link Element} of a semigroup with concatenation as binary operation. No functionality
 * is added. Some return types are updated.
 * <p>
 * @param <V> The generic type of values stored in this element
 * <p>
 * <p>
 * @author R. Haenni
 */
public interface ConcatenativeElement<V>
	   extends Element<V> {

	/**
	 * Returns the length of the element.
	 * <p>
	 * @return The length of the element
	 */
	public int getLength();

	/**
	 * This method is a synonym for {@link Element#apply(Element)}. It concatenates this element with the given element.
	 * <p>
	 * @param element The given element
	 * @return The concatenation of the two elements
	 * @see Element#apply(Element)
	 */
	public ConcatenativeElement<V> concatenate(Element element);

	/**
	 * This method is a synonym for {@link Element#selfApply(long)}. It applies the concatenation operation multiple
	 * times to this element. This is a convenient method for {@link ConcatenativeElement#selfConcatenate(BigInteger)}.
	 * <p>
	 * @param amount The given amount
	 * @return The result of applying concatenation multiple times to this element
	 * @see Element#selfApply(long)
	 */
	public ConcatenativeElement<V> selfConcatenate(long amount);

	/**
	 * This method is a synonym for {@link Element#selfApply(BigInteger)}. It applies the concatenation operation
	 * multiple times to this element.
	 * <p>
	 * @param amount The given amount
	 * @return The result of applying concatenation multiple times to this element
	 */
	public ConcatenativeElement<V> selfConcatenate(BigInteger amount);

	/**
	 * This method is a synonym for {@link Element#selfApply(Element)} and the same as
	 * {@link ConcatenativeElement#selfConcatenate(BigInteger)}, except that the exponent is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} exponent can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param amount The given amount
	 * @return The result of applying concatenation multiple times to this element
	 */
	public ConcatenativeElement<V> selfConcatenate(Element<BigInteger> amount);

	/**
	 * This method is a synonym for {@link Element#selfApply()}. It concatenates this element with itself.
	 * <p>
	 * @return The concatenated with itself
	 * @see Element#selfApply()
	 */
	public ConcatenativeElement<V> selfConcatenate();

	/**
	 * This method is a synonym for {@link Element#isIdentity()}. Returns {@code true} if the element is the empty
	 * element of length 0. Throws an exception if the method is called for an element not belonging to a monoid.
	 * <p>
	 * @return {@code true} if this element is the empty element, {@code false} otherwise
	 * @see Element#isIdentity()
	 */
	public boolean isEmptyElement();

	@Override
	public ConcatenativeSemiGroup<V> getSet();

	@Override
	public ConcatenativeElement<V> apply(Element element);

	@Override
	public ConcatenativeElement<V> applyInverse(Element element);

	@Override
	public ConcatenativeElement<V> selfApply(BigInteger amount);

	@Override
	public ConcatenativeElement<V> selfApply(Element<BigInteger> amount);

	@Override
	public ConcatenativeElement<V> selfApply(long amount);

	@Override
	public ConcatenativeElement<V> selfApply();

	@Override
	public ConcatenativeElement<V> invert();

}
