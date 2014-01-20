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
package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PermutationCommitmentTunedProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ShuffleProofGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGeneratorCounterMode;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import ch.bfh.unicrypt.math.helper.Permutation;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ShuffleProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	public ShuffleProofGeneratorTest() {
	}

	@Test
	public void testShuffleProofGenerator() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();
		final RandomGenerator randomGenerator = PseudoRandomGeneratorCounterMode.getInstance();

		final int size = 5;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerator(0, rrs);

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(8), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement(randomGenerator);
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Shuffle Proof Generator
		SigmaChallengeGenerator scg = ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = ShuffleProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		ShuffleProofGenerator spg = ShuffleProofGenerator.getInstance(scg, ecg, G_q, size, encryptionPK, rrs);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		Triple proof = spg.generate(privateInput, publicInput, randomGenerator);
		BooleanElement v = spg.verify(proof, publicInput);
		assertTrue(v.getValue());
	}

	@Test
	public void testShuffleProofGenerator2() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		final int size = 10;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerator(0, rrs);

		// Permutation
		Permutation permutation = Permutation.getRandomInstance(size);
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

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
		SigmaChallengeGenerator scg = ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = ShuffleProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		ShuffleProofGenerator spg = ShuffleProofGenerator.getInstance(scg, ecg, G_q, size, encryptionPK, rrs);

		// Proof and verify
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		Triple proof = spg.generate(privateInput, publicInput);
		BooleanElement v = spg.verify(proof, publicInput);
		assertTrue(v.getValue());
	}

	@Test
	public void testShuffleProofGenerator_Invalid() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomGenerator randomGenerator = PseudoRandomGeneratorCounterMode.getInstance();
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		final int size = 5;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerator(0, rrs);

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);
		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Ciphertexts
		Tuple rV = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(8), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement(randomGenerator);
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		// Shuffle Proof Generator
		SigmaChallengeGenerator scg = ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = ShuffleProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		ShuffleProofGenerator spg = ShuffleProofGenerator.getInstance(scg, ecg, G_q, size, encryptionPK, rrs);

		// Proof and verify
		// Invalid: uV with wrong permutation permuted
		Tuple uPrimeVInvalid = Tuple.getInstance(uPrimes[2], uPrimes[4], uPrimes[3], uPrimes[0], uPrimes[1]);
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeVInvalid);

		Triple proof = spg.generate(privateInput, publicInput, randomGenerator);
		BooleanElement v = spg.verify(proof, publicInput);
		assertTrue(!v.getValue());

		// Invalid: Wrong sV values
		Tuple sVInvalid = Tuple.getInstance(Z_q.getElement(23), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(6));
		privateInput = Tuple.getInstance(pi, sVInvalid, rV);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		proof = spg.generate(privateInput, publicInput, randomGenerator);
		v = spg.verify(proof, publicInput);
		assertTrue(!v.getValue());

		// Invalid: Wrong rV values
		Tuple rVInvalid = Tuple.getInstance(Z_q.getElement(7), Z_q.getElement(18), Z_q.getElement(9), Z_q.getElement(10), Z_q.getElement(11));
		privateInput = Tuple.getInstance(pi, sV, rVInvalid);
		publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);

		proof = spg.generate(privateInput, publicInput, randomGenerator);
		v = spg.verify(proof, publicInput);
		assertTrue(!v.getValue());
	}

	@Test
	public void testProofOfShuffle_COMPLETE() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		final int size = 100;
		final Element encryptionPK = G_q.getElement(4);
		final Element g = G_q.getIndependentGenerator(0, rrs);

		// Permutation
		Permutation permutation = Permutation.getRandomInstance(size);
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);
		System.out.println("Permutation-Commitment");
		logAndReset();

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
//		SigmaChallengeGenerator scgP = PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
//		ChallengeGenerator ecgP = PermutationCommitmentProofGenerator.createNonInteractiveMultiChallengeGenerator(G_q, size, ro);
//		PermutationCommitmentProofGenerator pcpg = PermutationCommitmentProofGenerator.getInstance(scgP, ecgP, G_q, size, rrs);
		SigmaChallengeGenerator scg = PermutationCommitmentTunedProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = PermutationCommitmentTunedProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		PermutationCommitmentTunedProofGenerator pcpg = PermutationCommitmentTunedProofGenerator.getInstance(scg, ecg, G_q, size, rrs);

		// Shuffle Proof Generator
		SigmaChallengeGenerator scgS = ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecgS = ShuffleProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		ShuffleProofGenerator spg = ShuffleProofGenerator.getInstance(scgS, ecgS, G_q, size, encryptionPK, rrs);

		System.out.println("Mixing");
		logAndReset();

		// Proof
		Pair proofPermutation = pcpg.generate(Pair.getInstance(pi, sV), cPiV);
		System.out.println("Permutation-Proof");
		logAndReset();

		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);
		Triple proofShuffle = spg.generate(privateInput, publicInput);
		System.out.println("Shuffle-Proof");
		logAndReset();

		// Verify
		// (Important: If it is not given from the context, check equality of
		//             the permutation commitments!)
		BooleanElement vPermutation = pcpg.verify(proofPermutation, cPiV);
		System.out.println("Verify Permutation-Proof");
		logAndReset();
		BooleanElement vShuffle = spg.verify(proofShuffle, publicInput);
		System.out.println("Verify Shuffle Proof");
		logAndReset();
		assertTrue(vPermutation.getValue() && vShuffle.getValue());

	}

	public void logAndReset() {
		System.out.println("COUNTER     : " + (GStarMod.modPowCounter_SelfApply - GStarMod.modPowCounter_IsGenerator));
		System.out.println("COUNTER Cont: " + GStarMod.modPowCounter_Contains);
		GStarMod.modPowCounter_SelfApply = 0;
		GStarMod.modPowCounter_Contains = 0;
		GStarMod.modPowCounter_IsGenerator = 0;
	}

}
