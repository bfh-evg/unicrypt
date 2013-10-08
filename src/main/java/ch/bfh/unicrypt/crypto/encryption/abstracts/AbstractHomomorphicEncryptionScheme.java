/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.encryption.interfaces.HomomorphicEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RemovalFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractHomomorphicEncryptionScheme<P extends Monoid, C extends Monoid, R extends Monoid, PE extends Element, CE extends Element> extends AbstractRandomizedEncryptionScheme<P, C, R, PE, CE> implements HomomorphicEncryptionScheme {

  private Function identityEncryptionFunction;
  private Function reEncryptionFunction;

  protected AbstractHomomorphicEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunction, Function decryptionFunction) {
    super(keyPairGenerator, encryptionFunction, decryptionFunction);
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
      ProductSet inputSpace = ProductSet.getInstance(this.getKeyPairGenerator().getPublicKeySpace(), this.getCiphertextSpace(), this.getRandomizationSpace());
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
