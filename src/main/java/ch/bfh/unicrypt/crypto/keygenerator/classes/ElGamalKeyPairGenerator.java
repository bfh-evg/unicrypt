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
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class ElGamalKeyPairGenerator
			 extends AbstractKeyPairGenerator<ZModPrime, ZModElement, GStarModSafePrime, GStarModElement> {

	private final Element generator;

	protected ElGamalKeyPairGenerator(GStarModSafePrime publicKeySpace) {
		super((ZModPrime) publicKeySpace.getZModOrder());
		this.generator = publicKeySpace.getDefaultGenerator();
	}

	protected ElGamalKeyPairGenerator(Element generator) {
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

	public static ElGamalKeyPairGenerator getInstance(GStarModSafePrime publicKeySpace) {
		if (publicKeySpace == null) {
			throw new IllegalArgumentException();
		}
		return new ElGamalKeyPairGenerator(publicKeySpace);
	}

	public static ElGamalKeyPairGenerator getInstance(Element generator) {
		if (generator == null) {
			throw new IllegalArgumentException();
		}
		return new ElGamalKeyPairGenerator(generator);
	}

}
