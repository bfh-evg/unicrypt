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
import ch.bfh.unicrypt.helper.random.RandomByteArraySequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;

/**
 * This abstract class implements the basic functionality of hybrid random bit generators such {@link HMAC_DRBG} or
 * {@link Hash_DRBG}. It also adjusts the return type of the method {@link #getRandomByteSequence()}. Instances of this
 * class derive random byte arrays from an initial seed and a personalization string. The seed is taken from an entropy
 * source. The class also provides an abstract factory class for constructing such instances.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class HybridRandomByteArraySequence
	   extends RandomByteArraySequence {

	protected final NonDeterministicRandomByteSequence entropySource;
	protected final ByteArray personalizationString;

	protected HybridRandomByteArraySequence(NonDeterministicRandomByteSequence entropySource,
		   ByteArray personalizationString) {
		this.entropySource = entropySource;
		this.personalizationString = personalizationString;
	}

	@Override
	public HybridRandomByteSequence getRandomByteSequence() {
		final HybridRandomByteArraySequence source = this;
		return new HybridRandomByteSequence() {

			@Override
			public RandomByteSequenceIterator iterator() {
				return source.byteIterator();
			}

		};
	}

	/**
	 * This is the abstract factory class for constructing instances of {@link HybridRandomByteArraySequence} from a
	 * given entropy source and a personalization string. Classes implementing this abstract class are responsible for
	 * the actual construction of the objects.
	 */
	public static abstract class Factory {

		/**
		 * Returns a new hybrid sequence of byte arrays. The default non-deterministic random byte sequence is selected
		 * as default entropy source. The default personalization string is an empty byte array.
		 * <p>
		 * @return The new hybrid byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance() {
			return this.getInstance(NonDeterministicRandomByteSequence.getInstance());
		}

		/**
		 * Returns a new hybrid sequence of byte arrays for the given entropySource. The default personalization string
		 * is an empty byte array.
		 * <p>
		 * @param entropySource The given entropy source
		 * @return The new hybrid byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance(NonDeterministicRandomByteSequence entropySource) {
			return this.getInstance(entropySource, ByteArray.getInstance());
		}

		/**
		 * Returns a new hybrid sequence of byte arrays for a given personalization string. The default
		 * non-deterministic random byte sequence is selected as default entropy source.
		 * <p>
		 * @param personalizationString The given personalizationS string
		 * @return The new hybrid byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance(ByteArray personalizationString) {
			return this.getInstance(NonDeterministicRandomByteSequence.getInstance(), personalizationString);
		}

		/**
		 * Returns a new hybrid sequence of byte arrays for the given entropySource and personalization string.
		 * <p>
		 * @param entropySource         The given entropy source
		 * @param personalizationString The given personalizationS string
		 * @return The new hybrid byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance(NonDeterministicRandomByteSequence entropySource,
			   ByteArray personalizationString) {
			if (entropySource == null || personalizationString == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(entropySource, personalizationString);
		}

		protected abstract HybridRandomByteArraySequence abstractGetInstance(
			   NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString);

	}

}
