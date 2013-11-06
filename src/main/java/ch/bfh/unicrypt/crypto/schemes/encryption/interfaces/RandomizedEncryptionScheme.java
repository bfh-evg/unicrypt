package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface RandomizedEncryptionScheme
       extends AsymmetricEncryptionScheme {

  /**
   *
   * @return
   */
  public Set getRandomizationSpace();

  /**
   *
   * @param encryptionKey
   * @param message
   * @param random
   * @return
   */
  public Element encrypt(Element encryptionKey, Element message, Random random);

  /**
   *
   * @param decryptionKey
   * @param message
   * @param randomization
   * @return
   */
  public Element encrypt(Element decryptionKey, Element message, Element randomization);

}
