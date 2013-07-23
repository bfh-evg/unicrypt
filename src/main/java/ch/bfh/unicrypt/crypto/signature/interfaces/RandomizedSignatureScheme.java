package ch.bfh.unicrypt.crypto.signature.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public interface RandomizedSignatureScheme extends SignatureScheme {

  public Element sign(final Element privateKey, final Element message, Random random);

  public Element sign(final Element privateKey, final Element message, final Element randomization);

  public Group getRandomizationSpace();

}
