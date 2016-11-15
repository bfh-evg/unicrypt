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
package ch.bfh.unicrypt.crypto.schemes.padding;

import ch.bfh.unicrypt.crypto.schemes.padding.classes.ANSIPaddingScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.classes.IECPaddingScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.classes.ISOPaddingScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.classes.PKCSPaddingScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.interfaces.ReversiblePaddingScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class PaddingSchemeTest {

	@Test
	public void testReversiblePaddingScheme() {

		for (int blockLength = 1; blockLength < 10; blockLength++) {
			ReversiblePaddingScheme[] schemes = new ReversiblePaddingScheme[]{
				ANSIPaddingScheme.getInstance(blockLength),
				IECPaddingScheme.getInstance(blockLength),
				ISOPaddingScheme.getInstance(blockLength),
				PKCSPaddingScheme.getInstance(blockLength)
			};
			for (ReversiblePaddingScheme scheme : schemes) {
				Element<ByteArray> message, paddedMessage, unpaddedMessage;
				for (int i = 0; i < 100; i++) {
					message = scheme.getMessageSpace().getRandomElement(i);
					paddedMessage = scheme.pad(message);
					unpaddedMessage = scheme.unpad(paddedMessage);
					assertEquals(0, paddedMessage.getValue().getLength() % blockLength);
					assertEquals(message, unpaddedMessage);
				}
			}
		}
	}

}
