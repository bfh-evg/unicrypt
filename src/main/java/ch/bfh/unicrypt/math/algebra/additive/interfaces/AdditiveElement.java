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

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an additively written {@link Element}. No functionality is added. Some return types are
 * updated.
 * <p>
 * @author R. Haenni
 * @param <V> Generic type of values stored in the element
 */
public interface AdditiveElement<V>
	   extends Element<V> {

	/**
	 * @see AdditiveSemiGroup#add(Element, Element)
	 */
	public AdditiveElement<V> add(Element element);

	/**
	 * @see AdditiveGroup#subtract(Element, Element)
	 */
	public AdditiveElement<V> subtract(Element element);

	/**
	 * @see AdditiveSemiGroup#times(Element, BigInteger)
	 */
	public AdditiveElement<V> times(BigInteger amount);

	/**
	 * @see AdditiveSemiGroup#times(Element, Element)
	 */
	public AdditiveElement<V> times(Element<BigInteger> amount);

	/**
	 * @see AdditiveSemiGroup#times(Element, long)
	 */
	public AdditiveElement<V> times(long amount);

	/**
	 * @see AdditiveSemiGroup#timesTwo(Element)
	 */
	public AdditiveElement<V> timesTwo();

	/**
	 * @see AdditiveGroup#negate(Element)
	 */
	public AdditiveElement<V> negate();

	public boolean isZero();

	@Override
	public AdditiveSemiGroup<V> getSet();

	@Override
	public AdditiveElement<V> apply(Element element);

	@Override
	public AdditiveElement<V> applyInverse(Element element);

	@Override
	public AdditiveElement<V> selfApply(BigInteger amount);

	@Override
	public AdditiveElement<V> selfApply(Element<BigInteger> amount);

	@Override
	public AdditiveElement<V> selfApply(long amount);

	@Override
	public AdditiveElement<V> selfApply();

	@Override
	public AdditiveElement<V> invert();

}
