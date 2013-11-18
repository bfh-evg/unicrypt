package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.DeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractDeterministicCommitmentScheme<MS extends Set, CS extends Set, CE extends Element>
       extends AbstractCommitmentScheme<MS, CS>
       implements DeterministicCommitmentScheme {

  @Override
  public final CE commit(final Element message) {
    return (CE) this.getCommitmentFunction().apply(message);
  }

  @Override
  public final BooleanElement decommit(final Element message, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(message, commitment);
  }

}
