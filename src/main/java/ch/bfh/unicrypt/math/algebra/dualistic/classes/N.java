/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;
import java.util.Random;

/**
 * /**
 * This class implements the additive cyclic group of non-negative integers with
 * infinite order. Its identity element is 0.
 * <p>
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a
 * href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class N
       extends AbstractSemiRing<NElement> {

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected NElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount));
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected NElement abstractApply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()));
  }

  @Override
  protected NElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected NElement abstractMultiply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }

  @Override
  protected NElement abstractGetOne() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected NElement abstractGetElement(BigInteger value) {
    return new NElement(this, value);
  }

  @Override
  protected NElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //
  private static N instance;

  /**
   * Returns the singleton object of this class.
   * <p>
   * @return The singleton object of this class
   */
  public static N getInstance() {
    if (N.instance == null) {
      N.instance = new N();
    }
    return N.instance;
  }

}
