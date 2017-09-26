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
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link Ring}.
 * <p>
 * @param <E> The generic type of the elements of this ring
 * @param <V> The generic type of the values stored in the elements of this ring
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractRing<E extends DualisticElement<V>, V>
	   extends AbstractSemiRing<E, V>
	   implements Ring<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractRing(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final E invert(final Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		return this.abstractInvert((E) element);
	}

	@Override
	public final E applyInverse(Element element1, Element element2) {
		return this.apply(element1, this.invert(element2));
	}

	@Override
	public final E invertSelfApply(Element element, long amount) {
		return this.invertSelfApply(element, BigInteger.valueOf(amount));
	}

	@Override
	public final E invertSelfApply(Element element, Element<BigInteger> amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.invertSelfApply(element, amount.getValue());
	}

	@Override
	public final E invertSelfApply(Element element, BigInteger amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (amount.signum() == 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, this, amount);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (!this.isFinite() || !this.hasKnownOrder()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		boolean positiveAmount = amount.signum() > 0;
		amount = MathUtil.modInv(amount.abs().mod(this.getOrder()), this.getOrder());
		E result = this.defaultSelfApplyAlgorithm((E) element, amount);
		if (positiveAmount) {
			return result;
		}
		return this.invert(result);
	}

	@Override
	public final E invertSelfApply(Element element) {
		return this.invertSelfApply(element, MathUtil.TWO);
	}

	@Override
	public final E negate(final Element element) {
		return this.invert(element);
	}

	@Override
	public final E subtract(final Element element1, final Element element2) {
		return this.applyInverse(element1, element2);
	}

	@Override
	public final E divide(Element element, long divisor) {
		return this.invertSelfApply(element, divisor);
	}

	@Override
	public final E divide(Element element, BigInteger divisor) {
		return this.invertSelfApply(element, divisor);
	}

	@Override
	public final E halve(Element element) {
		return this.invertSelfApply(element);
	}

	@Override
	protected E defaultSelfApply(E element, BigInteger factor) {
		boolean negFactor = factor.signum() < 0;
		factor = factor.abs();
		if (this.isFinite() && this.hasKnownOrder()) {
			factor = factor.mod(this.getOrder());
		}
		if (factor.signum() == 0) {
			return this.getIdentityElement();
		}
		E result = this.defaultSelfApplyAlgorithm(element, factor);
		if (negFactor) {
			return this.invert(result);
		}
		return result;
	}

	protected abstract E abstractInvert(E element);

}
