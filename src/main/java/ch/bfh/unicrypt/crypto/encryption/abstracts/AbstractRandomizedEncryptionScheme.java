/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encryption.interfaces.RandomizedEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractRandomizedEncryptionScheme<P extends Set, C extends Set, R extends Set, PE extends Element, CE extends Element> extends AbstractAsymmetricEncryptionScheme<P, C, PE, CE> implements RandomizedEncryptionScheme {

  protected AbstractRandomizedEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunction, Function decryptionFunction) {
    super(keyPairGenerator, encryptionFunction, decryptionFunction);
  }

  @Override
  public final R getRandomizationSpace() {
    return (R) ((ProductSet) this.getEncryptionFunction().getDomain()).getAt(2);
  }

  @Override
  public final CE encrypt(Element publicKey, Element plaintext) {
    return this.encrypt(publicKey, plaintext, (Random) null);
  }

  @Override
  public final CE encrypt(Element publicKey, Element plaintext, Random random) {
    return this.encrypt(publicKey, plaintext, this.getRandomizationSpace().getRandomElement(random));
  }

  @Override
  public final CE encrypt(Element publicKey, Element plaintext, Element randomization) {
    return (CE) this.getEncryptionFunction().apply(publicKey, plaintext, randomization);
  }

}
