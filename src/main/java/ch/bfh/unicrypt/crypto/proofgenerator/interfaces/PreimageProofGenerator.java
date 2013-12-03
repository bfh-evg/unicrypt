package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public interface PreimageProofGenerator
	   extends ProofGenerator {

	public Function getPreimageProofFunction();

	public HashMethod getHashMethod();

	public Set getCommitmentSpace();

	public ZMod getChallengeSpace();

	public Set getResponseSpace();

	public Element getCommitment(final Triple proof);

	public Element getChallenge(final Triple proof);

	public Element getResponse(final Triple proof);

	public ZModElement createChallenge(final Element commitment, final Element publicInput, final Element proverId);

}
