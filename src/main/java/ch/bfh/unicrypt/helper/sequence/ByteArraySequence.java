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
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class ByteArraySequence
	   extends Sequence<ByteArray> {

	protected ByteArraySequence() {
		super();
	}

	protected ByteArraySequence(BigInteger length) {
		super(length);
	}

	public Sequence<Byte> getByteSequence() {

		final Sequence<ByteArray> source = this;
		return new ByteSequence(Sequence.UNKNOWN) {

			@Override
			public SequenceIterator<Byte> iterator() {
				return new SequenceIterator<Byte>() {

					private final SequenceIterator<ByteArray> iterator = source.iterator();
					private ByteArray currentByteArray = null;
					private int currentIndex = 0;

					@Override
					public boolean hasNext() {
						return iterator.hasNext() || (this.currentByteArray != null && this.currentIndex < this.currentByteArray.getLength());
					}

					@Override
					public Byte next() {
						Byte b = this.performNext();
						this.iterator.defaultUpdate();
						return b;
					}

					protected Byte performNext() {
						if (this.currentByteArray == null || this.currentIndex == this.currentByteArray.getLength()) {
							this.currentByteArray = iterator.next();
							this.currentIndex = 0;
						}
						return this.currentByteArray.getAt(this.currentIndex++);
					}

					@Override
					public ByteArray next(int n) {
						if (n < 0) {
							throw new IllegalArgumentException();
						}
						int i = 0;
						byte[] result = new byte[n];
						while (i < n && this.hasNext()) {
							result[i] = this.performNext();
							i++;
						}
						this.iterator.defaultUpdate();
						return ByteArray.getInstance(result);
					}
				};
			}
		};
	}

}
