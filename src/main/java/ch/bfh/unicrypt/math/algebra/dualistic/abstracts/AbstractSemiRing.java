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
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link SemiRing}.
 * <p>
 * @param <E> The generic type of the elements of this semiring
 * @param <V> The generic type of the values stored in the elements of this semiring
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractSemiRing<E extends DualisticElement<V>, V>
	   extends AbstractAdditiveMonoid<E, V>
	   implements SemiRing<V> {

	private static final long serialVersionUID = 1L;

	// the identity element of the multiplication
	private E oneElement;

	protected AbstractSemiRing(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public E multiply(Element element1, Element element2) {
		if (!this.contains(element1) || !this.contains(element2)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element1, element2);
		}
		return this.abstractMultiply((E) element1, (E) element2);
	}

	@Override
	public E multiply(Element... elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultMultiply(Sequence.getInstance(elements).filter(Sequence.NOT_NULL));
	}

	@Override
	public E multiply(ImmutableArray<Element> elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		// no filtering of null values required
		return this.defaultMultiply(Sequence.getInstance(elements));
	}

	@Override
	public E multiply(Sequence<Element> elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultMultiply(elements.filter(Sequence.NOT_NULL));
	}

	@Override
	public E power(Element element, long exponent) {
		return this.power(element, BigInteger.valueOf(exponent));
	}

	@Override
	public E power(Element element, BigInteger exponent) {
		if (exponent == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, exponent);
		}
		return this.defaultPower((E) element, exponent);
	}

	@Override
	public E power(Element element, Element<BigInteger> exponent) {
		if (exponent == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.power(element, exponent.getValue());
	}

	@Override
	public E square(Element element) {
		return this.multiply(element, element);
	}

	@Override
	public E productOfPowers(Element[] elements, BigInteger[] exponents) {
		if (elements == null || exponents == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, elements, exponents);
		}
		if (elements.length != exponents.length) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, this, elements, exponents);
		}
		return this.defaultProductOfPowers(elements, exponents);
	}

	@Override
	public E getOneElement() {
		if (this.oneElement == null) {
			this.oneElement = this.abstractGetOne();
		}
		return this.oneElement;
	}

	@Override
	public boolean isOneElement(final Element element) {
		return element.isEquivalent(this.getOneElement());
	}

	protected E defaultMultiply(final Sequence<Element> elements) {
		final SemiRing<V> semiGroup = this;
		return (E) elements.reduce((element1, element2) -> semiGroup.multiply(element1, element2), this.getOneElement());
	}

	// this method is overridden in AbstractField
	protected E defaultPower(E element, BigInteger exponent) {
		if (exponent.signum() < 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, exponent);
		}
		if (exponent.signum() == 0) {
			return this.getOneElement();
		}
		return this.defaultPowerAlgorithm(element, exponent);
	}

	protected E defaultPowerAlgorithm(E element, BigInteger posExponent) {
		E result = element;
		for (int i = posExponent.bitLength() - 2; i >= 0; i--) {
			result = this.abstractMultiply(result, result);
			if (posExponent.testBit(i)) {
				result = this.abstractMultiply(result, element);
			}
		}
		return result;
	}

	protected E defaultProductOfPowers(final Element[] elements, final BigInteger[] exponents) {
		if (elements.length == 0) {
			return this.getOneElement();
		}
		Element[] results = new Element[elements.length];
		for (int i = 0; i < elements.length; i++) {
			results[i] = this.power(elements[i], exponents[i]);
		}
		return this.multiply(results);
	}

	@Override
	protected BigInteger defaultGetOrderLowerBound() {
		return MathUtil.TWO;
	}

	protected abstract E abstractMultiply(E element1, E element2);

	protected abstract E abstractGetOne();

}
