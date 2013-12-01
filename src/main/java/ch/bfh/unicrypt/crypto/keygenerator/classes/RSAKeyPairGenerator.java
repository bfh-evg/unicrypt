/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimes;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class RSAKeyPairGenerator
			 extends AbstractKeyPairGenerator<ZModPrimes, ZElement, ZModPrimes, ZElement> {

	private final ZModPrimes zModPrimes;

	protected RSAKeyPairGenerator(ZModPrimes zModPrimes) {
		this.zModPrimes = zModPrimes;
	}

	public ZModPrimes getZModPrimes() {
		return this.zModPrimes;
	}

	@Override
	protected KeyGenerator abstractGetPrivateKeyGenerator() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Function abstractGetPublicKeyFunction() {
		return InvertFunction.getInstance(this.getZModPrimes().getZStarModOrder());
	}

	public static RSAKeyPairGenerator getInstance(ZModPrimes zModPrimes) {
		if (zModPrimes == null) {
			throw new IllegalArgumentException();
		}
		return new RSAKeyPairGenerator(zModPrimes);
	}

}
