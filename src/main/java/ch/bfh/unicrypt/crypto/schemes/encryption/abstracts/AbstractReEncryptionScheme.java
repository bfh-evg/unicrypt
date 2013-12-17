/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.ReEncryptionScheme;
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

/**
 *
 * @author rolfhaenni
 * @param <MS>
 * @param <ES>
 * @param <ME>
 * @param <EE>
 * @param <RS>
 * @param <RE>
 * @param <EK>
 * @param <DK>
 * @param <KG>
 */
public abstract class AbstractReEncryptionScheme<MS extends Monoid, ME extends Element, ES extends Monoid, EE extends Element, RS extends Monoid, RE extends Element, EK extends Set, DK extends Set, KG extends KeyPairGenerator>
			 extends AbstractRandomizedEncryptionScheme<MS, ME, ES, EE, RS, RE, EK, DK, KG>
			 implements ReEncryptionScheme {

	private Function identityEncryptionFunction;
	private Function reEncryptionFunction;

	@Override
	public final Function getIdentityEncryptionFunction() {
		if (this.identityEncryptionFunction == null) {
			this.identityEncryptionFunction = this.getEncryptionFunction().partiallyApply(this.getMessageSpace().getIdentityElement(), 1);
		}
		return this.identityEncryptionFunction;
	}

	@Override
	public final Function getReEncryptionFunction() {
		if (this.reEncryptionFunction == null) {
			ProductSet inputSpace = ProductSet.getInstance(this.getEncryptionKeySpace(), this.getEncryptionSpace(), this.getRandomizationSpace());
			this.reEncryptionFunction = CompositeFunction.getInstance(
						 MultiIdentityFunction.getInstance(inputSpace, 2),
						 ProductFunction.getInstance(SelectionFunction.getInstance(inputSpace, 1),
																				 CompositeFunction.getInstance(RemovalFunction.getInstance(inputSpace, 1),
																																			 this.getIdentityEncryptionFunction())),
						 ApplyFunction.getInstance(this.getEncryptionSpace()));
		}
		return this.reEncryptionFunction;
	}

	@Override
	public final EE reEncrypt(final Element publicKey, final Element ciphertext) {
		return this.reEncrypt(publicKey, ciphertext, (RandomGenerator) null);
	}

	@Override
	public final EE reEncrypt(final Element publicKey, final Element ciphertext, RandomGenerator randomGenerator) {
		return this.reEncrypt(publicKey, ciphertext, this.getRandomizationSpace().getRandomElement(randomGenerator));
	}

	@Override
	public final EE reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization) {
		return (EE) this.getReEncryptionFunction().apply(publicKey, ciphertext, randomization);
	}

}
