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

	protected ECZModPrime(ZModPrime finiteField, ZModElement a, ZModElement b, ZModElement gx, ZModElement gy,
		   BigInteger subGroupOrder, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, subGroupOrder, coFactor);
	}

	@Override
	public boolean abstractContains(ZModElement x) {
		ZModElement ySquare = x.power(3).add(this.getA().multiply(x)).add(this.getB());
		if (!this.getFiniteField().hasSquareRoot(ySquare)) {
			return false;
		}
		if (this.getCoFactor().intValue() > 1) {
			ECZModElement element = this.abstractGetElement(Point.getInstance(x, this.abstractGetY(x)));
			if (!this.defaultSelfApplyAlgorithm(element, this.getOrder()).isZero()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean abstractContains(ZModElement x, ZModElement y) {
		// y²=x³+ax+b <=> x³+ax+b-y²=0
		if (!x.power(3).add(this.getA().multiply(x)).add(this.getB()).subtract(y.square()).isZero()) {
			return false;
		}
		if (this.getCoFactor().intValue() > 1) {
			ECZModElement element = this.abstractGetElement(Point.getInstance(x, y));
			if (!this.defaultSelfApplyAlgorithm(element, this.getOrder()).isZero()) {
				return false;
			}
		}
		return true;
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
	 * @param subGroupOrder Order of the the subgroup
	 * @param coFactor      Co-factor of the subgroup
	 * @return The resulting subgroup of the elliptic curve
	 */
	public static ECZModPrime getInstance(int securityLevel, ZModPrime primeField, ZModElement a, ZModElement b,
		   ZModElement gx, ZModElement gy, BigInteger subGroupOrder, BigInteger coFactor) {
		return ECZModPrime.getInstance(securityLevel, primeField, a, b, gx, gy, subGroupOrder, coFactor, false);
	}

	// a private helper method to include the possibility of test parameters which do not pass all tests
	private static ECZModPrime getInstance(int securityLevel, ZModPrime primeField, ZModElement a, ZModElement b,
		   ZModElement gx, ZModElement gy, BigInteger subGroupOrder, BigInteger coFactor, boolean isTest) {
		if (primeField == null || a == null || b == null || gx == null || gy == null || subGroupOrder == null
			   || coFactor == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, primeField, a, b, gx, gy, subGroupOrder,
											   coFactor);
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
		if (a.power(3).times(4).add(b.square().times(27)).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, a, b);
		}
		// Test4
		if (!gx.power(3).add(a.multiply(gx)).add(b).subtract(gy.square()).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, a, b, gx, gy);
		}
		// Test5
		if (!MathUtil.isPrime(subGroupOrder)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
		}
		// Test6a
		if (!isTest && coFactor.compareTo(MathUtil.powerOfTwo(securityLevel / 8)) > 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, coFactor);
		}
		// Test6b
		if (!MathUtil.sqrt(modulus.multiply(MathUtil.FOUR)).add(modulus).add(MathUtil.ONE).divide(subGroupOrder).equals(
			   coFactor)) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, modulus, subGroupOrder, coFactor);
		}
		// Test8a
		if (!isTest) {
			for (BigInteger i : BigIntegerSequence.getInstance(1, 99)) {
				if (MathUtil.modExp(modulus, i, subGroupOrder).equals(MathUtil.ONE)) {
					throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, i, subGroupOrder);
				}
			}
		}
		// Test8b
		if (modulus.equals(subGroupOrder)) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, modulus, subGroupOrder);
		}
		ECZModPrime instance = new ECZModPrime(primeField, a, b, gx, gy, subGroupOrder, coFactor);
		ECZModElement generator = instance.getDefaultGenerator();
		// Test7
		if (!instance.defaultSelfApplyAlgorithm(generator, subGroupOrder).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, generator, subGroupOrder);
		}
		return instance;
	}

	public static ECZModPrime getInstance(final ECParameters<ZModPrime, ZModElement> parameters) {
		if (parameters == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		return ECZModPrime.getInstance(
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
