package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface CommitmentScheme {

  public Set getMessageSpace();

  public Set getCommitmentSpace();

  public Function getCommitmentFunction();

  public Function getDecommitmentFunction();

}
