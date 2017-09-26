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
import ch.bfh.unicrypt.helper.prime.Prime;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;

/**
 * Inherits from ZStarMod but not from AbstractCyclicGroup. Therefore, the additional methods from AbstractCyclicGroup
 * need to be implemented.
 * <p>
 * @author R. Haenni
 */
public class ZStarModPrime
	   extends ZStarMod
	   implements MultiplicativeCyclicGroup<BigInteger> {

	private static final long serialVersionUID = 1L;
	private final static Cache<BigInteger, ZStarModPrime> CACHE = new Cache<>(Cache.SIZE_S);

	protected ZStarModPrime(Prime modulus) {
		super(modulus);
	}

	@Override
	public ZStarModElement getDefaultGenerator() {
		// finding a generator requires the factorization of p-1
		// given the factorization of p-1, finding the generator is still difficuclt
		throw new UniCryptRuntimeException(ErrorCode.NOT_YET_IMPLEMENTED, this);
	}

	@Override
	public final Sequence<ZStarModElement> getIndependentGenerators() {
		return this.getIndependentGenerators(DeterministicRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<ZStarModElement> getIndependentGenerators(
		   DeterministicRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	@Override
	public final ZStarModElement getRandomGenerator() {
		return this.getRandomGenerators().get();
	}

	@Override
	public final ZStarModElement getRandomGenerator(RandomByteSequence randomByteSequence) {
		return this.getRandomGenerators(randomByteSequence).get();
	}

	@Override
	public final Sequence<ZStarModElement> getRandomGenerators() {
		return this.getRandomGenerators(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<ZStarModElement> getRandomGenerators(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected Sequence<ZStarModElement> defaultGetRandomGenerators(RandomByteSequence randomByteSequence) {
		return this.abstractGetRandomElements(randomByteSequence).filter(value -> isGenerator(value));
	}

	@Override
	public boolean isGenerator(Element element) {
		throw new UniCryptRuntimeException(ErrorCode.NOT_YET_IMPLEMENTED, this);
	}

	public static ZStarModPrime getInstance(final long modulus) {
		return ZStarModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZStarModPrime getInstance(final BigInteger modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ZStarModPrime instance = ZStarModPrime.CACHE.get(modulus);
		if (instance == null) {
			instance = new ZStarModPrime(Prime.getInstance(modulus));
			ZStarModPrime.CACHE.put(modulus, instance);
		}
		return instance;
	}

	public static ZStarModPrime getInstance(final Prime modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ZStarModPrime instance = ZStarModPrime.CACHE.get(modulus.getValue());
		if (instance == null) {
			instance = new ZStarModPrime(modulus);
			ZStarModPrime.CACHE.put(modulus.getValue(), instance);
		}
		return instance;
	}

}
