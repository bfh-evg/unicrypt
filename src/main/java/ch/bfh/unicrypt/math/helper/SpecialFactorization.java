package ch.bfh.unicrypt.math.helper;

import java.math.BigInteger;


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
    this((doubling) ? new BigInteger[]{primeFactor, BigInteger.valueOf(2)} : new BigInteger[]{primeFactor}, (doubling) ? new int[]{exponent, 1} : new int[]{exponent});
  }

  private SpecialFactorization(BigInteger[] primeFactors, int[] exponents) {
    super(primeFactors, exponents);
    if (this.getValue().testBit(0)) { // getValue() is odd
      if (this.getPrimeFactors().length > 1) {
        throw new IllegalArgumentException();
      }
    } else { // getValue() is even
      if (this.getPrimeFactors().length == 1) { // getValue() is power of two
        if (this.getExponents()[0] > 2) {
          throw new IllegalArgumentException();          
        }
      } else { // getValue() is something times power of two
        if (!this.getValue().testBit(1)) { // getValue() is not something times two
          throw new IllegalArgumentException();
        }
      }
    }
  }
  
}
