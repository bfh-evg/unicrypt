/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimePair;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class RSAKeyPairGenerator
			 extends AbstractKeyPairGenerator<ZMod, ZModElement, ZMod, ZModElement> {

	private final ZModPrimePair zModPrimes;
	private final ZStarMod zStarMod;

	protected RSAKeyPairGenerator(ZModPrimePair zModPrimes) {
		super(zModPrimes.getZModOrder());
		this.zModPrimes = zModPrimes;
		this.zStarMod = ZStarMod.getInstance(zModPrimes.getZStarModOrder().getOrder());
	}

	public ZModPrimePair getZModPrimes() {
		return this.zModPrimes;
	}

	@Override
	protected Function standardGetPrivateKeyGenerationFunction() {
		return CompositeFunction.getInstance(
					 RandomFunction.getInstance(this.zStarMod),
					 ConvertFunction.getInstance(this.zStarMod, this.zModPrimes));
	}

	@Override
	protected Function abstractGetPublicKeyGenerationFunction() {
		return CompositeFunction.getInstance(
					 ConvertFunction.getInstance(this.zModPrimes, this.zStarMod),
					 InvertFunction.getInstance(this.zStarMod),
					 ConvertFunction.getInstance(this.zStarMod, this.zModPrimes));
	}

	public static RSAKeyPairGenerator getInstance(ZModPrimePair zModPrimes) {
		if (zModPrimes == null) {
			throw new IllegalArgumentException();
		}
		return new RSAKeyPairGenerator(zModPrimes);
	}

}
