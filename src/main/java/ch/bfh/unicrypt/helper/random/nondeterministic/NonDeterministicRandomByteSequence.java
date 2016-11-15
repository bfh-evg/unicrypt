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
package ch.bfh.unicrypt.helper.random.nondeterministic;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;

/**
 * This abstract class has various purposes. First, it provides a base class for implementations of non-deterministic
 * random byte sequences, which can be used as entropy sources. Instances of sub-classes of this class will always
 * return the same single iterator. Therefore, this class provides two convenience methods for accessing the iterator of
 * the sequence. Finally, this class also provides a static factory method for obtaining a default non-deterministic
 * random byte sequence.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class NonDeterministicRandomByteSequence
	   extends RandomByteSequence {

	final private RandomByteSequenceIterator iterator;

	protected NonDeterministicRandomByteSequence(RandomByteSequenceIterator iterator) {
		this.iterator = iterator;
	}

	@Override
	public RandomByteSequenceIterator iterator() {
		return this.iterator;
	}

	/**
	 * Returns the next byte of the sequence.
	 * <p>
	 * @return The next byte
	 */
	public Byte next() {
		return this.iterator.next();
	}

	/**
	 * Returns the next {@code n} bytes of the sequence.
	 * <p>
	 * @param n The number of returned bytes
	 * @return The next {@code n} bytes
	 */
	public ByteArray next(int n) {
		return this.iterator.next(n);
	}

	/**
	 * Returns the default non-deterministic random byte sequence, which is an instance of {@link SecureRandom_NRBG}.
	 * <p>
	 * @return The default non-deterministic random byte sequence
	 */
	public static NonDeterministicRandomByteSequence getInstance() {
		return SecureRandom_NRBG.getInstance();
	}

}
