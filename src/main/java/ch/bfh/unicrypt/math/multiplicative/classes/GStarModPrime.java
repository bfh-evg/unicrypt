/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.multiplicative.classes;

import ch.bfh.unicrypt.math.utility.Prime;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class GStarModPrime extends GStarMod {

  protected GStarModPrime(Prime modulo, Prime orderFactor) {
    super(modulo, orderFactor);
  }

  public static GStarModPrime getInstance(Prime modulo, Prime orderFactor) {
    if (modulo == null || orderFactor == null) {
      throw new IllegalArgumentException();
    }
    if (!modulo.getValue().subtract(BigInteger.ONE).mod(orderFactor.getValue()).equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException();
    }
    return new GStarModPrime(modulo, orderFactor);
  }

  public static GStarModPrime getInstance(final BigInteger modulus, BigInteger orderFactor) {
    return GStarModPrime.getInstance(new Prime(modulus), new Prime(orderFactor));
  }
}
