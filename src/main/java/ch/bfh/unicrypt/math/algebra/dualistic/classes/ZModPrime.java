/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class ZModPrime
	   extends ZMod
	   implements PrimeField {

	protected ZModPrime(Prime prime) {
		super(prime.getValue());
	}

	@Override
	public ZStarModPrime getMultiplicativeGroup() {
		return ZStarModPrime.getInstance(this.getModulus());
	}

	@Override
	public ZModElement divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public ZModElement oneOver(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		if (element.isEquivalent(this.getZeroElement())) {
			throw new UnsupportedOperationException();
		}
		return this.abstractGetElement(element.getValue().modInverse(this.getModulus()));
	}

	@Override
	protected ZModPrime standardGetZModOrder() {
		return ZModPrime.getInstance(this.getOrder());
	}

	@Override
	protected ZStarModPrime standardGetZStarModOrder() {
		return ZStarModPrime.getInstance(this.getOrder());
	}

	//
	// STATIC FACTORY METHODS
	//
	private static final Map<BigInteger, ZModPrime> instances = new HashMap<BigInteger, ZModPrime>();

	public static ZModPrime getInstance(final Prime modulus) {
		if (modulus == null) {
			throw new IllegalArgumentException();
		}
		ZModPrime instance = ZModPrime.instances.get(modulus.getValue());
		if (instance == null) {
			instance = new ZModPrime(modulus);
			ZModPrime.instances.put(modulus.getValue(), instance);
		}
		return instance;
	}

	public static ZModPrime getInstance(final int modulus) {
		return ZModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZModPrime getInstance(BigInteger modulus) {
		return ZModPrime.getInstance(Prime.getInstance(modulus));
	}

	public static ZModPrime getRandomInstance(int bitLength, RandomGenerator randomGenerator) {
		return ZModPrime.getInstance(Prime.getRandomInstance(bitLength, randomGenerator));
	}

	public static ZModPrime getRandomInstance(int bitLength) {
		return ZModPrime.getRandomInstance(bitLength, null);
	}

	@Override
	public BigInteger getCharacteristic() {
		return this.getOrder();
	}

}
