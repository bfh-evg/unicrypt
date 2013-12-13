/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.RandomizedEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 * @param <MS>
 * @param <ME>
 * @param <ES>
 * @param <EE>
 * @param <RS>
 * @param <RE>
 * @param <EK>
 * @param <DK>
 * @param <KG>
 */
public abstract class AbstractRandomizedEncryptionScheme<MS extends Set, ME extends Element, ES extends Set, EE extends Element, RS extends Set, RE extends Element, EK extends Set, DK extends Set, KG extends KeyPairGenerator>
			 extends AbstractAsymmetricEncryptionScheme<MS, ME, ES, EE, EK, DK, KG>
			 implements RandomizedEncryptionScheme {

	@Override
	public final RS getRandomizationSpace() {
		return (RS) ((ProductSet) this.getEncryptionFunction().getDomain()).getAt(2);
	}

	@Override
	public final EE encrypt(Element encryptionKey, Element message) {
		return this.encrypt(encryptionKey, message, (Random) null);
	}

	@Override
	public final EE encrypt(Element encryptionKey, Element message, Random random) {
		return this.encrypt(encryptionKey, message, this.getRandomizationSpace().getRandomElement(random));
	}

	@Override
	public final EE encrypt(Element encryptionKey, Element message, Element randomization) {
		if (!this.getEncryptionKeySpace().contains(encryptionKey) || !this.getMessageSpace().contains(message) || !this.getRandomizationSpace().contains(randomization)) {
			throw new IllegalArgumentException();
		}
		return (EE) this.getEncryptionFunction().apply(encryptionKey, message, randomization);
	}

}
