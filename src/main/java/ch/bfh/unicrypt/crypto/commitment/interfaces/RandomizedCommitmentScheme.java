package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface RandomizedCommitmentScheme extends CommitmentScheme {

  public Group getRandomizationSpace();

  public Element commit(final Element message, final Element randomization);

  public boolean open(final Element message, final Element randomization, final Element commitment);

}
