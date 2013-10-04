/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;
import java.util.Random;

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
public class NPlusTimes extends AbstractSemiRing<NPlusTimesElement> {

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected NPlusTimesElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount));
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected NPlusTimesElement abstractApply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()));
  }

  @Override
  protected NPlusTimesElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected NPlusTimesElement abstractMultiply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
  }

  @Override
  protected NPlusTimesElement abstractGetOne() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Group.INFINITE_ORDER;
  }

  @Override
  protected NPlusTimesElement abstractGetElement(BigInteger value) {
    return new NPlusTimesElement(this, value) {
    };
  }

  @Override
  protected NPlusTimesElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //
  private static NPlusTimes instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static NPlusTimes getInstance() {
    if (NPlusTimes.instance == null) {
      NPlusTimes.instance = new NPlusTimes();
    }
    return NPlusTimes.instance;
  }

}
