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
public class ZStarModPrime extends ZStarMod {

  protected ZStarModPrime(Prime modulus) {
    super(modulus);
  }

  public static ZStarModPrime getInstance(final Prime modulus) {
    return new ZStarModPrime(modulus);
  }

  public static ZStarModPrime getInstance(final BigInteger modulus) {
    return ZStarModPrime.getInstance(new Prime(modulus));
  }


}
