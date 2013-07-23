package ch.bfh.unicrypt.crypto.commitment.abstracts;

import ch.bfh.unicrypt.crypto.commitment.interfaces.RandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.BooleanGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public abstract class AbstractRandomizedCommitmentScheme extends AbstractCommitmentScheme implements RandomizedCommitmentScheme {

  protected Group randomizationSpace;

  @Override
  public Group getRandomizationSpace() {
    return this.randomizationSpace;
  }

  @Override
  public Element commit(final Element message, final Element randomization) {
    return this.getCommitFunction().apply(message, randomization);
  }

  @Override
  public boolean open(final Element message, final Element randomization, final Element commitment) {
    return this.getOpenFunction().apply(message, randomization, commitment).equals(BooleanGroup.TRUE);
  }

}
