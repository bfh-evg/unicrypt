/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.SymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

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
 */
public abstract class AbstractSymmetricEncryptionScheme<M extends Set, P extends Set, C extends Set, ME extends Element, CE extends Element, EK extends Set, DK extends Set>
       extends AbstractEncryptionScheme<M, P, C, ME, CE>
       implements SymmetricEncryptionScheme {

  private final KeyGenerator keyGenerator;

  protected AbstractSymmetricEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction, KeyGenerator keyGenerator) {
    super(messageSpace, encoder, encryptionFunction, decryptionFunction);
    this.keyGenerator = keyGenerator;
  }

  @Override
  public final KeyGenerator getKeyGenerator() {
    return this.keyGenerator;
  }

  @Override
  public EK getEncryptionKeySpace() {
    return (EK) this.getKeyGenerator().getKeySpace();
  }

  @Override
  public DK getDecryptionKeySpace() {
    return (DK) this.getKeyGenerator().getKeySpace();
  }

}
