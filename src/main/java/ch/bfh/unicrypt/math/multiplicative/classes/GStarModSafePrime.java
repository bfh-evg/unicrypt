/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.multiplicative.classes;

import ch.bfh.unicrypt.math.utility.Prime;
import ch.bfh.unicrypt.math.utility.SafePrime;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class GStarModSafePrime extends GStarModPrime {

  protected GStarModSafePrime(SafePrime modulo) {
    super(modulo, new Prime(modulo.getValue().subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
  }

  public static GStarModSafePrime getInstance(final BigInteger modulus) {
    return new GStarModSafePrime(new SafePrime(modulus));
  }

}
