package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;

/**
 *
 * @author rolfhaenni
 */
public interface AsymmetricEncryptionScheme
       extends EncryptionScheme {

  /**
   *
   * @return
   */
  public KeyPairGenerator getKeyPairGenerator();

}
