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
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.random.RandomByteArraySequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;

/**
 * This abstract class implements the basic functionality of password-based random bit generators such {@link PBKDF2}.
 * It also adjusts the return type of the method {@link #getRandomByteSequence()}. Instances of this class derive random
 * byte arrays from an initial password and salt. The class also provides an abstract factory class for constructing
 * such instances.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class PasswordRandomByteArraySequence
	   extends RandomByteArraySequence {

	protected final ByteArray password;
	protected final ByteArray salt;

	protected PasswordRandomByteArraySequence(ByteArray password, ByteArray salt) {
		this.password = password;
		this.salt = salt;
	}

	@Override
	public PasswordRandomByteSequence getRandomByteSequence() {
		final PasswordRandomByteArraySequence source = this;
		return new PasswordRandomByteSequence() {

			@Override
			public RandomByteSequenceIterator iterator() {
				return source.byteIterator();
			}

		};
	}

	/**
	 * This is the abstract factory class for constructing instances of {@link PasswordRandomByteArraySequence} from a
	 * given password and salt. Classes implementing this abstract class are responsible for the actual construction of
	 * the objects.
	 */
	public static abstract class Factory {

		protected Converter<String, ByteArray> converter;

		protected Factory(Converter<String, ByteArray> converter) {
			this.converter = converter;
		}

		/**
		 * Returns a new password-based sequence of byte arrays. The default password and the default salt are empty
		 * byte arrays.
		 * <p>
		 * @return The new password-based byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance() {
			return this.getInstance(ByteArray.getInstance());
		}

		/**
		 * Derives and returns a new password-based sequence of byte arrays from a given password. The default salt is
		 * an empty byte array.
		 * <p>
		 * @param password The given password
		 * @return The new password-based byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance(String password) {
			return this.getInstance(password, ByteArray.getInstance());
		}

		/**
		 * Derives and returns a new password-based sequence of byte arrays from a given password. The default salt is
		 * an empty byte array.
		 * <p>
		 * @param password The given password
		 * @return The new password-based byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance(ByteArray password) {
			return this.getInstance(password, ByteArray.getInstance());
		}

		/**
		 * Derives and returns a new password-based sequence of byte arrays from a given password and salt.
		 * <p>
		 * @param password The given password
		 * @param salt     The given salt
		 * @return The new password-based byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance(String password, ByteArray salt) {
			if (password == null || salt == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(converter.convert(password), salt);
		}

		/**
		 * Derives and returns a new password-based sequence of byte arrays from a given password and salt.
		 * <p>
		 * @param password The given password
		 * @param salt     The given salt
		 * @return The new password-based byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance(ByteArray password, ByteArray salt) {
			if (password == null || salt == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(password, salt);
		}

		// this method must be implemented in each sub-class
		protected abstract PasswordRandomByteArraySequence abstractGetInstance(ByteArray password, ByteArray salt);

	}

}
