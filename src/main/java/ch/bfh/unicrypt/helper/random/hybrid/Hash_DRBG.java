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
package ch.bfh.unicrypt.helper.random.hybrid;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteArraySequenceIterator;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;
import java.math.BigInteger;

/**
 * This class is an implementation of the NIST standard Hash_DRBG as described in NIST SP 800-90A "Recommendation for
 * Random Number Generation Using Deterministic Random Bit Generators" (Section 10.1.1). Instances of this class
 * generate a deterministic sequence of random byte arrays for a given seed and the given hash algorithm.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class Hash_DRBG
	   extends HybridRandomByteArraySequence {

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);
	private static final byte BYTE_THREE = MathUtil.getByte(0x03);

	private final HashAlgorithm hashAlgorithm;
	private final int minEntropy;
	private final int seedLength;
	private final ByteArrayToBigInteger converter;

	private Hash_DRBG(HashAlgorithm hashAlgorithm, NonDeterministicRandomByteSequence entropySource,
		   ByteArray personalizationString) {
		super(entropySource, personalizationString);
		this.hashAlgorithm = hashAlgorithm;
		// The minimal entropy is equal to 3/2 times the supported security strength of the hash function
		// See "8.6.7 Nonce" in NIST SP 800-90A and "Table 3" in NIST SP 800-57 Part 1
		this.minEntropy = 3 * this.hashAlgorithm.getBitLength() / 2;
		// values taken from Table 2 in NIST SP 800-90A
		if (this.hashAlgorithm.getBitLength() <= 256) {
			this.seedLength = 440;
		} else {
			this.seedLength = 888;
		}
		this.converter = ByteArrayToBigInteger.getInstance(this.seedLength / Byte.SIZE);
	}

	private ByteArray hashDerivationFunction(ByteArray input) {
		int max = MathUtil.divideUp(this.seedLength, this.hashAlgorithm.getBitLength());
		ByteArray[] hashes = new ByteArray[max];
		for (int i = 0; i < max; i++) {
			ByteArray hashInput = ByteArray.getInstance(ByteArray.getInstance(MathUtil.getByte(i + 1)), MathUtil.
														getByteArray(seedLength), input);
			hashes[i] = this.hashAlgorithm.getHashValue(hashInput);
		}
		return ByteArray.getInstance(hashes).extractPrefix(this.seedLength / Byte.SIZE);
	}

	// b1.getLength() = b3.getLength() = seedLength/Byte.SIZE, b3.getLength() <= seedLength/Byte.SIZE
	private ByteArray byteArraySum(ByteArray b1, ByteArray b2, ByteArray b3, long counter) {
		BigInteger i1 = this.converter.convert(b1);
		BigInteger i2 = this.converter.convert(b2.addPrefix(this.seedLength / Byte.SIZE - b2.getLength()));
		BigInteger i3 = this.converter.convert(b3);
		BigInteger sum = i1.add(i2).add(i3).add(BigInteger.valueOf(counter)).mod(MathUtil.powerOfTwo(this.seedLength));
		return this.converter.reconvert(sum);
	}

	// b.getLength() = seedLength/Byte.SIZE
	private ByteArray byteArrayAddOne(ByteArray b) {
		BigInteger i = converter.convert(b);
		BigInteger iPlusOne = i.add(MathUtil.ONE).mod(MathUtil.powerOfTwo(this.seedLength));
		return this.converter.reconvert(iPlusOne);
	}

	@Override
	public RandomByteArraySequenceIterator iterator() {

		return new RandomByteArraySequenceIterator() {

			private ByteArray value = hashDerivationFunction(entropySource.next(minEntropy / Byte.SIZE).append(
				   personalizationString));
			private final ByteArray constant = hashDerivationFunction(this.value.insert(BYTE_ZERO));
			private ByteArray data = this.value;
			private long counter = 1;

			@Override
			protected void updateAfter() {
				ByteArray hash = hashAlgorithm.getHashValue(this.value.insert(BYTE_THREE));
				this.value = byteArraySum(this.value, hash, this.constant, this.counter);
				this.counter++;
				this.data = this.value;
			}

			@Override
			public ByteArray abstractNext() {
				ByteArray next = hashAlgorithm.getHashValue(this.data);
				this.data = byteArrayAddOne(this.data);
				return next;
			}

		};
	}

	/**
	 * Returns a new factory for creating Hash_DRBG instances using the default hash algorithm.
	 * <p>
	 * @return The new Hash_DRBG factory
	 */
	public static HybridRandomByteArraySequence.Factory getFactory() {
		return Hash_DRBG.getFactory(HashAlgorithm.getInstance());
	}

	/**
	 * Returns a new factory for creating Hash_DRBG instances using the given hash algorithm.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new Hash_DRBG factory
	 */
	public static HybridRandomByteArraySequence.Factory getFactory(final HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		return new HybridRandomByteArraySequence.Factory() {

			@Override
			protected HybridRandomByteArraySequence abstractGetInstance(
				   NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString) {
				return new Hash_DRBG(hashAlgorithm, entropySource, personalizationString);
			}

		};
	}

}
