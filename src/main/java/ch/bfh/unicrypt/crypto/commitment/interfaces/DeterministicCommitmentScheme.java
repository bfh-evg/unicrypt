package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.element.Element;

public interface DeterministicCommitmentScheme extends CommitmentScheme {

  public Element commit(final Element message);

  public boolean open(final Element message, final Element commitment);

}
