package ch.bfh.unicrypt.crypto.schemes.commitment.interfaces;

import ch.bfh.unicrypt.crypto.schemes.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface CommitmentScheme extends Scheme {

  public Set getCommitmentSpace();

  public Function getCommitmentFunction();

  public Function getDecommitmentFunction();

}
