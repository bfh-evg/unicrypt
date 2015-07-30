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
package ch.bfh.unicrypt.helper.iterable;

import ch.bfh.unicrypt.helper.sequence.classes.BigIntegerSequence;
import ch.bfh.unicrypt.helper.sequence.classes.ShortenedSequence;
import ch.bfh.unicrypt.helper.sequence.interfaces.Sequence;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class ShortenedSequenceTest {

	@Test
	public void generalTest() {

		Sequence<BigInteger> sequence = BigIntegerSequence.getInstance(1, 100);

		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(sequence, 0);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(0, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(sequence, 1);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(1, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(sequence, 10);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(10, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(sequence, 100);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(100, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(sequence, 200);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(100, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(ShortenedSequence.getInstance(sequence, 50), 100);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(50, counter);
		}
		{
			Iterable<BigInteger> newSequence = ShortenedSequence.getInstance(ShortenedSequence.getInstance(sequence, 0), 5);
			int counter = 0;
			for (BigInteger i : newSequence) {
				counter++;
			}
			assertEquals(0, counter);
		}

	}

}
