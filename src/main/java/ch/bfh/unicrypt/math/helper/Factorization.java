package ch.bfh.unicrypt.math.helper;

import java.math.BigInteger;
import java.util.Arrays;

import ch.bfh.unicrypt.math.utility.MathUtil;

public class Factorization {

  private BigInteger value = BigInteger.ONE;
  private BigInteger[] primeFactors;
  private int[] exponents;

  public Factorization() {
    this(new BigInteger[]{});
  }

  public Factorization(BigInteger primeFactor) {
    this(new BigInteger[]{primeFactor});
  }

  public Factorization(BigInteger primeFactor, int exponent) {
    this(new BigInteger[]{primeFactor}, new int[]{exponent});
  }

  public Factorization(BigInteger... primeFactors) {
    this(primeFactors, computeExponents(primeFactors));
  }

  private static int[] computeExponents(BigInteger[] primeFactors) {
    if (primeFactors == null) {
      throw new IllegalArgumentException();
    }
    int[] exponents = new int[primeFactors.length];
    Arrays.fill(exponents, 1);
    return exponents;
  }
  
  public Factorization(BigInteger[] primeFactors, int[] exponents) {
    if (primeFactors == null || exponents == null || primeFactors.length != exponents.length) {
      throw new IllegalArgumentException();
    }
    for (int i=0; i<primeFactors.length; i++) {
      if (primeFactors[i] == null || !MathUtil.isPrime(primeFactors[i]) || exponents[i]<1) {
        throw new IllegalArgumentException();          
      }
      this.value = this.value.multiply(primeFactors[i].pow(exponents[i]));
    }
    this.primeFactors = MathUtil.removeDuplicates(primeFactors);
    this.exponents = new int[this.primeFactors.length];
    for (int i=0; i<this.primeFactors.length; i++) {
      for (int j=0; i<primeFactors.length; j++) {
        if (this.primeFactors[i].equals(primeFactors[j])) {
          this.exponents[i] = this.exponents[i] + exponents[j];
        }
      }
    }
  }
  
  public BigInteger getValue() {
    return this.value;
  }

  public void setValue(BigInteger value) {
    this.value = value;
  }

  public BigInteger[] getPrimeFactors() {
    return this.primeFactors;
  }

  public void setPrimeFactors(BigInteger[] primeFactors) {
    this.primeFactors = primeFactors;
  }

  public int[] getExponents() {
    return this.exponents;
  }

  public void setExponents(int[] exponents) {
    this.exponents = exponents;
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

}
