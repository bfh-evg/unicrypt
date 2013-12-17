/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper.factorization;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class SafePrime
			 extends Prime {

	protected SafePrime(BigInteger safePrime) {
		super(safePrime);
	}

	public static SafePrime getInstance(int safePrime) {
		return SafePrime.getInstance(BigInteger.valueOf(safePrime));
	}

	public static SafePrime getInstance(BigInteger safePrime) {
		if (!MathUtil.isSavePrime(safePrime)) {
			throw new IllegalArgumentException();
		}
		return new SafePrime(safePrime);
	}

	public static SafePrime getRandomInstance(int bitLength) {
		return SafePrime.getRandomInstance(bitLength, null);
	}

	public static SafePrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGenerator.DEFAULT;
		}
		return new SafePrime(randomGenerator.nextSavePrime(bitLength));
	}

}
