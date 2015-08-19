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
 * This class implements the standard PBKDF2 (password-based key derivation function) as defined in the NIST Special
 * Publication 800-132, "Recommendation for Password-Based Key Derivation", and in the RFC 2898, "PKCS #5:
 * Password-Based Cryptography Specification, Version 2.0". Instances of this class generate a deterministic sequence of
 * randomly looking byte arrays for a given password, salt, number of rounds, and hash algorithm. The number of rounds
 * determines the amount of work to compute the key.
 * <p>
 * @author R. Haenni
 */
public class PBKDF2
	   extends Sequence<ByteArray> {

	/**
	 * The default number of rounds
	 */
	public static final int DEFAULT_ROUNDS = 4096;

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);

	private final HashAlgorithm hashAlgorithm;
	private final ByteArray password;
	private final ByteArray salt;
	private final int rounds;

	private PBKDF2(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt, int rounds) {
		super(Sequence.INFINITE);
		this.hashAlgorithm = hashAlgorithm;
		this.password = password;
		this.salt = salt;
		this.rounds = rounds;
	}

	/**
	 * Returns a byte sequence consisting of the bytes contained in the blocks generated by PBKDF2. The byte sequence
	 * can be used to select password-derived key of a certain length.
	 * <p>
	 * @return The byte sequence
	 */
	public final ByteSequence getByteSequence() {
		return ByteSequence.getInstance(this);
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

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard, using the default hash algorithm and the
	 * default number of rounds. The sequence is determined by the given password. The default salt is the empty byte
	 * array.
	 * <p>
	 * @param password The given password
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(ByteArray password) {
		return PBKDF2.getInstance(ByteArray.getInstance());
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard, using the default hash algorithm and the
	 * default number of rounds. The sequence is determined by the given password and salt.The default salt is the empty
	 * byte array.
	 * <p>
	 * @param password The given password
	 * @param salt     The given salt
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(ByteArray password, ByteArray salt) {
		return PBKDF2.getInstance(HashAlgorithm.getInstance(), salt);
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard. The sequence is determined by the given
	 * password and hash algorithm. The default salt is the empty byte array.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param password      The given password
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(HashAlgorithm hashAlgorithm, ByteArray password) {
		return PBKDF2.getInstance(hashAlgorithm, ByteArray.getInstance());
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard, using the default number of rounds. The
	 * sequence is determined by the given password, salt, and hash algorithm.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param password      The given password
	 * @param salt          The given salt
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt) {
		return PBKDF2.getInstance(hashAlgorithm, password, salt, DEFAULT_ROUNDS);
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard, using the default hash algorithm. The
	 * sequence is determined by the given password and the number of rounds. The default salt is the empty byte array.
	 * <p>
	 * @param password The given password
	 * @param rounds   The number of rounds
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(ByteArray password, int rounds) {
		return PBKDF2.getInstance(ByteArray.getInstance(), rounds);
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard, using the default hash algorithm. The
	 * sequence is determined by the given password, salt, and number of rounds.
	 * <p>
	 * @param password The given password
	 * @param salt     The given salt
	 * @param rounds   The number of rounds
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(ByteArray password, ByteArray salt, int rounds) {
		return PBKDF2.getInstance(HashAlgorithm.getInstance(), salt, rounds);
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard. The sequence is determined by the given
	 * password, number of rounds, and the hash algorithm. The default salt is the byte array.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param password      The given password
	 * @param rounds        The number of rounds
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(HashAlgorithm hashAlgorithm, ByteArray password, int rounds) {
		return PBKDF2.getInstance(hashAlgorithm, ByteArray.getInstance(), rounds);
	}

	/**
	 * Returns a new sequence of byte arrays based on the PBKDF2 standard. The sequence is determined by the given
	 * password, salt, number of rounds, and the hash algorithm.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param password      The given password
	 * @param salt          The given salt
	 * @param rounds        The number of rounds
	 * @return The new PBKDF2 byte array sequence
	 */
	public static PBKDF2 getInstance(HashAlgorithm hashAlgorithm, ByteArray password, ByteArray salt, int rounds) {
		if (hashAlgorithm == null || password == null || salt == null || rounds < 1) {
			throw new IllegalArgumentException();
		}
		return new PBKDF2(hashAlgorithm, password, salt, rounds);
	}

}