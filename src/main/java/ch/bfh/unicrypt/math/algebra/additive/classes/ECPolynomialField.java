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
package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.exception.ErrorCode;
import ch.bfh.unicrypt.exception.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.params.interfaces.StandardECPolynomialFieldParams;
import java.math.BigInteger;

/**
 *
 * @author Christian Lutz
 */
public class ECPolynomialField
	   extends AbstractEC<PolynomialField, Polynomial<? extends DualisticElement<BigInteger>>, PolynomialElement, ECPolynomialElement> {

	private static final long serialVersionUID = 1L;

	public ECPolynomialField(PolynomialField finiteField, PolynomialElement a,
		   PolynomialElement b, PolynomialElement gx, PolynomialElement gy,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, givenOrder, coFactor);
	}

	public ECPolynomialField(PolynomialField finiteField, PolynomialElement a, PolynomialElement b,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, givenOrder, coFactor);
	}

	@Override
	protected boolean abstractContains(PolynomialElement x) {
		// True only if trace(x+a+b/x^2)=0 (Klaus Pommerening: "Quadratic Equations in Finite Fields of Characteristic 2")
		DualisticElement<BigInteger> trace = traceGF2m(x.add(this.getA()).add(this.getB().divide(x.square())), this);
		return trace.isEquivalent(ZModTwo.ZERO);
	}

	@Override
	protected boolean abstractContains(PolynomialElement x, PolynomialElement y) {
		PolynomialElement left = y.power(2).add(x.multiply(y));
		PolynomialElement right = x.power(3).add(x.power(2).multiply(getA())).add(getB());
		return left.isEquivalent(right);
	}

	@Override
	protected ECPolynomialElement abstractGetElement(
		   Point<PolynomialElement> value) {
		return new ECPolynomialElement(this, value);
	}

	@Override
	protected ECPolynomialElement[] abstractGetY(PolynomialElement x) {
		// described in "Mapping an arbitrary message to an elliptic curve when defined over GF(2^n)" p.172
		PolynomialElement t = x.add(this.getA()).add(this.getB().divide(x.square()));
		PolynomialElement l = this.getFiniteField().solveQuadradicEquation(t);

		ECPolynomialElement y1 = this.getElement(x, l.add(l.getSet().getOneElement()).multiply(x));
		ECPolynomialElement y2 = y1.invert();
		ECPolynomialElement[] y = {y1, y2};
		return y;
	}

	@Override
	protected ECPolynomialElement abstractGetIdentityElement() {
		return new ECPolynomialElement(this);
	}

	@Override
	protected ECPolynomialElement abstractApply(ECPolynomialElement element1, ECPolynomialElement element2) {
		if (element1.isZero()) {
			return element2;
		}
		if (element2.isZero()) {
			return element1;
		}
		if (element1.equals(element2.invert())) {
			return this.getIdentityElement();
		}
		PolynomialElement s, rx, ry;
		PolynomialElement px = element1.getX();
		PolynomialElement py = element1.getY();
		PolynomialElement qx = element2.getX();
		PolynomialElement qy = element2.getY();
		if (element1.equals(element2)) {
			final PolynomialElement one = this.getFiniteField().getOneElement();
			s = px.add(py.divide(px));
			rx = s.square().add(s).add(this.getA());
			ry = px.square().add((s.add(one)).multiply(rx));
		} else {
			s = py.add(qy).divide(px.add(qx));
			rx = s.square().add(s).add(px).add(qx).add(this.getA());
			ry = s.multiply(px.add(rx)).add(rx).add(py);
		}
		return this.abstractGetElement(Point.getInstance(rx, ry));
	}

	@Override
	protected ECPolynomialElement abstractInvert(ECPolynomialElement element) {
		if (element.isZero()) {
			return this.getZeroElement();
		}
		return this.abstractGetElement(Point.getInstance(element.getX(), element.getY().add(element.getX())));
	}

	@Override
	protected Sequence<ECPolynomialElement> abstractGetRandomElementsWithoutGenerator(RandomByteSequence randomByteSequence) {
		throw new UniCryptRuntimeException(ErrorCode.NOT_YET_IMPLEMENTED, this);
	}

	/**
	 * Checks curve parameters for validity according SEC1: Elliptic Curve Cryptographie Ver. 1.0 page 21
	 * <p>
	 * @return True if curve parameters are valid
	 */
	public boolean isValid() {
		boolean c1, c2, c3, c4, c5, c6, c7, c8;
		int m = this.getFiniteField().getDegree();

		c1 = this.getFiniteField().isIrreduciblePolynomial(this.getFiniteField().getIrreduciblePolynomial());

		c2 = this.getFiniteField().contains(getA());
		c2 = c2 && this.getFiniteField().contains(getB());
		c2 = c2 && this.getFiniteField().contains(this.getDefaultGenerator().getX());
		c2 = c2 && this.getFiniteField().contains(this.getDefaultGenerator().getY());

		c3 = !this.getB().equals(this.getFiniteField().getZeroElement());

		c4 = this.contains(this.getDefaultGenerator());

		c5 = MathUtil.isPrime(getOrder());

		c6 = this.selfApply(this.getDefaultGenerator(), getOrder()).isEquivalent(this.getZeroElement());

		c7 = true;
		for (BigInteger i = MathUtil.ONE; i.compareTo(new BigInteger("100")) < 0; i = i.add(MathUtil.ONE)) {
			if (MathUtil.TWO.modPow(i, getOrder()).equals(MathUtil.ONE)) {
				c7 = false;
			}
		}
		c8 = !getOrder().multiply(getCoFactor()).equals(MathUtil.TWO.pow(m));

		return c1 && c2 && c3 && c4 && c5 && c6 && c7 && c8;
	}

	/**
	 * Private method implements selfApply to check if a ECPolynomialElement is a valid generator
	 * <p>
	 * @param element
	 * @param posAmount
	 * @return
	 */
	private ECPolynomialElement selfApply(ECPolynomialElement element, BigInteger posAmount) {
		ECPolynomialElement result = element;
		for (int i = posAmount.bitLength() - 2; i >= 0; i--) {
			result = result.add(result);
			if (posAmount.testBit(i)) {
				result = result.add(element);
			}
		}
		return result;
	}

	/**
	 * Returns an elliptic curve over F2m y²+yx=x³+ax²+b if parameters are valid.
	 * <p>
	 * @param f          Finite field of type BinaryPolynomial
	 * @param a          Element of f representing a in the curve equation
	 * @param b          Element of f representing b in the curve equation
	 * @param givenOrder Order of the the used subgroup
	 * @param coFactor   Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECPolynomialField getInstance(PolynomialField f, PolynomialElement a, PolynomialElement b, BigInteger givenOrder, BigInteger coFactor) {
		ECPolynomialField newInstance = new ECPolynomialField(f, a, b, givenOrder, coFactor);

		if (newInstance.isValid()) {
			return newInstance;
		} else {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, f, a, b, givenOrder, coFactor);
		}
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b if parameters are valid.
	 * <p>
	 * @param f          Finite field of type ZModPrime
	 * @param a          Element of F_p representing a in the curve equation
	 * @param b          Element of F_p representing b in the curve equation
	 * @param gx         x-coordinate of the generator
	 * @param gy         y-coordinate of the generator
	 * @param givenOrder Order of the the used subgroup
	 * @param coFactor   Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECPolynomialField getInstance(PolynomialField f, PolynomialElement a, PolynomialElement b,
		   PolynomialElement gx, PolynomialElement gy, BigInteger givenOrder, BigInteger coFactor) {
		ECPolynomialField newInstance = new ECPolynomialField(f, a, b, gx, gy, givenOrder, coFactor);

		if (newInstance.isValid()) {
			return newInstance;
		} else {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, f, a, b, gx, gy, givenOrder, coFactor);
		}
	}

	public static ECPolynomialField getInstance(final StandardECPolynomialFieldParams params) {
		PolynomialField field;
		PolynomialElement a, b, gx, gy;
		BigInteger order, h;

		field = params.getFiniteField();
		a = params.getA();
		b = params.getB();
		gx = params.getGx();
		gy = params.getGy();
		order = params.getOrder();
		h = params.getH();
		return ECPolynomialField.getInstance(field, a, b, gx, gy, order, h);
	}

	/**
	 * <p>
	 * Returns the trace of an polynomial of characteristic 2 Source;: Quadratic Equations in Finite Fields of
	 * Characteristic 2 Klaus Pommerening May 2000 – english version February 2012, Page 2</p>
	 * <p>
	 * @param x
	 * @param ec
	 * @return </p>
	 */
	public static DualisticElement<BigInteger> traceGF2m(PolynomialElement x, ECPolynomialField ec) {
		int deg = ec.getFiniteField().getDegree();
		PolynomialElement trace = x;
		PolynomialElement tmp = x;

		for (int i = 1; i < deg; i++) {
			tmp = tmp.square();
			trace = trace.add(tmp);
		}

		DualisticElement<BigInteger> trace_Dualistic = trace.getValue().getCoefficient(0);
		return trace_Dualistic;
	}

}
