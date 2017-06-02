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
import ch.bfh.unicrypt.helper.converter.classes.biginteger.BigIntegerToBigInteger;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

/**
 * This class implements the {@link Ring} Z_n = {0,...,n-1} with the operation of addition modulo n. Its identity
 * element is 0. Every integer in Z_n that is relatively prime to n is a generator of Z_n. The smallest such group is
 * Z_1 = {0}.
 * <p>
 * @see "Handbook of Applied Cryptography, Definition 2.113"
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZMod
	   extends AbstractCyclicRing<ZModElement, BigInteger> {

	private static final long serialVersionUID = 1L;
	private static final Cache<BigInteger, ZMod> CACHE = new Cache<>(Cache.SIZE_S);

	protected final BigInteger modulus;

	protected ZMod(final BigInteger modulus) {
		super(BigInteger.class);
		this.modulus = modulus;
	}

	/**
	 * Returns the modulus of this group.
	 * <p>
	 * @return The modulus
	 */
	public final BigInteger getModulus() {
		return this.modulus;
	}

	/**
	 * Returns {@code true} if this class contains a given integer value
	 * <p>
	 * @param integerValue The given integer
	 * @return {@code true} if this class contains
	 */
	public final boolean contains(long integerValue) {
		return this.contains(BigInteger.valueOf(integerValue));
	}

	public final ZModElement getElement(long integerValue) {
		return this.getElement(BigInteger.valueOf(integerValue));
	}

	@Override
	protected ZModElement defaultSelfApplyAlgorithm(ZModElement element, BigInteger posFactor) {
		return this.abstractGetElement(element.getValue().multiply(posFactor).mod(this.modulus));
	}

	@Override
	protected ZModElement defaultPowerAlgorithm(ZModElement element, BigInteger exponent) {
		return this.abstractGetElement(MathUtil.modExp(element.getValue(), exponent, this.modulus));
	}

	@Override
	protected String defaultToStringContent() {
		return this.modulus.toString();
	}

	@Override
	protected ZModElement abstractApply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().add(element2.getValue()).mod(this.modulus));
	}

	@Override
	protected ZModElement abstractGetIdentityElement() {
		return this.abstractGetElement(MathUtil.ZERO);
	}

	@Override
	protected ZModElement abstractInvert(ZModElement element) {
		return this.abstractGetElement(this.modulus.subtract(element.getValue()).mod(this.modulus));
	}

	@Override
	protected ZModElement abstractMultiply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.modulus));
	}

	@Override
	protected ZModElement abstractGetOne() {
		// mod is necessary for the trivial group Z_1
		return this.abstractGetElement(MathUtil.ONE.mod(this.modulus));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.modulus;
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() >= 0 && value.compareTo(this.modulus) < 0;
	}

	@Override
	protected ZModElement abstractGetElement(BigInteger value) {
		return new ZModElement(this, value);
	}

	@Override
	protected Converter<BigInteger, BigInteger> abstractGetBigIntegerConverter() {
		return BigIntegerToBigInteger.getInstance(0);
	}

	@Override
	protected Sequence<ZModElement> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		return randomByteSequence.getRandomBigIntegerSequence(this.modulus.subtract(MathUtil.ONE)).map(value -> abstractGetElement(value));
	}

	@Override
	protected ZModElement abstractGetDefaultGenerator() {
		// mod is necessary to handle the trivial group Z_1
		return this.abstractGetElement(MathUtil.ONE.mod(this.modulus));
	}

	@Override
	protected boolean abstractIsGenerator(ZModElement element) {
		return MathUtil.areRelativelyPrime(element.getValue(), this.modulus);
	}

	@Override
	public boolean abstractEquals(final Set set) {
		final ZMod zMod = (ZMod) set;
		return this.modulus.equals(zMod.modulus);
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.modulus.hashCode();
		return hash;
	}

	public static ZMod getInstance(final long modulus) {
		return ZMod.getInstance(BigInteger.valueOf(modulus));
	}

	/**
	 * Returns the unique instance of this class for a given positive modulus. The instance for every modulus is created
	 * only once.
	 * <p>
	 * @param modulus The modulus
	 * @return The instance of this class for the given modulus
	 */
	public static ZMod getInstance(final BigInteger modulus) {
		if (modulus == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (modulus.compareTo(MathUtil.ONE) < 0) {
			throw new UniCryptRuntimeException(ErrorCode.SET_CONSTRUCTION_FAILURE, modulus);
		}
		ZMod instance = ZMod.CACHE.get(modulus);
		if (instance == null) {
			instance = new ZMod(modulus);
			ZMod.CACHE.put(modulus, instance);
		}
		return instance;
	}

	/**
	 * Returns the unique instance of this class for a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The instance of this class for a random modulus
	 */
	public static ZMod getRandomInstance(int bitLength) {
		return ZMod.getRandomInstance(bitLength, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Returns the unique instance of this class for a given bit length and a given {@link RandomByteSequence}.
	 * <p>
	 * @param bitLength          The given bit length
	 * @param randomByteSequence The given RandomByteSequence
	 * @return The instance of this class for a random modulus
	 */
	public static ZMod getRandomInstance(int bitLength, RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (bitLength < 2) {
			throw new UniCryptRuntimeException(ErrorCode.SET_CONSTRUCTION_FAILURE, bitLength);
		}
		return ZMod.getInstance(randomByteSequence.getRandomBigIntegerSequence(bitLength).get());
	}

}
