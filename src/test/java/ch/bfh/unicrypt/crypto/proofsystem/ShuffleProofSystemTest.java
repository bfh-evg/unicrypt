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
import ch.bfh.unicrypt.crypto.proofsystem.classes.PermutationCommitmentProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.ReEncryptionShuffleProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.RandomOracle;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
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

public class ShuffleProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final static Element proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("ShufflerXX");

	public ShuffleProofSystemTest() {
	}

	@Test
	public void testShuffleProofGenerator() {

		final GStarMod G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = RandomOracle.getInstance();
		final RandomByteSequence randomByteSequence = HybridRandomByteSequence.getInstance();
		final DeterministicRandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();

		final int size = 5;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerators(deterministicRandomByteSequence).get(0);

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, deterministicRandomByteSequence);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(8), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement(randomByteSequence);
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Shuffle Proof Generator
		ElGamalEncryptionScheme encryptionScheme = ElGamalEncryptionScheme.getInstance(g);
		SigmaChallengeGenerator scg = ReEncryptionShuffleProofSystem.createNonInteractiveSigmaChallengeGenerator(4, null);
		ChallengeGenerator ecg = ReEncryptionShuffleProofSystem.createNonInteractiveEValuesGenerator(4, size);
		ReEncryptionShuffleProofSystem spg = ReEncryptionShuffleProofSystem.getInstance(scg, ecg, size, encryptionScheme, encryptionPK, 2, deterministicRandomByteSequence);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		Tuple proof = spg.generate(privateInput, publicInput, randomByteSequence);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testShuffleProofGenerator2() {

		final GStarMod G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final DeterministicRandomByteSequence rbs = DeterministicRandomByteSequence.getInstance();

		final int size = 10;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerators(rbs).get(0);

		// Permutation
		PermutationElement pi = PermutationGroup.getInstance(size).getRandomElement();
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rbs);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = ProductGroup.getInstance(Z_q, size).getRandomElement();
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement();
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Shuffle Proof Generator
		ElGamalEncryptionScheme encryptionScheme = ElGamalEncryptionScheme.getInstance(g);
		ReEncryptionShuffleProofSystem spg = ReEncryptionShuffleProofSystem.getInstance(size, encryptionScheme, encryptionPK, proverId, 60, 60, 20, rbs);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		Tuple proof = spg.generate(privateInput, publicInput);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testShuffleProofGenerator_Invalid() {

		final GStarMod G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = RandomOracle.getInstance();
		final DeterministicRandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();

		final int size = 5;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerators(deterministicRandomByteSequence).get(0);

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, deterministicRandomByteSequence);
		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(8), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement(deterministicRandomByteSequence);
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Shuffle Proof Generator
		ElGamalEncryptionScheme encryptionScheme = ElGamalEncryptionScheme.getInstance(g);
		SigmaChallengeGenerator scg = ReEncryptionShuffleProofSystem.createNonInteractiveSigmaChallengeGenerator(80, null);
		ChallengeGenerator ecg = ReEncryptionShuffleProofSystem.createNonInteractiveEValuesGenerator(80, size);
		ReEncryptionShuffleProofSystem spg = ReEncryptionShuffleProofSystem.getInstance(scg, ecg, size, encryptionScheme, encryptionPK, 20, deterministicRandomByteSequence);

		// Proof and verify
		// Invalid: uV with wrong permutation permuted
		Tuple uPrimeVInvalid = Tuple.getInstance(uPrimes[2], uPrimes[4], uPrimes[3], uPrimes[0], uPrimes[1]);
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeVInvalid);

		Tuple proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		boolean v = spg.verify(proof, publicInput);
		assertTrue(!v);

		// Invalid: Wrong sV values
		Tuple sVInvalid = Tuple.getInstance(Z_q.getElement(23), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6));
		privateInput = Tuple.getInstance(pi, sVInvalid, rV);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		v = spg.verify(proof, publicInput);
		assertTrue(!v);

		// Invalid: Wrong rV values
		Tuple rVInvalid = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(18), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		privateInput = Tuple.getInstance(pi, sV, rVInvalid);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		proof = spg.generate(privateInput, publicInput, deterministicRandomByteSequence);
		v = spg.verify(proof, publicInput);
		assertTrue(!v);
	}

	@Test
	public void testProofOfShuffle_COMPLETE() {

		final GStarMod G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = RandomOracle.getInstance();
		final DeterministicRandomByteSequence rbs = DeterministicRandomByteSequence.getInstance();

		final int size = 100;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerators(rbs).get(0);

		// Permutation
		PermutationElement pi = PermutationGroup.getInstance(size).getRandomElement();

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rbs);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = ProductGroup.getInstance(Z_q, size).getRandomElement();
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement();
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofSystem.createNonInteractiveSigmaChallengeGenerator(80, null);
		ChallengeGenerator ecg = PermutationCommitmentProofSystem.createNonInteractiveEValuesGenerator(80, size);
		PermutationCommitmentProofSystem pcpg = PermutationCommitmentProofSystem.getInstance(scg, ecg, G_q, size, 20, rbs);

		// Shuffle Proof Generator
		ElGamalEncryptionScheme encryptionScheme = ElGamalEncryptionScheme.getInstance(g);
		SigmaChallengeGenerator scgS = ReEncryptionShuffleProofSystem.createNonInteractiveSigmaChallengeGenerator(80, null);
		ChallengeGenerator ecgS = ReEncryptionShuffleProofSystem.createNonInteractiveEValuesGenerator(80, size);
		ReEncryptionShuffleProofSystem spg = ReEncryptionShuffleProofSystem.getInstance(scgS, ecgS, size, encryptionScheme, encryptionPK, 20, rbs);

		// Proof
		Tuple proofPermutation = pcpg.generate(Pair.getInstance(pi, sV), cPiV);

		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);
		Tuple proofShuffle = spg.generate(privateInput, publicInput);

		// Verify
		// (Important: If it is not given from the context, check equality of
		//             the permutation commitments!)
		boolean vPermutation = pcpg.verify(proofPermutation, cPiV);

		boolean vShuffle = spg.verify(proofShuffle, publicInput);

		assertTrue(vPermutation && vShuffle);

	}

}
