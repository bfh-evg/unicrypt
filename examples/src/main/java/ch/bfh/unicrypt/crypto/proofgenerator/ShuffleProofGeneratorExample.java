/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PermutationCommitmentTunedProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ShuffleProofGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
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

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ShuffleProofGeneratorExample {

	final static int P1 = 167;
	//final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223"; //256
	//final static String P2 = "7345427226905070499764889740717053678145447263298168648090208932893269201500094910685938670933846388714280765884795335365679757155747184872890100586114127"; //512
	final static String P2 = "124839508901459225295131478904766553151715203799479873450319702669888301683936126519033292399204126892064039399466769614858812059914518351605494976695246338946504781671208279483554047133686061305170930849857475703281378907333309894394327830075584429809888154770188970744592711756609335320238222672153149255987"; //1024
	//final static String P2 = "32317006071311007300714876688669951960444102669715484032130345427524655138867890893197201411522913463688717960921898019494119559150490921095088152386448283120630877367300996091750197750389652106796057638384067568276792218642619756161838094338476170470581645852036305042887575891541065808607552399123930385521914333389668342420684974786564569494856176035326322058077805659331026192708460314150258592864177116725943603718461857357598351152301645904403697613233287231227125684710820209725157101726931323469678542580656697935045997268352998638215525166389437335543602135433229604645318478604952148193555853611059594288367"; //2048

	public void proofOfShuffle_COMPLETE() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZMod Z_q = G_q.getZModOrder();
		final RandomOracle ro = PseudoRandomOracle.DEFAULT;
		final RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

		final int size = 1000;
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
		BooleanElement vShuffle = spg.verify(proofShuffle, publicInput);
		System.out.println("Shuffle was sucessful: " + (vPermutation.getBoolean() && vShuffle.getBoolean()));
		System.out.println("Verify");
		logAndReset();
	}

	public void logAndReset() {
		System.out.println("COUNTER     : " + (GStarMod.modPowCounter_SelfApply - GStarMod.modPowCounter_IsGenerator));
		System.out.println("COUNTER Cont: " + GStarMod.modPowCounter_Contains);
		GStarMod.modPowCounter_SelfApply = 0;
		GStarMod.modPowCounter_Contains = 0;
		GStarMod.modPowCounter_IsGenerator = 0;
	}

	public static void main(String[] args) {
		//BigInteger bigInteger = PseudoRandomGenerator.getInstance().nextSavePrime(512);
		//System.out.println(bigInteger);
		ShuffleProofGeneratorExample ex = new ShuffleProofGeneratorExample();
		long time = System.currentTimeMillis();
		ex.proofOfShuffle_COMPLETE();
		System.out.println("Finished after: " + (System.currentTimeMillis() - time) + " MilliSeconds.");

	}

}
