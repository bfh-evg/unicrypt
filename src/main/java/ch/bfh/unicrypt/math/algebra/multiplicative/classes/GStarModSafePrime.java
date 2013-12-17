/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import ch.bfh.unicrypt.math.helper.factorization.SafePrime;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class GStarModSafePrime
			 extends GStarModPrime {

	protected GStarModSafePrime(SafePrime modulo) {
		super(modulo, Prime.getInstance(modulo.getValue().subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
	}

	@Override
	protected ZModPrime standardGetZModOrder() {
		return ZModPrime.getInstance(this.getOrder());
	}

	@Override
	protected ZStarModPrime standardGetZStarModOrder() {
		return ZStarModPrime.getInstance(this.getOrder());
	}

	@Override
	public String standardToStringContent() {
		return this.getModulus().toString();
	}

	public static GStarModSafePrime getInstance(final SafePrime safePrime) {
		if (safePrime == null) {
			throw new IllegalArgumentException();
		}
		return new GStarModSafePrime(safePrime);
	}

	public static GStarModSafePrime getInstance(final int modulus) {
		return GStarModSafePrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static GStarModSafePrime getInstance(final BigInteger modulus) {
		return new GStarModSafePrime(SafePrime.getInstance(modulus));
	}

	public static GStarModSafePrime getRandomInstance(int bitLength) {
		return GStarModSafePrime.getRandomInstance(bitLength, null);
	}

	public static GStarModSafePrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return new GStarModSafePrime(SafePrime.getRandomInstance(bitLength, randomGenerator));
	}

}
