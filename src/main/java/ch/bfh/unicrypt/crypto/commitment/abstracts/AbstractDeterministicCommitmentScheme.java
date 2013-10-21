package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.DeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractDeterministicCommitmentScheme<M extends Set, C extends Set, CE extends Element> extends AbstractCommitmentScheme<C> implements DeterministicCommitmentScheme {

  public AbstractDeterministicCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
    super(commitmentFunction, decommitmentFunction);
  }

  @Override
  public M getMessageSpace() {
    return (M) this.getCommitmentFunction().getDomain();
  }

  @Override
  public CE commit(final Element message) {
    return (CE) this.getCommitmentFunction().apply(message);
  }

  @Override
  public BooleanElement decommit(final Element message, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(message, commitment);
  }

}
