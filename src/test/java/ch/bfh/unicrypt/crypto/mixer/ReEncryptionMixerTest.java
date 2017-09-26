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
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.prime.SafePrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class ReEncryptionMixerTest {

	@Test
	public void testReEncryptionMixer() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(SafePrime.getRandomInstance(160));
		Element g = G_q.getDefaultGenerator();
		Element sk = G_q.getZModOrder().getElement(7);
		Element pk = g.selfApply(sk);
		int size = 10;

		ElGamalEncryptionScheme es = ElGamalEncryptionScheme.getInstance(g);

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		Element[] ciphertexts = new Element[size];

		for (int i = 0; i < size; i++) {
			ciphertexts[i] = es.encrypt(pk, messages.getAt(i));
		}

		ReEncryptionMixer mixer = ReEncryptionMixer.getInstance(es, pk, size);
		Tuple shuffledCiphertexts = mixer.shuffle(Tuple.getInstance(ciphertexts));

		// Check that ciphetexts have been re-encrypted
		assertTrue(!shuffledCiphertexts.isEquivalent(Tuple.getInstance(ciphertexts)));
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (int j = 0; j < size; j++) {
				if (shuffledCiphertexts.getAt(i).isEquivalent(ciphertexts[j])) {
					contains = true;
					break;
				}
			}
			assertTrue(!contains);
		}

		Element[] decryptedMessages = new Element[size];
		for (int i = 0; i < shuffledCiphertexts.getArity(); i++) {
			decryptedMessages[i] = es.decrypt(sk, shuffledCiphertexts.getAt(i));
		}

		// Check that messages have been permuted but are still the same
		assertTrue(!messages.isEquivalent(Tuple.getInstance(decryptedMessages)));
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (int j = 0; j < size; j++) {
				if (messages.getAt(i).isEquivalent(decryptedMessages[j])) {
					contains = true;
					break;
				}
			}
			assertTrue(contains);
		}
	}

	@Test
	public void testReEncryptionMixerDeterministic() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(167);
		ZMod Z_q = G_q.getZModOrder();
		Element g = G_q.getDefaultGenerator();
		Element sk = G_q.getZModOrder().getElement(7);
		Element pk = g.selfApply(sk);
		int size = 10;

		ElGamalEncryptionScheme es = ElGamalEncryptionScheme.getInstance(g);

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		Element[] ciphertexts = new Element[size];
		for (int i = 0; i < size; i++) {
			ciphertexts[i] = es.encrypt(pk, messages.getAt(i));
		}

		int[] pi = new int[]{5, 6, 1, 4, 3, 2, 9, 0, 8, 7};
		PermutationElement permutation = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(pi));
		Tuple randomizations = Tuple.getInstance(Z_q.getElement(3), Z_q.getElement(5), Z_q.getElement(8), Z_q.getElement(1), Z_q.getElement(34), Z_q.getElement(31), Z_q.getElement(17), Z_q.getElement(2), Z_q.getElement(9), Z_q.getElement(67));

		ReEncryptionMixer mixer = ReEncryptionMixer.getInstance(es, pk, size);
		Tuple shuffledCiphertexts = mixer.shuffle(Tuple.getInstance(ciphertexts), permutation, randomizations);

		// Verify shuffle
		for (int i = 0; i < size; i++) {

			Element e = es.reEncrypt(pk, ciphertexts[pi[i]], randomizations.getAt(pi[i]));
			assertTrue(e.isEquivalent(shuffledCiphertexts.getAt(i)));
		}
	}

	@Test
	public void testReEncryptionMixerSizeOne() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(167);
		ZMod Z_q = G_q.getZModOrder();
		Element g = G_q.getDefaultGenerator();
		Element sk = G_q.getZModOrder().getElement(7);
		Element pk = g.selfApply(sk);
		int size = 1;

		ElGamalEncryptionScheme es = ElGamalEncryptionScheme.getInstance(g);

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		Element[] ciphertexts = new Element[size];
		for (int i = 0; i < size; i++) {
			ciphertexts[i] = es.encrypt(pk, messages.getAt(i));
		}

		int[] pi = new int[]{0};
		PermutationElement permutation = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(pi));
		Tuple randomizations = Tuple.getInstance(Z_q.getElement(3));

		ReEncryptionMixer mixer = ReEncryptionMixer.getInstance(es, pk, size);
		Tuple shuffledCiphertexts = mixer.shuffle(Tuple.getInstance(ciphertexts), permutation, randomizations);

		// Verify shuffle
		for (int i = 0; i < size; i++) {
			Element e = es.reEncrypt(pk, ciphertexts[pi[i]], randomizations.getAt(pi[i]));
			assertTrue(e.isEquivalent(shuffledCiphertexts.getAt(i)));
		}
	}

}
