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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.helper.sequence.BigIntegerSequence;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECParameters;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import java.math.BigInteger;

/**
 * y²=x³+ax+b
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public class ECZModPrime
	   extends AbstractEC<ZModPrime, BigInteger, ZModElement, ECZModElement> {

	private static final long serialVersionUID = -5442792676496187516L;

	protected ECZModPrime(ZModPrime finiteField, ZModElement a, ZModElement b, ZModElement gx, ZModElement gy, BigInteger order, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, order, coFactor);
	}

	@Override
	public boolean abstractContains(ZModElement x) {
		ZModElement ySquare = x.power(3).add(this.getA().multiply(x)).add(this.getB());
		return this.getFiniteField().hasSquareRoot(ySquare);
		//TODO: test for subgroup membership missing
	}

	@Override
	protected boolean abstractContains(ZModElement x, ZModElement y) {
		// y²=x³+ax+b <=> x³+ax+b-y²=0
		return x.power(3).add(this.getA().multiply(x)).add(this.getB()).subtract(y.square()).isZero();
		//TODO: test for subgroup membership missing
	}

	@Override
	protected ECZModElement abstractGetElement(Point<ZModElement> value) {
		return new ECZModElement(this, value);
	}

	@Override
	protected ECZModElement abstractGetIdentityElement() {
		return new ECZModElement(this);
	}

	@Override
	protected ECZModElement abstractAdd(ZModElement x1, ZModElement y1, ZModElement x2, ZModElement y2) {
		// "SEC 1: Elliptic Curve Cryptography", Version 2.0, 2009 (Section 2.2.1, page 7)
		ZModElement lambda;
		if (x1.isEquivalent(x2)) { // testing only the x-coordinates is sufficient
			// λ=(3x1²+a)/2y2
			lambda = x1.square().times(3).add(this.getA()).divide(y1.times(2));
		} else {
			// λ=(y2-y1)/(x2-x1)
			lambda = y2.subtract(y1).divide(x2.subtract(x1));
		}
		// x=λ²-x1-x2 ￼￼￼
		ZModElement x = lambda.square().subtract(x1).subtract(x2);
		// y=λ(x1-x)-y1
		ZModElement y = lambda.multiply(x1.subtract(x)).subtract(y1);
		return this.abstractGetElement(Point.getInstance(x, y));
	}

	@Override
	protected ECZModElement abstractNegate(ZModElement x, ZModElement y) {
		return this.abstractGetElement(Point.getInstance(x, y.negate()));
	}

	@Override
	protected ZModElement abstractGetY(ZModElement x) {
		// y²=x³+ax+b <=> x³+ax+b-y²=0
		ZModElement ySquare = x.power(3).add(this.getA().multiply(x)).add(this.getB());
		return this.getFiniteField().getSquareRoot(ySquare);
	}

	/**
	 * Returns a subgroup of an elliptic curve E(F_p):y²=x³+ax+b over a prime field F_p. Checking the curve parameters
	 * is done according to "SEC1: Elliptic Curve Cryptography", Version 2.0, 2009 (Section 3.1.1.2.1, page 17-18).
	 * <p>
	 * @param securityLevel Security level
	 * @param primeField    Prime field F_p of type {@link ZModPrime}
	 * @param a             Element of F_p representing the coefficient {@code a} in the curve equation
	 * @param b             Element of F_p representing the coefficient {@code b} in the curve equation
	 * @param gx            x-coordinate of the generator
	 * @param gy            y-coordinate of the generator
	 * @param order         Order of the the subgroup
	 * @param coFactor      Co-factor of the subgroup
	 * @return The resulting subgroup of the elliptic curve
	 */
	public static ECZModPrime getInstance(int securityLevel, ZModPrime primeField, ZModElement a, ZModElement b, ZModElement gx, ZModElement gy, BigInteger order, BigInteger coFactor) {
		if (primeField == null || a == null || b == null || gx == null || gy == null || order == null || coFactor == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, primeField, a, b, gx, gy, order, coFactor);
		}
		BigInteger modulus = primeField.getModulus();
		// Test1
		if (2 * securityLevel > modulus.bitLength()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, securityLevel, modulus);
		}
		// Test2a
		if (!primeField.contains(a) || !primeField.contains(b)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, primeField, a, b);
		}
		// Test2b
		if (!primeField.contains(gx) || !primeField.contains(gy)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, primeField, gx, gy);
		}
		// Test3
		ZModElement e4 = primeField.getElement(4);
		ZModElement e27 = primeField.getElement(27);
		if (a.power(3).multiply(e4).add(b.square().multiply(e27)).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, a, b);
		}
		// Test4
		if (!gx.power(3).add(a.multiply(gx)).add(b).subtract(gy.square()).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, a, b, gx, gy);
		}
		// Test5
		if (!MathUtil.isPrime(order)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
		}
		// Test6a
		if (coFactor.compareTo(MathUtil.powerOfTwo(securityLevel / 8)) > 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
		}
		// Test6b
		if (!MathUtil.sqrt(modulus.multiply(MathUtil.FOUR)).add(modulus).add(MathUtil.ONE).divide(order).equals(coFactor)) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, modulus, order, coFactor);
		}
		// Test7: done elsewhere
		// Test8a
		for (BigInteger i : BigIntegerSequence.getInstance(1, 99)) {
			if (modulus.modPow(i, order).equals(MathUtil.ONE)) {
				throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, order);
			}
		}
		// Test8b
		if (modulus.equals(order)) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, modulus, order);
		}
		return new ECZModPrime(primeField, a, b, gx, gy, order, coFactor);
	}

	public static ECZModPrime getInstance(final ECParameters<ZModPrime, ZModElement> parameters) {
		if (parameters == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, parameters);
		}
		int securityLevel = parameters.getSecurityLevel();
		ZModPrime field = parameters.getFiniteField();
		ZModElement a = parameters.getA();
		ZModElement b = parameters.getB();
		ZModElement gx = parameters.getGx();
		ZModElement gy = parameters.getGy();
		BigInteger order = parameters.getOrder();
		BigInteger coFactor = parameters.getCoFactor();

		return ECZModPrime.getInstance(securityLevel, field, a, b, gx, gy, order, coFactor);
	}

}
