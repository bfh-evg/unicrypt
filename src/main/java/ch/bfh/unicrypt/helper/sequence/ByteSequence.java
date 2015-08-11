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
package ch.bfh.unicrypt.helper.sequence;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.random.HMAC_DRBG;
import ch.bfh.unicrypt.helper.sequence.random.Hash_DRBG;
import ch.bfh.unicrypt.helper.sequence.random.PBKDF2;
import java.math.BigInteger;

/**
 * The purpose of this specialization of {@link Sequence} is to adjust the return type of the method
 * {@link Sequence#group(int)} to {@link ByteArray}. Furthermore, the class provides a method for constructing a byte
 * sequence from a {@link ByteArray} sequence. This method is useful to transform instances of
 * {@link HMAC_DRBG}, {@link Hash_DRBG}, or {@link PBKDF2} into random byte sequences.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @see HMAC_DRBG
 * @see Hash_DRBG
 * @see PBKDF2
 */
public abstract class ByteSequence
	   extends Sequence<Byte> {

	protected ByteSequence(BigInteger length) {
		this.length = length;
	}

	@Override
	public abstract ByteSequenceIterator iterator();

	@Override
	public final Sequence<ByteArray> group(final int groupLength) {
		if (groupLength < 1) {
			throw new IllegalArgumentException();
		}
		BigInteger newLength;
		if (this.length.equals(Sequence.UNKNOWN) || this.length.equals(Sequence.INFINITE)) {
			newLength = this.length;
		} else {
			newLength = MathUtil.divideUp(this.length, BigInteger.valueOf(groupLength));
		}
		final ByteSequence source = this;
		return new Sequence<ByteArray>(newLength) {

			@Override
			public SequenceIterator<ByteArray> iterator() {
				return new SequenceIterator<ByteArray>() {

					private final ByteSequenceIterator iterator = source.iterator();

					@Override
					public boolean hasNext() {
						return this.iterator.hasNext();
					}

					@Override
					public ByteArray abstractNext() {
						int i = 0;
						byte[] result = new byte[groupLength];
						while (i < groupLength && this.iterator.hasNext()) {
							result[i] = this.iterator.abstractNext();
							i++;
						}
						// extra bytes are truncated
						return ByteArray.getInstance(result).extractPrefix(i);
					}

					@Override
					public void updateAfter() {
						this.iterator.updateAfter();
					}
				};

			}

		};

	}

	/**
	 * Returns a byte sequence obtained by concatenating the byte arrays from a given {@link ByteArray} sequence. This
	 * method is useful to transform instances of {@link HMAC_DRBG}, {@link Hash_DRBG}, or {@link PBKDF2} into random byte
	 * sequences.
	 * <p>
	 * @param source The given byte array sequences
	 * @return The new byte sequence
	 */
	public static ByteSequence getInstance(final Sequence<ByteArray> source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		return new ByteSequence(Sequence.UNKNOWN) {

			@Override
			public ByteSequenceIterator iterator() {
				return new ByteSequenceIterator() {

					private final SequenceIterator<ByteArray> iterator = source.iterator();
					private ByteArray currentByteArray = null;
					private int currentIndex = 0;

					@Override
					public boolean hasNext() {
						return iterator.hasNext() || (this.currentByteArray != null && this.currentIndex < this.currentByteArray.getLength());
					}

					@Override
					public Byte abstractNext() {
						if (this.currentByteArray == null || this.currentIndex == this.currentByteArray.getLength()) {
							this.currentByteArray = iterator.abstractNext();
							this.currentIndex = 0;
						}
						return this.currentByteArray.getAt(this.currentIndex++);
					}

					@Override
					public void updateAfter() {
						this.iterator.updateAfter();
					}
				};
			}
		};
	}

}
