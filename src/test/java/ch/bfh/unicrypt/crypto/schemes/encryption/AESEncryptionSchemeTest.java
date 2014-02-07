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

		Element message = aes.getMessageSpace().getRandomElement(510);
		Element key = aes.getKeyGenerator().generateSecretKey();
		Element encryptedMessage = aes.encrypt(key, message);
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);

		Assert.assertEquals(message, decryptedMessage);

//		System.out.println(key);
//		System.out.println(message);
//		System.out.println(encryptedMessage);
//		System.out.println(decryptedMessage);
	}

//	@Test
//	public void testEncryptionDecryptionGFSBox() {
// IV = 00000000000000000000000000000000
// KEY = 00000000000000000000000000000000
// plaintext f34481ec3cc627bacd5dc3fb08f273e69798c4640bad75c7c3227db910174e7296ab5c2ff612d9dfaae8c31f30c421686a118a874519e64e9963798a503f1d35cb9fceec81286ca3e989bd979b0cb284b26aeb1874e47ca8358ff22378f0914458c8e00b2631686d54eab84b91f0aca1
// ciphertext 0336763e966d92595a567cc9ce537f5ea9a1631bf4996954ebc093957b234589ff4f8391a6a40ca5b25d23bedd44a597dc43be40be0e53712f7e2bf5ca70720992beedab1895a94faa69b632e5cc47ce459264f4798f6a78bacb89c15ed3d60108a4e2efec8a8e3312ca7460b9040bbf
//	}
}
