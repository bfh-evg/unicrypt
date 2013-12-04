package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SetMembershipProofGenerator
	   extends ProofGenerator {

	public Element[] getMembers();

	public Function getSetMembershipProofFunction();

}
