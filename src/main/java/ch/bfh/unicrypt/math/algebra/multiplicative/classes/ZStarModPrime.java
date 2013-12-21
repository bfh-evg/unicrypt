/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class ZStarModPrime
	   extends ZStarMod {

	private final static Map<BigInteger, ZStarModPrime> instances = new HashMap<BigInteger, ZStarModPrime>();

	protected ZStarModPrime(Prime modulus) {
		super(modulus);
	}

	public static ZStarModPrime getInstance(final Prime modulus) {
		if (modulus == null) {
			throw new IllegalArgumentException();
		}
		ZStarModPrime instance = ZStarModPrime.instances.get(modulus.getValue());
		if (instance == null) {
			instance = new ZStarModPrime(modulus);
			ZStarModPrime.instances.put(modulus.getValue(), instance);
		}
		return instance;
	}

	public static ZStarModPrime getInstance(final int modulus) {
		return ZStarModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZStarModPrime getInstance(final BigInteger modulus) {
		return ZStarModPrime.getInstance(Prime.getInstance(modulus));
	}

	public static ZStarModPrime getRandomInstance(int bitLength) {
		return ZStarModPrime.getRandomInstance(bitLength, (RandomGenerator) null);
	}

	public static ZStarModPrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return ZStarModPrime.getInstance(Prime.getRandomInstance(bitLength, randomGenerator));
	}

}
