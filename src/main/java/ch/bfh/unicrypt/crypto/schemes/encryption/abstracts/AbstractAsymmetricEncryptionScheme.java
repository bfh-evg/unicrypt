/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.AsymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 * @param <MS>
 * @param <ME>
 * @param <ES>
 * @param <EE>
 * @param <EK>
 * @param <DK>
 * @param <KG>
 */
public abstract class AbstractAsymmetricEncryptionScheme<MS extends Set, ME extends Element, ES extends Set, EE extends Element, EK extends Set, DK extends Set, KG extends KeyPairGenerator>
			 extends AbstractEncryptionScheme<MS, ME, ES, EE>
			 implements AsymmetricEncryptionScheme {

	private KeyPairGenerator keyPairGenerator;

	@Override
	public final KeyPairGenerator getKeyPairGenerator() {
		if (this.keyPairGenerator == null) {
			this.keyPairGenerator = this.abstractGetKeyPairGenerator();
		}
		return this.keyPairGenerator;
	}

	@Override
	public final EK getEncryptionKeySpace() {
		return (EK) this.getKeyPairGenerator().getPublicKeySpace();
	}

	@Override
	public final DK getDecryptionKeySpace() {
		return (DK) this.getKeyPairGenerator().getPrivateKeySpace();
	}

	protected abstract KeyPairGenerator abstractGetKeyPairGenerator();

}
