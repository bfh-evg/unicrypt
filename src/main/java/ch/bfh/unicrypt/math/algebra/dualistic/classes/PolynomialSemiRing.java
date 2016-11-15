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
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
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

	private static final long serialVersionUID = 1L;

	private final SemiRing semiRing;

	protected PolynomialSemiRing(SemiRing semiRing) {
		super(Polynomial.class);
		this.semiRing = semiRing;
	}

	public static PolynomialSemiRing getInstance(SemiRing semiRing) {
		if (semiRing == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		return new PolynomialSemiRing(semiRing);
	}

	public SemiRing getSemiRing() {
		return this.semiRing;
	}

	public boolean isBinary() {
		return this.getSemiRing().getOrder().intValue() == 2;
	}

	public PolynomialElement getElement(Map<Integer, DualisticElement<BigInteger>> coefficientMap) {
		return this.getElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(),
													  this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElement(BitArray coefficients) {
		//TODO: Change back to protected -> ZModToBinaryPolynomialField is then not working anymore.
		return this.getElement(Polynomial.<DualisticElement<BigInteger>>getInstance(coefficients,
																					this.getSemiRing().getZeroElement(),
																					this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElement(DualisticElement... elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.getElement(Tuple.getInstance(elements));
	}

	public PolynomialElement getElement(Tuple coefficients) {
		if (coefficients == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!coefficients.isEmpty() && (!coefficients.getSet().isUniform() || !coefficients.getSet().getFirst().
			   isEquivalent(this.getSemiRing()))) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, coefficients);
		}
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
		for (int i = 0; i < coefficients.getArity(); i++) {
			coefficientMap.put(i, (DualisticElement<BigInteger>) coefficients.getAt(i));
		}
		return this.getElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(),
													  this.getSemiRing().getOneElement()));
	}

	public PolynomialElement getElementFrom(int... values) throws UniCryptException {
		if (values == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		BigInteger[] bigIntegers = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			bigIntegers[i] = BigInteger.valueOf(values[i]);
		}
		return this.getElementFrom(bigIntegers);
	}

	public PolynomialElement getElementFrom(BigInteger... values) throws UniCryptException {
		if (values == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.getElement(ProductSet.getInstance(this.getSemiRing(), values.length).getElementFrom(values));
	}

	public PolynomialElement getElementByRoots(Tuple roots) {
		if (roots == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (roots.isEmpty() || !roots.getSet().isUniform() || !roots.getSet().getFirst().
			   isEquivalent(this.getSemiRing())) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, roots);
		}

		DualisticElement zero = this.getSemiRing().getZeroElement();
		DualisticElement one = this.getSemiRing().getOneElement();

		int degree = roots.getArity();
		DualisticElement<BigInteger>[] coeffs = new DualisticElement[degree + 1];
		coeffs[degree] = one;
		coeffs[degree - 1] = (DualisticElement) roots.getAt(0).invert();

		for (int i = 1; i < degree; i++) {
			DualisticElement x = (DualisticElement) roots.getAt(i).invert();
			for (int j = degree - (i + 1); j < degree; j++) {
				coeffs[j] = coeffs[j + 1].multiply(x).add(coeffs[j] == null ? zero : coeffs[j]);
			}
		}

		return new PolynomialElement(this, Polynomial.<DualisticElement<BigInteger>>getInstance(coeffs, zero, one));
	}

	public PolynomialElement getRandomElement(int degree) {
		return this.getRandomElement(degree, HybridRandomByteSequence.getInstance());
	}

	// TODO Generalize to getRandomElements by replacing HybridRandomByteSequence by RandomByteSequence
	public PolynomialElement getRandomElement(int degree, HybridRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (degree < 0) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, degree);
		}
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap<>();
		for (int i = 0; i <= degree; i++) {
			DualisticElement<BigInteger> coefficient = this.getSemiRing().getRandomElement(randomByteSequence);
			if (!coefficient.isZero()) {
				coefficientMap.put(i, coefficient);
			}
		}
		return this.abstractGetElement(coefficientMap);
	}

	public PolynomialElement getRandomMonicElement(int degree, boolean a0NotZero) {
		return this.getRandomMonicElement(degree, a0NotZero, HybridRandomByteSequence.getInstance());
	}

	// TODO Generalize to getRandomElements by replacing HybridRandomByteSequence by RandomByteSequence
	public PolynomialElement getRandomMonicElement(int degree, boolean a0NotZero,
		   HybridRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (degree < 0) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, degree);
		}
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap<>();
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
		return this.abstractGetElement(coefficientMap);
	}

	protected PolynomialElement abstractGetElement(Map<Integer, DualisticElement<BigInteger>> coefficientMap) {
		return this.abstractGetElement(Polynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(),
															  this.getSemiRing().getOneElement()));
	}

	protected PolynomialElement abstractGetElement(BitArray coefficients) {
		return this.abstractGetElement(Polynomial.getInstance(coefficients, this.getSemiRing().getZeroElement(),
															  this.getSemiRing().getOneElement()));
	}

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
	protected String defaultToStringContent() {
		return this.getSemiRing().toString();
	}

	@Override
	protected boolean abstractContains(Polynomial value) {
		Sequence<Integer> indices = value.getCoefficientIndices();
		for (int i : indices) {
			if (!(Element.class.isInstance(value.getCoefficient(i))
				   && this.getSemiRing().contains((Element) value.getCoefficient(i)))) {
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
	protected Converter<Polynomial<? extends DualisticElement<BigInteger>>, BigInteger> abstractGetBigIntegerConverter() {
		return new AbstractBigIntegerConverter<Polynomial<? extends DualisticElement<BigInteger>>>(null) {

			@Override
			protected BigInteger abstractConvert(Polynomial<? extends DualisticElement<BigInteger>> polynomial) {
				int degree = polynomial.getDegree();
				BigInteger[] values = new BigInteger[degree + 1];
				for (int i = 0; i <= degree; i++) {
					values[i] = polynomial.getCoefficient(i).convertToBigInteger();
				}
				return MathUtil.pairWithSize(values);
			}

			@Override
			protected Polynomial<? extends DualisticElement<BigInteger>> abstractReconvert(BigInteger value) {
				BigInteger[] bigIntegers = MathUtil.unpairWithSize(value);
				DualisticElement[] elements = new DualisticElement[bigIntegers.length];
				int i = 0;
				for (BigInteger bigInteger : bigIntegers) {
					try {
						elements[i] = getSemiRing().getElementFrom(bigInteger);
					} catch (UniCryptException exception) {
						throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, exception, value);
					}
					i++;
				}
				Polynomial<? extends DualisticElement<BigInteger>> polynomial
					   = Polynomial.<DualisticElement<BigInteger>>getInstance(elements,
																			  getSemiRing().getZeroElement(),
																			  getSemiRing().getOneElement());
				return polynomial;
			}
		};
	}

	@Override
	protected Sequence<PolynomialElement> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

	@Override
	protected PolynomialElement defaultSelfApplyAlgorithm(PolynomialElement element, BigInteger posFactor) {
		// TODO Optimize for binary
		Polynomial<? extends DualisticElement<BigInteger>> polynomial = element.getValue();
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
		for (Integer i : polynomial.getCoefficientIndices()) {
			coefficientMap.put(i, polynomial.getCoefficient(i).selfApply(posFactor));
		}
		return this.abstractGetElement(coefficientMap);
	}

	@Override
	protected PolynomialElement abstractApply(PolynomialElement element1, PolynomialElement element2) {
		Polynomial<? extends DualisticElement<BigInteger>> polynomial1 = element1.getValue();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial2 = element2.getValue();

		if (this.isBinary()) {
			BitArray coefficients = polynomial1.getCoefficients().xor(polynomial2.getCoefficients(), false);
			return this.abstractGetElement(coefficients);
		} else {
			Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
			for (Integer i : polynomial1.getCoefficientIndices()) {
				coefficientMap.put(i, polynomial1.getCoefficient(i));
			}
			for (Integer i : polynomial2.getCoefficientIndices()) {
				DualisticElement<BigInteger> coefficient = coefficientMap.get(i);
				if (coefficient == null) {
					coefficientMap.put(i, polynomial2.getCoefficient(i));
				} else {
					coefficientMap.put(i, coefficient.add(polynomial2.getCoefficient(i)));
				}
			}
			return this.abstractGetElement(coefficientMap);
		}
	}

	@Override
	protected PolynomialElement abstractGetIdentityElement() {
		return this.abstractGetElement(new HashMap<Integer, DualisticElement<BigInteger>>());
	}

	@Override
	protected PolynomialElement abstractMultiply(PolynomialElement element1, PolynomialElement element2) {
		Polynomial<? extends DualisticElement<BigInteger>> polynomial1 = element1.getValue();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial2 = element2.getValue();

		if (element1.isEquivalent(this.getZeroElement()) || element2.isEquivalent(this.getZeroElement())) {
			return this.getZeroElement();
		}

		if (this.isBinary()) {
			return this.abstractGetElement(multiplyBinary(polynomial1, polynomial2));
		} else {
			return this.abstractGetElement(multiplyNonBinary(polynomial1, polynomial2));
		}
	}

	protected BitArray multiplyBinary(
		   Polynomial<? extends DualisticElement<BigInteger>> polynomial1,
		   Polynomial<? extends DualisticElement<BigInteger>> polynomial2) {
		BitArray c1 = polynomial1.getCoefficients();
		BitArray c2 = polynomial2.getCoefficients();
		if (polynomial2.getDegree() > polynomial1.getDegree()) {
			BitArray tmp = c1;
			c1 = c2;
			c2 = tmp;
		}
		BitArray zero = BitArray.getInstance(); // an empty bitarray
		BitArray result = zero;
		while (!c2.equals(zero)) {
			if (c2.getAt(0)) {
				result = result.xor(c1, false);
			}
			c1 = c1.shiftRight(1);
			c2 = c2.shiftLeft(1);
		}
		return result;
	}

	protected Map<Integer, DualisticElement<BigInteger>> multiplyNonBinary(
		   Polynomial<? extends DualisticElement<BigInteger>> polynomial1,
		   Polynomial<? extends DualisticElement<BigInteger>> polynomial2) {
		Map<Integer, DualisticElement<BigInteger>> coefficientMap = new HashMap();
		for (Integer i : polynomial1.getCoefficientIndices()) {
			for (Integer j : polynomial2.getCoefficientIndices()) {
				Integer k = i + j;
				DualisticElement<BigInteger> coefficient
					   = polynomial1.getCoefficient(i).multiply(polynomial2.getCoefficient(j));
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
		Map<Integer, DualisticElement<BigInteger>> coefficientMap
			   = new HashMap<>();
		coefficientMap.put(0, this.getSemiRing().getOneElement());
		return abstractGetElement(coefficientMap);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE;
	}

}
