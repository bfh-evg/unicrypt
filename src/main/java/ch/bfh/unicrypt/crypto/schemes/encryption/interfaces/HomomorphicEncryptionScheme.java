package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface HomomorphicEncryptionScheme
       extends RandomizedEncryptionScheme {

  /**
   *
   * @return
   */
  public Function getIdentityEncryptionFunction();

  /**
   *
   * @return
   */
  public Function getReEncryptionFunction();

  /**
   *
   * @param encryptionKey
   * @param ciphertext
   * @return
   */
  public Element reEncrypt(final Element encryptionKey, final Element ciphertext);

  /**
   *
   * @param encryptionKey
   * @param ciphertext
   * @param random
   * @return
   */
  public Element reEncrypt(final Element encryptionKey, final Element ciphertext, Random random);

  /**
   *
   * @param encryptionKey
   * @param ciphertext
   * @param randomization
   * @return
   */
  public Element reEncrypt(final Element encryptionKey, final Element ciphertext, final Element randomization);

  /**
   *
   * @return
   */
  @Override
  public Monoid getPlaintextSpace();

  /**
   *
   * @return
   */
  @Override
  public Monoid getCiphertextSpace();

}
