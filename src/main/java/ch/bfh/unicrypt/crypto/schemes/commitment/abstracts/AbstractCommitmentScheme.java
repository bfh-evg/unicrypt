package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.CommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractCommitmentScheme<MS extends Set, CS extends Set>
       extends AbstractScheme
       implements CommitmentScheme {

  protected Function commitmentFunction;
  protected Function decommitmentFunction;

  @Override
  public final MS getMessageSpace() {
    return (MS) ((ProductSet) this.getDecommitmentFunction().getDomain()).getAt(0);
  }

  @Override
  public final CS getCommitmentSpace() {
    return (CS) this.getCommitmentFunction().getCoDomain();
  }

  @Override
  public final Function getCommitmentFunction() {
    if (this.commitmentFunction == null) {
      this.commitmentFunction = this.abstractGetCommitmentFunction();
    }
    return this.commitmentFunction;
  }

  @Override
  public final Function getDecommitmentFunction() {
    if (this.decommitmentFunction == null) {
      this.decommitmentFunction = this.abstractGetDecommitmentFunction();
    }
    return this.decommitmentFunction;
  }

  protected abstract Function abstractGetCommitmentFunction();

  protected abstract Function abstractGetDecommitmentFunction();

}
