package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.PerfectlyHidingCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractPerfectlyHidingCommitmentScheme<M extends Set, R extends Set, C extends Set, CE extends Element> extends AbstractCommitmentScheme<C> implements PerfectlyHidingCommitmentScheme {

  public AbstractPerfectlyHidingCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
    super(commitmentFunction, decommitmentFunction);
  }

  @Override
  public M getMessageSpace() {
    return (M) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(0);
  }

  @Override
  public R getRandomizationSpace() {
    return (R) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(1);
  }

  @Override
  public CE commit(final Element message, final Element randomization) {
    return (CE) this.getCommitmentFunction().apply(message, randomization);
  }

  @Override
  public boolean decommit(final Element message, final Element randomization, final Element commitment) {
    return ((BooleanElement) this.getDecommitmentFunction().apply(message, randomization, commitment)).getBoolean();
  }

}
