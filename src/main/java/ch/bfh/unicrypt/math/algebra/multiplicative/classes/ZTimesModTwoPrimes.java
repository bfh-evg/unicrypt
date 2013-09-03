/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.helper.factorization.Prime;
import ch.bfh.unicrypt.math.helper.factorization.TwoPrimes;

/**
 *
 * @author rolfhaenni
 */
public class ZTimesModTwoPrimes extends ZTimesMod {

  TwoPrimes twoPrimes;

  protected ZTimesModTwoPrimes(TwoPrimes twoPrimes) {
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

  public static ZTimesModTwoPrimes getInstance(final TwoPrimes twoPrimes) {
    if (twoPrimes == null) {
      throw new IllegalArgumentException();
    }
    return new ZTimesModTwoPrimes(twoPrimes);
  }

  public static ZTimesModTwoPrimes getInstance(BigInteger prime1, BigInteger prime2) {
    return new ZTimesModTwoPrimes(TwoPrimes.getInstance(prime1, prime2));
  }

  public static ZTimesModTwoPrimes getInstance(Prime prime1, Prime prime2) {
    return new ZTimesModTwoPrimes(TwoPrimes.getInstance(prime1, prime2));
  }

  public static ZTimesModTwoPrimes getRandomInstance(int bitLength, Random random) {
    return new ZTimesModTwoPrimes(TwoPrimes.getRandomInstance(bitLength, random));
  }

  public static ZTimesModTwoPrimes getRandomInstance(int bitLength) {
    return ZTimesModTwoPrimes.getRandomInstance(bitLength, (Random) null);
  }

}
