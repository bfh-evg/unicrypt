/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author rolfhaenni
 */
public class RandomOracle {

	private static final String DEFAULT_ALGORITHM_NAME = "SHA1PRNG";
	public static final RandomOracle DEFAULT = RandomOracle.getInstance(DEFAULT_ALGORITHM_NAME);
	private final String algorithmName;

	protected RandomOracle(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public SecureRandom getRandom(long query) {
		if (query < 0) {
			throw new IllegalArgumentException();
		}
		try {
			SecureRandom random = SecureRandom.getInstance(this.getAlgorithmName());
			random.setSeed(query + 1); // to avoid 0 being ignored as seed
			return random;
		} catch (final NoSuchAlgorithmException e) {
			throw new InternalError();
		}
	}
//
// TODO: Replace?
//
//	public SecureRandom getRandom(long query) {
//		if (query < 0) {
//			throw new IllegalArgumentException();
//		}
//		// Taken from SecureRandom.longToByteArray
//		byte[] queryB = new byte[8];
//		for (int i = 0; i < 8; i++) {
//			queryB[i] = (byte) query;
//			query >>= 8;
//		}
//		return this.getRandom(queryB);
//	}
//
//

	public SecureRandom getRandom(byte[] query) {
		if (query == null || query.length < 1) {
			throw new IllegalArgumentException();
		}
		try {
			SecureRandom random = SecureRandom.getInstance(this.getAlgorithmName());
			random.setSeed(query);
			return random;
		} catch (final NoSuchAlgorithmException e) {
			throw new InternalError();
		}
	}

	public String getAlgorithmName() {
		return this.algorithmName;
	}

	public static RandomOracle getInstance() {
		return RandomOracle.DEFAULT;
	}

	public static RandomOracle getInstance(String algorithmName) {
		return new RandomOracle(algorithmName);
	}

}
