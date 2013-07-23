package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public interface CommitmentScheme {

  public Group getMessageSpace();

  public Group getCommitmentSpace();

  public Function getCommitFunction();

  public Function getOpenFunction();

}
