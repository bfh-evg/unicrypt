package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface RandomizedCommitmentScheme extends CommitmentScheme {

  public Set getRandomizationSpace();

  public Element commit(final Element message, final Element randomization);

  public boolean decommit(final Element message, final Element randomization, final Element commitment);

}
