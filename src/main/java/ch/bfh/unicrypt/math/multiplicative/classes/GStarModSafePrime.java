/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.multiplicative.classes;

import ch.bfh.unicrypt.math.utility.Prime;
import ch.bfh.unicrypt.math.utility.SafePrime;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class GStarModSafePrime extends GStarModPrime {

  protected GStarModSafePrime(SafePrime modulo) {
    super(modulo, Prime.getInstance(modulo.getValue().subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
  }

  public static GStarModSafePrime getInstance(final SafePrime safePrime) {
    if (safePrime == null) {
      throw new IllegalArgumentException();
    }
    return new GStarModSafePrime(safePrime);
  }

  public static GStarModSafePrime getInstance(final BigInteger modulus) {
    return new GStarModSafePrime(SafePrime.getInstance(modulus));
  }

  public static GStarModSafePrime getRandomInstance(int bitLength, Random random) {
    return new GStarModSafePrime(SafePrime.getRandomInstance(bitLength, random));
  }

  public static GStarModSafePrime getRandomInstance(int bitLength) {
    return GStarModSafePrime.getRandomInstance(bitLength, (Random) null);
  }

}
