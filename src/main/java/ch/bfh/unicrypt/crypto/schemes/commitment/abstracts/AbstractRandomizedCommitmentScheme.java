package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.RandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractRandomizedCommitmentScheme<MS extends Set, CS extends Set, CE extends Element, RS extends Set>
       extends AbstractCommitmentScheme<MS, CS>
       implements RandomizedCommitmentScheme {

  @Override
  public final RS getRandomizationSpace() {
    return (RS) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(1);
  }

  @Override
  public final CE commit(final Element message, final Element randomization) {
    return (CE) this.getCommitmentFunction().apply(message, randomization);
  }

  @Override
  public final BooleanElement decommit(final Element message, final Element randomization, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(message, randomization, commitment);
  }

}
