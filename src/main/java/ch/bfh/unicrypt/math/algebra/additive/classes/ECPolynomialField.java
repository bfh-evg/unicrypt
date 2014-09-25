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

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

import java.math.BigInteger;

/**
 *
 * @author Christian Lutz
 */
public class ECPolynomialField
	   extends AbstractEC<BinaryPolynomialField, Polynomial<DualisticElement<ZModTwo>>> {

	public ECPolynomialField(BinaryPolynomialField finiteField, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> a,
		   DualisticElement<Polynomial<DualisticElement<ZModTwo>>> b, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> gx, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> gy,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, givenOrder, coFactor);
	}

	public ECPolynomialField(BinaryPolynomialField finiteField, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> a, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> b,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, givenOrder, coFactor);
	}

	@Override
	protected boolean abstractContains(DualisticElement x) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean abstractContains(DualisticElement x, DualisticElement y) {
		DualisticElement<Polynomial> left = y.power(2).add(x.multiply(y));
		DualisticElement<Polynomial> right = x.power(3).add(x.power(2).multiply(getA())).add(getB());
		return left.isEquivalent(right);
	}

	@Override
	protected ECElement<Polynomial<DualisticElement<ZModTwo>>> abstractGetElement(Point<DualisticElement<Polynomial<DualisticElement<ZModTwo>>>> value) {
		return new ECElement<Polynomial<DualisticElement<ZModTwo>>>(this, value);
	}

	@Override
	protected ECElement<Polynomial<DualisticElement<ZModTwo>>> abstractGetIdentityElement() {
		return new ECElement<Polynomial<DualisticElement<ZModTwo>>>(this);
	}

	@Override
	protected ECElement<Polynomial<DualisticElement<ZModTwo>>> abstractApply(ECElement<Polynomial<DualisticElement<ZModTwo>>> element1, ECElement<Polynomial<DualisticElement<ZModTwo>>> element2) {
		if (element1.isZero()) {
			return element2;
		}
		if (element2.isZero()) {
			return element1;
		}
		if (element1.equals(element2.invert())) {
			return this.getIdentityElement();
		}
		DualisticElement<Polynomial<DualisticElement<ZModTwo>>> s, rx, ry;
		DualisticElement<Polynomial<DualisticElement<ZModTwo>>> px = element1.getX();
		DualisticElement<Polynomial<DualisticElement<ZModTwo>>> py = element1.getY();
		DualisticElement<Polynomial<DualisticElement<ZModTwo>>> qx = element2.getX();
		DualisticElement<Polynomial<DualisticElement<ZModTwo>>> qy = element2.getY();
		if (element1.equals(element2)) {
			final DualisticElement one = this.getFiniteField().getOneElement();
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
	protected ECElement<Polynomial<DualisticElement<ZModTwo>>> abstractInvert(ECElement<Polynomial<DualisticElement<ZModTwo>>> element) {
		if (element.isZero()) {
			return this.getZeroElement();
		}
		return this.abstractGetElement(Point.getInstance(element.getX(), element.getY().invert()));
	}

	@Override
	protected ECElement<Polynomial<DualisticElement<ZModTwo>>> getRandomElementWithoutGenerator(RandomByteSequence randomByteSequence) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Checks curve parameters for validity according SEC1: Elliptic Curve Cryptographie Ver. 1.0 page 21
	 * <p>
	 * @return True if curve parameters are valid
	 * @throws Exception 
	 */
	public boolean isValid() throws Exception {
		boolean c2, c3, c4, c5, c6, c7, c8;
		int m = this.getFiniteField().getDegree();
		final BigInteger TWO = new BigInteger("2");
		
		c2=this.getFiniteField().isIrreduciblePolynomial(this.getFiniteField().getIrreduciblePolynomial());
		
		c3=this.getFiniteField().contains(getA());
		c3=c3 && this.getFiniteField().contains(getB());
		c3=c3 && this.getFiniteField().contains(this.getDefaultGenerator().getX());
		c3=c3 && this.getFiniteField().contains(this.getDefaultGenerator().getY());
		
		c4=!this.getB().equals(this.getFiniteField().getZeroElement());
		
		c5=this.contains(this.getDefaultGenerator());
		
		c6=MathUtil.isPrime(getOrder());
		
		c7=this.getDefaultGenerator().times(getOrder().multiply(getCoFactor())).equals(this.getZeroElement());
		
		for (BigInteger i = new BigInteger("0"); i.compareTo(new BigInteger("100"))<0; i=i.add(BigInteger.ONE)) {
			if(TWO.modPow(i, getOrder()).equals(BigInteger.ONE)){
				throw new Exception("Curve parameter not valid");
			}
		}
		
		c8=!getOrder().multiply(getCoFactor()).equals(TWO.pow(m));
		
		return c2 && c3 && c4 && c5 && c6 && c7 && c8;
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * Returns an elliptic curve over F2m y²+yx=x³+ax²+b if parameters are valid.
	 * <p>
	 * @param f          Finite field of type BinaryPolynomial
	 * @param a          Element of f representing a in the curve equation
	 * @param b          Element of f representing b in the curve equation
	 * @param givenOrder Order of the the used subgroup
	 * @param coFactor   Co-factor h*order= N -> total order of the group
	 * @return
	 * @throws Exception
	 */
	public static ECPolynomialField getInstance(BinaryPolynomialField f, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> a, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> b, BigInteger givenOrder, BigInteger coFactor) throws Exception {
		ECPolynomialField newInstance = new ECPolynomialField(f, a, b, givenOrder, coFactor);

		if (newInstance.isValid()) {
			return newInstance;
		} else {
			throw new Exception("Curve parameters not valid!");
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
	 * @throws Exception
	 */
	public static ECPolynomialField getInstance(BinaryPolynomialField f, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> a, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> b, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> gx, DualisticElement<Polynomial<DualisticElement<ZModTwo>>> gy, BigInteger givenOrder, BigInteger coFactor) throws Exception {
		ECPolynomialField newInstance = new ECPolynomialField(f, a, b, gx, gy, givenOrder, coFactor);

		if (newInstance.isValid()) {
			return newInstance;
		} else {
			throw new Exception("Curve parameters not valid!");
		}
	}

}
