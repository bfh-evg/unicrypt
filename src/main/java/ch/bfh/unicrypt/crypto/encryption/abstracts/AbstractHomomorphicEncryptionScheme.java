/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encryption.interfaces.HomomorphicEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractHomomorphicEncryptionScheme<P extends Monoid, C extends Monoid, R extends Monoid, PE extends Element, CE extends Element> extends AbstractRandomizedEncryptionScheme<P, C, R, PE, CE> implements HomomorphicEncryptionScheme {

  protected AbstractHomomorphicEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunction, Function decryptionFunction) {
    super(keyPairGenerator, encryptionFunction, decryptionFunction);
  }

  @Override
  public final Function getReEncryptionFunction() {
    return null;
  }

  @Override
  public final CE reEncrypt(final Element publicKey, final Element ciphertext) {
    return this.reEncrypt(publicKey, ciphertext, (Random) null);
  }

  @Override
  public final CE reEncrypt(final Element publicKey, final Element ciphertext, Random random) {
    return this.reEncrypt(publicKey, ciphertext, this.getRandomizationSpace().getRandomElement(random));
  }

  @Override
  public final CE reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization) {
    return (CE) this.getReEncryptionFunction().apply(publicKey, ciphertext, randomization);
  }

}
