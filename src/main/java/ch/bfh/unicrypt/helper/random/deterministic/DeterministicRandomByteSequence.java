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
import ch.bfh.unicrypt.helper.random.RandomByteSequence;

/**
 * The purpose of this abstract class is twofold. First, it provides a base class for implementations of deterministic
 * random byte sequences. Second, it provides various static factory methods for deriving new deterministic random byte
 * sequences from deterministic random bit generators such as {@link CTR_DRBG} or {@link OFB_DRBG}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class DeterministicRandomByteSequence
	   extends RandomByteSequence {

	/**
	 * Constructs a new deterministic random byte sequence. The default random bit generator is created using the
	 * default {@link CTR_DRBG} factory and the default seed is a sequence of zero-bytes of the required length.
	 * <p>
	 * @return The new random byte sequence
	 */
	public static DeterministicRandomByteSequence getInstance() {
		return DeterministicRandomByteSequence.getInstance(CTR_DRBG.getFactory());
	}

	/**
	 * Constructs a new deterministic random byte sequence for a given seed. The default random bit generator is created
	 * using the default {@link CTR_DRBG} factory.
	 * <p>
	 * @param seed The given seed
	 * @return The new random byte sequence
	 */
	public static DeterministicRandomByteSequence getInstance(ByteArray seed) {
		return DeterministicRandomByteSequence.getInstance(CTR_DRBG.getFactory(), seed);
	}

	/**
	 * Constructs a new deterministic random byte sequence from a given factory of a random bit generator. The default
	 * seed is a sequence of zero-bytes of the required length.
	 * <p>
	 * @param factory The given factory class of the deterministic random bit generator
	 * @return The new random byte sequence
	 */
	public static DeterministicRandomByteSequence getInstance(DeterministicRandomByteArraySequence.Factory factory) {
		return DeterministicRandomByteSequence.getInstance(factory, ByteArray.getInstance(false, factory.
																						  getSeedByteLength()));
	}

	/**
	 * Constructs a new deterministic random byte sequence for a given factory of a random bit generator and a seed.
	 * <p>
	 * @param factory The given factory class of the deterministic random bit generator
	 * @param seed    The given seed
	 * @return The new random byte sequence
	 */
	public static DeterministicRandomByteSequence getInstance(DeterministicRandomByteArraySequence.Factory factory,
		   ByteArray seed) {
		if (factory == null || seed == null) {
			throw new IllegalArgumentException();
		}
		return factory.getInstance(seed).getRandomByteSequence();
	}

}
