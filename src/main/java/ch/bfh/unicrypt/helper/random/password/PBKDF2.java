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
package ch.bfh.unicrypt.helper.random.password;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteArraySequenceIterator;

/**
 * This class implements the standard PBKDF2 (password-based key derivation function) as defined in the NIST Special
 * Publication 800-132, "Recommendation for Password-Based Key Derivation", and in the RFC 2898, "PKCS #5:
 * Password-Based Cryptography Specification, Version 2.0". Instances of this class generate a deterministic sequence of
 * random byte arrays for a given password, salt, number of rounds, and hash algorithm. The number of rounds determines
 * the amount of work to derive the sequence.
 * <p>
 * @author R. Haenni
 */
public class PBKDF2
	   extends PasswordRandomByteArraySequence {

	/**
	 * The default number of rounds
	 */
	public static final int DEFAULT_ROUNDS = 100000;

	private static final byte BYTE_ZERO = MathUtil.getByte(0x00);

	private final HashAlgorithm hashAlgorithm;
	private final int rounds;

	private PBKDF2(HashAlgorithm hashAlgorithm, int rounds, ByteArray password, ByteArray salt) {
		super(password, salt);
		this.hashAlgorithm = hashAlgorithm;
		this.rounds = rounds;
	}

	@Override
	public RandomByteArraySequenceIterator iterator() {

		return new RandomByteArraySequenceIterator() {

			private int counter = 1;

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
	 * Returns a new factory for constructing new instances of this class. It uses the default hash algorithm, the
	 * default number of rounds, and the default {@link StringToByteArray} converter.
	 * <p>
	 * @return The new PBKDF2 factory
	 */
	public static PasswordRandomByteArraySequence.Factory getFactory() {
		return PBKDF2.getFactory(HashAlgorithm.getInstance());
	}

	/**
	 * For a given number of rounds, this method returns a new factory for constructing new instances of this class. It
	 * uses the default hash algorithm and the default {@link StringToByteArray} converter.
	 * <p>
	 * @param rounds The desired number of rounds
	 * @return The new PBKDF2 factory
	 */
	public static PasswordRandomByteArraySequence.Factory getFactory(int rounds) {
		return PBKDF2.getFactory(HashAlgorithm.getInstance(), rounds);
	}

	/**
	 * For a given hash algorithm, this method returns a new factory for constructing new instances of this class. It
	 * uses the default number of rounds and the default {@link StringToByteArray} converter.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new PBKDF2 factory
	 */
	public static PasswordRandomByteArraySequence.Factory getFactory(HashAlgorithm hashAlgorithm) {
		return PBKDF2.getFactory(hashAlgorithm, PBKDF2.DEFAULT_ROUNDS);
	}

	/**
	 * For a given hash algorithm and number of rounds, this method returns a new factory for constructing new instances
	 * of this class. It uses the default {@link StringToByteArray} converter.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param rounds        The desired number of rounds
	 * @return The new PBKDF2 factory
	 */
	public static PasswordRandomByteArraySequence.Factory getFactory(final HashAlgorithm hashAlgorithm,
		   final int rounds) {
		return PBKDF2.getFactory(hashAlgorithm, rounds, StringToByteArray.getInstance());
	}

	/**
	 * For a given hash algorithm, number of rounds, and {@link StringToByteArray} converter, this method returns a new
	 * factory for constructing new instances of this class. The converter is used to convert string passwords into byte
	 * arrays.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param rounds        The desired number of rounds
	 * @param converter		   The converter from string to byteArray
	 * @return The new PBKDF2 factory
	 */
	public static PasswordRandomByteArraySequence.Factory getFactory(final HashAlgorithm hashAlgorithm,
		   final int rounds, final Converter<String, ByteArray> converter) {
		if (hashAlgorithm == null || rounds < 1 || converter == null) {
			throw new IllegalArgumentException();
		}
		return new PasswordRandomByteArraySequence.Factory(converter) {

			@Override
			protected PasswordRandomByteArraySequence abstractGetInstance(ByteArray password, ByteArray salt) {
				return new PBKDF2(hashAlgorithm, rounds, password, salt);
			}

		};
	}

}
