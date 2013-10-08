package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface CommitmentScheme {

  public Group getMessageSpace();

  public Group getCommitmentSpace();

  public Function getCommitFunction();

  public Function getOpenFunction();

}
