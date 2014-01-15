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
package ch.bfh.unicrypt.crypto.mixer;

import ch.bfh.unicrypt.crypto.mixer.classes.ReEncryptionMixer;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;

/**
 *
 * @author philipp
 */
public class ReEncryptionMixerExample {

	public static void elGamalShuffleExample() {

		// P R E P A R E
		//---------------
		// Create cyclic group and get default generator
		CyclicGroup G_q = GStarModSafePrime.getRandomInstance(160);
		Element g = G_q.getDefaultGenerator();

		// Set size
		int size = 10;

		// Create ElGamal keys and encryption system
		ElGamalEncryptionScheme es = ElGamalEncryptionScheme.getInstance(g);
		Element privateKey = es.getKeyPairGenerator().generatePrivateKey();
		Element publicKey = es.getKeyPairGenerator().generatePublicKey(privateKey);

		// Create ciphertexts
		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		Element[] ciphertextArray = new Element[size];
		for (int i = 0; i < size; i++) {
			ciphertextArray[i] = es.encrypt(publicKey, messages.getAt(i));
		}
		Tuple ciphertexts = Tuple.getInstance(ciphertextArray);

		// S H U F F L E
		//---------------
		// Create mixer and shuffle
		ReEncryptionMixer mixer = ReEncryptionMixer.getInstance(es, publicKey, size);
		Tuple shuffledCiphertexts = mixer.shuffle(ciphertexts);
	}

	public static void elGamalShuffleExample2() {

		// P R E P A R E
		//---------------
		// Create cyclic group and get default generator
		CyclicGroup G_q = GStarModSafePrime.getRandomInstance(160);
		Element g = G_q.getDefaultGenerator();

		// Set size
		int size = 10;

		// Create ElGamal keys and encryption system
		ElGamalEncryptionScheme es = ElGamalEncryptionScheme.getInstance(g);
		Element privateKey = es.getKeyPairGenerator().generatePrivateKey();
		Element publicKey = es.getKeyPairGenerator().generatePublicKey(privateKey);

		// Create ciphertexts
		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		Element[] ciphertextArray = new Element[size];
		for (int i = 0; i < size; i++) {
			ciphertextArray[i] = es.encrypt(publicKey, messages.getAt(i));
		}
		Tuple ciphertexts = Tuple.getInstance(ciphertextArray);

		// Create permutation
		PermutationElement permutation = PermutationGroup.getInstance(size).getRandomElement();

		// Create randomizations
		Tuple randomizations = ProductGroup.getInstance(G_q.getZModOrder(), size).getRandomElement();

		// S H U F F L E
		//---------------
		// Create mixer and shuffle
		ReEncryptionMixer mixer = ReEncryptionMixer.getInstance(es, publicKey, size);
		Tuple shuffledCiphertexts = mixer.shuffle(ciphertexts, permutation, randomizations);
	}

	public static void main(String args[]) {
		ReEncryptionMixerExample.elGamalShuffleExample();
		ReEncryptionMixerExample.elGamalShuffleExample2();
	}

}
