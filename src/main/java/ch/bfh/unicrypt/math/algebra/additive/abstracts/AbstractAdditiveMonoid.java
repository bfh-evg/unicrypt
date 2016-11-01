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

import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link AdditiveMonoid}.
 * <p>
 * @param <E> The generic type of the elements of this monoid
 * @param <V> The generic type of the values stored in the elements of this monoid
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractAdditiveMonoid<E extends AdditiveElement<V>, V>
	   extends AbstractMonoid<E, V>
	   implements AdditiveMonoid<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractAdditiveMonoid(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final E add(final Element element1, final Element element2) {
		return this.apply(element1, element2);
	}

	@Override
	public final E add(final Element... elements) {
		return this.apply(elements);
	}

	@Override
	public final E add(final ImmutableArray<Element> elements) {
		return this.apply(elements);
	}

	@Override
	public final E add(final Sequence<Element> elements) {
		return this.apply(elements);
	}

	@Override
	public final E times(final Element element, final BigInteger factor) {
		return this.selfApply(element, factor);
	}

	@Override
	public final E times(final Element element, final Element<BigInteger> factor) {
		return this.selfApply(element, factor);
	}

	@Override
	public final E times(final Element element, final long factor) {
		return this.selfApply(element, factor);
	}

	@Override
	public final E timesTwo(Element element) {
		return this.selfApply(element);
	}

	@Override
	public final E sumOfProducts(Element[] elements, BigInteger[] factors) {
		return this.multiSelfApply(elements, factors);
	}

	@Override
	public final E getZeroElement() {
		return this.getIdentityElement();
	}

	@Override
	public final boolean isZeroElement(Element element) {
		return this.isIdentityElement(element);
	}

}
