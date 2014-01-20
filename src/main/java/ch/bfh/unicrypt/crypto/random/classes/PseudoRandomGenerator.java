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
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * This PseudoRandomGenerator creates the hash value of the seed and stores this internally as a ByteArrayElement. The
 * hash will be done according to the given hashMethod. Then the internal counter will be created as another
 * ByteArrayElement. These two byteArrayElement will be hashed as in @see AbstractElement#getHashValue(HashMethod
 * hashMethod); The resulting bytes will be used for pseudoRandomness
 * <p>
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PseudoRandomGenerator
	   extends AbstractRandomGenerator {

	public static final Element DEFAULT_SEED = Z.getInstance().getElement(0);
	public static final PseudoRandomGenerator DEFAULT = getInstance();

	private final HashMethod hashMethod;
	private final Element seed;
	private final ByteArray hashedSeed;
	private int counter;
	// TODO: Better with ByteArrayOutputStream
	private byte[] randomByteBuffer;
	private int randomByteBufferPosition;

	// Random random;
	protected PseudoRandomGenerator(HashMethod hashMethod, final Element seed) {
		this.hashMethod = hashMethod;
		this.seed = seed;
		//The following lines are needed in order to speed up calculation of randomBytes. @see#fillRandomByteBuffer
		this.randomByteBuffer = new byte[hashMethod.getLength()];
		this.counter = -1;
		hashedSeed = this.seed.getHashValue(hashMethod).getValue();
		reset();
	}

	protected byte[] getRandomByteBuffer(int counter) {
		//Even though the following is the nice way to program it with unicrypt, it is too expensive. Reason: If the first part of a pair is a big tuple, it has to be hashed each time... Reprogram?!
		//this.digestBytes=Pair.getInstance(seed,Z.getInstance().getElement(counter)).getHashValue(hashMethod).getByteArray();
		//-->This is, why the following implementation exists.
		MessageDigest digest = hashMethod.getMessageDigest();
		digest.reset();
		return digest.digest(hashedSeed.concatenate(ByteArray.getInstance(BigInteger.valueOf(counter).toByteArray())).getBytes());
//		return digest.digest(hashedSeed.concatenate(ByteArrayMonoid.getInstance().getElement(counter).getByteArray()).getBytes());

		//this.digestBytes = ByteArrayMonoid.getInstance().apply(hashedSeedElement, Z.getInstance().getElement(counter).getHashValue(hashMethod).getByteArrayElement()).getHashValue(hashMethod).getByteArray();
		//this.digestBytes = ByteArrayMonoid.getInstance().apply(seedByteArray, ByteArrayMonoid.getInstance().getElement(counter)).getHashValue(hashMethod).getByteArray();
		//System.out.println("Pair: " + pair + " digestPosition: " + digestBytesPosition + "  " + Arrays.toString(this.digestBytes));
	}

	public HashMethod getHashMethod() {
		return hashMethod;
	}

	//It would be better to only get the seedHash! (Seed should not be stored)
	public Element getSeed() {
		return this.seed;
	}

	public int getCounter() {
		return this.counter;
	}

	protected void reset() {
		setCounter(0);
	}

	protected boolean isReset() {
		return counter == 0 && this.randomByteBufferPosition == 0;
	}

	protected void setCounter(final int counter) {
		//Do not re-calculate the hash if it is only the digestBytesPosition to be reset to 0
		if (this.counter != counter) {
			this.counter = counter;
			this.randomByteBuffer = getRandomByteBuffer(counter);
		}
		this.randomByteBufferPosition = 0;

	}

	@Override
	protected boolean abstractNextBoolean() {
		return nextBytes(1)[0] % 2 == 1;
	}

	/**
	 * Counter goes up after digest.length bytes, after initialization with sha256, 32bytes are ready to be read and
	 * counter is at 0 after having read 32 bytes counter jumps to 1 and another 32 bytes are ready to be read
	 * <p>
	 * @param length
	 * @return
	 */
	@Override
	protected byte[] abstractNextBytes(int length) {
		byte[] randomBytes = new byte[length];
		int randomBytesPosition = 0;
		while (randomBytesPosition < length) {
			int amount = Math.min((length - randomBytesPosition), (randomByteBuffer.length - randomByteBufferPosition));
			System.arraycopy(randomByteBuffer, randomByteBufferPosition, randomBytes, randomBytesPosition, amount);
			randomBytesPosition += amount;
			randomByteBufferPosition += amount;
			if (randomByteBufferPosition == randomByteBuffer.length) {
				setCounter(getCounter() + 1);
			}
		}
		return randomBytes;
	}

	@Override
	protected int abstractNextInteger(int maxValue) {
		//This is a slow implementation.
		return nextBigInteger(BigInteger.valueOf(maxValue)).intValue();
	}

	/**
	 * MSB is always set
	 * <p>
	 * @param bitLength
	 * @return
	 */
	@Override
	protected BigInteger abstractNextBigInteger(int bitLength) {
		return internalNextBigInteger(bitLength, true);
	}

	private BigInteger internalNextBigInteger(int bitLength, boolean isMsbSet) {
		if (bitLength < 1) {
			return BigInteger.ZERO;
		}
		int amountOfBytes = (int) Math.ceil(bitLength / 8.0);
		byte[] bytes = nextBytes(amountOfBytes);

		int shift = 8 - (bitLength % 8);
		if (shift == 8) {
			shift = 0;
		}
		if (isMsbSet) {
			bytes[0] = (byte) (((bytes[0] & 0xFF) | 0x80) >> shift);
		} else {
			bytes[0] = (byte) ((bytes[0] & 0xFF) >> shift);

		}
		return new BigInteger(1, bytes);
	}

	@Override
	protected BigInteger abstractNextBigInteger(BigInteger maxValue) {
		BigInteger randomValue;
		int bitLength = maxValue.bitLength();
		do {
			randomValue = internalNextBigInteger(bitLength, false);
		} while (randomValue.compareTo(maxValue) > 0);
		return randomValue;
	}

	/**
	 * MSB always set
	 * <p>
	 * @param bitLength
	 * @return
	 */
	@Override
	protected BigInteger abstractNextPrime(int bitLength) {
		BigInteger bigInteger = null;
		do {
			bigInteger = internalNextBigInteger(bitLength, true);
		} while (!bigInteger.isProbablePrime(MathUtil.NUMBER_OF_PRIME_TESTS));
		return bigInteger;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.hashMethod != null ? this.hashMethod.hashCode() : 0);
		hash = 17 * hash + (hashedSeed != null ? hashedSeed.hashCode() : 0);
		hash = 17 * hash + this.counter;
		hash = 17 * hash + this.randomByteBufferPosition;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PseudoRandomGenerator other = (PseudoRandomGenerator) obj;
		if (this.hashMethod != other.hashMethod && (this.hashMethod == null || !this.hashMethod.equals(other.hashMethod))) {
			return false;
		}
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		if (this.counter != other.counter) {
			return false;
		}
		if (this.randomByteBufferPosition != other.randomByteBufferPosition) {
			return false;
		}
		return true;
	}

	public static PseudoRandomGenerator getInstance() {
		return PseudoRandomGenerator.getInstance(HashMethod.DEFAULT, DEFAULT_SEED);
	}

	public static PseudoRandomGenerator getInstance(HashMethod hashMethod) {
		return new PseudoRandomGenerator(hashMethod, DEFAULT_SEED);
	}

	public static PseudoRandomGenerator getInstance(Element seed) {
		return new PseudoRandomGenerator(HashMethod.DEFAULT, seed);
	}

	public static PseudoRandomGenerator getInstance(HashMethod hashMethod, Element seed) {
		if (seed == null) {
			throw new IllegalArgumentException();
		}
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomGenerator(hashMethod, seed);
	}

}
