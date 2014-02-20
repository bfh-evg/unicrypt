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

import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.helper.bytetree.ByteTreeLeaf;
import ch.bfh.unicrypt.helper.numerical.WholeNumber;
import ch.bfh.unicrypt.math.MathUtil;
import java.math.BigInteger;

/**
 * /**
 * This class implements the additive cyclic group of (positive and negative) integers with infinite order. Its identity
 * element is 0, and there are exactly two generators, namely 1 and -1. To invert an element, it is multiplied with -1.
 * <p>
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class Z
	   extends AbstractCyclicRing<ZElement, WholeNumber> {

	public Z() {
		super(WholeNumber.class);
	}

	public final boolean contains(int integerValue) {
		return this.contains(WholeNumber.getInstance(integerValue));
	}

	public final boolean contains(BigInteger integerValue) {
		return this.contains(WholeNumber.getInstance(integerValue));
	}

	public final ZElement getElement(int integerValue) {
		return this.getElement(WholeNumber.getInstance(integerValue));
	}

	public final ZElement getElement(BigInteger integerValue) {
		return this.getElement(WholeNumber.getInstance(integerValue));
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	protected ZElement defaultSelfApply(ZElement element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().multiply(WholeNumber.getInstance(amount)));
	}

	@Override
	protected ZElement defaultGetRandomGenerator(final RandomByteSequence randomByteSequence) {
		if (randomByteSequence.getRandomNumberGenerator().nextBoolean()) {
			return this.getDefaultGenerator();
		}
		return this.getDefaultGenerator().invert();
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ZElement abstractApply(ZElement element1, ZElement element2) {
		return this.abstractGetElement(element1.getValue().add(element2.getValue()));
	}

	@Override
	protected ZElement abstractGetIdentityElement() {
		return this.abstractGetElement(WholeNumber.ZERO.ZERO);
	}

	@Override
	protected ZElement abstractInvert(ZElement element) {
		return this.abstractGetElement(element.getValue().negate());
	}

	@Override
	protected ZElement abstractMultiply(ZElement element1, ZElement element2) {
		return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
	}

	@Override
	protected ZElement abstractGetOne() {
		return this.abstractGetElement(WholeNumber.ONE);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Group.INFINITE_ORDER;
	}

	@Override
	protected boolean abstractContains(WholeNumber value) {
		return true;
	}

	@Override
	protected ZElement abstractGetElement(WholeNumber value) {
		return new ZElement(this, value);
	}

	@Override
	protected ZElement abstractGetElementFrom(BigInteger value) {
		return this.abstractGetElement(WholeNumber.getInstance(MathUtil.unfold(value)));
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(WholeNumber value) {
		return MathUtil.fold(value.getBigInteger());
	}

	@Override
	protected ZElement abstractGetElementFrom(ByteTree byteTree) {
		if (byteTree.isLeaf()) {
			BigInteger bigInteger = ((ByteTreeLeaf) byteTree).convertToBigInteger();
			if (this.contains(bigInteger)) {
				return this.abstractGetElement(WholeNumber.getInstance(bigInteger));
			}
		}
		// no such element
		return null;
	}

	@Override
	protected ByteTree abstractGetByteTreeFrom(WholeNumber value) {
		return ByteTree.getInstance(value.getBigInteger());
	}

	@Override
	protected ZElement abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ZElement abstractGetDefaultGenerator() {
		return this.abstractGetElement(WholeNumber.ONE);
	}

	@Override
	protected boolean abstractIsGenerator(final ZElement element) {
		return element.getValue().getBigInteger().abs().equals(BigInteger.ONE);
	}

	@Override
	protected boolean abstractEquals(Set set) {
		return true;
	}

	@Override
	protected int abstractHashCode() {
		return 1;
	}

	//
	// STATIC FACTORY METHODS
	//
	private static Z instance = new Z();

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @return The singleton object of this class
	 */
	public static Z getInstance() {
		if (Z.instance == null) {
			Z.instance = new Z();
		}
		return Z.instance;
	}

}
