package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.schemes.Scheme;
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
  public Set getPlaintextSpace();

  /**
   *
   * @return
   */
  public Set getCiphertextSpace();

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
   * @param ciphertext
   * @return
   */
  public Element decrypt(Element decryptionKey, Element ciphertext);

  public Set getEncryptionKeySpace();

  public Set getDecryptionKeySpace();

}
