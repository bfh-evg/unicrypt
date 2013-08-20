package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractAdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.abstracts.AbstractMultiplicativeMonoid;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
/**
 * This class implements the multiplicative monoid of (positive and negative) integers with infinite order.
 * Its identity element is 0. The methods {@link #getRandomElement()} and {@link #getRandomElement(Random)}
 * return random elements of a certain bit length.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZTimes extends AbstractMultiplicativeMonoid {

  private static final long serialVersionUID = 1L;
  private static final int RANDOM_ELEMENT_BIT_LENGTH = 1000;

  /**
   * This is the private constructor of this class. It is called by the static factory methods
   * of the static nested class Factory.
   */
  protected ZTimes() {
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //

  @Override
  protected Element abstractGetRandomElement(final Random random) {
    return this.abstractGetElement(RandomUtil.createRandomBigInteger(RANDOM_ELEMENT_BIT_LENGTH, random));
    // THIS IS NOT A UNIFORM DISTRIBUTION!!!
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return true;
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected Element abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected Element abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }

  @Override
  protected Element abstractGetElement(BigInteger value) {
    return new ZTimesElement(this, value);
  }

  @Override
  protected boolean abstractEquals(Set set) {
    return true;
  }

  //
  // LOCAL ELEMENT CLASS
  //

  final private class ZTimesElement extends Element {

    private static final long serialVersionUID = 1L;

    protected ZTimesElement(final Set set, final BigInteger value) {
      super(set, value);
    }

  }

  //
  // STATIC FACTORY METHODS
  //

  private static ZTimes instance;

  /**
   * Returns the singleton object of this class.
   * @return The singleton object of this class
   */
  public static ZTimes getInstance() {
    if (ZTimes.instance == null) {
      ZTimes.instance = new ZTimes();
    }
    return ZTimes.instance;
  }

}