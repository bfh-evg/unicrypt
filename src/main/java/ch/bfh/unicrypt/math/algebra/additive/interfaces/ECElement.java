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
package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

public interface ECElement<V, E extends DualisticElement<V>>
	   extends AdditiveElement<Point<E>> {

	/**
	 * @see Group#apply(Element, Element)
	 */
	public ECElement<V,E> add(Element element);

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	public ECElement<V,E> subtract(Element element);

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	public ECElement<V,E> times(BigInteger amount);

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	public ECElement<V,E> times(Element<BigInteger> amount);

	/**
	 * @see Group#selfApply(Element, int)
	 */
	public ECElement<V,E> times(int amount);

	/**
	 * @see Group#selfApply(Element)
	 */
	public ECElement<V,E> timesTwo();

	/**
	 * @see Group#negate(Element)
	 */
	public ECElement<V,E> negate();

	public boolean isZero();

	// The following methods are overridden from Element with an adapted return type


	@Override
	public ECElement<V,E> apply(Element element);

	@Override
	public ECElement<V,E> applyInverse(Element element);

	@Override
	public ECElement<V,E> selfApply(BigInteger amount);

	@Override
	public ECElement<V,E> selfApply(Element<BigInteger> amount);

	@Override
	public ECElement<V,E> selfApply(int amount);

	@Override
	public ECElement<V,E> selfApply();

	@Override
	public ECElement<V,E> invert();

	public E getY();
	public E getX();

}
