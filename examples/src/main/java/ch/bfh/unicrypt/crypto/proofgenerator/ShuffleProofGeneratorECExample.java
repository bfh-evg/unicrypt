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
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PermutationCommitmentProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ShuffleProofGenerator;
import ch.bfh.unicrypt.crypto.random.classes.RandomOracle;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.StandardECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsFp;
import java.math.BigInteger;

/**
 *
 * @author philipp
 */
public class ShuffleProofGeneratorECExample {

	public Triple createCiphertexts(int size, CyclicGroup G_q, Element encryptionPK, PermutationElement pi) {

		final ZMod Z_q = G_q.getZModOrder();
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();
		final Element g = G_q.getIndependentGenerator(0, rrs);

		// Ciphertexts
		Tuple rV = ProductGroup.getInstance(Z_q, size).getRandomElement();
		ProductGroup uVSpace = ProductGroup.getInstance(ProductGroup.getInstance(G_q, 2), size);
		Tuple uV = uVSpace.getRandomElement();
		Element[] uPrimes = new Element[size];
		for (int i = 0; i < size; i++) {
			uPrimes[i] = uV.getAt(i).apply(Tuple.getInstance(g.selfApply(rV.getAt(i)), encryptionPK.selfApply(rV.getAt(i))));
		}
		Tuple uPrimeV = PermutationFunction.getInstance(ProductGroup.getInstance(G_q, 2), size).apply(Tuple.getInstance(uPrimes), pi);

		return Triple.getInstance(uV, uPrimeV, rV);
	}

	public void proofOfShuffle(int size, CyclicGroup G_q, Element encryptionPK, PermutationElement pi, Tuple uV, Tuple uPrimeV, Tuple rV) {

		final RandomOracle ro = RandomOracle.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		// Permutation commitment
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);
		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = PermutationCommitmentProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		PermutationCommitmentProofGenerator pcpg = PermutationCommitmentProofGenerator.getInstance(scg, ecg, G_q, size, rrs);

		// Shuffle Proof Generator
		SigmaChallengeGenerator scgS = ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecgS = ShuffleProofGenerator.createNonInteractiveEValuesGenerator(G_q, size, ro);
		ShuffleProofGenerator spg = ShuffleProofGenerator.getInstance(scgS, ecgS, G_q, size, encryptionPK, rrs);

		// Proof
		Pair proofPermutation = pcpg.generate(Pair.getInstance(pi, sV), cPiV);
		Tuple privateInput = Tuple.getInstance(pi, sV, rV);
		Tuple publicInput = Tuple.getInstance(cPiV, uV, uPrimeV);
		Triple proofShuffle = spg.generate(privateInput, publicInput);

		// Verify
		// (Important: If it is not given from the context, check equality of
		//             the permutation commitments!)
		BooleanElement vPermutation = pcpg.verify(proofPermutation, cPiV);
		BooleanElement vShuffle = spg.verify(proofShuffle, publicInput);
		System.out.println("Shuffle was sucessful: " + (vPermutation.getValue() && vShuffle.getValue()));
	}

	public static void main(String[] args) {

		// Setup
		final int size = 100;
		ZModPrime f = ZModPrime.getInstance(29);
		ZModElement a = f.getElement(4);
		ZModElement b = f.getElement(20);
		ZModElement gx = f.getElement(1);
		ZModElement gy = f.getElement(5);
		BigInteger order = BigInteger.valueOf(37);
		BigInteger h = BigInteger.ONE;
		//final ECZModPrime G_q = ECZModPrime.getInstance(f, a, b, gx, gy, order, h);

		final StandardECZModPrime G_q = StandardECZModPrime.getInstance(SECECCParamsFp.secp160r1); //Possible curves secp{112,160,192,224,256,384,521}r1

		// Create random encryption key
		final Element encryptionPK = G_q.getRandomElement();

		// Create random permutation
		final Permutation permutation = Permutation.getRandomInstance(size);
		final PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		// Create example instance
		ShuffleProofGeneratorECExample ex = new ShuffleProofGeneratorECExample();

		// Create ciphertexts (uV: input, uPrimeV: shuffled output, rV: randomness of re-encryption)
		final Triple c = ex.createCiphertexts(size, G_q, encryptionPK, pi);
		final Tuple uV = (Tuple) c.getFirst();
		final Tuple uPrimeV = (Tuple) c.getSecond();
		final Tuple rV = (Tuple) c.getThird();

		// Create and verify proof
		long time = System.currentTimeMillis();
		ex.proofOfShuffle(size, G_q, encryptionPK, pi, uV, uPrimeV, rV);
		System.out.println("Finished after: " + (System.currentTimeMillis() - time) + " MilliSeconds.");

	}

}
