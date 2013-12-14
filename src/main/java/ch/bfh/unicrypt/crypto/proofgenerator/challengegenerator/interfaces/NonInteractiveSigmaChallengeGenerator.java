package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;

//
// TODO: Extends from NonInteractiveChallengeGenerator -> as soon as it
//       uses RandomOracle instead of HashMethod
//
public interface NonInteractiveSigmaChallengeGenerator
	   extends SigmaChallengeGenerator {

	public HashMethod getHashMethod();

	// May return null!
	public Element getProverId();

}
