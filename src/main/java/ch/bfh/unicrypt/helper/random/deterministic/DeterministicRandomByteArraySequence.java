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
package ch.bfh.unicrypt.helper.random.deterministic;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomByteArraySequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;

/**
 * This abstract class implements the basic functionality of deterministic random bit generators such {@link CTR_DRBG}
 * or {@link OFB_DRBG}. It also adjusts the return type of the method {@link #getRandomByteSequence()}. Instances of
 * this class derive random byte arrays from an initial seed. The class also provides an abstract factory class for
 * constructing such instances.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class DeterministicRandomByteArraySequence
	   extends RandomByteArraySequence {

	protected final ByteArray seed;

	protected DeterministicRandomByteArraySequence(ByteArray seed) {
		this.seed = seed;
	}

	@Override
	public DeterministicRandomByteSequence getRandomByteSequence() {
		final DeterministicRandomByteArraySequence source = this;
		return new DeterministicRandomByteSequence() {

			@Override
			public RandomByteSequenceIterator iterator() {
				return source.byteIterator();
			}

		};
	}

	/**
	 * This is the abstract factory class for constructing instances of {@link DeterministicRandomByteArraySequence}
	 * from a given seed. Classes implementing this abstract class are responsible for the actual construction of the
	 * objects. They must also define the length of the seed.
	 */
	public static abstract class Factory {

		/**
		 * Returns a new deterministic sequence of byte arrays. The default seed is an array of zero-bytes of the
		 * required length.
		 * <p>
		 * @return The new deterministic byte array sequence
		 */
		public DeterministicRandomByteArraySequence getInstance() {
			return this.getInstance(ByteArray.getInstance(false, this.getSeedByteLength()));
		}

		/**
		 * Returns a new deterministic sequence of byte arrays for the given seed.
		 * <p>
		 * @param seed The given seed
		 * @return The new deterministic byte array sequence
		 */
		public DeterministicRandomByteArraySequence getInstance(ByteArray seed) {
			if (seed == null || this.getSeedByteLength() != seed.getLength()) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(seed);
		}

		/**
		 * Returns the required byte length of the seed for constructing new deterministic random bit generators.
		 * <p>
		 * @return The byte length of the seed
		 */
		public int getSeedByteLength() {
			return this.getSeedBitLength() / Byte.SIZE;
		}

		/**
		 * Returns the required bit length of the seed for constructing new deterministic random bit generators.
		 * <p>
		 * @return The bit length of the seed
		 */
		public abstract int getSeedBitLength();

		// this method must be implemented in each sub-class
		protected abstract DeterministicRandomByteArraySequence abstractGetInstance(ByteArray seed);

	}

}
