/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;

/**
 *
 * @author rolfhaenni
 */
public class AESKeyGenerator
			 extends AbstractKeyGenerator<FiniteByteArraySet, FiniteByteArrayElement> {

	static final int DEFAULT_KEY_LENGTH = 128;
	static final int[] SUPPORTED_KEY_LENGTHS = {128, 192, 256};

	protected AESKeyGenerator(int keyLength) {
		super(FiniteByteArraySet.getInstance(keyLength / 8, true));
	}

	public static AESKeyGenerator getInstance() {
		return AESKeyGenerator.getInstance(AESKeyGenerator.DEFAULT_KEY_LENGTH);
	}

	public static AESKeyGenerator getInstance(int keyLength) {
		for (int length : AESKeyGenerator.SUPPORTED_KEY_LENGTHS) {
			if (keyLength == length) {
				return new AESKeyGenerator(keyLength);
			}
		}
		throw new IllegalArgumentException();
	}

}
