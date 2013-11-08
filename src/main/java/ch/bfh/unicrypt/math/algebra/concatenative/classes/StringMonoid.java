/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class StringMonoid
       extends AbstractConcatenativeMonoid<StringElement> {

  private final Alphabet alphabet;

  private StringMonoid(Alphabet alphabet) {
    this.alphabet = alphabet;
  }

  public Alphabet getAlphabet() {
    return this.alphabet;
  }

  public final StringElement getElement(final String string) {
    if (string == null || !this.getAlphabet().isValidString(string)) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(string);
  }

  protected StringElement standardGetElement(String string) {
    return new StringElement(this, string) {
    };
  }

  @Override
  protected StringElement abstractGetElement(BigInteger value) {
    String result = "";
    BigInteger base = BigInteger.valueOf(this.getAlphabet().getSize());
    while (!value.equals(BigInteger.ZERO)) {
      value = value.subtract(BigInteger.ONE);
      result = this.getAlphabet().getCharacter(value.mod(base).intValue()) + result;
      value = value.divide(base);
    }
    return this.standardGetElement(result);
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected StringElement abstractGetIdentityElement() {
    return this.standardGetElement("");
  }

  @Override
  protected StringElement abstractApply(Element element1, Element element2) {
    return this.standardGetElement(((StringElement) element1).getString() + ((StringElement) element2).getString());
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Set.INFINITE_ORDER;
  }

  @Override
  protected StringElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //

  public static StringMonoid getInstance(Alphabet alphabet) {
    if (alphabet == null) {
      throw new IllegalArgumentException();
    }
    return new StringMonoid(alphabet);
  }

}
