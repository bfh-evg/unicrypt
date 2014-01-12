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
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayMonoid
	   extends AbstractConcatenativeMonoid<ByteArrayElement> {

	private final int blockLength;

	private ByteArrayMonoid(int blockLength) {
		this.blockLength = blockLength;
	}

	@Override
	public int getBlockLength() {
		return this.blockLength;
	}

	public final ByteArrayElement getElement(final byte[] bytes) {
		if (bytes == null || bytes.length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(bytes);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length) {
		return this.getRandomElement(length, null);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length, RandomGenerator randomGenerator) {
		if (length < 0 || length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = randomGenerator.nextByte();
		}
		return this.standardGetElement(bytes);
	}

	protected ByteArrayElement standardGetElement(byte[] bytes) {
		return new ByteArrayElement(this, bytes);
	}

	@Override
	protected ByteArrayElement abstractGetElement(BigInteger value) {
		int blockLength = this.getBlockLength();
		LinkedList<Byte> byteList = new LinkedList<Byte>();
		BigInteger byteSize = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger blockSize = BigInteger.valueOf(1 << (Byte.SIZE * blockLength));
		while (!value.equals(BigInteger.ZERO)) {
			value = value.subtract(BigInteger.ONE);
			BigInteger remainder = value.mod(blockSize);
			for (int i = 0; i < blockLength; i++) {
				byteList.addFirst(remainder.mod(byteSize).byteValue());
				remainder = remainder.divide(byteSize);
			}
			value = value.divide(blockSize);
		}
		return this.standardGetElement(ArrayUtil.byteListToByteArray(byteList));
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ByteArrayElement abstractGetIdentityElement() {
		return this.standardGetElement(new byte[]{});
	}

	@Override
	protected ByteArrayElement abstractApply(ByteArrayElement element1, ByteArrayElement element2) {
		byte[] bytes1 = element1.getByteArray();
		byte[] bytes2 = element2.getByteArray();
		byte[] result = Arrays.copyOf(bytes1, bytes1.length + bytes2.length);
		System.arraycopy(bytes2, 0, result, bytes2.length, bytes2.length);
		return this.standardGetElement(result);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	@Override
	protected ByteArrayElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() >= 0;
	}
	//
	// STATIC FACTORY METHODS
	//
	private static final Map<Integer, ByteArrayMonoid> instances = new HashMap<Integer, ByteArrayMonoid>();

	public static ByteArrayMonoid getInstance() {
		return ByteArrayMonoid.getInstance(1);
	}

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @param blockLength
	 * @return The singleton object of this class
	 */
	public static ByteArrayMonoid getInstance(int blockLength) {
		if (blockLength < 1) {
			throw new IllegalArgumentException();
		}
		ByteArrayMonoid instance = ByteArrayMonoid.instances.get(Integer.valueOf(blockLength));
		if (instance == null) {
			instance = new ByteArrayMonoid(blockLength);
			ByteArrayMonoid.instances.put(Integer.valueOf(blockLength), instance);
		}
		return instance;
	}

}
