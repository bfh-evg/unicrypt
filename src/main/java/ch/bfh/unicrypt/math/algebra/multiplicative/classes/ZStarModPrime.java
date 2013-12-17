/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ZStarModPrime
			 extends ZStarMod {

	protected ZStarModPrime(Prime modulus) {
		super(modulus);
	}

	public static ZStarModPrime getInstance(final Prime modulus) {
		return new ZStarModPrime(modulus);
	}

	public static ZStarModPrime getInstance(final int modulus) {
		return ZStarModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZStarModPrime getInstance(final BigInteger modulus) {
		return ZStarModPrime.getInstance(Prime.getInstance(modulus));
	}

	public static ZStarModPrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return ZStarModPrime.getInstance(Prime.getRandomInstance(bitLength, randomGenerator));
	}

	public static ZStarModPrime getRandomInstance(int bitLength) {
		return ZStarModPrime.getRandomInstance(bitLength, null);
	}

}
