/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.CyclicField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import ch.bfh.unicrypt.math.helper.factorization.Prime;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ZPlusTimesModPrime extends ZPlusTimesMod implements CyclicField {

  protected ZPlusTimesModPrime(Prime prime) {
    super(prime.getValue());
  }

  @Override
  public ZStarModPrime getMultiplicativeGroup() {
    return ZStarModPrime.getInstance(this.getModulus());
  }

  @Override
  public ZPlusTimesModElement divide(Element element1, Element element2) {
    return this.multiply(element1, this.oneOver(element2));
  }

  @Override
  public ZPlusTimesModElement oneOver(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    if (element.equals(this.getZero())) {
      throw new UnsupportedOperationException();
    }
    return this.abstractGetElement(element.getValue().modInverse(this.getModulus()));
  }

  //
  // STATIC FACTORY METHODS
  //
  public static ZPlusTimesModPrime getInstance(final Prime prime) {
    if (prime == null) {
      throw new IllegalArgumentException();
    }
    return new ZPlusTimesModPrime(prime);
  }

  public static ZPlusTimesModPrime getInstance(final int modulus) {
    return ZPlusTimesModPrime.getInstance(BigInteger.valueOf(modulus));
  }

  public static ZPlusTimesModPrime getInstance(BigInteger prime) {
    return new ZPlusTimesModPrime(Prime.getInstance(prime));
  }

  public static ZPlusTimesModPrime getRandomInstance(int bitLength, Random random) {
    return new ZPlusTimesModPrime(Prime.getRandomInstance(bitLength, random));
  }

  public static ZPlusTimesModPrime getRandomInstance(int bitLength) {
    return ZPlusTimesModPrime.getRandomInstance(bitLength, (Random) null);
  }

}
