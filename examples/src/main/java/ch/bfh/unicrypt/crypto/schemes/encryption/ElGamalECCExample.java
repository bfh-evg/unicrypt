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

public class ElGamalECCExample {

//	public static void example1() {
//
//		// Example Elgamal over ECFp
//		final StandardECZModPrime g_q = StandardECZModPrime.getInstance(SECECCParamsFp.secp521r1); //Possible curves secp{112,160,192,224,256,384,521}r1
//		final ElGamalEncryptionScheme elGamal = ElGamalEncryptionScheme.getInstance(g_q);
//		final KeyPairGenerator keyGen = elGamal.getKeyPairGenerator();
//
//		ProbabilisticECGroupFpEncoder enc = ProbabilisticECGroupFpEncoder.getInstance(g_q);
//
//		// Generate private key
//		final Element privateKey = keyGen.generatePrivateKey();
//		System.out.println("Private Key: " + privateKey);
//
//		//Generate pubilc key
//		long time = System.currentTimeMillis();
//		Element publigKey = keyGen.generatePublicKey(privateKey);
//		time = System.currentTimeMillis() - time;
//		System.out.println("Public Key: " + publigKey);
//		System.out.println("Time for public key: " + time + " ms");
//
//		//encoding
//		FiniteField f = g_q.getFiniteField();
//		DualisticElement m = f.getElement(123456789);
//		time = System.currentTimeMillis();
//		ECZModPrimeElement message = enc.encode(m);
//		time = System.currentTimeMillis() - time;
//		System.out.println("Message: " + m);
//		System.out.println("Message encoded: " + message);
//		System.out.println("Time for encoding: " + time + " ms");
//
//		//Encrypt message
//		time = System.currentTimeMillis();
//		final Tuple cipherText = elGamal.encrypt(publigKey, message);
//		time = System.currentTimeMillis() - time;
//		System.out.println("Cipher Text: " + cipherText);
//		System.out.println("Time for encryption: " + time + " ms");
//
//		//decrypt message
//		time = System.currentTimeMillis();
//		Element newMessage = elGamal.decrypt(privateKey, cipherText);
//		time = System.currentTimeMillis() - time;
//		System.out.println("New Message: " + newMessage);
//		System.out.println("Message == New Message: " + message.isEquivalent(newMessage));
//		System.out.println("Time for decryption: " + time + " ms");
//
//		//decode message
//		time = System.currentTimeMillis();
//		ZModElement plain = enc.decode(newMessage);
//		time = System.currentTimeMillis() - time;
//		System.out.println("Message decoded: " + plain);
//		System.out.println("Time for decoding: " + time + " ms");
//
//	}
	public static void main(final String[] args) {
		Example.runExamples();
	}

}
