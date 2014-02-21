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
package ch.bfh.unicrypt.crypto.schemes.encryption;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.crypto.encoder.classes.StringToByteArrayEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.AESEncryptionScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.classes.PKCSPaddingScheme;
import ch.bfh.unicrypt.crypto.schemes.padding.interfaces.ReversiblePaddingScheme;
import ch.bfh.unicrypt.helper.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class AESEncryptionExample {

	public static void example1() {

		AESEncryptionScheme aes = AESEncryptionScheme.getInstance();

		// a random key (default length = 16 bytes)
		Element key = aes.generateSecretKey();

		// a random message (length = 32 bytes = multiple of block length)
		Element message = aes.getMessageSpace().getRandomElement(32);

		// perform the encryption
		Element encryptedMessage = aes.encrypt(key, message);

		// perform the decryption
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);

		Example.setLabelLength("Encrypted Message");
		Example.printLine("Key", key);
		Example.printLine("Message", message);
		Example.printLine("Encrypted Message", encryptedMessage);
		Example.printLine("Decrypted Message", decryptedMessage);
	}

	public static void example2() {

		// Random message (length = 20 bytes)
		Element message = ByteArrayMonoid.getInstance().getRandomElement(20);

		// Apply padding scheme (increase length to 32 bytes)
		ReversiblePaddingScheme pkcs = PKCSPaddingScheme.getInstance(16);
		Element paddedMessage = pkcs.pad(message);

		// Perform the encryption
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance();
		Element key = aes.generateSecretKey();
		Element encryptedMessage = aes.encrypt(key, paddedMessage);

		// Perform the decryption
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);

		// Apply padding scheme (decrease length to 20 bytes)
		Element unpaddedMessage = pkcs.unpad(decryptedMessage);
		Example.setLabelLength("Encrypted Message");
		Example.printLine("Key", key);
		Example.printLine("Message", message);
		Example.printLine("Padded Message", paddedMessage);
		Example.printLine("Encrypted Message", encryptedMessage);
		Example.printLine("Decrypted Message", decryptedMessage);
		Example.printLine("Unpadded Message", unpaddedMessage);
	}

	public static void example3() {

		// Create default AES encryption scheme
		AESEncryptionScheme aes = AESEncryptionScheme.getInstance();

		// Define alphabet, string monoid, and encoder
		Alphabet alphabet = Alphabet.ALPHANUMERIC;
		StringMonoid stringMonoid = StringMonoid.getInstance(alphabet);
		Encoder encoder = StringToByteArrayEncoder.getInstance(stringMonoid);

		// Create a random key (default length = 16 bytes)
		Element key = aes.generateSecretKey();

		// Create, encode, and encrypt string message
		Element message = stringMonoid.getElement("HalloWorld");
		Element encodedMessage = encoder.encode(message);
		Element encryptedMessage = aes.encrypt(key, encodedMessage);

		// Perform the decryption and decoding
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);
		Element decodedMessage = encoder.decode(decryptedMessage);

		Example.setLabelLength("Encrypted Message");
		Example.printLine("Key", key);
		Example.printLine("Message", message);
		Example.printLine("EncodedMessage", encodedMessage);
		Example.printLine("Encrypted Message", encryptedMessage);
		Example.printLine("Decrypted Message", decryptedMessage);
		Example.printLine("DecodedMessage", decodedMessage);
	}

	public static void example4() {

		AESEncryptionScheme aes = AESEncryptionScheme.getInstance(
			   // AESEncryptionScheme.KeyLength.KEY192,
			   AESEncryptionScheme.KeyLength.KEY128,
			   AESEncryptionScheme.Mode.CBC,
			   AESEncryptionScheme.DEFAULT_IV);

		// a random key (length = 16 bytes)
		Element key = aes.generateSecretKey();

		// a random message (length = 32 bytes)
		Element message = aes.getMessageSpace().getRandomElement(32);

		// perform the encryption
		Element encryptedMessage = aes.encrypt(key, message);

		// perform the decryption
		Element decryptedMessage = aes.decrypt(key, encryptedMessage);

		Example.setLabelLength("Encrypted Message");
		Example.printLine("Key", key);
		Example.printLine("Message", message);
		Example.printLine("Encrypted Message", encryptedMessage);
		Example.printLine("Decrypted Message", decryptedMessage);
	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
