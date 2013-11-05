/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.HomomorphicEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RemovalFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
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
public abstract class AbstractHomomorphicEncryptionScheme<M extends Set, P extends Monoid, C extends Monoid, ME extends Element, CE extends Element, EK extends Set, DK extends Set, R extends Monoid>
       extends AbstractRandomizedEncryptionScheme<M, P, C, ME, CE, EK, DK, R>
       implements HomomorphicEncryptionScheme {

  private Function identityEncryptionFunction;
  private Function reEncryptionFunction;

  protected AbstractHomomorphicEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction, KeyPairGenerator keyPairGenerator) {
    super(messageSpace, encoder, encryptionFunction, decryptionFunction, keyPairGenerator);
  }

  @Override
  public Function getIdentityEncryptionFunction() {
    if (this.identityEncryptionFunction == null) {
      this.identityEncryptionFunction = this.getEncryptionFunction().partiallyApply(this.getPlaintextSpace().getIdentityElement(), 1);
    }
    return this.identityEncryptionFunction;
  }

  @Override
  public final Function getReEncryptionFunction() {
    if (this.reEncryptionFunction == null) {
      ProductSet inputSpace = ProductSet.getInstance(this.getEncryptionKeySpace(), this.getCiphertextSpace(), this.getRandomizationSpace());
      this.reEncryptionFunction = CompositeFunction.getInstance(
             MultiIdentityFunction.getInstance(inputSpace, 2),
             ProductFunction.getInstance(SelectionFunction.getInstance(inputSpace, 1),
                                         CompositeFunction.getInstance(RemovalFunction.getInstance(inputSpace, 1),
                                                                       this.getIdentityEncryptionFunction())),
             ApplyFunction.getInstance(this.getCiphertextSpace()));
    }
    return this.reEncryptionFunction;
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
