package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;

public interface NonInteractiveSigmaChallengeGenerator
	   extends SigmaChallengeGenerator {

	public HashMethod getHashMethod();

	// May return null!
	public Element getProverId();

}
