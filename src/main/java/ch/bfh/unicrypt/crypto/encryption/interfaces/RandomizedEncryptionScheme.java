package ch.bfh.unicrypt.crypto.encryption.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.interfaces.RandomizedKeyGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface RandomizedEncryptionScheme extends EncryptionScheme {

  public Group getRandomizationSpace();

  public Element encrypt(Element key, Element plaintext, Random random);

  public Element encrypt(Element key, Element plaintext, Element randomization);

  @Override
  public RandomizedKeyGenerator getKeyGenerator();

}
