package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.DeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractDeterministicCommitmentScheme<M extends Set, C extends Set, CE extends Element> extends AbstractCommitmentScheme<M, C> implements DeterministicCommitmentScheme {

  public AbstractDeterministicCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder, commitmentFunction, decommitmentFunction);
  }

  @Override
  public CE commit(final Element message) {
    return (CE) this.getCommitmentFunction().apply(this.getEncoder().encode(message));
  }

  @Override
  public BooleanElement decommit(final Element message, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(this.getEncoder().encode(message), commitment);
  }

}
