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
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.cache.Cache;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.prime.Prime;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class ZModPrime
	   extends ZMod
	   implements PrimeField<BigInteger> {

	private static final long serialVersionUID = 1L;
	private static final Cache<BigInteger, ZModPrime> CACHE = new Cache<>(Cache.SIZE_S);

	protected ZModPrime(Prime prime) {
		super(prime.getValue());
	}

	public ZModElement getSquareRoot(ZModElement element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		return this.getElement(MathUtil.sqrtModPrime(element.getValue(), this.getModulus()));
	}

	public boolean hasSquareRoot(ZModElement element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		return element.power(this.modulus.subtract(MathUtil.ONE).divide(MathUtil.TWO)).isOne();
	}

	@Override
	public BigInteger getCharacteristic() {
		return this.getOrder();
	}

	@Override
	public ZStarModPrime getMultiplicativeGroup() {
		return ZStarModPrime.getInstance(this.modulus);
	}

	@Override
	public ZModElement divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public final ZModElement nthRoot(Element element, long n) {
		return this.nthRoot(element, BigInteger.valueOf(n));
	}

	@Override
	public final ZModElement nthRoot(Element element, Element<BigInteger> n) {
		if (n == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.nthRoot(element, n.getValue());
	}

	@Override
	public final ZModElement nthRoot(Element element, BigInteger n) {
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
		n = MathUtil.modInv(n.abs().mod(this.getOrder()), this.getOrder());
		ZModElement result = this.defaultPowerAlgorithm((ZModElement) element, n);
		if (positive) {
			return result;
		}
		return this.invert(result);
	}

	@Override
	public final ZModElement squareRoot(Element element) {
		return this.nthRoot(element, MathUtil.TWO);
	}

	@Override
	public ZModElement oneOver(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		ZModElement zModElement = (ZModElement) element;
		if (zModElement.isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.DIVISION_BY_ZERO, this);
		}
		return this.abstractGetElement(MathUtil.modInv(zModElement.getValue(), this.modulus));
	}

	@Override
	public ZModPrime getZModOrder() {
		return ZModPrime.getInstance(this.getOrder());
	}

	@Override
	public ZStarModPrime getZStarModOrder() {
		return ZStarModPrime.getInstance(this.getOrder());
	}

	public static ZModPrime getInstance(final long modulus) {
		return ZModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZModPrime getInstance(BigInteger modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ZModPrime instance = ZModPrime.CACHE.get(modulus);
		if (instance == null) {
			instance = new ZModPrime(Prime.getInstance(modulus));
			ZModPrime.CACHE.put(modulus, instance);
		}
		return instance;
	}

	public static ZModPrime getInstance(final Prime modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ZModPrime instance = ZModPrime.CACHE.get(modulus.getValue());
		if (instance == null) {
			instance = new ZModPrime(modulus);
			ZModPrime.CACHE.put(modulus.getValue(), instance);
		}
		return instance;
	}

	public static ZModPrime getFirstInstance(int bitLength) {
		return ZModPrime.getInstance(Prime.getSmallestInstance(bitLength));
	}

}
