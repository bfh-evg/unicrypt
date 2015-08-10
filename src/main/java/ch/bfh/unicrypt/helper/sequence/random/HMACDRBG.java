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
package ch.bfh.unicrypt.helper.sequence.random;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.ByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;

/**
 * NIST SP 800-90
 * <p>
 * @author R. Haenni
 */
public class HMACDRBG
	   extends Sequence<ByteArray> {

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);
	private static final byte BYTE_ONE = MathUtil.getByte(0x01);

	final HashAlgorithm hashAlgorithm;
	final ByteArray seed;

	private HMACDRBG(HashAlgorithm hashAlgorithm, ByteArray seed) {
		super(Sequence.INFINITE);
		this.hashAlgorithm = hashAlgorithm;
		this.seed = seed;
	}

	public final ByteSequence getByteSequence() {
		return ByteSequence.getInstance(this);
	}

	@Override
	public SequenceIterator<ByteArray> iterator() {

		return new SequenceIterator<ByteArray>() {

			private ByteArray key = ByteArray.getInstance(BYTE_ZERO, hashAlgorithm.getByteLength());
			private ByteArray value = ByteArray.getInstance(BYTE_ONE, hashAlgorithm.getByteLength());

			{
				// initialize internal state
				this.stateUpdate(seed);
			}

			@Override
			protected void defaultUpdate() {
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
			public boolean hasNext() {
				return true;
			}

			@Override
			public ByteArray abstractNext() {
				return this.value = hashAlgorithm.getHashValue(this.value, this.key);
			}

		};
	}

	public static HMACDRBG getInstance() {
		return HMACDRBG.getInstance(ByteArray.getInstance());
	}

	public static HMACDRBG getInstance(ByteArray seed) {
		return HMACDRBG.getInstance(HashAlgorithm.getInstance(), seed);
	}

	public static HMACDRBG getInstance(HashAlgorithm hashAlgorithm) {
		return HMACDRBG.getInstance(hashAlgorithm, ByteArray.getInstance());
	}

	public static HMACDRBG getInstance(HashAlgorithm hashAlgorithm, ByteArray seed) {
		if (hashAlgorithm == null || seed == null) {
			throw new IllegalArgumentException();
		}
		return new HMACDRBG(hashAlgorithm, seed);
	}

	public static ByteArray getSeed(ByteArray entropy, ByteArray nonce, ByteArray personalization) {
		return ByteArray.getInstance(entropy, nonce, personalization);
	}

}
