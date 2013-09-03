package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeMonoid;

/**
 * /**
 * This class implements the multiplicative monoid of non-negative integers with
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
public class NTimes extends AbstractMultiplicativeMonoid<NTimesElement> {

  /**
   * This is the private constructor of this class. It is called by the static
   * factory methods of the static nested class Factory.
   */
  private NTimes() {
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected NTimesElement abstractGetElement(BigInteger value) {
    return new NTimesElement(this, value) {
    };
  }

  @Override
  protected NTimesElement abstractGetRandomElement(final Random random) {
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
  protected NTimesElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected NTimesElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }
  //
  // STATIC FACTORY METHODS
  //
  private static NTimes instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static NTimes getInstance() {
    if (NTimes.instance == null) {
      NTimes.instance = new NTimes();
    }
    return NTimes.instance;
  }

}
