package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public interface ReEncryptionScheme
			 extends RandomizedEncryptionScheme {

	/**
	 *
	 * @return
	 */
	public Function getIdentityEncryptionFunction();

	/**
	 *
	 * @return
	 */
	public Function getReEncryptionFunction();

	/**
	 *
	 * @param encryptionKey
	 * @param ciphertext
	 * @return
	 */
	public Element reEncrypt(final Element encryptionKey, final Element ciphertext);

	/**
	 *
	 * @param encryptionKey
	 * @param ciphertext
	 * @param randomGenerator
	 * @return
	 */
	public Element reEncrypt(final Element encryptionKey, final Element ciphertext, RandomGenerator randomGenerator);

	/**
	 *
	 * @param encryptionKey
	 * @param ciphertext
	 * @param randomization
	 * @return
	 */
	public Element reEncrypt(final Element encryptionKey, final Element ciphertext, final Element randomization);

	/**
	 *
	 * @return
	 */
	@Override
	public Monoid getMessageSpace();

	/**
	 *
	 * @return
	 */
	@Override
	public Monoid getEncryptionSpace();

}
