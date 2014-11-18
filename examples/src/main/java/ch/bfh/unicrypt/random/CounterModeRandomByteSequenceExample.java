/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.random;

import ch.bfh.unicrypt.random.classes.CounterModeRandomByteSequence;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class CounterModeRandomByteSequenceExample {

	public static void main(String[] args) {
		System.out.println("An Example with a CounterModeRandomByteSequence");

		CounterModeRandomByteSequence cmrbs = CounterModeRandomByteSequence.getInstance();
		System.out.println("Using the following Algorithm: " + cmrbs.getHashAlgorithm());

		System.out.println("Counter: " + cmrbs.getCounter());
		System.out.println("Seed: " + cmrbs.getSeed());

		System.out.println("64 bit (8 Bytes): " + cmrbs.getNextByteArray(8));
		System.out.println("Counter: " + cmrbs.getCounter());

		System.out.println("192 bit (24 Bytes): " + cmrbs.getNextByteArray(24));
		System.out.println("Counter: " + cmrbs.getCounter());

		cmrbs.setSeed(cmrbs.getSeed());
		System.out.println("RESET");

		System.out.println("Counter: " + cmrbs.getCounter());
		System.out.println("Seed: " + cmrbs.getSeed());

		System.out.println("64 bit (8 Bytes): " + cmrbs.getNextByteArray(8));
		System.out.println("Counter: " + cmrbs.getCounter());

		System.out.println("192 bit (24 Bytes): " + cmrbs.getNextByteArray(24));
		System.out.println("Counter: " + cmrbs.getCounter());

		System.out.println("256 bit (32 Bytes): " + cmrbs.getNextByteArray(32));
		System.out.println("Counter: " + cmrbs.getCounter());

		cmrbs.setSeed(cmrbs.getSeed());
		System.out.println("RESET");

		System.out.println("Counter: " + cmrbs.getCounter());
		System.out.println("Seed: " + cmrbs.getSeed());
		System.out.println("508 bit (63 Bytes): " + cmrbs.getNextByteArray(63));
		System.out.println("Counter: " + cmrbs.getCounter());

		System.out.println("8 bit (1 Byte): " + cmrbs.getNextByteArray(1));
		System.out.println("Counter: " + cmrbs.getCounter());

	}

}
