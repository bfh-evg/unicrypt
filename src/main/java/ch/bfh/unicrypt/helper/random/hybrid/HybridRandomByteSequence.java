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
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;

/**
 * The purpose of this abstract class is twofold. First, it provides a base class for implementations of hybrid random
 * byte sequences. Second, it provides various static factory methods for deriving new hybrid random byte sequences from
 * hybrid random bit generators such as {@link HMAC_DRBG} or {@link Hash_DRBG}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class HybridRandomByteSequence
	   extends RandomByteSequence {

	/**
	 * Constructs a new hybrid random byte sequence. The default random bit generator is created using the default
	 * {@link HMAC_DRBG} factory. The default non-deterministic random byte sequence is selected as default entropy
	 * source. The default personalization string is an empty byte array.
	 * <p>
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance() {
		return HybridRandomByteSequence.getInstance(NonDeterministicRandomByteSequence.getInstance());
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given personalization string. The default random bit
	 * generator is created using the default {@link HMAC_DRBG} factory. The default non-deterministic random byte
	 * sequence is selected as default entropy source.
	 * <p>
	 * @param personalizationString
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(ByteArray personalizationString) {
		return HybridRandomByteSequence.getInstance(NonDeterministicRandomByteSequence.getInstance(),
													personalizationString);
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given entropy source. The default random bit generator is
	 * created using the default {@link HMAC_DRBG} factory. The default personalization string is an empty byte array.
	 * <p>
	 * @param entropySource	The given entropy source
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(NonDeterministicRandomByteSequence entropySource) {
		return HybridRandomByteSequence.getInstance(HMAC_DRBG.getFactory(), entropySource);
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given entropy source and personalization string. The default
	 * random bit generator is created using the default {@link HMAC_DRBG} factory.
	 * <p>
	 * @param entropySource	        The given entropy source
	 * @param personalizationString The given personalization string
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(NonDeterministicRandomByteSequence entropySource,
		   ByteArray personalizationString) {
		return HybridRandomByteSequence.getInstance(HMAC_DRBG.getFactory(), entropySource, personalizationString);
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given factory of a random bit generator. The default
	 * non-deterministic random byte sequence is selected as default entropy source. The default personalization string
	 * is an empty byte array.
	 * <p>
	 * @param factory The given factory class of the hybrid random bit generator
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(HybridRandomByteArraySequence.Factory factory) {
		return HybridRandomByteSequence.getInstance(factory, NonDeterministicRandomByteSequence.getInstance());
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given factory of a random bit generator and a given
	 * personalization string. The default non-deterministic random byte sequence is selected as default entropy source.
	 * <p>
	 * @param factory               The given factory class of the hybrid random bit generator
	 * @param personalizationString The given personalization string
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(HybridRandomByteArraySequence.Factory factory,
		   ByteArray personalizationString) {
		return HybridRandomByteSequence.getInstance(factory, NonDeterministicRandomByteSequence.getInstance(),
													personalizationString);
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given factory of a random bit generator and a given entropy
	 * source. The default personalization string is an empty byte array.
	 * <p>
	 *
	 * @param factory       The given factory class of the hybrid random bit generator
	 * @param entropySource	The given entropy source
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(HybridRandomByteArraySequence.Factory factory,
		   NonDeterministicRandomByteSequence entropySource) {
		return HybridRandomByteSequence.getInstance(factory, entropySource, ByteArray.getInstance());
	}

	/**
	 * Constructs a new hybrid random byte sequence from a given factory of a random bit generator and a given entropy
	 * source and personalization string.
	 * <p>
	 * @param factory               The given factory class of the hybrid random bit generator
	 * @param entropySource	        The given entropy source
	 * @param personalizationString The given personalization string
	 * @return The new hybrid random byte sequence
	 */
	public static HybridRandomByteSequence getInstance(HybridRandomByteArraySequence.Factory factory,
		   NonDeterministicRandomByteSequence entropySource, ByteArray personalizationString) {
		if (factory == null || entropySource == null || personalizationString == null) {
			throw new IllegalArgumentException();
		}
		return factory.getInstance(entropySource, personalizationString).getRandomByteSequence();
	}

}
