package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface DeterministicCommitmentScheme extends CommitmentScheme {

  public Element commit(final Element message);

  public BooleanElement decommit(final Element message, final Element commitment);

}
