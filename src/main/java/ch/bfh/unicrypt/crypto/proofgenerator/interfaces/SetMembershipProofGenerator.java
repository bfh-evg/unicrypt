package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SetMembershipProofGenerator
	   extends ProofGenerator {

	public Tuple getMembers();

	public Function getSetMembershipProofFunction();

}
