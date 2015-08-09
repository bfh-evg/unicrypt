/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.sequence;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class HashValueSequence
	   extends ByteArraySequence {

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);
	private static final byte BYTE_THREE = MathUtil.getByte(0x03);

	private final HashAlgorithm hashAlgorithm;
	private final ByteArray seed;
	private final int seedLength;
	private final BigIntegerToByteArray converter = BigIntegerToByteArray.getInstance();

	private HashValueSequence(HashAlgorithm hashAlgorithm, ByteArray seed) {
		super(Sequence.INFINITE);
		this.hashAlgorithm = hashAlgorithm;
		this.seed = seed;
		// values taken from Table 2 in NIST SP 800-90A
		if (this.hashAlgorithm.getBitLength() <= 256) {
			this.seedLength = 440;
		} else {
			this.seedLength = 888;
		}
	}

	private ByteArray hashDerivationFunction(ByteArray input) {
		int max = MathUtil.divideUp(this.seedLength, this.hashAlgorithm.getBitLength());
		ByteArray[] hashes = new ByteArray[max];
		for (int i = 0; i < max; i++) {
			ByteArray hashInput = ByteArray.getInstance(ByteArray.getInstance(MathUtil.getByte(i + 1)), MathUtil.getByteArray(seedLength), input);
			hashes[i] = this.hashAlgorithm.getHashValue(hashInput);
		}
		return ByteArray.getInstance(hashes).extractPrefix(seedLength / Byte.SIZE);
	}

	private ByteArray byteArraySum(ByteArray b1, ByteArray b2, ByteArray b3, long counter) {
		BigInteger i1 = this.converter.reconvert(b1);
		BigInteger i2 = this.converter.reconvert(b2);
		BigInteger i3 = this.converter.reconvert(b3);
		BigInteger sum = i1.add(i2).add(i3).add(BigInteger.valueOf(counter)).mod(MathUtil.powerOfTwo(this.seedLength));
		return this.converter.convert(sum).extractSuffix(seedLength / Byte.SIZE);
	}

	private ByteArray byteArrayAddOne(ByteArray b) {
		BigInteger sum = converter.reconvert(b).add(MathUtil.ONE).mod(MathUtil.powerOfTwo(this.seedLength));
		return this.converter.convert(sum).extractSuffix(seedLength / Byte.SIZE);
	}

	@Override
	public SequenceIterator<ByteArray> iterator() {

		return new SequenceIterator<ByteArray>() {

			private ByteArray value;
			private final ByteArray constant;
			private ByteArray data;
			private long counter;

			{
				// initialize internal state
				this.value = hashDerivationFunction(seed);
				this.constant = hashDerivationFunction(this.value.insert(BYTE_ZERO));
				this.counter = 1;
				this.data = this.value;
			}

			@Override
			protected void defaultUpdate() {
				// update operation is called after each call to next() or next(n)
				ByteArray hash = hashAlgorithm.getHashValue(this.value.insert(BYTE_THREE));
				this.value = byteArraySum(this.value, hash, this.constant, this.counter);
				this.counter++;
				this.data = this.value;
			}

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public ByteArray next() {
				ByteArray next = hashAlgorithm.getHashValue(this.data);
				this.data = byteArrayAddOne(this.data);
				return next;
			}

		};
	}

	public static HashValueSequence getInstance() {
		return HashValueSequence.getInstance(ByteArray.getInstance());
	}

	public static HashValueSequence getInstance(ByteArray seed) {
		return HashValueSequence.getInstance(HashAlgorithm.getInstance(), seed);
	}

	public static HashValueSequence getInstance(HashAlgorithm hashAlgorithm) {
		return HashValueSequence.getInstance(hashAlgorithm, ByteArray.getInstance());
	}

	public static HashValueSequence getInstance(HashAlgorithm hashAlgorithm, ByteArray seed) {
		if (hashAlgorithm == null || seed == null) {
			throw new IllegalArgumentException();
		}
		return new HashValueSequence(hashAlgorithm, seed);
	}

	public static ByteArray getSeed(ByteArray entropy, ByteArray nonce, ByteArray personalization) {
		return ByteArray.getInstance(entropy, nonce, personalization);
	}

}
