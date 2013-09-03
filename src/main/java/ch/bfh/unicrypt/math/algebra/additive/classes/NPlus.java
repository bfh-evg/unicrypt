package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 * /**
 * This class implements the additive cyclic group of non-negative integers with
 * infinite order. Its identity element is 0.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a
 * href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class NPlus extends AbstractAdditiveMonoid<NPlusElement> {

  /**
   * This is the private constructor of this class. It is called by the static
   * factory methods of the static nested class Factory.
   */
  private NPlus() {
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected NPlusElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount));
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected NPlusElement abstractGetElement(BigInteger value) {
    return new NPlusElement(this, value) {
    };
  }

  @Override
  protected NPlusElement abstractGetRandomElement(final Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return value.signum() >= 0;
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected NPlusElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected NPlusElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()));
  }
  //
  // STATIC FACTORY METHODS
  //
  private static NPlus instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static NPlus getInstance() {
    if (NPlus.instance == null) {
      NPlus.instance = new NPlus();
    }
    return NPlus.instance;
  }

}
