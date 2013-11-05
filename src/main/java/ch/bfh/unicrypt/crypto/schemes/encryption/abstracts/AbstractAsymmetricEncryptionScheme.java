/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.AsymmetricEncryptionScheme;
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
public abstract class AbstractAsymmetricEncryptionScheme<M extends Set, P extends Set, C extends Set, ME extends Element, CE extends Element, EK extends Set, DK extends Set>
       extends AbstractEncryptionScheme<M, P, C, ME, CE>
       implements AsymmetricEncryptionScheme {

  private final KeyPairGenerator keyPairGenerator;

  protected AbstractAsymmetricEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction, KeyPairGenerator keyPairGenerator) {
    super(messageSpace, encoder, encryptionFunction, decryptionFunction);
    this.keyPairGenerator = keyPairGenerator;
  }

  @Override
  public final KeyPairGenerator getKeyPairGenerator() {
    return this.keyPairGenerator;
  }

  @Override
  public EK getEncryptionKeySpace() {
    return (EK) this.getKeyPairGenerator().getPublicKeySpace();
  }

  @Override
  public DK getDecryptionKeySpace() {
    return (DK) this.getKeyPairGenerator().getPrivateKeySpace();
  }

}
