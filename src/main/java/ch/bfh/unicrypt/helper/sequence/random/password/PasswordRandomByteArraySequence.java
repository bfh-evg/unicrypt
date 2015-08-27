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
package ch.bfh.unicrypt.helper.sequence.random.password;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.sequence.random.RandomByteArraySequence;
import ch.bfh.unicrypt.helper.sequence.random.RandomByteSequenceIterator;

/**
 *
 * @author rolfhaenni
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
				return source.getRandomByteSequenceIterator();
			}

		};
	}

	public static abstract class Factory {

		protected Converter<String, ByteArray> converter;

		protected Factory(Converter<String, ByteArray> converter) {
			this.converter = converter;
		}

		/**
		 * Returns a new sequence of byte arrays. The default seed is an array of zero-bytes of the required length.
		 * <p>
		 * @return The new byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance() {
			return this.getInstance(ByteArray.getInstance());
		}

		public PasswordRandomByteArraySequence getInstance(ByteArray password) {
			return this.getInstance(password, ByteArray.getInstance(ByteArray.getInstance()));
		}

		/**
		 * Returns a new sequence of byte arrays for the given seed.
		 * <p>
		 * @param password
		 * @param salt
		 * @return The new byte array sequence
		 */
		public PasswordRandomByteArraySequence getInstance(ByteArray password, ByteArray salt) {
			if (password == null || salt == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(password, salt);
		}

		protected abstract PasswordRandomByteArraySequence abstractGetInstance(ByteArray password, ByteArray salt);

		public PasswordRandomByteArraySequence getInstance(String password) {
			return this.getInstance(password, "");
		}

		public PasswordRandomByteArraySequence getInstance(String password, String salt) {
			if (password == null || salt == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(converter.convert(password), converter.convert(salt));
		}

	}

}
