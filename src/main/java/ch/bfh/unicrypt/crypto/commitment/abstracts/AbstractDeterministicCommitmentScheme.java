package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.DeterministicCommitmentScheme;

public abstract class AbstractDeterministicCommitmentScheme extends AbstractCommitmentScheme implements DeterministicCommitmentScheme {

  @Override
  public Element commit(final Element message) {
    return this.getCommitFunction().apply(message);
  }

  @Override
  public boolean open(final Element message, final Element commitment) {
    return this.getOpenFunction().apply(message, commitment).equals(BooleanGroup.TRUE);
  }

}
