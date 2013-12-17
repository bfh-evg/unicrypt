package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public interface RandomizedEncryptionScheme
			 extends AsymmetricEncryptionScheme {

	/**
	 *
	 * @return
	 */
	public Set getRandomizationSpace();

	/**
	 *
	 * @param encryptionKey
	 * @param message
	 * @param randomGenerator
	 * @return
	 */
	public Element encrypt(Element encryptionKey, Element message, RandomGenerator randomGenerator);

	/**
	 *
	 * @param decryptionKey
	 * @param message
	 * @param randomization
	 * @return
	 */
	public Element encrypt(Element decryptionKey, Element message, Element randomization);

}
