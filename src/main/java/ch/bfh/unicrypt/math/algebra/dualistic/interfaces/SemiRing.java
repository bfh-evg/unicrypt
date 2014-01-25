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
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.crypto.random.classes.RandomNumberGenerator;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface SemiRing
			 extends AdditiveMonoid, MultiplicativeMonoid {

	// The following methods are overridden from Set with an adapted return type
	@Override
	public DualisticElement getElement(int value);

	@Override
	public DualisticElement getElement(BigInteger value);

	@Override
	public DualisticElement getElement(Element element);

	@Override
	public DualisticElement getRandomElement();

	@Override
	public DualisticElement getRandomElement(RandomNumberGenerator randomGenerator);

	// The following methods are overridden from SemiGroup with an adapted return type
	@Override
	public DualisticElement apply(Element element1, Element element2);

	@Override
	public DualisticElement apply(Element... elements);

	@Override
	public DualisticElement selfApply(Element element, BigInteger amount);

	@Override
	public DualisticElement selfApply(Element element, Element amount);

	@Override
	public DualisticElement selfApply(Element element, int amount);

	@Override
	public DualisticElement selfApply(Element element);

	@Override
	public DualisticElement multiSelfApply(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from Monoid with an adapted return type
	@Override
	public DualisticElement getIdentityElement();

	// The following methods are overridden from AdditiveSemiGroup with an adapted return type
	@Override
	public DualisticElement add(Element element1, Element element2);

	@Override
	public DualisticElement add(Element... elements);

	@Override
	public DualisticElement times(Element element, BigInteger amount);

	@Override
	public DualisticElement times(Element element, Element amount);

	@Override
	public DualisticElement times(Element element, int amount);

	@Override
	public DualisticElement timesTwo(Element element);

	@Override
	public DualisticElement sumOfProducts(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from AdditiveMonoid with an adapted return type
	@Override
	public DualisticElement getZeroElement();

	// The following methods are overridden from MultiplicativeSemiGroup with an adapted return type
	@Override
	public DualisticElement multiply(Element element1, Element element2);

	@Override
	public DualisticElement multiply(Element... elements);

	@Override
	public DualisticElement power(Element element, BigInteger amount);

	@Override
	public DualisticElement power(Element element, Element amount);

	@Override
	public DualisticElement power(Element element, int amount);

	@Override
	public DualisticElement square(Element element);

	@Override
	public DualisticElement productOfPowers(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from MultiplicativeMonoid with an adapted return type
	@Override
	public DualisticElement getOneElement();

}
