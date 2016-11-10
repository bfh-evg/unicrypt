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
import ch.bfh.unicrypt.helper.random.RandomByteSequence;

/**
 * The purpose of this abstract class is twofold. First, it provides a base class for implementations of password-based
 * random byte sequences. Second, it provides various static factory methods for deriving new password-based random byte
 * sequences from password-based random bit generators such as {@link PBKDF2}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class PasswordRandomByteSequence
	   extends RandomByteSequence {

	/**
	 * Constructs a new password-based random byte sequence. The default random bit generator is created using the
	 * default {@link PBKDF2} factory and the default password and salt are empty byte arrays.
	 * <p>
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance() {
		return PasswordRandomByteSequence.getInstance(ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence for a given password. The default random bit generator is
	 * created using the default {@link PBKDF2} factory and the default salt is an empty byte arrays.
	 * <p>
	 * @param password The given password
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(ByteArray password) {
		return PasswordRandomByteSequence.getInstance(password, ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence for a given password and salt. The default random bit
	 * generator is created using the default {@link PBKDF2} factory.
	 * <p>
	 * @param password The given password
	 * @param salt     The given salt
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(ByteArray password, ByteArray salt) {
		return PasswordRandomByteSequence.getInstance(PBKDF2.getFactory(), password, salt);
	}

	/**
	 * Constructs a new password-based random byte sequence from a given factory of a random bit generator. The default
	 * password and salt are empty byte arrays.
	 * <p>
	 * @param factory The given factory class of the password-based random bit generator
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(PasswordRandomByteArraySequence.Factory factory) {
		return PasswordRandomByteSequence.getInstance(factory, ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence from a given factory of a random bit generator and a given
	 * password. The default salt is an empty byte array.
	 * <p>
	 * @param factory  The given factory class of the password-based random bit generator
	 * @param password The given password
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(PasswordRandomByteArraySequence.Factory factory,
		   ByteArray password) {
		return PasswordRandomByteSequence.getInstance(factory, password, ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence from a given factory of a random bit generator and a given
	 * password and salt.
	 * <p>
	 * @param factory  The given factory class of the password-based random bit generator
	 * @param password The given password
	 * @param salt     The given salt
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(PasswordRandomByteArraySequence.Factory factory,
		   ByteArray password, ByteArray salt) {
		if (factory == null || password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		return factory.getInstance(password, salt).getRandomByteSequence();
	}

	/**
	 * Constructs a new password-based random byte sequence for a given password. The default random bit generator is
	 * created using the default {@link PBKDF2} factory and the default salt is an empty byte arrays.
	 * <p>
	 * @param password The given password
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(String password) {
		return PasswordRandomByteSequence.getInstance(password, ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence for a given password and salt. The default random bit
	 * generator is created using the default {@link PBKDF2} factory.
	 * <p>
	 * @param password The given password
	 * @param salt     The given salt
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(String password, ByteArray salt) {
		return PasswordRandomByteSequence.getInstance(PBKDF2.getFactory(), password, salt);
	}

	/**
	 * Constructs a new password-based random byte sequence from a given factory of a random bit generator and a given
	 * password. The default salt is an empty byte array.
	 * <p>
	 * @param factory  The given factory class of the password-based random bit generator
	 * @param password The given password
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(PasswordRandomByteArraySequence.Factory factory,
		   String password) {
		return PasswordRandomByteSequence.getInstance(factory, password, ByteArray.getInstance());
	}

	/**
	 * Constructs a new password-based random byte sequence from a given factory of a random bit generator and a given
	 * password and salt.
	 * <p>
	 * @param factory  The given factory class of the password-based random bit generator
	 * @param password The given password
	 * @param salt     The given salt
	 * @return The new random byte sequence
	 */
	public static PasswordRandomByteSequence getInstance(PasswordRandomByteArraySequence.Factory factory,
		   String password, ByteArray salt) {
		if (factory == null || password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		return factory.getInstance(password, salt).getRandomByteSequence();
	}

}
