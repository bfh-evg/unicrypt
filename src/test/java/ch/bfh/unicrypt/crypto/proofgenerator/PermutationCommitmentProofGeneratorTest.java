package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ElementChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PermutationCommitmentProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.math.BigInteger;
import java.util.Random;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PermutationCommitmentProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	public PermutationCommitmentProofGeneratorTest() {
	}

	@Test
	public void testPermutationCommitemntProofGenerator() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = RandomOracle.DEFAULT;
		final Random random = new Random(1);

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 1, 4, 0});  //{3, 2, 1, 4, 0} {3, 1, 4, 0, 2}
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, ro);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ElementChallengeGenerator ecg = PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(G_q, size, ro);
		PermutationCommitmentProofGenerator pcpg = PermutationCommitmentProofGenerator.getInstance(scg, ecg, G_q, size, ro);

		// Proof and verify
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV, random);
		BooleanElement v = pcpg.verify(proof, cPiV);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPermutationCommitemntProofGenerator2() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final RandomOracle ro = RandomOracle.DEFAULT;

		final int size = 20;

		// Permutation
		Permutation permutation = Permutation.getRandomInstance(size);
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, ro);

		Tuple sV = pcs.getRandomizationSpace().getRandomElement();
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ElementChallengeGenerator ecg = PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(G_q, size, ro);
		PermutationCommitmentProofGenerator pcpg = PermutationCommitmentProofGenerator.getInstance(scg, ecg, G_q, size, ro);

		// Proof and verify
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV);
		BooleanElement v = pcpg.verify(proof, cPiV);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPermutationCommitemntProofGenerator_Invalid() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = RandomOracle.DEFAULT;
		final Random random = new Random(1);

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 2, 4, 0, 1});
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, ro);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7));
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ElementChallengeGenerator ecg = PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(G_q, size, ro);
		PermutationCommitmentProofGenerator pcpg = PermutationCommitmentProofGenerator.getInstance(scg, ecg, G_q, size, ro);

		// Proof and verify
		// Invalid: Wrong sV
		Tuple sVInvalid = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(12), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7));
		Pair proof = pcpg.generate(Pair.getInstance(pi, sVInvalid), cPiV, random);
		BooleanElement v = pcpg.verify(proof, cPiV);
		assertTrue(!v.getBoolean());

		// Invalid: Wrong pi
		PermutationElement piInvalid = PermutationGroup.getInstance(size).getElement(Permutation.getInstance(new int[]{3, 0, 4, 2, 1}));
		proof = pcpg.generate(Pair.getInstance(piInvalid, sV), cPiV, random);
		v = pcpg.verify(proof, cPiV);
		assertTrue(!v.getBoolean());

	}

}
