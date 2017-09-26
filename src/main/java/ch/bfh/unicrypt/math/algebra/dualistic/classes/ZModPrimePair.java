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
import ch.bfh.unicrypt.helper.prime.Prime;
import ch.bfh.unicrypt.helper.prime.PrimePair;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrimePair;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class ZModPrimePair
	   extends ZMod {

	private static final Cache<BigInteger, ZModPrimePair> CACHE = new Cache<>(Cache.SIZE_S);

	private final PrimePair primePair;

	protected ZModPrimePair(PrimePair primePair) {
		super(primePair.getValue());
		this.primePair = primePair;
	}

	public PrimePair getPrimePair() {
		return this.primePair;
	}

	@Override
	public ZModPrimePair getZModOrder() {
		return ZModPrimePair.getInstance(this.getPrimePair());
	}

	@Override
	public ZStarModPrimePair getZStarModOrder() {
		return ZStarModPrimePair.getInstance(this.getPrimePair());
	}

	public static ZModPrimePair getInstance(final long prime1, final long prime2) {
		return ZModPrimePair.getInstance(BigInteger.valueOf(prime1), BigInteger.valueOf(prime2));
	}

	public static ZModPrimePair getInstance(BigInteger prime1, BigInteger prime2) {
		if (prime1 == null || prime2 == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, prime1, prime2);
		}
		BigInteger value = prime1.multiply(prime2);
		ZModPrimePair instance = ZModPrimePair.CACHE.get(value);
		if (instance == null) {
			instance = new ZModPrimePair(PrimePair.getInstance(prime1, prime2));
			ZModPrimePair.CACHE.put(value, instance);
		}
		return instance;
	}

	public static ZModPrimePair getInstance(Prime prime1, Prime prime2) {
		return new ZModPrimePair(PrimePair.getInstance(prime1, prime2));
	}

	public static ZModPrimePair getInstance(final PrimePair primePair) {
		if (primePair == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ZModPrimePair instance = ZModPrimePair.CACHE.get(primePair.getValue());
		if (instance == null) {
			instance = new ZModPrimePair(primePair);
			ZModPrimePair.CACHE.put(primePair.getValue(), instance);
		}
		return instance;
	}

}
