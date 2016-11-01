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
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;

/**
 * This interface provides the renaming of some methods for the case of a additively written commutative {@link Group}.
 * No functionality is added. Some return types are adjusted.
 * <p>
 * @param <V> The generic type of the values stored in the elements of this group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveGroup<V>
	   extends Group<V>, AdditiveMonoid<V> {

	/**
	 * This method is a synonym for {@link Group#invert(Element)}. It computes the additive inverse of the given
	 * element.
	 * <p>
	 * @param element The given element
	 * @return The additive inverse of the given element
	 * @see Group#invert(Element)
	 */
	public AdditiveElement<V> negate(Element element);

	/**
	 * This method is a synonym for {@link Group#applyInverse(Element, Element)}. It subtracts the second element from
	 * the first element.
	 * <p>
	 * @param element1 The first given element
	 * @param element2 The second given element
	 * @return The second element subtracted from the first element
	 * @see Group#applyInverse(Element, Element)
	 */
	public AdditiveElement<V> subtract(Element element1, Element element2);

	public AdditiveElement<V> divide(Element element, long divisor);

	public AdditiveElement<V> divide(Element element, BigInteger divisor);

	public AdditiveElement<V> halve(Element element);

	@Override
	public AdditiveElement<V> invert(Element element);

	@Override
	public AdditiveElement<V> applyInverse(Element element1, Element element2);

	@Override
	public AdditiveElement<V> invertSelfApply(Element element, long amount);

	@Override
	public AdditiveElement<V> invertSelfApply(Element element, BigInteger amount);

	@Override
	public AdditiveElement<V> invertSelfApply(Element element, Element<BigInteger> amount);

	@Override
	public AdditiveElement<V> invertSelfApply(Element element);

}
