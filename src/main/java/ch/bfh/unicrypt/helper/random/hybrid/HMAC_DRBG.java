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
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteArraySequenceIterator;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;

/**
 * This class is an implementation of the NIST standard HMAC_DRBG as described in NIST SP 800-90A "Recommendation for
 * Random Number Generation Using Deterministic Random Bit Generators" (Section 10.1.2). Instances of this class
 * generate a deterministic sequence of random byte arrays for a given seed and the given hash algorithm.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class HMAC_DRBG
	   extends HybridRandomByteArraySequence {

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);
	private static final byte BYTE_ONE = MathUtil.getByte(0x01);

	private final HashAlgorithm hashAlgorithm;
	private final int minEntropy;

	private HMAC_DRBG(HashAlgorithm hashAlgorithm, NonDeterministicRandomByteSequence entropySource,
		   ByteArray personalizationString) {
		super(entropySource, personalizationString);
		this.hashAlgorithm = hashAlgorithm;
		// The minimal entropy is equal to 3/2 times the supported security strength of the hash function
		// See "8.6.7 Nonce" in NIST SP 800-90A and "Table 3" in NIST SP 800-57 Part 1
		this.minEntropy = 3 * this.hashAlgorithm.getBitLength() / 2;
	}

	@Override
	public RandomByteArraySequenceIterator iterator() {

		return new RandomByteArraySequenceIterator() {

			private ByteArray key = ByteArray.getInstance(BYTE_ZERO, hashAlgorithm.getByteLength());
			private ByteArray value = ByteArray.getInstance(BYTE_ONE, hashAlgorithm.getByteLength());

			{
				// initialize internal state
				this.stateUpdate(entropySource.next(minEntropy / Byte.SIZE).append(personalizationString));
			}

			@Override
			protected void updateAfter() {
				// update operation is called after each call to next() or next(n)
				this.stateUpdate(ByteArray.getInstance());
			}

			protected void stateUpdate(ByteArray data) {
				this.key = hashAlgorithm.getHashValue(this.value.add(BYTE_ZERO).append(data), this.key);
				this.value = hashAlgorithm.getHashValue(this.value, this.key);
				if (!data.isEmpty()) {
					this.key = hashAlgorithm.getHashValue(this.value.add(BYTE_ONE).append(data), this.key);
					this.value = hashAlgorithm.getHashValue(this.value, this.key);
				}
			}

			@Override
			public ByteArray abstractNext() {
				return this.value = hashAlgorithm.getHashValue(this.value, this.key);
			}

		};
	}

	/**
	 * Returns a new factory for creating HMAC_DRBG instances using the default hash algorithm.
	 * <p>
	 * @return The new HMAC_DRBG factory
	 */
	public static HybridRandomByteArraySequence.Factory getFactory() {
		return HMAC_DRBG.getFactory(HashAlgorithm.getInstance());
	}

	/**
	 * Returns a new factory for creating HMAC_DRBG instances using the given hash algorithm.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new HMAC_DRBG factory
	 */
	public static HybridRandomByteArraySequence.Factory getFactory(final HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		return new HybridRandomByteArraySequence.Factory() {

			@Override
			protected HybridRandomByteArraySequence abstractGetInstance(
				   NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString) {
				return new HMAC_DRBG(hashAlgorithm, entropySource, personalizationString);
			}

		};
	}

}
