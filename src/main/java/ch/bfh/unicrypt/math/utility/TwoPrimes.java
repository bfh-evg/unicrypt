/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class TwoPrimes extends Factorization {

  public TwoPrimes(BigInteger prime1, BigInteger prime2) {
    super(new BigInteger[]{prime1, prime2});
    if (prime1.equals(prime2)) {
      throw new IllegalArgumentException();
    }
  }

  public BigInteger getFirstPrime() {
    return this.getPrimeFactors()[0];
  }

  public BigInteger getSecondPrime() {
    return this.getPrimeFactors()[1];
  }

}
