package ch.bfh.unicrypt.crypto.schemes.signature.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface RandomizedSignatureScheme extends SignatureScheme {

  public Group getRandomizationSpace();

  public Element sign(final Element privateKey, final Element message, Random random);

  public Element sign(final Element privateKey, final Element message, final Element randomization);

}
