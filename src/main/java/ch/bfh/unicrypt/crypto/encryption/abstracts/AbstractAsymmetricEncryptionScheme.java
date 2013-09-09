/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encryption.interfaces.AsymmetricEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAsymmetricEncryptionScheme<P extends Set, C extends Set, PE extends Element, CE extends Element> extends AbstractEncryptionScheme<P, C, PE, CE> implements AsymmetricEncryptionScheme {

  private KeyPairGenerator keyPairGenerator;

  protected AbstractAsymmetricEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunction, Function decryptionFunction) {
    super(encryptionFunction, decryptionFunction);
    this.keyPairGenerator = keyPairGenerator;
  }

  @Override
  public final KeyPairGenerator getKeyPairGenerator() {
    return this.keyPairGenerator;
  }

}
