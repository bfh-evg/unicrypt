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
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;

/**
 * NIST Special Publication 800-132: "Recommendation for Password-Based Key Derivation" RFC 2898 "PKCS #5:
 * Password-Based Cryptography Specification, Version 2.0"
 * <p>
 * @author rolfhaenni
 */
public class PBKD2ByteArraySequence
	   extends ByteArraySequence {

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);
	private static final int DEFUALT_ROUNDS = 4096;

	private final HashAlgorithm hashAlgorithm;
	private final ByteArray password;
	private final ByteArray salt;
	private final int rounds;

	private PBKD2ByteArraySequence(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt, int rounds) {
		super(Sequence.INFINITE);
		this.hashAlgorithm = hashAlgorithm;
		this.password = password;
		this.salt = salt;
		this.rounds = rounds;
	}

	@Override
	public SequenceIterator<ByteArray> iterator() {

		return new SequenceIterator<ByteArray>() {

			private int counter = 1;

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public ByteArray abstractNext() {
				ByteArray result = ByteArray.getInstance(BYTE_ZERO, hashAlgorithm.getByteLength());
				ByteArray currentHash = salt.append(MathUtil.getByteArray(counter));
				for (int i = 1; i <= rounds; i++) {
					currentHash = hashAlgorithm.getHashValue(currentHash, password);
					result = result.xor(currentHash);
				}
				counter++;
				return result;
			}

		};
	}

	public static PBKD2ByteArraySequence getInstance(ByteArray password) {
		return PBKD2ByteArraySequence.getInstance(ByteArray.getInstance());
	}

	public static PBKD2ByteArraySequence getInstance(ByteArray password, ByteArray salt) {
		return PBKD2ByteArraySequence.getInstance(HashAlgorithm.getInstance(), salt);
	}

	public static PBKD2ByteArraySequence getInstance(HashAlgorithm hashAlgorithm, ByteArray password) {
		return PBKD2ByteArraySequence.getInstance(hashAlgorithm, ByteArray.getInstance());
	}

	public static PBKD2ByteArraySequence getInstance(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt) {
		return PBKD2ByteArraySequence.getInstance(hashAlgorithm, password, salt, DEFUALT_ROUNDS);
	}

	public static PBKD2ByteArraySequence getInstance(ByteArray password, int rounds) {
		return PBKD2ByteArraySequence.getInstance(ByteArray.getInstance(), rounds);
	}

	public static PBKD2ByteArraySequence getInstance(ByteArray password, ByteArray salt, int rounds) {
		return PBKD2ByteArraySequence.getInstance(HashAlgorithm.getInstance(), salt, rounds);
	}

	public static PBKD2ByteArraySequence getInstance(HashAlgorithm hashAlgorithm, ByteArray password, int rounds) {
		return PBKD2ByteArraySequence.getInstance(hashAlgorithm, ByteArray.getInstance(), rounds);
	}

	public static PBKD2ByteArraySequence getInstance(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt, int rounds) {
		if (hashAlgorithm == null || password == null || salt == null || rounds < 1) {
			throw new IllegalArgumentException();
		}
		return new PBKD2ByteArraySequence(hashAlgorithm, password, salt, rounds);
	}

}
