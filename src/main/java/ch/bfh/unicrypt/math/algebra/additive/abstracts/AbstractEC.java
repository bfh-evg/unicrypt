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
import ch.bfh.unicrypt.helper.sequence.functions.Predicate;
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
 * This abstract class provides a basis implementation for objects of type {@link EC}. Many of the EC particularities
 * are pre-implemented.
 * <p>
 * @param <F>  The generic type of the underlying finite field
 * @param <V>  The generic type of the values stored in the elements of the underlying finite field
 * @param <DE> The generic type of the dualistic elements of the underlying finite field
 * @param <EE> The generic type of the elliptic curve elements
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public abstract class AbstractEC<F extends FiniteField<V>, V, DE extends DualisticElement<V>, EE extends ECElement<V, DE>>
	   extends AbstractAdditiveCyclicGroup<EE, Point<DE>>
	   implements EC<V, DE> {

	private static final long serialVersionUID = 1L;

	private final F finiteField;
	private final DE a, b;
	private final EE defaultGenerator;
	private final BigInteger order;
	private final BigInteger coFactor;
	private final Point<DualisticElement<V>> infinityPoint = Point.<DualisticElement<V>>getInstance();

	protected AbstractEC(F finiteField, DE a, DE b, DE gx, DE gy, BigInteger order, BigInteger coFactor) {
		super(Point.class);
		this.finiteField = finiteField;
		this.a = a;
		this.b = b;
		this.order = order;
		this.coFactor = coFactor;
		this.defaultGenerator = this.getElement(gx, gy);
	}

	protected AbstractEC(F finitefield, DE a, DE b, BigInteger order, BigInteger coFactor) {
		super(Pair.class);
		this.finiteField = finitefield;
		this.a = a;
		this.b = b;
		this.order = order;
		this.coFactor = coFactor;
		this.defaultGenerator = this.computeGenerator();
	}

	// helper method to compute a default generator
	private EE computeGenerator() {
		return this.finiteField.getElements().filter(new Predicate<DualisticElement<V>>() {

			@Override
			public boolean test(DualisticElement<V> value) {
				return contains(value);
			}

		}).map(new Mapping<DualisticElement<V>, EE>() {

			@Override
			public EE apply(DualisticElement<V> x) {
				return getElement((DE) x);
			}

		}).get();
	}

	@Override
	public final F getFiniteField() {
		return this.finiteField;
	}

	@Override
	public final DE getB() {
		return this.b;
	}

	@Override
	public final DE getA() {
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
	public EE getElement(DE x) {
		if (!this.contains(x)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, x);
		}
		return this.getElement(Point.getInstance(x, this.abstractGetY(x)));
	}

	@Override
	public final boolean contains(DE xValue) {
		if (xValue == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, xValue);
		}
		if (!this.getFiniteField().contains(xValue)) {
			return false;
		}
		return this.abstractContains(xValue);
	}

	@Override
	public final boolean contains(DE xValue, DE yValue) {
		if (xValue == null || yValue == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, xValue, yValue);
		}
		if (!this.getFiniteField().contains(xValue) || !this.getFiniteField().contains(yValue)) {
			return false;
		}
		return this.abstractContains((DE) xValue, (DE) yValue);
	}

	@Override
	public final EE getElement(DE xValue, DE yValue) {
		if (!this.contains(xValue, yValue)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, xValue, yValue);
		}
		return this.abstractGetElement(Point.getInstance((DE) xValue, (DE) yValue));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.order;
	}

	@Override
	protected final boolean abstractContains(Point<DE> value) {
		return this.abstractContains(value.getX(), value.getY());
	}

	@Override
	protected final EE abstractInvert(EE element) {
		if (element.isZero()) {
			return this.getZeroElement();
		}
		return this.abstractGetElement(Point.getInstance(element.getX(), this.abstractInvertY(element.getX(), element.getY())));
	}

	@Override
	protected Converter<Point<DE>, BigInteger> abstractGetBigIntegerConverter() {
		return new AbstractBigIntegerConverter<Point<DE>>(null) { // class parameter not needed

			@Override
			protected BigInteger abstractConvert(Point<DE> point) {
				if (point.equals(infinityPoint)) {
					return MathUtil.ZERO;
				}
				return MathUtil.pair(point.getX().convertToBigInteger(), point.getY().convertToBigInteger()).add(MathUtil.ONE);
			}

			@Override
			protected Point<DE> abstractReconvert(BigInteger value) {
				if (value.equals(MathUtil.ZERO)) {
					return getZeroElement().getValue();
				}
				BigInteger[] result = MathUtil.unpair(value.subtract(MathUtil.ONE));
				try {
					DualisticElement<V> xValue = getFiniteField().getElementFrom(result[0]);
					DualisticElement<V> yValue = getFiniteField().getElementFrom(result[1]);
					return Point.getInstance((DE) xValue, (DE) yValue);
				} catch (UniCryptException ex) {
					throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONVERSION_FAILURE, this, value, result);
				}
			}
		};
	}

	@Override
	protected EE abstractGetDefaultGenerator() {
		return this.defaultGenerator;
	}

	@Override
	protected boolean abstractIsGenerator(EE element) {
		return MathUtil.isPrime(this.getOrder()) && this.selfApply(element, this.getOrder()).isZero();
	}

	@Override
	protected Sequence<EE> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		return randomByteSequence.getRandomBigIntegerSequence(this.getFiniteField().getOrder().subtract(MathUtil.ONE)).map(new Mapping<BigInteger, EE>() {

			@Override
			public EE apply(BigInteger value) {
				return selfApply(getDefaultGenerator(), value);
			}

		});
	}

	@Override
	protected boolean abstractEquals(Set set) {
		AbstractEC<F, V, DE, EE> other = (AbstractEC<F, V, DE, EE>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.equals(other.a)) {
			return false;
		}
		if (!this.b.equals(other.b)) {
			return false;
		}
		if (!this.order.equals(other.order)) {
			return false;
		}
		if (!this.coFactor.equals(other.coFactor)) {
			return false;
		}
		return this.defaultGenerator.equals(other.defaultGenerator);
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.finiteField.hashCode();
		hash = 47 * hash + this.a.hashCode();
		hash = 47 * hash + this.b.hashCode();
		hash = 47 * hash + this.order.hashCode();
		hash = 47 * hash + this.coFactor.hashCode();
		hash = 47 * hash + this.defaultGenerator.hashCode();
		return hash;
	}

	@Override
	protected boolean defaultIsEquivalent(Set set) {
		AbstractEC<F, V, DE, EE> other = (AbstractEC<F, V, DE, EE>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.isEquivalent(other.a)) {
			return false;
		}
		if (!this.b.isEquivalent(other.b)) {
			return false;
		}
		if (!this.order.equals(other.order)) {
			return false;
		}
		return this.coFactor.equals(other.coFactor);
	}

	@Override
	protected String defaultToStringContent() {
		return this.getA().getValue() + "," + this.getB().getValue();
	}

	protected abstract DE abstractGetY(DE xValue);

	protected abstract boolean abstractContains(DE xValue);

	protected abstract boolean abstractContains(DE xValue, DE yValue);

	protected abstract DE abstractInvertY(DE x, DE y);

}
