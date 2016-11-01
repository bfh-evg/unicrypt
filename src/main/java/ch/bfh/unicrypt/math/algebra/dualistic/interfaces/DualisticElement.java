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

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 * This interface represents an element of a semigroup, ring, or field. It is called "dualistic" due to the two
 * conceptually contrasted aspects of the two operations of addition and multiplication.
 * <p>
 * The interface is implemented as a specialization of both {@link AdditiveElement} and {@link MultiplicativeElement}.
 * No functionality is added. Some return types are updated.
 * <p>
 * @param <V> The generic type of values stored in this element
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public interface DualisticElement<V>
	   extends AdditiveElement<V>, MultiplicativeElement<V> {

	@Override
	public SemiRing<V> getSet();

	@Override
	public DualisticElement<V> apply(Element element);

	@Override
	public DualisticElement<V> applyInverse(Element element);

	@Override
	public DualisticElement<V> selfApply(long factor);

	@Override
	public DualisticElement<V> selfApply(BigInteger factor);

	@Override
	public DualisticElement<V> selfApply(Element<BigInteger> factor);

	@Override
	public DualisticElement<V> selfApply();

	@Override
	public DualisticElement<V> invertSelfApply(long amount);

	@Override
	public DualisticElement<V> invertSelfApply(BigInteger amount);

	@Override
	public DualisticElement<V> invertSelfApply(Element<BigInteger> amount);

	@Override
	public DualisticElement<V> invertSelfApply();

	@Override
	public DualisticElement<V> invert();

	@Override
	public DualisticElement<V> add(Element element);

	@Override
	public DualisticElement<V> times(long factor);

	@Override
	public DualisticElement<V> times(BigInteger factor);

	@Override
	public DualisticElement<V> times(Element<BigInteger> factor);

	@Override
	public DualisticElement<V> timesTwo();

	@Override
	public DualisticElement<V> divide(long divisor);

	@Override
	public DualisticElement<V> divide(BigInteger divisor);

	@Override
	public DualisticElement<V> halve();

	@Override
	public DualisticElement<V> negate();

	@Override
	public DualisticElement<V> subtract(Element element);

	@Override
	public DualisticElement<V> multiply(Element element);

	@Override
	public DualisticElement<V> power(long exponent);

	@Override
	public DualisticElement<V> power(BigInteger exponent);

	@Override
	public DualisticElement<V> power(Element<BigInteger> exponent);

	@Override
	public DualisticElement<V> square();

	@Override
	public DualisticElement<V> nthRoot(long n);

	@Override
	public DualisticElement<V> nthRoot(BigInteger n);

	@Override
	public DualisticElement<V> nthRoot(Element<BigInteger> n);

	@Override
	public DualisticElement<V> squareRoot();

	@Override
	public DualisticElement<V> oneOver();

	@Override
	public DualisticElement<V> divide(Element element);

}
