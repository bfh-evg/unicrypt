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
package ch.bfh.unicrypt.crypto.proofsystem;

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.classes.IdentityShuffleProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class IdentityShuffleProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final static Element proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("ShufflerXX");

	@Test
	public void testIdentityShuffleProofGenerator() {

		final GStarMod G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final DeterministicRandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();
		final RandomByteSequence randomByteSequence = HybridRandomByteSequence.getInstance();

		final int size = 5;
		final Element alpha = Z_q.getElement(4);
		final Element gK_1 = G_q.getIndependentGenerators(deterministicRandomByteSequence).get(0);
		final Element gK = gK_1.selfApply(alpha);

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, deterministicRandomByteSequence);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6));
		Tuple cPiV = pcs.commit(pi, sV);

		// Identities
		ProductGroup uVSpace = ProductGroup.getInstance(G_q, size);
		Tuple uV = uVSpace.getRandomElement(randomByteSequence);
		Tuple uPrimeV = PermutationFunction.getInstance(G_q, size).apply(uV.selfApply(alpha), pi);

		// Identity Shuffle Proof Generator
		SigmaChallengeGenerator scg = IdentityShuffleProofSystem.createNonInteractiveSigmaChallengeGenerator(4, null);
		ChallengeGenerator ecg = IdentityShuffleProofSystem.createNonInteractiveEValuesGenerator(4, size);
		IdentityShuffleProofSystem spg = IdentityShuffleProofSystem.getInstance(scg, ecg, size, G_q, 2, deterministicRandomByteSequence);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, alpha);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV, gK_1, gK);

		Tuple proof = spg.generate(privateInput, publicInput, randomByteSequence);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testIdentityShuffleProofGenerator2() {

		final GStarMod G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final DeterministicRandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();
		final RandomByteSequence randomByteSequence = HybridRandomByteSequence.getInstance();

		final int size = 10;
		final Element alpha = Z_q.getElement(4);
		final Element gK_1 = G_q.getIndependentGenerators(deterministicRandomByteSequence).get(0);
		final Element gK = gK_1.selfApply(alpha);

		// Permutation
		PermutationElement pi = PermutationGroup.getInstance(size).getRandomElement();
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, deterministicRandomByteSequence);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Identities
		ProductGroup uVSpace = ProductGroup.getInstance(G_q, size);
		Tuple uV = uVSpace.getRandomElement(randomByteSequence);
		Tuple uPrimeV = PermutationFunction.getInstance(G_q, size).apply(uV.selfApply(alpha), pi);

		// Identity Shuffle Proof Generator
		IdentityShuffleProofSystem spg = IdentityShuffleProofSystem.getInstance(size, G_q, proverId, 60, 60, 20, deterministicRandomByteSequence);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, alpha);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV, gK_1, gK);

		Tuple proof = spg.generate(privateInput, publicInput, randomByteSequence);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testIdentityShuffleProofGenerator_invalid() {

		final GStarMod G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final DeterministicRandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();

		final int size = 10;
		final Element alpha = Z_q.getElement(4);
		final Element gK_1 = G_q.getIndependentGenerators(deterministicRandomByteSequence).get(0);
		final Element gK = gK_1.selfApply(alpha);

		// Permutation
		PermutationElement pi = PermutationGroup.getInstance(size).getRandomElement();
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, deterministicRandomByteSequence);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Identities
		ProductGroup uVSpace = ProductGroup.getInstance(G_q, size);
		Tuple uV = uVSpace.getRandomElement(deterministicRandomByteSequence);
		Tuple uPrimeV = PermutationFunction.getInstance(G_q, size).apply(uV.selfApply(alpha), pi);

		// Identity Shuffle Proof Generator
		IdentityShuffleProofSystem spg = IdentityShuffleProofSystem.getInstance(size, G_q, proverId, 60, 60, 20, deterministicRandomByteSequence);

		// Proof and verify
		// -> invalid alpha
		Tuple privateInput = Tuple.getInstance(pi, sV, alpha.apply(Z_q.getElement(2)));
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV, gK_1, gK);

		Tuple proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(!v);

		// -> differnt alpha for gk and identities
		privateInput = Tuple.getInstance(pi, sV, alpha);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV, gK_1, gK_1.selfApply(3));

		proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		v = spg.verify(proof, publicInput);
		assertTrue(!v);

		// -> differnt alpha for gk and identities
		privateInput = Tuple.getInstance(pi, pcs.getRandomizationSpace().getRandomElement(), alpha);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV, gK_1, gK);

		proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		v = spg.verify(proof, publicInput);
		assertTrue(!v);
	}

}
