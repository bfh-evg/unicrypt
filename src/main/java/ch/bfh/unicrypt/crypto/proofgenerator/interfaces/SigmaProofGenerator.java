package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SigmaProofGenerator
			 extends ProofGenerator {

	public Function getPreimageProofFunction();

	public SigmaChallengeGenerator getChallengeGenerator();

	public Set getCommitmentSpace();

	public ZMod getChallengeSpace();

	public Set getResponseSpace();

	public Element getCommitment(final Triple proof);

	public Element getChallenge(final Triple proof);

	public Element getResponse(final Triple proof);

	@Override
	public Triple generate(Element privateInput, Element publicInput);

	@Override
	public Triple generate(Element privateInput, Element publicInput, RandomGenerator randomGeneratorı);

	@Override
	public ProductSet getProofSpace();

}
