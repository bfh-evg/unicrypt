/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class SizedStringSet
       extends AbstractSet<SizedStringElement> {

  private final int maxSize;
  private final Alphabet alphabet;

  private SizedStringSet(int maxSize, Alphabet alphabet) {
    this.maxSize = maxSize;
    this.alphabet = alphabet;
  }

  public int getMaxSize() {
    return this.maxSize;
  }

  public Alphabet getAlphabet() {
    return this.alphabet;
  }

  public final SizedStringElement getElement(final String string) {
    if (string == null || string.length() > this.getMaxSize()) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(string);
  }

  protected SizedStringElement standardGetElement(String string) {
    return new SizedStringElement(this, string);
  }

  @Override
  protected SizedStringElement abstractGetElement(BigInteger value) {
    String result = "";
    BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
    while (!value.equals(BigInteger.ZERO)) {
      value = value.subtract(BigInteger.ONE);
      result = this.getAlphabet().getCharacter(value.mod(size).intValue()) + result;
      value = value.divide(size);
    }
    return this.standardGetElement(result);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    throw new UnsupportedOperationException();
    // to implement this, a charset needs to be defined in this class
  }

  @Override
  protected SizedStringElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
    // to implement this, a charset needs to be defined in this class
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //

  public static SizedStringSet getInstance(final int maxSize, final Alphabet alphabet) {
    if (maxSize < 0 || alphabet == null) {
      throw new IllegalArgumentException();
    }
    return new SizedStringSet(maxSize, alphabet);
  }

}
