package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.RandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractRandomizedCommitmentScheme<M extends Set, R extends Set, C extends Set, CE extends Element> extends AbstractCommitmentScheme<M, C> implements RandomizedCommitmentScheme {

  public AbstractRandomizedCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder, commitmentFunction, decommitmentFunction);
  }

  @Override
  public R getRandomizationSpace() {
    return (R) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(1);
  }

  @Override
  public CE commit(final Element message, final Element randomization) {
    return (CE) this.getCommitmentFunction().apply(this.getEncoder().encode(message), randomization);
  }

  @Override
  public BooleanElement decommit(final Element message, final Element randomization, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(this.getEncoder().encode(message), randomization, commitment);
  }

}
