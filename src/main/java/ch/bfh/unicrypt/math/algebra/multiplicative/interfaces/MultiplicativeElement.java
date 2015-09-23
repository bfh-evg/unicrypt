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
package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an {@link Element} of a multiplicatively written semigroup. No functionality is added. Some
 * return types are updated.
 * <p>
 * @author R. Haenni
 * @param <V> Generic type of values stored in this element
 */
public interface MultiplicativeElement<V>
	   extends Element<V> {

	/**
	 * @param element
	 * @return
	 * @see Element#apply(Element)
	 */
	public MultiplicativeElement<V> multiply(Element element);

	/**
	 * @param exponent
	 * @return
	 * @see Element#selfApply(long)
	 */
	public MultiplicativeElement<V> power(long exponent);

	/**
	 * @param exponent
	 * @return
	 * @see Element#selfApply(BigInteger)
	 */
	public MultiplicativeElement<V> power(BigInteger exponent);

	/**
	 * @param exponent
	 * @return
	 * @see Element#selfApply(Element)
	 */
	public MultiplicativeElement<V> power(Element<BigInteger> exponent);

	/**
	 * @return x
	 * @see Element#selfApply()
	 */
	public MultiplicativeElement<V> square();

	/**
	 *
	 * @return x
	 * @see Element#isIdentity()
	 */
	public boolean isOne();

	/**
	 * @return x
	 * @see Element#invert()
	 */
	public MultiplicativeElement<V> oneOver();

	/**
	 * @param element
	 * @return
	 * @see Element#applyInverse(Element)
	 */
	public MultiplicativeElement<V> divide(Element element);

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
	public MultiplicativeElement<V> invert();

	@Override
	public MultiplicativeElement<V> applyInverse(Element element);

}
