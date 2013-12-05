package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SigmaSetMembershipProofGenerator
	   extends SigmaProofGenerator, SetMembershipProofGenerator {

	public Function getDeltaFunction();

}
