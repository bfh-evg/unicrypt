/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class TwoPrimes extends Factorization {

  protected TwoPrimes(Prime prime1, Prime prime2) {
    this(prime1.getValue(), prime2.getValue());
  }

  protected TwoPrimes(BigInteger prime1, BigInteger prime2) {
    super(new BigInteger[]{prime1, prime2});
  }

  public BigInteger getFirstPrime() {
    return this.getPrimeFactors()[0];
  }

  public BigInteger getSecondPrime() {
    return this.getPrimeFactors()[1];
  }

  public static TwoPrimes getInstance(BigInteger prime1, BigInteger prime2) {
    if (prime1.equals(prime2)) {
      throw new IllegalArgumentException();
    }
    return new TwoPrimes(prime1, prime2);
  }

  public static TwoPrimes getInstance(Prime prime1, Prime prime2) {
    if (prime1.equals(prime2)) {
      throw new IllegalArgumentException();
    }
    return new TwoPrimes(prime1, prime2);
  }

  public static TwoPrimes getRandomInstance(int bitLength) {
    return TwoPrimes.getRandomInstance(bitLength, (Random) null);
  }

  public static TwoPrimes getRandomInstance(int bitLength, Random random) {
    return TwoPrimes.getInstance(Prime.getRandomInstance(bitLength, random), Prime.getRandomInstance(bitLength, random));
  }

}
