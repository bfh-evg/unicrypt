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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class GStarModSafePrime
	   extends GStarModPrime {

	private final static Map<BigInteger, GStarModSafePrime> instances = new HashMap<BigInteger, GStarModSafePrime>();

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
		GStarModSafePrime instance = GStarModSafePrime.instances.get(safePrime.getValue());
		if (instance == null) {
			instance = new GStarModSafePrime(safePrime);
			GStarModSafePrime.instances.put(safePrime.getValue(), instance);
		}
		return instance;
	}

	public static GStarModSafePrime getInstance(final int modulus) {
		return GStarModSafePrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static GStarModSafePrime getInstance(final BigInteger modulus) {
		return GStarModSafePrime.getInstance(SafePrime.getInstance(modulus));
	}

	public static GStarModSafePrime getRandomInstance(int bitLength) {
		return GStarModSafePrime.getRandomInstance(bitLength, null);
	}

	public static GStarModSafePrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return GStarModSafePrime.getInstance(SafePrime.getRandomInstance(bitLength, randomGenerator));
	}

}
