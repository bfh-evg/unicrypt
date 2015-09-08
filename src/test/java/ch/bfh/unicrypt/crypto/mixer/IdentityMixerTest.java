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

import ch.bfh.unicrypt.crypto.mixer.classes.IdentityMixer;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
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
public class IdentityMixerTest {

	@Test
	public void testIdentityMixer() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(167);
		int size = 100;

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();

		IdentityMixer mixer = IdentityMixer.getInstance(G_q, size);
		Tuple shuffledMessages = mixer.shuffle(messages, DeterministicRandomByteSequence.getInstance());

		// Verify shuffle function
		DeterministicRandomByteSequence rrs = DeterministicRandomByteSequence.getInstance();
		PermutationGroup.getInstance(size).getRandomElement(rrs);
		Element r = G_q.getZModOrder().getRandomElement(rrs);
		Element[] shuffledMessages2 = new Element[size];
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			shuffledMessages2[i] = messages.getAt(i).selfApply(r);

			for (int j = 0; j < size; j++) {
				if (shuffledMessages2[i].isEquivalent(shuffledMessages.getAt(j))) {
					contains = true;
				}
			}
			assertTrue(contains);
		}

		// Verifiy permutation
		assertTrue(!shuffledMessages.isEquivalent(Tuple.getInstance(shuffledMessages2)));
	}

	@Test
	public void testIdentityMixerDeterministic() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(167);
		int size = 10;

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		int[] pi = new int[]{4, 5, 6, 1, 3, 2, 9, 7, 8, 0};
		PermutationElement permutation = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(pi));
		Element alpha = G_q.getZModOrder().getElement(7);

		IdentityMixer mixer = IdentityMixer.getInstance(G_q, size);
		Tuple shuffledMessges = mixer.shuffle(messages, permutation, alpha);

		// Verify shuffle
		for (int i = 0; i < size; i++) {
			Element e = messages.getAt(pi[i]).selfApply(alpha);
			assertTrue(e.isEquivalent(shuffledMessges.getAt(i)));
		}
	}

	@Test
	public void testIdentityMixerSizeOne() {

		CyclicGroup G_q = GStarModSafePrime.getInstance(167);
		int size = 1;

		Tuple messages = ProductGroup.getInstance(G_q, size).getRandomElement();
		int[] pi = new int[]{0};
		PermutationElement permutation = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(pi));
		Element alpha = G_q.getZModOrder().getElement(7);

		IdentityMixer mixer = IdentityMixer.getInstance(G_q, size);
		Tuple shuffledMessges = mixer.shuffle(messages, permutation, alpha);

		// Verify shuffle
		for (int i = 0; i < size; i++) {
			Element e = messages.getAt(pi[i]).selfApply(alpha);
			assertTrue(e.isEquivalent(shuffledMessges.getAt(i)));
		}
	}

}
