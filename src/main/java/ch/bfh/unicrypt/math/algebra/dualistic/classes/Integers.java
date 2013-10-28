/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * /**
 * This class implements the additive cyclic group of (positive and negative)
 * integers with infinite order. Its identity element is 0, and there are
 * exactly two generators, namely 1 and -1. To invert an element, it is
 * multiplied with -1.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a
 * href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class Integers extends AbstractCyclicRing<IntegerElement> {

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected IntegerElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount));
  }

  @Override
  protected IntegerElement standardGetRandomGenerator(final Random random) {
    if (RandomUtil.getRandomBoolean(random)) {
      return this.getDefaultGenerator();
    }
    return this.getDefaultGenerator().invert();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected IntegerElement abstractApply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()));
  }

  @Override
  protected IntegerElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected IntegerElement abstractInvert(Element element) {
    return this.abstractGetElement(element.getValue().negate());
  }

  @Override
  protected IntegerElement abstractMultiply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }

  @Override
  protected IntegerElement abstractGetOne() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected IntegerElement abstractGetElement(BigInteger value) {
    return new IntegerElement(this, value) {
    };
  }

  @Override
  protected IntegerElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return true;
  }

  @Override
  protected IntegerElement abstractGetDefaultGenerator() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected boolean abstractIsGenerator(final Element element) {
    return element.getValue().abs().equals(BigInteger.ONE);
  }

  //
  // STATIC FACTORY METHODS
  //
  private static Integers instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static Integers getInstance() {
    if (Integers.instance == null) {
      Integers.instance = new Integers();
    }
    return Integers.instance;
  }

}
