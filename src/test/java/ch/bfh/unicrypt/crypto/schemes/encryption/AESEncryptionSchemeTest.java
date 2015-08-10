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
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
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
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00".toUpperCase()));
		Element<ByteArray> message = aes.getMessageSpace().getElement("F3|44|81|EC|3C|C6|27|BA|CD|5D|C3|FB|08|F2|73|E6|97|98|C4|64|0B|AD|75|C7|C3|22|7D|B9|10|17|4E|72|96|AB|5C|2F|F6|12|D9|DF|AA|E8|C3|1F|30|C4|21|68|6A|11|8A|87|45|19|E6|4E|99|63|79|8A|50|3F|1D|35|CB|9F|CE|EC|81|28|6C|A3|E9|89|BD|97|9B|0C|B2|84|B2|6A|EB|18|74|E4|7C|A8|35|8F|F2|23|78|F0|91|44|58|C8|E0|0B|26|31|68|6D|54|EA|B8|4B|91|F0|AC|A1");
		Element<ByteArray> key = aes.getSecretKeyGenerator().getSecretKeySpace().getElement("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00");
		Element<ByteArray> encryptedMessage = aes.encrypt(key, message);
		Element<ByteArray> expectedEncryption = aes.getEncryptionSpace().getElement("03|36|76|3E|96|6D|92|59|5A|56|7C|C9|CE|53|7F|5E|A9|A1|63|1B|F4|99|69|54|EB|C0|93|95|7B|23|45|89|FF|4F|83|91|A6|A4|0C|A5|B2|5D|23|BE|DD|44|A5|97|DC|43|BE|40|BE|0E|53|71|2F|7E|2B|F5|CA|70|72|09|92|BE|ED|AB|18|95|A9|4F|AA|69|B6|32|E5|CC|47|CE|45|92|64|F4|79|8F|6A|78|BA|CB|89|C1|5E|D3|D6|01|08|A4|E2|EF|EC|8A|8E|33|12|CA|74|60|B9|04|0B|BF");
		Assert.assertEquals(encryptedMessage, expectedEncryption);
	}

	@Test
	public void testPasswordSameKeys() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key = aes.getSecretKeyGenerator().generateSecretKey("This is the Test");

		AESEncryptionScheme aes2 = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key2 = aes2.getSecretKeyGenerator().generateSecretKey("This is the Test");
		Assert.assertEquals(key, key2);
	}

	@Test
	public void testPasswordDifferentKeys() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key = aes.getSecretKeyGenerator().generateSecretKey("This is the Test");

		AESEncryptionScheme aes2 = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key2 = aes2.getSecretKeyGenerator().generateSecretKey("Thas is the Test");
		Assert.assertFalse(key.equals(key2));
	}

	@Test
	public void testEncryptionDecryptionWithPassword() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> message = aes.getMessageSpace().getElement("F3|44|81|EC|3C|C6|27|BA|CD|5D|C3|FB|08|F2|73|E6|97|98|C4|64|0B|AD|75|C7|C3|22|7D|B9|10|17|4E|72|96|AB|5C|2F|F6|12|D9|DF|AA|E8|C3|1F|30|C4|21|68|6A|11|8A|87|45|19|E6|4E|99|63|79|8A|50|3F|1D|35|CB|9F|CE|EC|81|28|6C|A3|E9|89|BD|97|9B|0C|B2|84|B2|6A|EB|18|74|E4|7C|A8|35|8F|F2|23|78|F0|91|44|58|C8|E0|0B|26|31|68|6D|54|EA|B8|4B|91|F0|AC|A1");
		Element<ByteArray> key = aes.getPasswordBasedKey("This is the Test");
		Element<ByteArray> encryptedMessage = aes.encrypt(key, message);

		AESEncryptionScheme aes2 = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));

		Element<ByteArray> expectedMessage = aes2.decrypt(key, encryptedMessage);
		Assert.assertEquals(message, expectedMessage);
	}

	@Test
	public void testEncryptionDecryptionWithSamePasswords() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> message = aes.getMessageSpace().getElement("F3|44|81|EC|3C|C6|27|BA|CD|5D|C3|FB|08|F2|73|E6|97|98|C4|64|0B|AD|75|C7|C3|22|7D|B9|10|17|4E|72|96|AB|5C|2F|F6|12|D9|DF|AA|E8|C3|1F|30|C4|21|68|6A|11|8A|87|45|19|E6|4E|99|63|79|8A|50|3F|1D|35|CB|9F|CE|EC|81|28|6C|A3|E9|89|BD|97|9B|0C|B2|84|B2|6A|EB|18|74|E4|7C|A8|35|8F|F2|23|78|F0|91|44|58|C8|E0|0B|26|31|68|6D|54|EA|B8|4B|91|F0|AC|A1");
		Element<ByteArray> key = aes.getPasswordBasedKey("This is the Test");
		Element<ByteArray> encryptedMessage = aes.encrypt(key, message);

		AESEncryptionScheme aes2 = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key2 = aes2.getPasswordBasedKey("This is the Test");
		Element<ByteArray> expectedMessage = aes2.decrypt(key2, encryptedMessage);
		Assert.assertEquals(message, expectedMessage);
	}

	@Test
	public void testEncryptionDecryptionWithDifferentPasswords() {
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> message = aes.getMessageSpace().getElement("F3|44|81|EC|3C|C6|27|BA|CD|5D|C3|FB|08|F2|73|E6|97|98|C4|64|0B|AD|75|C7|C3|22|7D|B9|10|17|4E|72|96|AB|5C|2F|F6|12|D9|DF|AA|E8|C3|1F|30|C4|21|68|6A|11|8A|87|45|19|E6|4E|99|63|79|8A|50|3F|1D|35|CB|9F|CE|EC|81|28|6C|A3|E9|89|BD|97|9B|0C|B2|84|B2|6A|EB|18|74|E4|7C|A8|35|8F|F2|23|78|F0|91|44|58|C8|E0|0B|26|31|68|6D|54|EA|B8|4B|91|F0|AC|A1");
		Element<ByteArray> key = aes.getPasswordBasedKey("This is the Test");
		Element<ByteArray> encryptedMessage = aes.encrypt(key, message);

		AESEncryptionScheme aes2 = AESEncryptionScheme.getInstance(
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.ECB,
			   ByteArray.getInstance("00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00"));
		Element<ByteArray> key2 = aes2.getPasswordBasedKey("That is the Test");
		Element<ByteArray> expectedMessage = aes2.decrypt(key2, encryptedMessage);
		assertFalse(message.equals(expectedMessage));
	}

}
