/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.RandomizedEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 * @param <M>
 * @param <P>
 * @param <C>
 * @param <ME>
 * @param <CE>
 * @param <EK>
 * @param <DK>
 * @param <R>
 */
public abstract class AbstractRandomizedEncryptionScheme<M extends Set, P extends Set, C extends Set, ME extends Element, CE extends Element, EK extends Set, DK extends Set, R extends Set>
       extends AbstractAsymmetricEncryptionScheme<M, P, C, ME, CE, EK, DK>
       implements RandomizedEncryptionScheme {

  protected AbstractRandomizedEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction, KeyPairGenerator keyPairGenerator) {
    super(messageSpace, encoder, encryptionFunction, decryptionFunction, keyPairGenerator);
  }

  @Override
  public final R getRandomizationSpace() {
    return (R) ((ProductSet) this.getEncryptionFunction().getDomain()).getAt(2);
  }

  @Override
  public final CE encrypt(Element encryptionKey, Element message) {
    return this.encrypt(encryptionKey, message, (Random) null);
  }

  @Override
  public CE encrypt(Element encryptionKey, Element message, Random random) {
    return this.encrypt(encryptionKey, message, this.getRandomizationSpace().getRandomElement(random));
  }

  @Override
  public CE encrypt(Element encryptionKey, Element message, Element randomization) {
    if (!this.getEncryptionKeySpace().contains(encryptionKey) || !this.getMessageSpace().contains(message) || !this.getRandomizationSpace().contains(randomization)) {
      throw new IllegalArgumentException();
    }
    return (CE) this.getEncryptionFunction().apply(encryptionKey, this.encodeMessage(message), randomization);
  }

}
