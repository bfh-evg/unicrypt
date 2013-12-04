package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface TCSSetMembershipProofGenerator
	   extends TCSProofGenerator, SetMembershipProofGenerator {

	public Function getDeltaFunction();

}
