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

import ch.bfh.unicrypt.crypto.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTreeLeaf;
import ch.bfh.unicrypt.math.helper.numerical.NaturalNumber;
import ch.bfh.unicrypt.math.helper.numerical.ResidueClass;
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
	   extends AbstractCyclicRing<ZModElement, ResidueClass> {

	protected final BigInteger modulus;

	protected ZMod(final BigInteger modulus) {
		super(ResidueClass.class);
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

	public final boolean contains(int integerValue) {
		return this.contains(ResidueClass.getInstance(BigInteger.valueOf(integerValue), this.modulus));
	}

	public final boolean contains(BigInteger integerValue) {
		return this.contains(ResidueClass.getInstance(integerValue, this.modulus));
	}

	public final ZModElement getElement(int integerValue) {
		return this.getElement(ResidueClass.getInstance(BigInteger.valueOf(integerValue), this.modulus));
	}

	public final ZModElement getElement(BigInteger integerValue) {
		return this.getElement(ResidueClass.getInstance(integerValue, this.modulus));
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	protected ZModElement defaultSelfApply(ZModElement element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().multiply(ResidueClass.getInstance(amount, this.modulus)));
	}

	@Override
	protected ZModElement defaultPower(ZModElement element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().power(NaturalNumber.getInstance(amount)));
	}

	@Override
	public String defaultToStringValue() {
		return this.modulus.toString();
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ZModElement abstractApply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().add(element2.getValue()));
	}

	@Override
	protected ZModElement abstractGetIdentityElement() {
		return this.abstractGetElement(ResidueClass.getInstance(BigInteger.ZERO, this.modulus));
	}

	@Override
	protected ZModElement abstractInvert(ZModElement element) {
		return this.abstractGetElement(element.getValue().negate());
	}

	@Override
	protected ZModElement abstractMultiply(ZModElement element1, ZModElement element2) {
		return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
	}

	@Override
	protected ZModElement abstractGetOne() {
		// mod is necessary for the trivial group Z_1
		return this.abstractGetElement(ResidueClass.getInstance(BigInteger.ONE.mod(this.modulus), this.modulus));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.modulus;
	}

	@Override
	protected boolean abstractContains(ResidueClass value) {
		return this.modulus.equals(value.getModulus());
	}

	@Override
	protected ZModElement abstractGetElement(ResidueClass value) {
		return new ZModElement(this, value);
	}

	@Override
	protected ZModElement abstractGetElementFrom(BigInteger value) {
		if (value.compareTo(this.modulus) >= 0) {
			return null; // no such element
		}
		return this.abstractGetElement(ResidueClass.getInstance(value, this.modulus));
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(ResidueClass value) {
		return value.getBigInteger();
	}

	@Override
	protected ZModElement abstractGetElementFrom(ByteTree byteTree) {
		if (byteTree.isLeaf()) {
			BigInteger bigInteger = ((ByteTreeLeaf) byteTree).convertToBigInteger();
			if (this.contains(bigInteger)) {
				return this.abstractGetElement(ResidueClass.getInstance(bigInteger, this.modulus));
			}
		}
		// no such element
		return null;
	}

	@Override
	protected ByteTree abstractGetByteTreeFrom(ResidueClass value) {
		return ByteTree.getInstance(value.getBigInteger());
	}

	@Override
	protected ZModElement abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		return this.abstractGetElement(ResidueClass.getInstance(randomByteSequence.getRandomNumberGenerator().nextBigInteger(this.modulus.subtract(BigInteger.ONE)), this.modulus));
	}

	@Override
	protected ZModElement abstractGetDefaultGenerator() {
		// mod is necessary for the trivial group Z_1
		return this.abstractGetElement(ResidueClass.getInstance(BigInteger.ONE.mod(this.modulus), this.modulus));
	}

	@Override
	protected boolean abstractIsGenerator(ZModElement element) {
		if (this.modulus.equals(BigInteger.ONE)) {
			return true;
		}
		return MathUtil.areRelativelyPrime(element.getValue().getBigInteger(), this.modulus);
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

	public static ZMod getRandomInstance(int bitLength, RandomByteSequence randomByteSequence) {
		if (bitLength < 2 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return ZMod.getInstance(randomByteSequence.getRandomNumberGenerator().nextBigInteger(bitLength));
	}

	public static ZMod getRandomInstance(int bitLength) {
		return ZMod.getRandomInstance(bitLength, HybridRandomByteSequence.getInstance());
	}

}
