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
public class ZTimesModPrime extends ZTimesMod {

  protected ZTimesModPrime(Prime prime) {
    super(prime.getValue());
  }

  public static ZTimesModPrime getInstance(final Prime prime) {
    if (prime == null) {
      throw new IllegalArgumentException();
    }
    return new ZTimesModPrime(prime);
  }

  public static ZTimesModPrime getInstance(BigInteger prime) {
    return ZTimesModPrime.getInstance(new Prime(prime));
  }

}
