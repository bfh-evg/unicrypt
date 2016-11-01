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
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;

/**
 * This interface provides the renaming of some methods for the case of a multiplicatively written commutative
 * {@link Group}. No functionality is added. Some return types are adjusted.
 * <p>
 * @param <V> The generic type of the values stored in the elements of this group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeGroup<V>
	   extends Group<V>, MultiplicativeMonoid<V> {

	/**
	 * This method is a synonym for {@link Group#invert(Element)}. It computes the multiplicative inverse of the given
	 * element.
	 * <p>
	 * @param element The given element
	 * @return The multiplicative inverse of the given element
	 * @see Group#invert(Element)
	 */
	public MultiplicativeElement<V> oneOver(Element element);

	/**
	 * This method is a synonym for {@link Group#applyInverse(Element, Element)}. It computes the division of the first
	 * element over the second element.
	 * <p>
	 * @param element1 The first given element
	 * @param element2 The second given element
	 * @return The first element divided over the second element
	 * @see Group#applyInverse(Element, Element)
	 */
	public MultiplicativeElement<V> divide(Element element1, Element element2);

	/**
	 * This method is a synonym for {@link Group#invertSelfApply(Element, long)}. It computes the nth root of the given
	 * element. To perform this operation, the group order must be known and {@code amount} must be relatively prime to
	 * the order. This is a convenient method for {@link MultiplicativeGroup#nthRoot(Element, BigInteger)}.
	 * <p>
	 * @param element The given input element
	 * @param n       The given degree of the root
	 * @return The nth root of the input element
	 * @see Group#invertSelfApply(Element, long)
	 */
	public MultiplicativeElement<V> nthRoot(Element element, long n);

	/**
	 * This method is a synonym for {@link Group#invertSelfApply(Element, BigInteger)}. It computes the nth root of the
	 * given element. To perform this operation, the group order must be known and {@code amount} must be relatively
	 * prime to the order.
	 * <p>
	 * @param element The given input element
	 * @param n       The given degree of the root
	 * @return The nth root of the input element
	 * @see Group#invertSelfApply(Element, BigInteger)
	 */
	public MultiplicativeElement<V> nthRoot(Element element, BigInteger n);

	/**
	 * This method is a synonym for {@link Group#invertSelfApply(Element, Element)} and the same as
	 * {@link MultiplicativeGroup#nthRoot(Element, BigInteger)}, except that the degree of the root is given as an
	 * instance of {@code Element<BigInteger>}, from which a {@code BigInteger} degree can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param element The given input element
	 * @param n       The given degree of the root
	 * @return The nth root of the input element
	 * @see Group#invertSelfApply(Element, Element)
	 */
	public MultiplicativeElement<V> nthRoot(Element element, Element<BigInteger> n);

	/**
	 * This method is a synonym for {@link Group#invertSelfApply(Element)} and the same as
	 * {@link Group#invertSelfApply(Element, long)} for {@code n=2}. It computes the square root of the given input
	 * element.
	 * <p>
	 * @param element A given input element
	 * @return The square root of the input element
	 * @see Group#invertSelfApply(Element)
	 */
	public MultiplicativeElement<V> squareRoot(Element element);

	@Override
	public MultiplicativeElement<V> invert(Element element);

	@Override
	public MultiplicativeElement<V> applyInverse(Element element1, Element element2);

	@Override
	public MultiplicativeElement<V> invertSelfApply(Element element, long amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply(Element element, Element<BigInteger> amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply(Element element, BigInteger amount);

	@Override
	public MultiplicativeElement<V> invertSelfApply(Element element);

}
