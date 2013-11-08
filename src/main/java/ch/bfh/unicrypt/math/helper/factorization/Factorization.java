package ch.bfh.unicrypt.math.helper.factorization;

import ch.bfh.unicrypt.math.helper.UniCrypt;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Arrays;

public class Factorization
       extends UniCrypt {

  private final BigInteger value;
  private final BigInteger[] primeFactors;
  private final int[] exponents;

  protected Factorization(BigInteger value, BigInteger[] primeFactors, int[] exponents) {
    this.value = value;
    this.primeFactors = primeFactors;
    this.exponents = exponents;
  }

  public BigInteger getValue() {
    return this.value;
  }

  public BigInteger[] getPrimeFactors() {
    return this.primeFactors;
  }

  public int[] getExponents() {
    return this.exponents;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Factorization other = (Factorization) obj;
    return this.value.equals(other.value);
  }

  public static Factorization getInstance() {
    return Factorization.getInstance(new BigInteger[]{});
  }

  public static Factorization getInstance(BigInteger primeFactor) {
    return Factorization.getInstance(new BigInteger[]{primeFactor});
  }

  public static Factorization getInstance(BigInteger primeFactor, int exponent) {
    return Factorization.getInstance(new BigInteger[]{primeFactor}, new int[]{exponent});
  }

  public static Factorization getInstance(BigInteger... primeFactors) {
    if (primeFactors == null) {
      throw new IllegalArgumentException();
    }
    int[] exponents = new int[primeFactors.length];
    Arrays.fill(exponents, 1);
    return Factorization.getInstance(primeFactors, exponents);
  }

  public static Factorization getInstance(BigInteger[] primeFactors, int[] exponents) {
    if (primeFactors == null || exponents == null || primeFactors.length != exponents.length) {
      throw new IllegalArgumentException();
    }
    BigInteger value = BigInteger.ONE;
    for (int i = 0; i < primeFactors.length; i++) {
      if (primeFactors[i] == null || !MathUtil.isPrime(primeFactors[i]) || exponents[i] < 1) {
        throw new IllegalArgumentException();
      }
      value = value.multiply(primeFactors[i].pow(exponents[i]));
    }
    BigInteger[] newPrimeFactors = MathUtil.removeDuplicates(primeFactors);
    int newLength = newPrimeFactors.length;
    int[] newExponents = new int[newLength];
    for (int i = 0; i < newLength; i++) {
      for (int j = 0; j < primeFactors.length; j++) {
        if (newPrimeFactors[i].equals(primeFactors[j])) {
          newExponents[i] = newExponents[i] + exponents[j];
        }
      }
    }
    return new Factorization(value, newPrimeFactors, newExponents);
  }

  @Override
  public final String toString() {
    return this.getClass().getSimpleName() + "[" + this.getValue() + "]";
  }

}
