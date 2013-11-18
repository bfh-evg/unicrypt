package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public interface EncryptionScheme
       extends Scheme {

  /**
   *
   * @return
   */
  public Set getEncryptionSpace();

  /**
   *
   * @return
   */
  public Function getEncryptionFunction();

  /**
   *
   * @return
   */
  public Function getDecryptionFunction();

  /**
   *
   * @param encryptionKey
   * @param message
   * @return
   */
  public Element encrypt(Element encryptionKey, Element message);

  /**
   *
   * @param decryptionKey
   * @param encryption
   * @return
   */
  public Element decrypt(Element decryptionKey, Element encryption);

  public Set getEncryptionKeySpace();

  public Set getDecryptionKeySpace();

}
