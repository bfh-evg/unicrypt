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
	 *
	 * @return
	 */
	public int getLength();

	/**
	 * @see Group#apply(Element, Element)
	 */
	public ConcatenativeElement<V> concatenate(Element element);

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	public ConcatenativeElement<V> selfConcatenate(BigInteger amount);

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	public ConcatenativeElement<V> selfConcatenate(Element<BigInteger> amount);

	/**
	 * @see Group#selfApply(Element, long)
	 */
	public ConcatenativeElement<V> selfConcatenate(long amount);

	/**
	 * @see Group#selfApply(Element)
	 */
	public ConcatenativeElement<V> selfConcatenate();

	/**
	 *
	 * @return
	 */
	public boolean isEmptyElement();

	@Override
	public ConcatenativeSemiGroup getSet();

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
