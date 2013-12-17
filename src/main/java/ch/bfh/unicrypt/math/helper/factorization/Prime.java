/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper.factorization;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class Prime
			 extends SpecialFactorization {

	protected Prime(BigInteger prime) {
		super(prime, new BigInteger[]{prime}, new int[]{1});
	}

	public static Prime getInstance(int prime) {
		return Prime.getInstance(BigInteger.valueOf(prime));
	}

	public static Prime getInstance(BigInteger prime) {
		return new Prime(prime);
	}

	public static Prime getRandomInstance(int bitLength) {
		return Prime.getRandomInstance(bitLength, null);
	}

	public static Prime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGenerator.DEFAULT;
		}
		return Prime.getInstance(randomGenerator.nextPrime(bitLength));
	}

}
