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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
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
	default public DualisticElement<V> add(Element element) {
		return this.apply(element);
	}

	@Override
	default public DualisticElement<V> times(long factor) {
		return this.selfApply(factor);
	}

	@Override
	default public DualisticElement<V> times(BigInteger factor) {
		return this.selfApply(factor);
	}

	@Override
	default public DualisticElement<V> times(Element<BigInteger> factor) {
		return this.selfApply(factor);
	}

	@Override
	default public DualisticElement<V> timesTwo() {
		return this.selfApply();
	}

	@Override
	default public DualisticElement<V> negate() {
		return this.invert();
	}

	@Override
	default public DualisticElement<V> subtract(Element element) {
		return this.applyInverse(element);
	}

	@Override
	default public DualisticElement<V> divide(long divisor) {
		return this.invertSelfApply(divisor);
	}

	@Override
	default public DualisticElement<V> divide(BigInteger divisor) {
		return this.invertSelfApply(divisor);
	}

	@Override
	default public DualisticElement<V> halve() {
		return this.invertSelfApply();
	}

	@Override
	default public DualisticElement<V> multiply(Element element) {
		return this.getSet().multiply(this, element);
	}

	@Override
	default public DualisticElement<V> power(long exponent) {
		return this.getSet().power(this, exponent);
	}

	@Override
	default public DualisticElement<V> power(BigInteger exponent) {
		return this.getSet().power(this, exponent);
	}

	@Override
	default public DualisticElement<V> power(Element<BigInteger> exponent) {
		return this.getSet().power(this, exponent);
	}

	@Override
	default public DualisticElement<V> square() {
		return this.getSet().square(this);
	}

	@Override
	default public boolean isOne() {
		return this.getSet().isOneElement(this);
	}

	@Override
	default public DualisticElement<V> oneOver() {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.oneOver(this);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

	@Override
	default public DualisticElement<V> divide(Element element) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.divide(this, element);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, element);

	}

	@Override
	default public DualisticElement<V> nthRoot(long n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);

	}

	@Override
	default public DualisticElement<V> nthRoot(BigInteger n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);

	}

	@Override
	default public DualisticElement<V> nthRoot(Element<BigInteger> n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);
	}

	@Override
	default public DualisticElement<V> squareRoot() {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return field.squareRoot(this);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

}
