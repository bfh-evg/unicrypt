/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class SchnorrSignatureKeyPairGenerator
			 extends AbstractKeyPairGenerator<ZModPrime, ZModElement, GStarModPrime, GStarModElement> {

	private final Element generator;

	protected SchnorrSignatureKeyPairGenerator(GStarModSafePrime publicKeySpace) {
		this.generator = publicKeySpace.getDefaultGenerator();
	}

	protected SchnorrSignatureKeyPairGenerator(Element generator) {
		this.generator = generator;
	}

	public Element getGenerator() {
		return this.generator;
	}

	@Override
	protected KeyGenerator abstractGetPrivateKeyGenerator() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Function abstractGetPublicKeyFunction() {
		return GeneratorFunction.getInstance(this.getGenerator());
	}

	public static SchnorrSignatureKeyPairGenerator getInstance(GStarModSafePrime publicKeySpace) {
		if (publicKeySpace == null) {
			throw new IllegalArgumentException();
		}
		return new SchnorrSignatureKeyPairGenerator(publicKeySpace);
	}

	public static SchnorrSignatureKeyPairGenerator getInstance(Element generator) {
		if (generator == null) {
			throw new IllegalArgumentException();
		}
		return new SchnorrSignatureKeyPairGenerator(generator);
	}

}
