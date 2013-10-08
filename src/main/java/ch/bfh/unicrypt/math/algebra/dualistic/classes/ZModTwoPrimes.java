/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.helper.factorization.Prime;
import ch.bfh.unicrypt.math.helper.factorization.TwoPrimes;

/**
 *
 * @author rolfhaenni
 */
public class ZModTwoPrimes extends ZMod {

  TwoPrimes twoPrimes;

  protected ZModTwoPrimes(TwoPrimes twoPrimes) {
    super(twoPrimes.getValue());
    this.twoPrimes = twoPrimes;
  }

  public TwoPrimes getTwoPrimes() {
    return this.getTwoPrimes();
  }

  public BigInteger getFirstPrime() {
    return this.getTwoPrimes().getFirstPrime();
  }

  public BigInteger getSecondPrime() {
    return this.getTwoPrimes().getSecondPrime();
  }

  public static ZModTwoPrimes getInstance(final TwoPrimes twoPrimes) {
    if (twoPrimes == null) {
      throw new IllegalArgumentException();
    }
    return new ZModTwoPrimes(twoPrimes);
  }

  public static ZModTwoPrimes getInstance(final int prime1, final int prime2) {
    return ZModTwoPrimes.getInstance(BigInteger.valueOf(prime1), BigInteger.valueOf(prime2));
  }

  public static ZModTwoPrimes getInstance(BigInteger prime1, BigInteger prime2) {
    return new ZModTwoPrimes(TwoPrimes.getInstance(prime1, prime2));
  }

  public static ZModTwoPrimes getInstance(Prime prime1, Prime prime2) {
    return new ZModTwoPrimes(TwoPrimes.getInstance(prime1, prime2));
  }

  public static ZModTwoPrimes getRandomInstance(int bitLength, Random random) {
    return new ZModTwoPrimes(TwoPrimes.getRandomInstance(bitLength, random));
  }

  public static ZModTwoPrimes getRandomInstance(int bitLength) {
    return ZModTwoPrimes.getRandomInstance(bitLength, (Random) null);
  }

}
