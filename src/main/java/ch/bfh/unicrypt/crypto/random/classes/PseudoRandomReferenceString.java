/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomReferenceString
			 extends PseudoRandomGenerator
			 implements RandomReferenceString {

	private final String algorithmName;
	private final byte[] seed;

	public PseudoRandomReferenceString(SecureRandom secureRandom, byte[] seed) {
		super(secureRandom);
		this.algorithmName = secureRandom.getAlgorithm();
		this.seed = seed;
	}

	@Override
	public void reset() {
		if (!isReset()) {
			SecureRandom secureRandom;
			try {
				secureRandom = SecureRandom.getInstance(this.algorithmName);
			} catch (final NoSuchAlgorithmException exception) {
				throw new IllegalArgumentException(exception);
			}
			secureRandom.setSeed(this.seed);
			this.random = secureRandom;
			this.counter = 0;
		}
	}

	@Override
	public boolean isReset() {
		return this.counter == 0;
	}

	public static PseudoRandomReferenceString getInstance() {
		return PseudoRandomReferenceString.getInstance(PseudoRandomGenerator.DEFAULT_ALGORITHM_NAME);
	}

	public static PseudoRandomReferenceString getInstance(String algorithmName) {
		return PseudoRandomReferenceString.getInstance(algorithmName, new byte[0]);
	}

	public static PseudoRandomReferenceString getInstance(String algorithmName, byte[] seed) {
		if (algorithmName == null || seed == null) {
			throw new IllegalArgumentException();
		}
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance(algorithmName);
		} catch (final NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
		secureRandom.setSeed(seed); // initiates the entropy gathering
		return new PseudoRandomReferenceString(secureRandom, seed);
	}

}
