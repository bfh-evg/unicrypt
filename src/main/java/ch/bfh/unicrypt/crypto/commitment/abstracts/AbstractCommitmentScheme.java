package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.CommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractCommitmentScheme<C extends Set> implements CommitmentScheme {

  protected Function commitmentFunction;
  protected Function decommitmentFunction;

  protected AbstractCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
    this.commitmentFunction = commitmentFunction;
    this.decommitmentFunction = decommitmentFunction;
  }

  @Override
  public C getCommitmentSpace() {
    return (C) this.getCommitmentFunction().getCoDomain();
  }

  @Override
  public Function getCommitmentFunction() {
    return this.commitmentFunction;
  }

  @Override
  public Function getDecommitmentFunction() {
    return this.decommitmentFunction;
  }

}
