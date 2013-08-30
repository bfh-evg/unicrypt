package ch.bfh.unicrypt.math.helper.factorization;

import java.math.BigInteger;

/**
 * This is the general static factory method for this class. Returns a new
 * instance for the general case n=p^k or n=2p^k, where p is prime and e>=1 (and
 * e=1 for p=2).
 *
 * @param prime The given prime number p
 * @param exponent The given exponent e
 * @param doubling A Boolean value indicating whether n=p^k ({@code false}) or
 * n=2p^k ({@code true})
 * @throws IllegalArgumentException if {@code prime} is null or not prime
 * @throws IllegalArgumentException if {@code exponent<1}
 * @
 * throws IllegalArgumentException if {@code prime=2} and {@code exponent>1}
 */
public class SpecialFactorization extends Factorization {

  public SpecialFactorization(BigInteger primeFactor) {
    this(primeFactor, 1, false);
  }

  public SpecialFactorization(BigInteger primeFactor, int exponent) {
    this(primeFactor, exponent, false);
  }

  public SpecialFactorization(BigInteger primeFactor, boolean doubling) {
    this(primeFactor, 1, doubling);
  }

  public SpecialFactorization(BigInteger primeFactor, int exponent, boolean doubling) {
    super((doubling) ? new BigInteger[]{primeFactor, BigInteger.valueOf(2)} : new BigInteger[]{primeFactor}, (doubling) ? new int[]{exponent, 1} : new int[]{exponent});
  }

}
