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
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * The purpose of this abstract sub-class of {@link Sequence} is twofold. First, it serves as a base implementation for
 * various types of infinitely long byte sequences. Second, it adjusts the return type of the method
 * {@link Sequence#group(int)} to {@link ByteArray}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class RandomByteSequence
	   extends Sequence<Byte> {

	protected RandomByteSequence() {
		super(Sequence.INFINITE);
	}

	@Override
	public final RandomByteArraySequence group(final int groupLength) {
		if (groupLength < 1) {
			throw new IllegalArgumentException();
		}
		final RandomByteSequence source = this;
		return new RandomByteArraySequence() {

			@Override
			public RandomByteArraySequenceIterator iterator() {
				return source.byteArrayIterator(groupLength);
			}

		};

	}

	protected RandomByteArraySequenceIterator byteArrayIterator(final int length) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		final RandomByteSequenceIterator iterator = this.iterator();
		return new RandomByteArraySequenceIterator() {

			@Override
			protected ByteArray abstractNext() {
				int i = 0;
				byte[] result = new byte[length];
				while (i < length) {
					result[i] = iterator.abstractNext();
					i++;
				}
				return SafeByteArray.getInstance(result);
			}

			@Override

			protected void updateBefore() {
				iterator.updateBefore();
			}

			@Override
			protected void updateAfter() {
				iterator.updateAfter();
			}
		};
	}

	@Override
	public abstract RandomByteSequenceIterator iterator();

}
