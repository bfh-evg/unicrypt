package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.CommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractCommitmentScheme<M extends Set, C extends Set> extends AbstractScheme<M> implements CommitmentScheme {

  protected Function commitmentFunction;
  protected Function decommitmentFunction;

  protected AbstractCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder);
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
