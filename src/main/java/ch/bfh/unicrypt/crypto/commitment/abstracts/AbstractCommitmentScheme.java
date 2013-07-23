package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.CommitmentScheme;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public abstract class AbstractCommitmentScheme implements CommitmentScheme {

  protected Group messageSpace;
  protected Group commitmentSpace;

  protected Function commitFunction;
  protected Function openFunction;

  @Override
  public Group getMessageSpace() {
    return this.messageSpace;
  }

  @Override
  public Group getCommitmentSpace() {
    return this.commitmentSpace;
  }

  @Override
  public Function getCommitFunction() {
    return this.commitFunction;
  }

  @Override
  public Function getOpenFunction() {
    return this.openFunction;
  }

}
