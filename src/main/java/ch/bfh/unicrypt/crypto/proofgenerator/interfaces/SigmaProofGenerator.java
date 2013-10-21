package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

public interface SigmaProofGenerator extends ProofGenerator {

  public Group getCommitmentSpace();

  public Group getResponseSpace();

  public Element getCommitment(Tuple proof);

  public Element getResponse(Tuple proof);

  public HashFunction getHashFunction();

}
