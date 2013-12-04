package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public interface TCSProofGenerator
	   extends ProofGenerator {

	public Function getPreimageProofFunction();

	public HashMethod getHashMethod();

	public Set getCommitmentSpace();

	public ZMod getChallengeSpace();

	public Set getResponseSpace();

	public Element getCommitment(final Triple proof);

	public Element getChallenge(final Triple proof);

	public Element getResponse(final Triple proof);

	@Override
	public Triple generate(Element privateInput, Element publicInput);

	@Override
	public Triple generate(Element privateInput, Element publicInput, Element proverID);

	@Override
	public Triple generate(Element privateInput, Element publicInput, Random random);

	@Override
	public Triple generate(Element privateInput, Element publicInput, Element proverID, Random random);

	@Override
	public ProductSet getProofSpace();

}
