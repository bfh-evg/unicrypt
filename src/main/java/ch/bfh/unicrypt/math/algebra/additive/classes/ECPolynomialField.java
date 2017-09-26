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
package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.helper.sequence.BigIntegerSequence;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECParameters;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import java.math.BigInteger;

/**
 * y²+xy=x³+ax²+b
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public class ECPolynomialField
	   extends AbstractEC<PolynomialField, Polynomial<? extends DualisticElement<BigInteger>>, PolynomialElement, ECPolynomialElement> {

	private static final long serialVersionUID = 1L;

	private final BigInteger traceA;

	protected ECPolynomialField(PolynomialField finiteField, PolynomialElement a, PolynomialElement b,
		   PolynomialElement gx, PolynomialElement gy, BigInteger subGroupOrder, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, subGroupOrder, coFactor);
		this.traceA = this.trace(a);
	}

	@Override
	// see Gadiel Seroussi, "Compact Representation of Elliptic Curve Points over F_2^n", 1998, p.3
	// see Brian King, "Mapping an arbitrary message to an elliptic curve when defined over GF(2^n)", 2009, p.172
	protected boolean abstractContains(PolynomialElement x) {
		// if x=0, then -(0,y)=(0,y) is possibly a point on the curve, but not in the prime order subgroup
		if (x.isZero()) {
			return false;
		}
		if (this.getCoFactor().intValue() == 2) {
			// false if trace(x)≠trace(a) or trace(b/x²)≠0
			if (!this.trace(x).equals(this.traceA) || !trace(this.getB().divide(x.square())).equals(MathUtil.ZERO)) {
				return false;
			}
		}
		if (this.getCoFactor().intValue() > 2) {
			//TODO: test for subgroup membership missing
		}
		return true;
	}

	@Override
	protected boolean abstractContains(PolynomialElement x, PolynomialElement y) {
		// if x=0, then -(0,y)=(0,y) is possibly a point on the curve, but not in the prime order subgroup
		if (x.isZero()) {
			return false;
		}
		// y²+xy=x³+ax²+b <=> x³+ax²+b-(y²+xy)=0
		if (!x.power(3).add(this.getA().multiply(x.power(2))).add(this.getB()).subtract(y.power(2).add(x.multiply(y))).
			   isZero()) {
			return false;
		}
		if (this.getCoFactor().intValue() == 2) {
			// false if trace(x)≠trace(a)
			if (!this.trace(x).equals(this.traceA)) {
				return false;
			}
		}
		if (this.getCoFactor().intValue() > 2) {
			//TODO: test for subgroup membership missing
		}
		return true;
	}

	@Override
	protected ECPolynomialElement abstractGetElement(Point<PolynomialElement> value) {
		return new ECPolynomialElement(this, value);
	}

	@Override
	protected ECPolynomialElement abstractGetIdentityElement() {
		return new ECPolynomialElement(this);
	}

	@Override
	protected ECPolynomialElement abstractAdd(PolynomialElement x1, PolynomialElement y1, PolynomialElement x2,
		   PolynomialElement y2) {
		// "SEC 1: Elliptic Curve Cryptography", Version 2.0, 2009 (Section 2.2.2, page 8)
		PolynomialElement lambda, x, y;
		if (x1.isEquivalent(x2)) { // testing only the x-coordinates is sufficient
			// λ=x1+(y1/x1)
			lambda = x1.add(y1.divide(x1));
			// x=λ²+λ+a
			x = lambda.square().add(lambda).add(this.getA());
			// y=x1²+(λ+1)x
			y = x1.square().add(lambda.add(this.getFiniteField().getOneElement()).multiply(x));
		} else {
			// λ=(y1+y2)/(x1+x2)
			lambda = y1.add(y2).divide(x1.add(x2));
			// x=λ²+λ+a+x1+x2
			x = lambda.square().add(lambda).add(this.getA().add(x1).add(x2));
			// y=λ(x1+x)+x+y1
			y = lambda.multiply(x1.add(x)).add(x).add(y1);
		}
		return this.abstractGetElement(Point.getInstance(x, y));
	}

	@Override
	protected ECPolynomialElement abstractNegate(PolynomialElement x, PolynomialElement y) {
		return this.abstractGetElement(Point.getInstance(x, y.add(x)));
	}

	@Override
	protected PolynomialElement abstractGetY(PolynomialElement x) {
		// computes one of the two y-coordinates for x≠0
		// see B. King, "Mapping an arbitrary message to an elliptic curve when defined over GF(2^n)" p.172
		PolynomialElement lambda = this.halfTrace(x.add(this.getA()).add(this.getB().divide(x.square())));
		return lambda.multiply(x);
	}

	// Returns the trace of a polynomial of characteristic 2
	// see "IEEE Standard Specifications for Public-Key Cryptography, IEEE Std 1363-2000", 2000, A.4.5, p.89
	private BigInteger trace(PolynomialElement x) {
		int deg = this.getFiniteField().getDegree();
		PolynomialElement trace = x;
		for (int i = 1; i < deg; i++) {
			x = x.square();
			trace = trace.add(x);
		}
		return trace.getValue().getCoefficient(0).getValue();
	}

	// Returns the half-trace of a polynomial of characteristic 2 (only if the degree is odd)
	// The result is a solution for the quadratic equation z²+z=x
	// see "IEEE Standard Specifications for Public-Key Cryptography, IEEE Std 1363-2000", 2000, A.4.6, A.4.7, p.90
	// see B. King, "Mapping an arbitrary message to an elliptic curve when defined over GF(2^n)", Section 2.2, p.170
	private PolynomialElement halfTrace(PolynomialElement x) {
		int deg = this.getFiniteField().getDegree();
		PolynomialElement trace = x;
		for (int i = 1; i <= (deg - 1) / 2; i++) {
			x = x.square().square();
			trace = trace.add(x);
		}
		return trace;
	}

	/**
	 * Returns a subgroup of an elliptic curve E(F_p):y²+xy=x³+ax²+b over a polynomial field F_{2^n}. Checking the curve
	 * parameters is done according to "SEC1: Elliptic Curve Cryptography", Version 2.0, 2009 (Section 3.1.2.2.1, page
	 * 20).
	 * <p>
	 * @param securityLevel   Security level
	 * @param polynomialField Finite field of type {@link PolynomialField}
	 * @param a               Element of F_{2^n} representing the coefficient {@code a} in the curve equation
	 * @param b               Element of F_{2^n} representing the coefficient {@code b} in the curve equation
	 * @param gx              x-coordinate of the generator
	 * @param gy              y-coordinate of the generator
	 * @param subGroupOrder   Order of the the subgroup
	 * @param coFactor        Co-factor of the subgroup
	 * @return The resulting subgroup of the elliptic curve
	 */
	public static ECPolynomialField getInstance(int securityLevel, PolynomialField polynomialField, PolynomialElement a,
		   PolynomialElement b, PolynomialElement gx, PolynomialElement gy, BigInteger subGroupOrder,
		   BigInteger coFactor) {
		return ECPolynomialField.getInstance(securityLevel, polynomialField, a, b, gx, gy, subGroupOrder, coFactor,
											 false);
	}

	// a private helper method to include the possibility of test parameters which do not pass all tests
	private static ECPolynomialField getInstance(int securityLevel, PolynomialField polynomialField,
		   PolynomialElement a, PolynomialElement b, PolynomialElement gx, PolynomialElement gy,
		   BigInteger subGroupOrder, BigInteger coFactor, boolean isTest) {
		if (polynomialField == null || a == null || b == null || gx == null || gy == null || subGroupOrder == null
			   || coFactor == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, polynomialField, a, b, gx, gy, subGroupOrder,
											   coFactor);
		}
		int degree = polynomialField.getDegree();
		BigInteger fieldOrder = polynomialField.getOrder();
		// Test1
		if (2 * securityLevel >= degree) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, securityLevel, degree);
		}
		// Test2: not necessary
		// Test3a
		if (!polynomialField.contains(a) || !polynomialField.contains(b)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, polynomialField, a, b);
		}
		// Test3b
		if (!polynomialField.contains(gx) || !polynomialField.contains(gy)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, polynomialField, gx, gy);
		}
		// Test4
		if (b.isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, b);
		}
		// Test5
		if (!gx.power(3).add(a.multiply(gx.power(2))).add(b).subtract(gy.power(2).add(gx.multiply(gy))).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, a, b, gx, gy);
		}
		// Test6
		if (!MathUtil.isPrime(subGroupOrder)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
		}
		if (!isTest) {
			// Test7a
			if (coFactor.compareTo(MathUtil.powerOfTwo(securityLevel / 8)) > 0) {
				throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
			}
			// Test7b
			if (!MathUtil.sqrt(fieldOrder.multiply(MathUtil.FOUR)).add(fieldOrder).add(MathUtil.ONE).divide(
				   subGroupOrder).equals(coFactor)) {
				throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, fieldOrder,
												   subGroupOrder, coFactor);
			}
			// Test9a
			for (BigInteger i : BigIntegerSequence.getInstance(1, 100 * degree - 1)) {
				if (MathUtil.modExp(MathUtil.TWO, i, subGroupOrder).equals(MathUtil.ONE)) {
					throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, i, subGroupOrder);
				}
			}
		}
		// Test9b
		if (subGroupOrder.multiply(coFactor).equals(fieldOrder)) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, subGroupOrder, coFactor, degree);
		}
		ECPolynomialField instance = new ECPolynomialField(polynomialField, a, b, gx, gy, subGroupOrder, coFactor);
		ECPolynomialElement generator = instance.getDefaultGenerator();
		// Test8
		if (!instance.defaultSelfApplyAlgorithm(generator, subGroupOrder).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, generator, subGroupOrder, instance.
											   defaultSelfApplyAlgorithm(generator, subGroupOrder));
		}
		return instance;
	}

	public static ECPolynomialField getInstance(final ECParameters<PolynomialField, PolynomialElement> parameters) {
		if (parameters == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		return ECPolynomialField.getInstance(
			   parameters.getSecurityLevel(),
			   parameters.getFiniteField(),
			   parameters.getA(),
			   parameters.getB(),
			   parameters.getGx(),
			   parameters.getGy(),
			   parameters.getSubGroupOrder(),
			   parameters.getCoFactor(),
			   parameters.isTest()
		);
	}

}
