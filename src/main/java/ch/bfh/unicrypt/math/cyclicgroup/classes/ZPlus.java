package ch.bfh.unicrypt.math.cyclicgroup.classes;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.cyclicgroup.abstracts.AbstractAdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
/**
 * This class implements the additive cyclic group of (positive and negative) integers with infinite order.
 * Its identity element is 0, and there are exactly two generators, namely 1 and -1. To invert an element,
 * it is multiplied with -1.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZPlus extends AbstractAdditiveCyclicGroup {

  /**
   * This is the private constructor of this class. It is called by the static factory methods
   * of the static nested class Factory.
   */
  private ZPlus() {
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  protected AdditiveElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount));
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //

  @Override
  protected AdditiveElement abstractGetRandomElement(final Random random) {
    throw new UnsupportedOperationException();
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
  protected AdditiveElement abstractGetDefaultGenerator() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  public boolean abstractIsGenerator(final Element element) {
    return element.getValue().abs().equals(BigInteger.ONE);
  }

  @Override
  protected AdditiveElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected AdditiveElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()));
  }

  @Override
  protected AdditiveElement abstractInvert(final Element element) {
    return this.abstractGetElement(element.getValue().negate());
  }

  @Override
  public AdditiveElement abstractGetRandomGenerator(final Random random) {
    if (RandomUtil.createRandomBoolean(random)) {
      return this.getDefaultGenerator();
    }
    return this.invert(this.getDefaultGenerator());
  }

  //
  // STATIC FACTORY METHODS
  //

  private static ZPlus instance;

  /**
   * Returns the singleton object of this class.
   * @return The singleton object of this class
   */
  public static ZPlus getInstance() {
    if (ZPlus.instance == null) {
      ZPlus.instance = new ZPlus();
    }
    return ZPlus.instance;
  }

}