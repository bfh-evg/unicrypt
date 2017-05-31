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
package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a base implementation for the interface {@link Monoid}.
 * <p>
 * @param <E> The generic type of elements of this monoid
 * @param <V> The generic type of values stored in the elements of this monoid
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractMonoid<E extends Element<V>, V>
	   extends AbstractSemiGroup<E, V>
	   implements Monoid<V> {

	private static final long serialVersionUID = 1L;

	private E identityElement;

	protected AbstractMonoid(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final E getIdentityElement() {
		if (this.identityElement == null) {
			this.identityElement = this.abstractGetIdentityElement();
		}
		return this.identityElement;
	}

	@Override
	public final boolean isIdentityElement(final Element element) {
		return element.isEquivalent(this.getIdentityElement());
	}

	@Override
	protected BigInteger defaultGetOrderLowerBound() {
		return MathUtil.ONE;
	}

	@Override
	protected E defaultApply(final Sequence<Element> elements) {
		final SemiGroup<V> monoid = this;
		return (E) elements.reduce((element1, element2) -> monoid.apply(element1, element2), this.getIdentityElement());
	}

	@Override
	protected E defaultSelfApply(E element, BigInteger amount) {
		if (amount.signum() < 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, amount);
		}
		if (amount.signum() == 0) {
			return this.getIdentityElement();
		}
		return this.defaultSelfApplyAlgorithm(element, amount);
	}

	@Override
	protected E defaultMultiSelfApply(final Element[] elements, BigInteger[] amounts) {
		if (elements.length == 0) {
			return this.getIdentityElement();
		}
		return super.defaultMultiSelfApply(elements, amounts);
	}

	protected abstract E abstractGetIdentityElement();

}
