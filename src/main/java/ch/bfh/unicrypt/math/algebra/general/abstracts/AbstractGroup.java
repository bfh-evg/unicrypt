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
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;

/**
 * This abstract class provides a base implementation for the interface {@link Group}.
 * <p>
 * @param <E> The generic type of elements of this group
 * @param <V> The generic type of values stored in the elements of this group
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup<E extends Element<V>, V>
	   extends AbstractMonoid<E, V>
	   implements Group<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractGroup(Class<?> valueClass) {
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
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, amount);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (!this.isFinite() || !this.hasKnownOrder()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		boolean positiveAmount = amount.signum() > 0;
		amount = amount.abs().mod(this.getOrder());
		if (!MathUtil.areRelativelyPrime(amount, this.getOrder())) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, amount);
		}
		E result = this.defaultSelfApplyAlgorithm((E) element, MathUtil.modInv(amount, this.getOrder()));
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
	protected E defaultSelfApply(E element, BigInteger amount) {
		boolean positiveAmount = amount.signum() > 0;
		amount = amount.abs();
		if (this.isFinite() && this.hasKnownOrder()) {
			amount = amount.mod(this.getOrder());
		}
		if (amount.signum() == 0) {
			return this.getIdentityElement();
		}
		E result = this.defaultSelfApplyAlgorithm(element, amount);
		if (positiveAmount) {
			return result;
		}
		return this.invert(result);
	}

	protected abstract E abstractInvert(E element);

}
