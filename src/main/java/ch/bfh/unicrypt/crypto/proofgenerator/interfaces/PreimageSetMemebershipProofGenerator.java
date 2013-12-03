package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface PreimageSetMemebershipProofGenerator
	   extends PreimageProofGenerator, SetMembershipProofGenerator {

	public Function getSetMemberShipFunction();

	public Function getDeltaFunction();

}
