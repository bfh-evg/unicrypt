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

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Point;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public abstract class AbstractEC<E extends ECElement, F extends FiniteField, D extends DualisticElement>
	   extends AbstractAdditiveCyclicGroup<E, Point<D>>
	   implements EC {

	private final F finiteField;
	private final D a, b;
	private final E givenGenerator;
	private final BigInteger givenOrder, coFactor;

	protected AbstractEC(F finiteField, D a, D b, D gx, D gy, BigInteger givenOrder, BigInteger coFactor) {
		super(Point.class);
		this.finiteField = finiteField;
		this.a = a;
		this.b = b;
		this.givenOrder = givenOrder;
		this.coFactor = coFactor;
		this.givenGenerator = this.getElement(gx, gy);
	}

	protected AbstractEC(F finitefield, D a, D b, BigInteger givenOrder, BigInteger coFactor) {
		super(Pair.class);
		this.finiteField = finitefield;
		this.a = a;
		this.b = b;
		this.givenOrder = givenOrder;
		this.coFactor = coFactor;
		this.givenGenerator = this.computeGenerator();
	}

	@Override
	public final F getFiniteField() {
		return this.finiteField;
	}

	@Override
	public final D getB() {
		return this.b;
	}

	@Override
	public final D getA() {
		return this.a;
	}

	@Override
	public final BigInteger getCoFactor() {
		return this.coFactor;
	}

	@Override
	public final boolean contains(DualisticElement xValue) {
		if (xValue == null || !this.getFiniteField().contains(xValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains((D) xValue);
	}

	protected abstract boolean abstractContains(D xValue);

	@Override
	public final boolean contains(DualisticElement xValue, DualisticElement yValue) {
		if (xValue == null || yValue == null || !this.getFiniteField().contains(xValue) || !this.getFiniteField().contains(yValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains((D) xValue, (D) yValue);
	}

	protected abstract boolean abstractContains(D xValue, D yValue);

	@Override
	public final E getElement(DualisticElement xValue, DualisticElement yValue) {
		if (!this.contains(xValue, yValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetElement(Point.getInstance((D) xValue, (D) yValue));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.givenOrder;
	}

	@Override
	protected boolean abstractContains(BigInteger bigInteger) {
		if (bigInteger.signum() < 0) {
			return false;
		}
		if (bigInteger.equals(BigInteger.ZERO)) {
			return true;
		}
		BigInteger[] result = MathUtil.unpair(bigInteger.subtract(BigInteger.ONE));
		if (!this.getFiniteField().contains(result[0]) || !this.getFiniteField().contains(result[1])) {
			return false;
		}
		D xValue = (D) this.getFiniteField().getElementFrom(result[0]);
		D yValue = (D) this.getFiniteField().getElementFrom(result[1]);
		return this.abstractContains(xValue, yValue);
	}

	@Override
	protected boolean abstractContains(Point<D> value) {
		return this.abstractContains(value.getX(), value.getY());
	}

	@Override
	protected E abstractGetElement(BigInteger bigInteger) {
		if (bigInteger.equals(BigInteger.ZERO)) {
			return this.getZeroElement();
		}
		BigInteger[] result = MathUtil.unpair(bigInteger.subtract(BigInteger.ONE));
		D xValue = (D) this.getFiniteField().getElementFrom(result[0]);
		D yValue = (D) this.getFiniteField().getElementFrom(result[1]);
		return this.abstractGetElement(Point.getInstance(xValue, yValue));
	}

	@Override
	protected E abstractGetDefaultGenerator() {
		return this.givenGenerator;
	}

	private E computeGenerator() {
		E element = this.selfApply(this.getRandomElement(), this.getCoFactor());
		while (!this.isGenerator(element)) {
			element = this.getRandomElement();
		}
		return element;
	}

	@Override
	protected boolean abstractIsGenerator(E element) {
		return MathUtil.isPrime(this.getOrder()) && this.selfApply(element, this.getOrder()).isZero();
	}

	@Override
	protected E abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		if (this.getDefaultGenerator() != null) {
			D r = (D) this.getFiniteField().getRandomElement(randomByteSequence);
			return (E) this.getDefaultGenerator().selfApply(r);
		} else {
			return this.getRandomElementWithoutGenerator(randomByteSequence);
		}
	}

	@Override
	protected boolean abstractEquals(Set set) {
		AbstractEC<E, F, D> other = (AbstractEC<E, F, D>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.equals(other.a)) {
			return false;
		}
		if (!this.b.equals(other.b)) {
			return false;
		}
		if (!this.givenOrder.equals(other.givenOrder)) {
			return false;
		}
		if (!this.coFactor.equals(other.coFactor)) {
			return false;
		}
		if (!this.givenGenerator.equals(other.givenGenerator)) {
			return false;
		}
		return true;
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.finiteField.hashCode();
		hash = 47 * hash + this.a.hashCode();
		hash = 47 * hash + this.b.hashCode();
		hash = 47 * hash + this.givenOrder.hashCode();
		hash = 47 * hash + this.coFactor.hashCode();
		hash = 47 * hash + this.givenGenerator.hashCode();
		return hash;
	}

	@Override
	protected boolean standardIsEquivalent(Set set) {
		AbstractEC<E, F, D> other = (AbstractEC<E, F, D>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.isEquivalent(other.a)) {
			return false;
		}
		if (!this.b.isEquivalent(other.b)) {
			return false;
		}
		if (!this.givenOrder.equals(other.givenOrder)) {
			return false;
		}
		if (!this.coFactor.equals(other.coFactor)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns random element witcoFactorout knowing a generator of tcoFactore group
	 * <p>
	 * @param randomByteSequence
	 * @return
	 */
	protected abstract E getRandomElementWithoutGenerator(RandomByteSequence randomByteSequence);

	@Override
	public String standardToStringContent() {
		return this.getA().getValue() + "," + this.getB().getValue();
	}

}
