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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.functions.Mapping;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link EC}.
 * <p>
 * @param <F>  Generic type of the {@link FiniteField} of this elliptic curve
 * @param <V>  Generic type of values stored in the elements of this elliptic curve
 * @param <D>
 * @param <EE> x
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public abstract class AbstractEC<F extends FiniteField<V>, V, D extends DualisticElement<V>, EE extends ECElement<V, D>>
	   extends AbstractAdditiveCyclicGroup<EE, Point<D>>
	   implements EC<V, D> {

	private static final long serialVersionUID = 1L;

	private final F finiteField;
	private final D a, b;
	private final EE givenGenerator;
	private final BigInteger givenOrder, coFactor;
	private final Point<DualisticElement<V>> infinityPoint = Point.<DualisticElement<V>>getInstance();

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

	// helper method to compute a default generator
	private EE computeGenerator() {
		EE element = this.selfApply(this.getRandomElement(), this.getCoFactor());
		while (!this.isGenerator(element)) {
			element = this.getRandomElement();
		}
		return element;
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
	public ZModPrime getZModOrder() {
		return ZModPrime.getInstance(this.getOrder());
	}

	@Override
	public ZStarModPrime getZStarModOrder() {
		return ZStarModPrime.getInstance(this.getOrder());
	}

	@Override
	public EE[] getY(D xValue) {
		if (!this.contains(xValue)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, xValue);
		}
		return this.abstractGetY(xValue);
	}

	@Override
	public final boolean contains(D xValue) {
		if (xValue == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, xValue);
		}
		if (!this.getFiniteField().contains(xValue)) {
			return false;
		}
		return this.abstractContains(xValue);
	}

	@Override
	public final boolean contains(D xValue, D yValue) {
		if (xValue == null || yValue == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, xValue, yValue);
		}
		if (!this.getFiniteField().contains(xValue) || !this.getFiniteField().contains(yValue)) {
			return false;
		}
		return this.abstractContains((D) xValue, (D) yValue);
	}

	@Override
	public final EE getElement(D xValue, D yValue) {
		if (!this.contains(xValue, yValue)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, xValue, yValue);
		}
		return this.abstractGetElement(Point.getInstance((D) xValue, (D) yValue));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.givenOrder;
	}

	@Override
	protected boolean abstractContains(Point<D> value) {
		return this.abstractContains(value.getX(), value.getY());
	}

	@Override
	protected Converter<Point<D>, BigInteger> abstractGetBigIntegerConverter() {
		return new AbstractBigIntegerConverter<Point<D>>(null) { // class parameter not needed

			@Override
			protected BigInteger abstractConvert(Point<D> point) {
				if (point.equals(infinityPoint)) {
					return MathUtil.ZERO;
				}
				return MathUtil.pair(point.getX().convertToBigInteger(), point.getY().convertToBigInteger()).add(MathUtil.ONE);
			}

			@Override
			protected Point<D> abstractReconvert(BigInteger value) {
				if (value.equals(MathUtil.ZERO)) {
					return getZeroElement().getValue();
				}
				BigInteger[] result = MathUtil.unpair(value.subtract(MathUtil.ONE));
				try {
					DualisticElement<V> xValue = getFiniteField().getElementFrom(result[0]);
					DualisticElement<V> yValue = getFiniteField().getElementFrom(result[1]);
					return Point.getInstance((D) xValue, (D) yValue);
				} catch (UniCryptException ex) {
					throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONVERSION_FAILURE, this, value, result);
				}
			}
		};
	}

	@Override
	protected EE abstractGetDefaultGenerator() {
		return this.givenGenerator;
	}

	@Override
	protected boolean abstractIsGenerator(EE element) {
		return MathUtil.isPrime(this.getOrder()) && this.selfApply(element, this.getOrder()).isZero();
	}

	@Override
	protected Sequence<EE> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		if (this.getDefaultGenerator() != null) {
			return randomByteSequence.getRandomBigIntegerSequence(this.getFiniteField().getOrder().subtract(MathUtil.ONE)).map(new Mapping<BigInteger, EE>() {

				@Override
				public EE apply(BigInteger value) {
					return selfApply(getDefaultGenerator(), value);
				}

			});
		} else {
			return this.abstractGetRandomElementsWithoutGenerator(randomByteSequence);
		}
	}

	@Override
	protected boolean abstractEquals(Set set) {
		AbstractEC<F, V, D, EE> other = (AbstractEC<F, V, D, EE>) set;
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
		return this.givenGenerator.equals(other.givenGenerator);
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
	protected boolean defaultIsEquivalent(Set set) {
		AbstractEC<F, V, D, EE> other = (AbstractEC<F, V, D, EE>) set;
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
		return this.coFactor.equals(other.coFactor);
	}

	@Override
	protected String defaultToStringContent() {
		return this.getA().getValue() + "," + this.getB().getValue();
	}

	protected abstract EE[] abstractGetY(D xValue);

	protected abstract boolean abstractContains(D xValue);

	protected abstract boolean abstractContains(D xValue, D yValue);

	// Returns a random element without knowing a generator of the group.
	protected abstract Sequence<EE> abstractGetRandomElementsWithoutGenerator(RandomByteSequence randomByteSequence);

}
