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
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link Field}.
 * <p>
 * @param <E> The generic type of the elements of this field
 * @param <V> The generic type of the values stored in the elements of this field
 * @param <M> The generic type of the multiplicative group of this field
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractField<E extends DualisticElement<V>, M extends MultiplicativeGroup<V>, V>
	   extends AbstractRing<E, V>
	   implements Field<V> {

	private static final long serialVersionUID = 1L;

	// the multiplicative group of this field
	private M multiplicativeGroup;

	protected AbstractField(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final M getMultiplicativeGroup() {
		if (this.multiplicativeGroup == null) {
			this.multiplicativeGroup = this.abstractGetMultiplicativeGroup();
		}
		return this.multiplicativeGroup;
	}

	@Override
	public final E oneOver(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (((E) element).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.DIVISION_BY_ZERO, this, element);
		}
		return this.abstractOneOver((E) element);
	}

	@Override
	public final E divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public final E nthRoot(Element element, long n) {
		return this.nthRoot(element, BigInteger.valueOf(n));
	}

	@Override
	public final E nthRoot(Element element, Element<BigInteger> n) {
		if (n == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.nthRoot(element, n.getValue());
	}

	@Override
	public final E nthRoot(Element element, BigInteger n) {
		if (n == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (n.signum() == 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, this, n);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (((E) element).isZero()) {
			return this.getZeroElement();
		}
		if (!this.isFinite() || !this.hasKnownOrder()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		boolean positive = n.signum() > 0;
		n = MathUtil.modInv(n.abs().mod(this.getOrder()), this.getOrder());
		E result = this.defaultPowerAlgorithm((E) element, n);
		if (positive) {
			return result;
		}
		return this.invert(result);
	}

	@Override
	public final E squareRoot(Element element) {
		return this.nthRoot(element, MathUtil.TWO);
	}

	@Override
	protected E defaultPower(E element, BigInteger exponent) {
		if (element.isZero()) {
			return element;
		}
		boolean negExponent = exponent.signum() < 0;
		exponent = exponent.abs();
		if (this.isFinite() && this.hasKnownOrder()) {
			exponent = exponent.mod(this.getOrder().subtract(MathUtil.ONE));
		}
		if (exponent.signum() == 0) {
			return this.getOneElement();
		}
		E result = this.defaultPowerAlgorithm(element, exponent);
		if (negExponent) {
			return this.oneOver(result);
		}
		return result;
	}

	protected abstract M abstractGetMultiplicativeGroup();

	protected abstract E abstractOneOver(E element);

}
