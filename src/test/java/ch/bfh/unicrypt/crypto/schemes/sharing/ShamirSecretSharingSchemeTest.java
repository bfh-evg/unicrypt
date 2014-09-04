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
package ch.bfh.unicrypt.crypto.schemes.sharing;

import ch.bfh.unicrypt.crypto.schemes.sharing.classes.ShamirSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.security.SecureRandom;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ShamirSecretSharingSchemeTest {

	final SecureRandom random = new SecureRandom();

	final int size = random.nextInt(100) + 3;
	final int threshold = size - random.nextInt(size / 3) - 1;

	@Test
	public void testShare() {

		// Create a field and initialize Scheme
		ZModPrime field = ZModPrime.getInstance(167);
		ShamirSecretSharingScheme ssss = ShamirSecretSharingScheme.getInstance(field, size, threshold);

		// Choose a random message that will be shared
		ZModElement message = field.getRandomElement();

		// Share the message
		Tuple shares = ssss.share(message);

		assertTrue(shares.getArity() == size);

		for (Element share : shares) {
			Pair pair = (Pair) share;
			assertTrue(pair.getArity() == 2);
			assertTrue(field.contains(pair.getFirst()));
			assertTrue(field.contains(pair.getSecond()));
		}
	}

	@Test
	public void testRecover() {
		// Create a field and initialize Scheme
		ZModPrime field = ZModPrime.getInstance(167);
		ShamirSecretSharingScheme ssss = ShamirSecretSharingScheme.getInstance(field, size, threshold);

		// Choose a random message that will be shared
		ZModElement message = field.getRandomElement();

		// Share the message
		Tuple shares = ssss.share(message);

		// Shuffle the list of shares
//		shares = shares.shuffle();
		// choose a random number of shares between the threshold and size to remove
		int numberToRemove = random.nextInt(size - threshold);

		// remove the last shares
		Tuple remainingShares = shares.removeSuffix(numberToRemove);

		// recover message and check whether it is equal with original message
		ZModElement recoveredMessage = ssss.recover(remainingShares);
		assertTrue(recoveredMessage.isEquivalent(message));
	}

}
