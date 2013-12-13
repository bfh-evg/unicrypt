/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.RSAKeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractAsymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimePair;
import ch.bfh.unicrypt.math.function.classes.PowerFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class RSAEncryptionScheme
			 extends AbstractAsymmetricEncryptionScheme<ZMod, ZModElement, ZMod, ZModElement, ZMod, ZMod, RSAKeyPairGenerator> {

	private final ZMod zMod;

	protected RSAEncryptionScheme(ZMod zMod) {
		this.zMod = zMod;
	}

	public ZMod getZMod() {
		return this.zMod;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		return PowerFunction.getInstance(zMod);
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return PowerFunction.getInstance(zMod);
	}

	@Override
	protected RSAKeyPairGenerator abstractGetKeyPairGenerator() {
		if (this.getZMod() instanceof ZModPrimePair) {
			return RSAKeyPairGenerator.getInstance((ZModPrimePair) this.getZMod());
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public static RSAEncryptionScheme getInstance(ZMod zMod) {
		if (zMod == null) {
			throw new IllegalArgumentException();
		}
		return new RSAEncryptionScheme(zMod);
	}

}
