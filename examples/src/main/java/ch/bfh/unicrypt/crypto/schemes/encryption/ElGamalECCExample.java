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
import ch.bfh.unicrypt.crypto.encoder.classes.ProbabilisticECGroupFpEncoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.StandardECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.params.classes.SECECCParamsFp;
import java.math.BigInteger;

public class ElGamalECCExample {

	public static void example1() throws Exception {

		// Example Elgamal over ECFp with 123456789 as text to encode using ProbabilisticECGroupFpEncode
		//Generate schema and keypair
		final StandardECZModPrime g_q = StandardECZModPrime.getInstance(SECECCParamsFp.secp521r1); //Possible curves secp{112,160,192,224,256,384,521}r1
		final ElGamalEncryptionScheme elGamal = ElGamalEncryptionScheme.getInstance(g_q);
		final KeyPairGenerator keyGen = elGamal.getKeyPairGenerator();

		//Create encode/decoder
		ProbabilisticECGroupFpEncoder enc = ProbabilisticECGroupFpEncoder.getInstance(g_q);

		// Generate private key
		final Element privateKey = keyGen.generatePrivateKey();
		Example.printLine("Private Key: " + privateKey);

		//Generate pubilc key
		Element publigKey = keyGen.generatePublicKey(privateKey);
		Example.printLine("Public Key: " + publigKey);

		//encoding
		FiniteField f = g_q.getFiniteField();
		Element m = f.getElementFrom(new BigInteger("123456789"));
		ECElement<ResidueClass> message = enc.encode(m);
		Example.printLine("Message: " + m);
		Example.printLine("Message encoded: " + message);

		//Encrypt message
		final Tuple cipherText = elGamal.encrypt(publigKey, message);
		Example.printLine("Cipher Text: " + cipherText);

		//decrypt message
		Element newMessage = elGamal.decrypt(privateKey, cipherText);
		Example.printLine("Decrypted Message: " + newMessage);
		Example.printLine("Message == Decrypted Message: " + message.isEquivalent(newMessage));

		//decode message
		Element plain = enc.decode(newMessage);
		Example.printLine("Message decoded: " + plain);

	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
