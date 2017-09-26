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
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class PolynomialField
	   extends PolynomialRing
	   implements FiniteField<Polynomial<? extends DualisticElement<BigInteger>>> {

	private static final long serialVersionUID = 1L;

	private final PolynomialElement irreduciblePolynomial;

	protected PolynomialField(PrimeField primeField, PolynomialElement irreduciblePolynomial) {
		super(primeField);
		this.irreduciblePolynomial = irreduciblePolynomial;
	}

	public PrimeField getPrimeField() {
		return (PrimeField) super.getRing();
	}

	public PolynomialElement getIrreduciblePolynomial() {
		return this.irreduciblePolynomial;
	}

	public int getDegree() {
		return this.irreduciblePolynomial.getValue().getDegree();
	}

	@Override
	protected BigInteger abstractGetOrder() {
		// p^m
		return this.getCharacteristic().pow(this.getDegree());
	}

	@Override
	protected boolean abstractContains(Polynomial value) {
		return super.abstractContains(value) && value.getDegree() < this.getDegree();
	}

	@Override
	protected PolynomialElement abstractGetElement(Polynomial value) {
		return new PolynomialElement(this, value);
	}

	@Override
	protected Converter<Polynomial<? extends DualisticElement<BigInteger>>, BigInteger>
		   abstractGetBigIntegerConverter() {
		return new AbstractBigIntegerConverter<Polynomial<? extends DualisticElement<BigInteger>>>(null) {

			@Override
			protected BigInteger abstractConvert(Polynomial<? extends DualisticElement<BigInteger>> polynomial) {
				int degree = getDegree();
				BigInteger[] values = new BigInteger[degree];
				for (int i = 0; i < degree; i++) {
					values[i] = polynomial.getCoefficient(i).convertToBigInteger();
				}
				return MathUtil.pair(values);
			}

			@Override
			protected Polynomial<? extends DualisticElement<BigInteger>> abstractReconvert(BigInteger value) {
				BigInteger[] bigIntegers = MathUtil.unpair(value, getDegree());
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
	// TODO Generalize to getRandomElements by replacing HybridRandomByteSequence by RandomByteSequence
	public PolynomialElement getRandomElement(int degree, HybridRandomByteSequence randomByteSequence) {
		if (degree >= this.getDegree()) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, degree);
		}
		return super.getRandomElement(degree, randomByteSequence);
	}

	@Override
	// TODO Generalize to getRandomElements by replacing HybridRandomByteSequence by RandomByteSequence
	public PolynomialElement getRandomMonicElement(int degree, boolean a0NotZero,
		   HybridRandomByteSequence randomByteSequence) {
		if (degree >= this.getDegree()) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, degree);
		}
		return super.getRandomMonicElement(degree, a0NotZero, randomByteSequence);
	}

	@Override
	public BigInteger getCharacteristic() {
		return this.getPrimeField().getOrder();
	}

	@Override
	public MultiplicativeGroup<Polynomial<? extends DualisticElement<BigInteger>>> getMultiplicativeGroup() {
		// TODO Create muliplicative.classes.FStar (Definition 2.228, Fact 2.229/2.230)
		throw new UniCryptRuntimeException(ErrorCode.NOT_YET_IMPLEMENTED, this);
	}

	@Override
	protected PolynomialElement abstractMultiply(PolynomialElement element1, PolynomialElement element2) {
		Polynomial<? extends DualisticElement<BigInteger>> polynomial1 = element1.getValue();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial2 = element2.getValue();

		if (element1.isEquivalent(this.getZeroElement()) || element2.isEquivalent(this.getZeroElement())) {
			return this.getZeroElement();
		}
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());
		PolynomialElement result;
		if (this.isBinary()) {
			result = ring.abstractGetElement(multiplyBinary(polynomial1, polynomial2));
		} else {
			result = ring.abstractGetElement(multiplyNonBinary(polynomial1, polynomial2));
		}
		return this.getElement(this.modulo(result).getValue());
	}

	@Override
	public PolynomialElement divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public final PolynomialElement nthRoot(Element element, long n) {
		return this.nthRoot(element, BigInteger.valueOf(n));
	}

	@Override
	public final PolynomialElement nthRoot(Element element, Element<BigInteger> n) {
		if (n == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.nthRoot(element, n.getValue());
	}

	@Override
	public final PolynomialElement nthRoot(Element element, BigInteger n) {
		if (n == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (n.signum() == 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, this, n);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (((AdditiveElement<BigInteger>) element).isZero()) {
			return this.getZeroElement();
		}
		if (!this.isFinite() || !this.hasKnownOrder()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		boolean positive = n.signum() > 0;
		n = MathUtil.modInv(n.abs().mod(this.getOrder()),this.getOrder());
		PolynomialElement result = this.defaultPowerAlgorithm((PolynomialElement) element, n);
		if (positive) {
			return result;
		}
		return this.invert(result);
	}

	@Override
	public final PolynomialElement squareRoot(Element element) {
		return this.nthRoot(element, MathUtil.TWO);
	}

	@Override
	public PolynomialElement oneOver(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (((AdditiveElement<Polynomial<? extends DualisticElement<BigInteger>>>) element).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.DIVISION_BY_ZERO, this, element);
		}
		// see extended Euclidean algorithm for polynomials (Algorithm 2.226)
		Triple euclid = this.extendedEuclidean((PolynomialElement) element, this.irreduciblePolynomial);
		return this.getElement(((PolynomialElement) euclid.getSecond()).getValue());

	}

	// g(x) mod this = h(x)
	private PolynomialElement modulo(PolynomialElement g) {
		if (g.getValue().getDegree() < this.getDegree()) {
			return g;
		}
		Pair longDiv = this.longDivision(g, this.irreduciblePolynomial);
		return (PolynomialElement) longDiv.getSecond();
	}

	public static <V> PolynomialField getInstance(PrimeField primeField, int degree) {
		return getInstance(primeField, degree, HybridRandomByteSequence.getInstance());
	}

	public static <V> PolynomialField getInstance(PrimeField primeField, int degree,
		   HybridRandomByteSequence randomByteSequence) {
		if (primeField == null || randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, primeField, randomByteSequence);
		}
		if (degree < 1) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_DEGREE, degree);
		}
		PolynomialRing ring = PolynomialRing.getInstance(primeField);
		PolynomialElement irreduciblePolynomial = ring.findIrreduciblePolynomial(degree, randomByteSequence);
		return new PolynomialField(primeField, irreduciblePolynomial);
	}

	public static PolynomialField getInstance(PrimeField primeField, PolynomialElement irreduciblePolynomial) {
		if (primeField == null || irreduciblePolynomial == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, primeField, irreduciblePolynomial);
		}
		if (!irreduciblePolynomial.getSet().getSemiRing().isEquivalent(primeField) || !irreduciblePolynomial.
			   isIrreducible()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, primeField, irreduciblePolynomial);
		}
		return new PolynomialField(primeField, irreduciblePolynomial);
	}

}
