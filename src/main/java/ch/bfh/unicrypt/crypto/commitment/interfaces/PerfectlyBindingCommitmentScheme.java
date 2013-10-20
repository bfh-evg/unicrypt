package ch.bfh.unicrypt.crypto.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface PerfectlyBindingCommitmentScheme extends CommitmentScheme {

  public Element commit(final Element message);

  public boolean decommit(final Element message, final Element commitment);

}
