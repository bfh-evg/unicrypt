/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class GStarModPrime
			 extends GStarMod {

	protected GStarModPrime(Prime modulo, Prime orderFactor) {
		super(modulo, orderFactor);
	}

	public static GStarModPrime getInstance(Prime modulus, Prime orderFactor) {
		if (modulus == null || orderFactor == null) {
			throw new IllegalArgumentException();
		}
		if (!modulus.getValue().subtract(BigInteger.ONE).mod(orderFactor.getValue()).equals(BigInteger.ZERO)) {
			throw new IllegalArgumentException();
		}
		return new GStarModPrime(modulus, orderFactor);
	}

	public static GStarModPrime getInstance(final int modulus, final int orderFactor) {
		return GStarModPrime.getInstance(BigInteger.valueOf(modulus), BigInteger.valueOf(orderFactor));
	}

	public static GStarModPrime getInstance(final BigInteger modulus, BigInteger orderFactor) {
		return GStarModPrime.getInstance(Prime.getInstance(modulus), Prime.getInstance(orderFactor));
	}

}
