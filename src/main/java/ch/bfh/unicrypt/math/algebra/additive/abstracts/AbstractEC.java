/* 
 * UniCrypt
 * 
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public abstract class AbstractEC<E extends AbstractECElement, F extends FiniteField, D extends DualisticElement>
			 extends AbstractAdditiveCyclicGroup<E> {

	private final F finiteField;
	private final E generator;
	private final D a, b;
	private final BigInteger order, h;
	protected final D zero = null;

	protected AbstractEC(F finiteField, D a, D b, D gx, D gy, BigInteger order, BigInteger h) {
		super();
		this.a = a;
		this.b = b;
		this.order = order;
		this.h = h;
		this.finiteField = finiteField;
		this.generator = this.getElement(gx, gy);

		if (!isValid()) { throw new IllegalArgumentException("Curve parameters are not valid"); } 
	}

	protected AbstractEC(F finitefield, D a, D b, BigInteger order, BigInteger h) {
		super();
		this.a = a;
		this.b = b;
		this.order = order;
		this.h = h;
		this.finiteField = finitefield;
		this.generator = this.computeGenerator();

		if (!isValid()) {
			throw new IllegalArgumentException("Curve parameters are not valid");
		}
	}

	@Override
	protected E abstractGetDefaultGenerator() {
		return this.generator;
	}

	protected E computeGenerator() {
		E e = (E) this.getRandomElement().selfApply(this.getH());
		while (!this.isGenerator(e)) {
			e = this.getRandomElement();
		}
		return e;
	}

	@Override
	protected boolean abstractIsGenerator(E element) {
		E e = (E) element;
		e = (E) e.selfApply(this.getOrder());
		return MathUtil.isPrime(this.getOrder()) && e.isZero();
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.order;
	}

	@Override
	protected E abstractGetElement(BigInteger value) {
		if (value.equals(zero)) {
			return this.getIdentityElement();
		} else {
			BigInteger[] result = MathUtil.unpair(value);
			D x = (D) this.getFiniteField().getElement(result[0]);
			D y = (D) this.getFiniteField().getElement(result[1]);

			return this.getElement(x, y);

		}
	}

	public E getElement(D x, D y) {
		if (x == zero || y == zero) {
			throw new IllegalArgumentException("One coordinate is zero");
		} else {
			return abstractGetElement(x, y);
		}

	}

	protected abstract E abstractGetElement(D x, D y);

	@Override
	protected E abstractGetRandomElement(RandomGenerator randomGenerator) {
		if (this.getDefaultGenerator() != null) {
			D r = (D) this.getFiniteField().getRandomElement(randomGenerator);
			return (E) this.getDefaultGenerator().selfApply(r);
		} else {
			return this.getRandomElementWithoutGenerator(randomGenerator);
		}

	}

	/**
	 * Returns random element without knowing a generator of the group
	 * <p>
	 * @param randomGenerator
	 * @return
	 */
	protected abstract E getRandomElementWithoutGenerator(RandomGenerator randomGenerator);

	@Override
	protected boolean abstractContains(BigInteger value) {
		BigInteger[] result = MathUtil.unpair(value);
		D x = (D) this.getFiniteField().getElement(result[0]);
		D y = (D) this.getFiniteField().getElement(result[1]);
		return this.contains(x, y);
	}

	/*
	 * --- Abstract methods - must be implemented in concrete classes ---
	 */
	protected abstract Boolean contains(D x, D y);

	protected abstract Boolean contains(D x);

	protected abstract boolean isValid();

	/*
	 * --- Getter methods for additional fields ---
	 */
	public F getFiniteField() {
		return this.finiteField;
	}

	public D getB() {
		return this.b;
	}

	public D getA() {
		return this.a;
	}

	public BigInteger getH() {
		return this.h;
	}

	@Override
	public String standardToStringContent() {
		return this.getA().getValue() + "," + getB().getValue();
	}

}
