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

import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;
import java.security.SecureRandom;

/**
 * This class is a wrapper class for an instance of the Java class {@link SecureRandom}. It uses the method
 * {@link SecureRandom#generateSeed(int)} to obtain the desired non-deterministic random bytes. Note that depending on
 * the implementation, this method may block as entropy is being gathered, for example, if they need to read from
 * {@code /dev/random} on various UNIX-like operating systems. This class is implemented as a singleton class.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class SecureRandom_NRBG
	   extends NonDeterministicRandomByteSequence {

	private static SecureRandom_NRBG instance = null;

	private SecureRandom_NRBG(RandomByteSequenceIterator iterator) {
		super(iterator);
	}

	/**
	 * Return the single instance of this class, which itself holds a single instance of {@link SecureRandom}.
	 * <p>
	 * @return The single instance of this class
	 */
	public static SecureRandom_NRBG getInstance() {
		if (SecureRandom_NRBG.instance == null) {
			RandomByteSequenceIterator iterator = new RandomByteSequenceIterator() {

				private final SecureRandom secureRandom = new SecureRandom();

				@Override
				protected Byte abstractNext() {
					return this.secureRandom.generateSeed(1)[0];
				}

			};
			SecureRandom_NRBG.instance = new SecureRandom_NRBG(iterator);
		}
		return SecureRandom_NRBG.instance;
	}

}
