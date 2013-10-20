package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.PerfectlyBindingCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractPerfectlyBindingCommitmentScheme<M extends Set, C extends Set, CE extends Element> extends AbstractCommitmentScheme<C> implements PerfectlyBindingCommitmentScheme {

  public AbstractPerfectlyBindingCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
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
  public boolean decommit(final Element message, final Element commitment) {
    return ((BooleanElement) this.getDecommitmentFunction().apply(message, commitment)).getBoolean();
  }

}
