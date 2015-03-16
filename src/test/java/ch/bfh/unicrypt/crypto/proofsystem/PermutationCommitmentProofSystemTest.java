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
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.helper.Permutation;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.random.classes.CounterModeRandomByteSequence;
import ch.bfh.unicrypt.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;
import java.lang.reflect.Field;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PermutationCommitmentProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	@Test
	public void testPermutationCommitemntProofGenerator() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomByteSequence randomGenerator = CounterModeRandomByteSequence.getInstance();
		final ReferenceRandomByteSequence rrs = ReferenceRandomByteSequence.getInstance();

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 1, 4, 0, 2});  //{3, 2, 1, 4, 0} {3, 1, 4, 0, 2}
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofSystem.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = PermutationCommitmentProofSystem.createNonInteractiveEValuesGenerator(G_q, size);
		PermutationCommitmentProofSystem pcpg = PermutationCommitmentProofSystem.getInstance(scg, ecg, G_q, size);

		// Proof and verify
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV, randomGenerator);
		boolean v = pcpg.verify(proof, cPiV);
		assertTrue(v);
	}

	@Test
	public void testPermutationCommitemntProofGenerator2() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final ReferenceRandomByteSequence rrs = ReferenceRandomByteSequence.getInstance();

		final int size = 20;

		// Permutation
		Permutation permutation = Permutation.getRandomInstance(size);
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofSystem.createNonInteractiveSigmaChallengeGenerator(G_q, size, 60, null);
		ChallengeGenerator ecg = PermutationCommitmentProofSystem.createNonInteractiveEValuesGenerator(G_q, size, 60, ro);
		PermutationCommitmentProofSystem pcpg = PermutationCommitmentProofSystem.getInstance(scg, ecg, G_q, size, 20, rrs);

		// Proof and verify
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV);
		boolean v = pcpg.verify(proof, cPiV);
		assertTrue(v);
	}

	@Test
	public void testPermutationCommitemntProofGenerator_Invalid() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomByteSequence randomGenerator = CounterModeRandomByteSequence.getInstance();
		final ReferenceRandomByteSequence rrs = ReferenceRandomByteSequence.getInstance();

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 4, 0, 1});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7));
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		PermutationCommitmentProofSystem pcpg = PermutationCommitmentProofSystem.getInstance(G_q, size);

		// Proof and verify
		// Invalid: Wrong sV
		Tuple sVInvalid = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(12), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7));
		Pair proof = pcpg.generate(Pair.getInstance(pi, sVInvalid), cPiV, randomGenerator);
		boolean v = pcpg.verify(proof, cPiV);
		assertTrue(!v);

		// Invalid: Wrong pi
		PermutationElement piInvalid = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(new int[]{3, 0, 4, 2, 1}));
		proof = pcpg.generate(Pair.getInstance(piInvalid, sV), cPiV, randomGenerator);
		v = pcpg.verify(proof, cPiV);
		assertTrue(!v);
	}

	@Test
	public void testPermutationCommitemntProofGenerator_InvalidPermutation() throws Exception {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomByteSequence randomGenerator = CounterModeRandomByteSequence.getInstance(ByteArray.getInstance((byte) 7));
		final ReferenceRandomByteSequence rrs = ReferenceRandomByteSequence.getInstance();

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 4, 0, 1});
		assertTrue(permutation.permute(1) == 2);
		assertTrue(permutation.permute(2) == 4);

		// Manipulate permutation
		Field permVectorField = Permutation.class.getDeclaredField("permutationVector");
		permVectorField.setAccessible(true);
		permVectorField.set(permutation, new int[]{3, 2, 2, 0, 1});
		assertTrue(permutation.permute(1) == 2);
		assertTrue(permutation.permute(2) == 2);

		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);
		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);
		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7));
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		PermutationCommitmentProofSystem pcpg = PermutationCommitmentProofSystem.getInstance(G_q, size, null, 60, 60, 20, rrs);

		// Proof and verify x
		// Invalid: Modified permutation
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV, randomGenerator);
		boolean v = pcpg.verify(proof, cPiV);
		assertTrue(!v);

	}

}
