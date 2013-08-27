/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.monoid.classes;

import ch.bfh.unicrypt.math.utility.TwoPrimes;
import java.math.BigInteger;

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
    if (twoPrimes== null) {
      throw new IllegalArgumentException();
    }
    return new ZTimesModTwoPrimes(twoPrimes);
  }

  public static ZTimesModTwoPrimes getInstance(BigInteger prime1, BigInteger prime2) {
    return ZTimesModTwoPrimes.getInstance(new TwoPrimes(prime1, prime2));
  }

}
