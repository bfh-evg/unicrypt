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
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link AdditiveElement}.
 * <p>
 * @param <S> The generic type of the {@link AdditiveSemiGroup} of this element
 * @param <E> The generic type of this element
 * @param <V> The generic type of the value stored in this element
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractAdditiveElement<S extends AdditiveSemiGroup<V>, E extends AdditiveElement<V>, V>
	   extends AbstractElement<S, E, V>
	   implements AdditiveElement<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractAdditiveElement(final AbstractSet<E, V> semiGroup, final V value) {
		super(semiGroup, value);
	}

	@Override
	public final E add(final Element element) {
		return this.apply(element);
	}

	@Override
	public final E times(final Element<BigInteger> factor) {
		return this.selfApply(factor);
	}

	@Override
	public final E times(final long factor) {
		return this.selfApply(factor);
	}

	@Override
	public final E times(final BigInteger factor) {
		return this.selfApply(factor);
	}

	@Override
	public final E timesTwo() {
		return this.selfApply();
	}

	@Override
	public boolean isZero() {
		return this.isIdentity();
	}

	@Override
	public final E negate() {
		return this.invert();
	}

	@Override
	public final E subtract(final Element element) {
		return this.applyInverse(element);
	}

	@Override
	public final E divide(long divisor) {
		return this.invertSelfApply(divisor);
	}

	@Override
	public final E divide(BigInteger divisor) {
		return this.invertSelfApply(divisor);
	}

	@Override
	public final E halve() {
		return this.invertSelfApply();
	}

}
