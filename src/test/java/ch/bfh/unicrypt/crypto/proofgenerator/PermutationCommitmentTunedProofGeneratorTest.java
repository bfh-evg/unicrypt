package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PermutationCommitmentTunedProofGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGenerator;
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
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Permutation;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PermutationCommitmentTunedProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	@Test
	public void testPermutationCommitemntProofGenerator() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomGenerator randomGenerator = PseudoRandomGenerator.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		final int size = 5;

		// Permutation
		Permutation permutation = Permutation.getInstance(new int[]{3, 1, 4, 0, 2});  //{3, 2, 1, 4, 0} {3, 1, 4, 0, 2}
		PermutationElement pi = PermutationGroup.getInstance(size).getElement(permutation);

		PermutationCommitmentScheme pcs = PermutationCommitmentScheme.getInstance(G_q, size, rrs);

		Tuple sV = Tuple.getInstance(Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5), Z_q.getElement(7)); //pcs.getRandomizationSpace().getRandomElement(random);
		Tuple cPiV = pcs.commit(pi, sV);

		// Permutation commitment proof generator
		SigmaChallengeGenerator scg = PermutationCommitmentTunedProofGenerator.createNonInteractiveSigmaChallengeGenerator(G_q, size);
		ChallengeGenerator ecg = PermutationCommitmentTunedProofGenerator.createNonInteractiveElementChallengeGenerator(G_q, size, ro);
		PermutationCommitmentTunedProofGenerator pcpg = PermutationCommitmentTunedProofGenerator.getInstance(scg, ecg, G_q, size, rrs);

		// Proof and verify
		Pair proof = pcpg.generate(Pair.getInstance(pi, sV), cPiV, randomGenerator);
		BooleanElement v = pcpg.verify(proof, cPiV);
		assertTrue(v.getBoolean());
	}

}
