/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import ch.bfh.unicrypt.math.helper.factorization.PrimePair;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class ZModPrimePair
	   extends ZMod {

	PrimePair primePair;

	protected ZModPrimePair(PrimePair primePair) {
		super(primePair.getValue());
		this.primePair = primePair;
	}

	public PrimePair getPrimePair() {
		return this.primePair;
	}

	public BigInteger getFirstPrime() {
		return this.getPrimePair().getFirstPrime();
	}

	public BigInteger getSecondPrime() {
		return this.getPrimePair().getSecondPrime();
	}

	@Override
	protected ZModPrimePair standardGetZModOrder() {
		return ZModPrimePair.getInstance(this.getPrimePair());
	}

	@Override
	protected ZStarMod standardGetZStarModOrder() {
		return ZStarMod.getInstance(this.getPrimePair());
	}

	private static final Map<BigInteger, ZModPrimePair> instances = new HashMap<BigInteger, ZModPrimePair>();

	public static ZModPrimePair getInstance(final PrimePair twoPrimes) {
		if (twoPrimes == null) {
			throw new IllegalArgumentException();
		}
		ZModPrimePair instance = ZModPrimePair.instances.get(twoPrimes.getValue());
		if (instance == null) {
			instance = new ZModPrimePair(twoPrimes);
			ZModPrimePair.instances.put(twoPrimes.getValue(), instance);
		}
		return instance;
	}

	public static ZModPrimePair getInstance(final int prime1, final int prime2) {
		return ZModPrimePair.getInstance(BigInteger.valueOf(prime1), BigInteger.valueOf(prime2));
	}

	public static ZModPrimePair getInstance(BigInteger prime1, BigInteger prime2) {
		return ZModPrimePair.getInstance(PrimePair.getInstance(prime1, prime2));
	}

	public static ZModPrimePair getInstance(Prime prime1, Prime prime2) {
		return new ZModPrimePair(PrimePair.getInstance(prime1, prime2));
	}

	public static ZModPrimePair getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return new ZModPrimePair(PrimePair.getRandomInstance(bitLength, randomGenerator));
	}

	public static ZModPrimePair getRandomInstance(int bitLength) {
		return ZModPrimePair.getRandomInstance(bitLength, null);
	}

}
