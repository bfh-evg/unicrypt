/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public class Strings extends AbstractConcatenativeMonoid<StringElement> {

  public static final StringElement EMPTY_STRING = Strings.getInstance().getElement("");

  private Strings() {
  }

  public final StringElement getElement(final String string) {
    if (string == null) {
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
    return this.standardGetElement(new String(value.toByteArray()));
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
  private static Strings instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static Strings getInstance() {
    if (Strings.instance == null) {
      Strings.instance = new Strings();
    }
    return Strings.instance;
  }

}
