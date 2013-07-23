package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public interface SigmaProofGenerator extends ProofGenerator {

  public Group getCommitmentSpace();

  public Group getResponseSpace();

  public Element getCommitment(TupleElement proof);

  public Element getResponse(TupleElement proof);

  public HashFunction getHashFunction();

}