/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyPairGenerator;
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
		super((ZModPrime) publicKeySpace.getZModOrder());
		this.generator = publicKeySpace.getDefaultGenerator();
	}

	protected SchnorrSignatureKeyPairGenerator(Element generator) {
		super((ZModPrime) generator.getSet().getZModOrder());
		this.generator = generator;
	}

	public Element getGenerator() {
		return this.generator;
	}

	@Override
	protected Function abstractGetPublicKeyGenerationFunction() {
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
