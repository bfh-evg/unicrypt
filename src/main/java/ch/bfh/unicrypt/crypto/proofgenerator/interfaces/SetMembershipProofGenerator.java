package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SetMembershipProofGenerator
	   extends ProofGenerator {

	public Subset getMembers();

	public Function getSetMembershipProofFunction();

}
