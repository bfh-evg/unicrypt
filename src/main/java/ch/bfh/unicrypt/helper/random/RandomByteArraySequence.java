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
package ch.bfh.unicrypt.helper.random;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.hybrid.HMAC_DRBG;
import ch.bfh.unicrypt.helper.random.hybrid.Hash_DRBG;
import ch.bfh.unicrypt.helper.random.password.PBKDF2;
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * The purpose of this abstract sub-class of {@link Sequence} is twofold. First, it serves as a base implementation for
 * various types of infinitely long byte array sequences. Second, it provides a method for transforming the byte array
 * sequence into a byte sequence. This method is useful to transform instances of {@link HMAC_DRBG}, {@link Hash_DRBG},
 * or {@link PBKDF2} into random byte sequences.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class RandomByteArraySequence
	   extends Sequence<ByteArray> {

	protected RandomByteArraySequence() {
		super(Sequence.INFINITE);
	}

	/**
	 * Returns a byte sequence obtained by concatenating the random byte arrays from this sequence. This method is
	 * useful to transform instances of {@link HMAC_DRBG}, {@link Hash_DRBG}, or {@link PBKDF2} into random byte
	 * sequences.
	 * <p>
	 * @return The new byte sequence
	 */
	public RandomByteSequence getRandomByteSequence() {
		final RandomByteArraySequence source = this;
		return new RandomByteSequence() {

			@Override
			public RandomByteSequenceIterator iterator() {
				return source.byteIterator();
			}

		};
	}

	protected RandomByteSequenceIterator byteIterator() {
		return new RandomByteSequenceIterator() {

			private final RandomByteArraySequenceIterator iterator = iterator();
			private int currentIndex = 0;
			private ByteArray currentByteArray = this.iterator.abstractNext();

			@Override
			protected Byte abstractNext() {
				if (this.currentIndex == this.currentByteArray.getLength()) {
					this.currentIndex = 0;
					this.currentByteArray = this.iterator.abstractNext();
				}
				return this.currentByteArray.getAt(this.currentIndex++);
			}

			@Override
			protected void updateBefore() {
				this.iterator.updateBefore();
			}

			@Override
			protected void updateAfter() {
				this.iterator.updateAfter();
			}
		};
	}

	@Override
	public abstract RandomByteArraySequenceIterator iterator();

}
