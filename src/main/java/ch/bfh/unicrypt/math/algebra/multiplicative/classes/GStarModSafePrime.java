/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.helper.factorization.Prime;
import ch.bfh.unicrypt.math.helper.factorization.SafePrime;

/**
 *
 * @author rolfhaenni
 */
public class GStarModSafePrime extends GStarModPrime {

  protected GStarModSafePrime(SafePrime modulo) {
    super(modulo, Prime.getInstance(modulo.getValue().subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
  }

  @Override
  public String standardToStringContent() {
    return this.getModulus().toString();
  }

  public static GStarModSafePrime getInstance(final SafePrime safePrime) {
    if (safePrime == null) {
      throw new IllegalArgumentException();
    }
    return new GStarModSafePrime(safePrime);
  }

  public static GStarModSafePrime getInstance(final int modulus) {
    return GStarModSafePrime.getInstance(BigInteger.valueOf(modulus));
  }

  public static GStarModSafePrime getInstance(final BigInteger modulus) {
    return new GStarModSafePrime(SafePrime.getInstance(modulus));
  }

  public static GStarModSafePrime getRandomInstance(int bitLength) {
    return GStarModSafePrime.getRandomInstance(bitLength, (Random) null);
  }

  public static GStarModSafePrime getRandomInstance(int bitLength, Random random) {
    return new GStarModSafePrime(SafePrime.getRandomInstance(bitLength, random));
  }

}
