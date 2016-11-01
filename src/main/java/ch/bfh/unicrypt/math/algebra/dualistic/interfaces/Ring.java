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
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents the mathematical concept of a ring. A ring is a semiring where the set together with
 * addition forms a commutative group. Therefore, every element of the ring has an additive inverse.
 * <p>
 * The ring interface is therefore implemented as a specialization of {@link SemiRing} and {@link AdditiveGroup}. No
 * functionality is added. Some return types are updated.
 * <p>
 * @param <V> The generic type of the values representing the elements of a ring
 * <p>
 * @author R. Haenni
 * <p>
 * @see SemiRing
 * @see AdditiveGroup
 * @see DualisticElement
 * @see "Handbook of Applied Cryptography, Definition 2.175"
 */
public interface Ring<V>
	   extends SemiRing<V>, AdditiveGroup<V> {

	@Override
	public DualisticElement<V> negate(Element element);

	@Override
	public DualisticElement<V> subtract(Element element1, Element element2);

	@Override
	public DualisticElement<V> divide(Element element, long divisor);

	@Override
	public DualisticElement<V> divide(Element element, BigInteger divisor);

	@Override
	public DualisticElement<V> halve(Element element);

}
