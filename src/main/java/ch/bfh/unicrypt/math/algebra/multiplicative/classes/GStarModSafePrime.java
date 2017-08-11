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
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.cache.Cache;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.prime.Prime;
import ch.bfh.unicrypt.helper.prime.SafePrime;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class GStarModSafePrime
	   extends GStarModPrime {

	private static final long serialVersionUID = 1L;
	private final static Cache<BigInteger, GStarModSafePrime> CACHE = new Cache<>(Cache.SIZE_S);

	protected GStarModSafePrime(SafePrime modulus) {
		super(modulus, Prime.getInstance(modulus.getValue().subtract(MathUtil.ONE).divide(MathUtil.TWO)));
	}

	@Override
	protected String defaultToStringContent() {
		return this.getModulus().toString();
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() > 0
			   && value.compareTo(this.modulus) < 0
			   && MathUtil.isQuadraticResidue(value, this.modulus);
	}

	public static GStarModSafePrime getInstance(long modulus) {
		return GStarModSafePrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static GStarModSafePrime getInstance(BigInteger modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		GStarModSafePrime instance = GStarModSafePrime.CACHE.get(modulus);
		if (instance == null) {
			instance = new GStarModSafePrime(SafePrime.getInstance(modulus));
			GStarModSafePrime.CACHE.put(modulus, instance);
		}
		return instance;
	}

	public static GStarModSafePrime getInstance(SafePrime modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		GStarModSafePrime instance = GStarModSafePrime.CACHE.get(modulus.getValue());
		if (instance == null) {
			instance = new GStarModSafePrime(modulus);
			GStarModSafePrime.CACHE.put(modulus.getValue(), instance);
		}
		return instance;
	}

}
