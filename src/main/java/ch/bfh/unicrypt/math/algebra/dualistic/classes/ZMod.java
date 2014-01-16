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
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGeneratorCounterMode;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the cyclic group Z_n = {0,...,n-1} with the operation of addition modulo n. Its identity
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
			 extends AbstractCyclicRing<ZModElement> {

	private BigInteger modulus;

	protected ZMod(final BigInteger modulus) {
		this.modulus = modulus;
	}

	/**
	 * Returns the modulus if this group.
	 * <p>
	 * @return The modulus
	 */
	public final BigInteger getModulus() {
		return this.modulus;
	}

	//
	// The following protected methods override the standard implementation from
	// various super-classes
	//
	@Override
	protected ZModElement standardSelfApply(Element element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().multiply(amount).mod(this.getModulus()));
	}

	@Override
	protected ZModElement standardPower(Element element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().modPow(amount, this.getModulus()));
	}

	@Override
	public boolean standardIsEquivalent(final Set set) {
		final ZMod zPlusTimesMod = (ZMod) set;
		return this.getModulus().equals(zPlusTimesMod.getModulus());
	}

	@Override
	protected boolean standardIsCompatible(Set set) {
		return (set instanceof ZMod);
	}

	@Override
	public String standardToStringContent() {
		return this.getModulus().toString();
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ZModElement abstractApply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().add(element2.getValue()).mod(this.getModulus()));
	}

	@Override
	protected ZModElement abstractGetIdentityElement() {
		return this.abstractGetElement(BigInteger.ZERO);
	}

	@Override
	protected ZModElement abstractInvert(ZModElement element) {
		return this.abstractGetElement(this.getModulus().subtract(element.getValue()).mod(this.getModulus()));
	}

	@Override
	protected ZModElement abstractMultiply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
	}

	@Override
	protected ZModElement abstractGetOne() {
		if (this.getModulus().equals(BigInteger.ONE)) {
			return this.abstractGetElement(BigInteger.ZERO);
		}
		return this.abstractGetElement(BigInteger.ONE);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.getModulus();
	}

	@Override
	protected ZModElement abstractGetElement(BigInteger value) {
		return new ZModElement(this, value);
	}

	@Override
	protected ZModElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		return this.abstractGetElement(randomGenerator.nextBigInteger(this.getModulus().subtract(BigInteger.ONE)));
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0;
	}

	@Override
	protected ZModElement abstractGetDefaultGenerator() {
		return this.abstractGetElement(BigInteger.ONE.mod(this.getModulus())); // mod is necessary for the trivial group Z_1
	}

	@Override
	protected boolean abstractIsGenerator(ZModElement element) {
		if (this.getModulus().equals(BigInteger.ONE)) {
			return true;
		}
		return MathUtil.areRelativelyPrime(element.getValue(), this.getModulus());
	}
	//
	// STATIC FACTORY METHODS
	//
	private static final Map<BigInteger, ZMod> instances = new HashMap<BigInteger, ZMod>();

	public static ZMod getInstance(final int modulus) {
		return ZMod.getInstance(BigInteger.valueOf(modulus));
	}

	/**
	 * Returns a the unique instance of this class for a given positive modulus.
	 * <p>
	 * @param modulus The modulus
	 * @return
	 * @throws IllegalArgumentException if {@literal modulus} is null, zero, or negative
	 */
	public static ZMod getInstance(final BigInteger modulus) {
		if ((modulus == null) || (modulus.compareTo(BigInteger.valueOf(2)) < 0)) {
			throw new IllegalArgumentException();
		}
		ZMod instance = ZMod.instances.get(modulus);
		if (instance == null) {
			instance = new ZMod(modulus);
			ZMod.instances.put(modulus, instance);
		}
		return instance;
	}

	public static ZMod getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGeneratorCounterMode.DEFAULT;
		}
		return ZMod.getInstance(randomGenerator.nextBigInteger(bitLength));
	}

	public static ZMod getRandomInstance(int bitLength) {
		return ZMod.getRandomInstance(bitLength, null);
	}

}
