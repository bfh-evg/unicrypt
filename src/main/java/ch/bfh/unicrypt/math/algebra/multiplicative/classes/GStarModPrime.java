/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class GStarModPrime
	   extends GStarMod {

	private final static Map<BigInteger, Map<BigInteger, GStarModPrime>> instanceMaps = new HashMap<BigInteger, Map<BigInteger, GStarModPrime>>();

	protected GStarModPrime(Prime modulus, Prime orderFactor) {
		super(modulus, orderFactor);
	}

	public static GStarModPrime getInstance(Prime modulus, Prime orderFactor) {
		if (modulus == null || orderFactor == null) {
			throw new IllegalArgumentException();
		}
		if (!modulus.getValue().subtract(BigInteger.ONE).mod(orderFactor.getValue()).equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException();
		}
		Map<BigInteger, GStarModPrime> instanceMap = GStarModPrime.instanceMaps.get(modulus.getValue());
		if (instanceMap == null) {
			instanceMap = new HashMap<BigInteger, GStarModPrime>();
			GStarModPrime.instanceMaps.put(modulus.getValue(), instanceMap);
		}
		GStarModPrime instance = instanceMap.get(orderFactor.getValue());
		if (instance == null) {
			instance = new GStarModPrime(modulus, orderFactor);
			instanceMap.put(orderFactor.getValue(), instance);
		}
		return instance;
	}

	public static GStarModPrime getInstance(final int modulus, final int orderFactor) {
		return GStarModPrime.getInstance(BigInteger.valueOf(modulus), BigInteger.valueOf(orderFactor));
	}

	public static GStarModPrime getInstance(final BigInteger modulus, BigInteger orderFactor) {
		return GStarModPrime.getInstance(Prime.getInstance(modulus), Prime.getInstance(orderFactor));
	}

}
