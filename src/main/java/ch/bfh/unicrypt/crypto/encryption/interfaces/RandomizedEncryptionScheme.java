package ch.bfh.unicrypt.crypto.encryption.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface RandomizedEncryptionScheme extends AsymmetricEncryptionScheme {

  public Set getRandomizationSpace();

  public Element encrypt(Element publicKey, Element plaintext, Random random);

  public Element encrypt(Element publicKey, Element plaintext, Element randomization);

}
