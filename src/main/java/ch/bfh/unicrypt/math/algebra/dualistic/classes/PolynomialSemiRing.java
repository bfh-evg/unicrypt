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
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.BigIntegerConverter;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class PolynomialSemiRing
	   extends AbstractSemiRing<PolynomialElement, Polynomial<? extends DualisticElement<BigInteger>>> {

	private final SemiRing semiRing;

	protected PolynomialSemiRing(SemiRing semiRing) {
		super(Polynomial.class);
		this.semiRing = semiRing;
	}

	public SemiRing getSemiRing() {
		return this.semiRing;
	}

	public boolean isBinary() {
		return this.getSemiRing().getOrder().intValue() == 2;
	}

	public PolynomialElement getElement(Map<Integer, DualisticElement<BigInteger>> coefficientMap) {
		return this.getElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElement(ByteArray coefficients) {//TODO: Change back to protected -> ZModToBinaryEncoder is then not working anymore.
		return this.getElement(Polynomial.<DualisticElement<BigInteger>>getInstance(coefficients, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElement(BigInteger... values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(ProductSet.getInstance(this.getSemiRing(), values.length).getElementFrom(values));
	}

	public PolynomialElement getElement(DualisticElement... elements) {
		if (elements == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(Tuple.getInstance(elements));
	}

	public PolynomialElement getElement(Tuple coefficients) {
		if (coefficients == null || !coefficients.isEmpty() && (!coefficients.getSet().isUniform() || !coefficients.getSet().getFirst().isEquivalent(this.getSemiRing()))) {
			throw new IllegalArgumentException();
		}

		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
		for (int i = 0; i < coefficients.getArity(); i++) {
			coefficientMap.put(i, (DualisticElement<BigInteger>) coefficients.getAt(i));
		}
		return this.getElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElementByRoots(Tuple roots) {
		if (roots == null || roots.isEmpty() || !roots.getSet().isUniform() || !roots.getSet().getFirst().isEquivalent(this.getSemiRing())) {
			throw new IllegalArgumentException();
		}
		DualisticElement zero = this.getSemiRing().getZeroElement();
		DualisticElement one = this.getSemiRing().getOneElement();

		PolynomialElement poly = new PolynomialElement(this, Polynomial.<DualisticElement<BigInteger>>getInstance(new DualisticElement[]{(DualisticElement) roots.getAt(0).invert(), one}, zero, one));
		for (int i = 1; i < roots.getArity(); i++) {
			poly = poly.multiply(new PolynomialElement(this, Polynomial.<DualisticElement<BigInteger>>getInstance(new DualisticElement[]{(DualisticElement) roots.getAt(i).invert(), one}, zero, one)));
		}

		return poly;
	}

	public PolynomialElement getRandomElement(int degree) {
		return this.getRandomElement(degree, HybridRandomByteSequence.getInstance());
	}

	public PolynomialElement getRandomElement(int degree, RandomByteSequence randomByteSequence) {
		if (degree < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap<Integer, DualisticElement<BigInteger>>();
		for (int i = 0; i <= degree; i++) {
			DualisticElement<BigInteger> coefficient = this.getSemiRing().getRandomElement(randomByteSequence);
			if (!coefficient.isZero()) {
				coefficientMap.put(i, coefficient);
			}
		}
		return this.getElementUnchecked(coefficientMap);
	}

	public PolynomialElement getRandomMonicElement(int degree, boolean a0NotZero) {
		return this.getRandomMonicElement(degree, a0NotZero, HybridRandomByteSequence.getInstance());
	}

	public PolynomialElement getRandomMonicElement(int degree, boolean a0NotZero, RandomByteSequence randomByteSequence) {
		if (degree < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap<Integer, DualisticElement<BigInteger>>();
		for (int i = 0; i <= degree - 1; i++) {
			DualisticElement<BigInteger> coefficient = this.getSemiRing().getRandomElement(randomByteSequence);
			while (i == 0 && a0NotZero && coefficient.isZero()) {
				coefficient = this.getSemiRing().getRandomElement(randomByteSequence);
			}
			if (!coefficient.isZero()) {
				coefficientMap.put(i, coefficient);
			}
		}
		coefficientMap.put(degree, this.getSemiRing().getOneElement());

		return getElementUnchecked(coefficientMap);
	}

	protected PolynomialElement getElementUnchecked(Map<Integer, DualisticElement<BigInteger>> coefficientMap) {
		return abstractGetElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	protected PolynomialElement getElementUnchecked(ByteArray coefficients) {//TODO: Change back to protected -> ZModToBinaryEncoder is then not working anymore.
		return abstractGetElement(Polynomial.getInstance(coefficients, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	protected boolean abstractEquals(final Set set) {
		final PolynomialSemiRing other = (PolynomialSemiRing) set;
		return this.getSemiRing().isEquivalent(other.getSemiRing());
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getSemiRing().hashCode();
		return hash;
	}

	@Override
	protected String defaultToStringValue() {
		return this.getSemiRing().toString();
	}

	@Override
	protected boolean abstractContains(Polynomial value) {
		DenseArray<Integer> indices = value.getIndices();
		for (int i : indices) {
			if (!(Element.class.isInstance(value.getCoefficient(i)) && this.getSemiRing().contains((Element) value.getCoefficient(i)))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected PolynomialElement abstractGetElement(Polynomial value) {
		return new PolynomialElement(this, value);
	}

	@Override
	protected BigIntegerConverter<Polynomial<? extends DualisticElement<BigInteger>>> abstractGetBigIntegerConverter() {
		return new AbstractBigIntegerConverter<Polynomial<? extends DualisticElement<BigInteger>>>(null) { // class not needed

			@Override
			protected BigInteger abstractConvert(Polynomial<? extends DualisticElement<BigInteger>> polynomial) {
				int degree = polynomial.getDegree();
				BigInteger[] values = new BigInteger[degree + 1];
				for (int i = 0; i <= degree; i++) {
					values[i] = polynomial.getCoefficient(i).getBigInteger();
				}
				return MathUtil.pairWithSize(values);
			}

			@Override
			protected Polynomial<? extends DualisticElement<BigInteger>> abstractReconvert(BigInteger value) {
				BigInteger[] bigIntegers = MathUtil.unpairWithSize(value);
				DualisticElement[] elements = new DualisticElement[bigIntegers.length];
				int i = 0;
				for (BigInteger bigInteger : bigIntegers) {
					DualisticElement<BigInteger> element = getSemiRing().getElementFrom(bigInteger);
					elements[i] = element;
					i++;
				}
				Polynomial<? extends DualisticElement<BigInteger>> polynomial = Polynomial.<DualisticElement<BigInteger>>getInstance(elements, getSemiRing().getZeroElement(), getSemiRing().getOneElement());
				return polynomial;
			}
		};
	}

	@Override
	protected PolynomialElement abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		throw new UnsupportedOperationException("Not possible for infinite order.");
	}

	@Override
	protected PolynomialElement abstractApply(PolynomialElement element1, PolynomialElement element2) {
		Polynomial<? extends DualisticElement<BigInteger>> polynomial1 = element1.getValue();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial2 = element2.getValue();

		if (this.isBinary()) {
			ByteArray coefficients = polynomial1.getCoefficients().xorFillZero(polynomial2.getCoefficients());
			return this.getElementUnchecked(coefficients);
		} else {
			Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
			for (Integer i : polynomial1.getIndices()) {
				coefficientMap.put(i, polynomial1.getCoefficient(i));
			}
			for (Integer i : polynomial2.getIndices()) {
				DualisticElement<BigInteger> coefficient = coefficientMap.get(i);
				if (coefficient == null) {
					coefficientMap.put(i, polynomial2.getCoefficient(i));
				} else {
					coefficientMap.put(i, coefficient.add(polynomial2.getCoefficient(i)));
				}
			}
			return this.getElementUnchecked(coefficientMap);
		}
	}

	@Override
	protected PolynomialElement abstractGetIdentityElement() {
		return this.getElementUnchecked(new HashMap<Integer, DualisticElement<BigInteger>>());
	}

	@Override
	protected PolynomialElement abstractMultiply(PolynomialElement element1, PolynomialElement element2) {
		Polynomial<? extends DualisticElement<BigInteger>> polynomial1 = element1.getValue();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial2 = element2.getValue();

		if (element1.isEquivalent(this.getZeroElement()) || element2.isEquivalent(this.getZeroElement())) {
			return this.getZeroElement();
		}

		if (this.isBinary()) {
			return this.getElementUnchecked(multiplyBinary(polynomial1, polynomial2));
		} else {
			return this.getElementUnchecked(multiplyNonBinary(polynomial1, polynomial2));
		}
	}

	protected ByteArray multiplyBinary(Polynomial<? extends DualisticElement<BigInteger>> polynomial1, Polynomial<? extends DualisticElement<BigInteger>> polynomial2) {
		ByteArray p1 = polynomial1.getCoefficients();
		ByteArray p2 = polynomial2.getCoefficients();
		if (polynomial2.getDegree() > polynomial1.getDegree()) {
			ByteArray tmp = p1;
			p1 = p2;
			p2 = tmp;
		}
		ByteArray zero = ByteArray.getInstance(); // an empty byte array
		ByteArray result = zero;
		while (!p2.equals(zero)) {
			if (p2.getBitAt(0)) {
				result = result.xorFillZero(p1);
			}
			// removeSuffix was added to avoid an endless loop in PolynomialFieldTest
			// the problem could probably be avoided by working with BitArray
			p1 = p1.shiftBitsRight(1).removeSuffix();
			p2 = p2.shiftBitsLeft(1).removeSuffix();
		}
		return result;
	}

	protected Map<Integer, DualisticElement<BigInteger>> multiplyNonBinary(Polynomial<? extends DualisticElement<BigInteger>> polynomial1, Polynomial<? extends DualisticElement<BigInteger>> polynomial2) {
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
		for (Integer i : polynomial1.getIndices()) {
			for (Integer j : polynomial2.getIndices()) {
				Integer k = i + j;
				DualisticElement<BigInteger> coefficient = polynomial1.getCoefficient(i).multiply(polynomial2.getCoefficient(j));
				DualisticElement<BigInteger> newCoefficient = coefficientMap.get(k);
				if (newCoefficient == null) {
					coefficientMap.put(k, coefficient);
				} else {
					coefficientMap.put(k, newCoefficient.add(coefficient));
				}
			}
		}
		return coefficientMap;
	}

	@Override
	protected PolynomialElement abstractGetOne() {
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap<Integer, DualisticElement<BigInteger>>();
		coefficientMap.put(0, this.getSemiRing().getOneElement());
		return getElementUnchecked(coefficientMap);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static PolynomialSemiRing getInstance(SemiRing semiRing) {
		if (semiRing == null) {
			throw new IllegalArgumentException();
		}
		return new PolynomialSemiRing(semiRing);
	}

}
