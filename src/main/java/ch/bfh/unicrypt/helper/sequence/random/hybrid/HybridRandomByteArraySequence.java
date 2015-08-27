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
package ch.bfh.unicrypt.helper.sequence.random.hybrid;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.sequence.random.RandomByteArraySequence;
import ch.bfh.unicrypt.helper.sequence.random.RandomByteSequenceIterator;
import ch.bfh.unicrypt.helper.sequence.random.nondeterministic.NonDeterministicRandomByteSequence;

/**
 *
 * @author rolfhaenni
 */
public abstract class HybridRandomByteArraySequence
	   extends RandomByteArraySequence {

	protected final NonDeterministicRandomByteSequence entropySource;
	protected final ByteArray personalizationString;

	protected HybridRandomByteArraySequence(NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString) {
		this.entropySource = entropySource;
		this.personalizationString = personalizationString;
	}

	@Override
	public HybridRandomByteSequence getRandomByteSequence() {
		final HybridRandomByteArraySequence source = this;
		return new HybridRandomByteSequence() {

			@Override
			public RandomByteSequenceIterator iterator() {
				return source.getRandomByteSequenceIterator();
			}

		};
	}

	protected ByteArray getEntropyInput(int entropy) {
		return this.entropySource.iterator().next(entropy / Byte.SIZE);
	}

	public static abstract class Factory {

		/**
		 * Returns a new sequence of byte arrays using the default hash algorithm. The default seed is an array of
		 * zero-bytes of the required length.
		 * <p>
		 * @return The new byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance() {
			return this.getInstance(NonDeterministicRandomByteSequence.getInstance());
		}

		public HybridRandomByteArraySequence getInstance(NonDeterministicRandomByteSequence entropySource) {
			return this.getInstance(entropySource, ByteArray.getInstance());
		}

		public HybridRandomByteArraySequence getInstance(ByteArray personalizationString) {
			return this.getInstance(NonDeterministicRandomByteSequence.getInstance(), personalizationString);
		}

		/**
		 * Returns a new sequence of byte arrays for the given hash algorithm and seed.
		 * <p>
		 *
		 * @param personalizationString @param entropySource
		 * @return The new byte array sequence
		 */
		public HybridRandomByteArraySequence getInstance(NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString) {
			if (entropySource == null || personalizationString == null) {
				throw new IllegalArgumentException();
			}
			return this.abstractGetInstance(entropySource, personalizationString);
		}

		protected abstract HybridRandomByteArraySequence abstractGetInstance(NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString);

	}

}
