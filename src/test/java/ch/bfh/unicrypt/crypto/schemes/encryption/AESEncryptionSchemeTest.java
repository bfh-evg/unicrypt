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
 * Security in the Information Society (RISIS), E-Voting Group (EVG)
 * Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption;

import ch.bfh.unicrypt.crypto.schemes.encryption.classes.AESEncryptionScheme;
import ch.bfh.unicrypt.helper.array.ByteArray;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class AESEncryptionSchemeTest {

	@Test
	public void testEncryptionDecryption() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance();

		Element message = aes.getMessageSpace().getRandomElement(512);
		Element key = aes.getSecretKeyGenerator().generateSecretKey();
		Element encryptedMessage = aes.encrypt(key, message);
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);
		Assert.assertEquals(message, decryptedMessage);
	}

	@Test
	public void testEncryptionDecryptionGFSBox() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> message = aes.getMessageSpace().getElement("f3|44|81|ec|3c|c6|27|ba|cd|5d|c3|fb|08|f2|73|e6|97|98|c4|64|0b|ad|75|c7|c3|22|7d|b9|10|17|4e|72|96|ab|5c|2f|f6|12|d9|df|aa|e8|c3|1f|30|c4|21|68|6a|11|8a|87|45|19|e6|4e|99|63|79|8a|50|3f|1d|35|cb|9f|ce|ec|81|28|6c|a3|e9|89|bd|97|9b|0c|b2|84|b2|6a|eb|18|74|e4|7c|a8|35|8f|f2|23|78|f0|91|44|58|c8|e0|0b|26|31|68|6d|54|ea|b8|4b|91|f0|ac|a1");
		Element<ByteArray> key = aes.getSecretKeyGenerator().getSecretKeySpace().getElement("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00");
		Element<ByteArray> encryptedMessage = aes.encrypt(key, message);
		Element<ByteArray> expectedEncryption = aes.getEncryptionSpace().getElement("03|36|76|3e|96|6d|92|59|5a|56|7c|c9|ce|53|7f|5e|a9|a1|63|1b|f4|99|69|54|eb|c0|93|95|7b|23|45|89|ff|4f|83|91|a6|a4|0c|a5|b2|5d|23|be|dd|44|a5|97|dc|43|be|40|be|0e|53|71|2f|7e|2b|f5|ca|70|72|09|92|be|ed|ab|18|95|a9|4f|aa|69|b6|32|e5|cc|47|ce|45|92|64|f4|79|8f|6a|78|ba|cb|89|c1|5e|d3|d6|01|08|a4|e2|ef|ec|8a|8e|33|12|ca|74|60|b9|04|0b|bf");
		Assert.assertEquals(encryptedMessage, expectedEncryption);
	}

}
