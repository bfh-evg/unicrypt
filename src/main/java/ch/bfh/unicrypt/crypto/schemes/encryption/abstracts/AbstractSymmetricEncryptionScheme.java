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
 * @param <ME>
 * @param <ES>
 * @param <EE>
 * @param <KS>
 * @param <KE>
 * @param <KG>
 */
public abstract class AbstractSymmetricEncryptionScheme<MS extends Set, ME extends Element, ES extends Set, EE extends Element, KS extends Set, KG extends KeyGenerator>
			 extends AbstractEncryptionScheme<MS, ME, ES, EE>
			 implements SymmetricEncryptionScheme {

	private KG keyGenerator;

	@Override
	public final KG getKeyGenerator() {
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

	protected abstract KG abstractGetKeyGenerator();

}
