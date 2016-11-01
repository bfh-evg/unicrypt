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
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link DualisticElement}.
 * <p>
 * @param <S> The generic type of the {@link SemiRing} of this element
 * @param <E> The generic type of this element
 * @param <V> The generic type of the value stored in this element
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractDualisticElement<S extends SemiRing<V>, E extends DualisticElement<V>, V>
	   extends AbstractAdditiveElement<S, E, V>
	   implements DualisticElement<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractDualisticElement(final AbstractSet<E, V> ring, final V value) {
		super(ring, value);
	}

	@Override
	public final E multiply(final Element element) {
		return (E) this.getSet().multiply(this, element);
	}

	@Override
	public final E power(final long exponent) {
		return (E) this.getSet().power(this, exponent);
	}

	@Override
	public final E power(final BigInteger exponent) {
		return (E) this.getSet().power(this, exponent);
	}

	@Override
	public final E power(final Element<BigInteger> exponent) {
		return (E) this.getSet().power(this, exponent);
	}

	@Override
	public final E square() {
		return (E) this.getSet().square(this);
	}

	@Override
	public final E divide(final Element element) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.divide(this, element);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, element);
	}

	@Override
	public final E nthRoot(long n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);
	}

	@Override
	public final E nthRoot(BigInteger n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);
	}

	@Override
	public final E nthRoot(Element<BigInteger> n) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.nthRoot(this, n);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this, n);
	}

	@Override
	public final E squareRoot() {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.squareRoot(this);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

	@Override
	public final E oneOver() {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.oneOver(this);
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

	@Override
	public boolean isOne() {
		return this.getSet().isOneElement(this);
	}

}
