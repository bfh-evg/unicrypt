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

  protected SpecialFactorization(BigInteger value, BigInteger[] primeFactors, int[] exponents) {
    super(value, primeFactors, exponents);
  }

  public static SpecialFactorization getInstance(BigInteger primeFactor) {
    return SpecialFactorization.getInstance(primeFactor, 1, false);
  }

  public static SpecialFactorization getInstance(BigInteger primeFactor, int exponent) {
    return SpecialFactorization.getInstance(primeFactor, exponent, false);
  }

  public static SpecialFactorization getInstance(BigInteger primeFactor, boolean doubling) {
    return SpecialFactorization.getInstance(primeFactor, 1, doubling);
  }

  public static SpecialFactorization getInstance(BigInteger primeFactor, int exponent, boolean doubling) {
    BigInteger[] primeFactors;
    int[] exponents;
    BigInteger value;
    if (doubling) {
      value = primeFactor.pow(exponent).multiply(BigInteger.valueOf(2));
      primeFactors = new BigInteger[]{primeFactor, BigInteger.valueOf(2)};
      exponents = new int[]{exponent, 1};
    } else {
      value = primeFactor.pow(exponent);
      primeFactors = new BigInteger[]{primeFactor};
      exponents = new int[]{exponent};
    }
    return new SpecialFactorization(value, primeFactors, exponents);
  }

}
