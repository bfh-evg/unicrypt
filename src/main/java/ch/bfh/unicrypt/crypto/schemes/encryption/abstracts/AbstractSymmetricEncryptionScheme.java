/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.SymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 * @param <MS>
 * @param <ES>
 * @param <ME>
 * @param <EE>
 * @param <KS>
 */
public abstract class AbstractSymmetricEncryptionScheme<MS extends Set, ES extends Set, ME extends Element, EE extends Element, KS extends Set>
       extends AbstractEncryptionScheme<MS, ES, ME, EE>
       implements SymmetricEncryptionScheme {

  private KeyGenerator keyGenerator;

  @Override
  public final KeyGenerator getKeyGenerator() {
    if (this.keyGenerator == null) {
      this.keyGenerator = this.abstractGetKeyGenerator();
    }
    return this.keyGenerator;
  }

  @Override
  public final KS getEncryptionKeySpace() {
    return (KS) this.getKeyGenerator().getKeySpace();
  }

  @Override
  public final KS getDecryptionKeySpace() {
    return (KS) this.getKeyGenerator().getKeySpace();
  }

  protected abstract KeyGenerator abstractGetKeyGenerator();

}
