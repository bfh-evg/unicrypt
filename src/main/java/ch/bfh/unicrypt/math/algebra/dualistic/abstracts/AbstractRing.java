/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractRing<E extends DualisticElement> extends AbstractAdditiveGroup<E> implements Ring {

  private E one;

  @Override
  public E multiply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return this.abstractMultiply(element1, element2);
  }

  @Override
  public E multiply(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    return this.standardMultiply(elements);
  }

  @Override
  public E power(Element element, BigInteger amount) {
    if (!this.contains(element) || (amount == null)) {
      throw new IllegalArgumentException();
    }
    return this.standardPower(element, amount);
  }

  @Override
  public E power(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.power(element, amount.getValue());
  }

  @Override
  public E power(Element element, int amount) {
    return this.power(element, BigInteger.valueOf(amount));
  }

  @Override
  public E square(Element element) {
    return this.multiply(element, element);
  }

  @Override
  public E productOfPowers(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length)) {
      throw new IllegalArgumentException();
    }
    return this.standardProductOfPowers(elements, amounts);
  }

  @Override
  public E getOne() {
    if (this.one == null) {
      this.one = this.abstractGetOne();
    }
    return this.one;
  }

  @Override
  public boolean isOne(final Element element) {
    return this.areEqual(element, this.getOne());
  }

//
// The following protected methods are standard implementations for sets.
// They may need to be changed in certain sub-classes.
//
  protected E standardMultiply(final Element... elements) {
    if (elements.length == 0) {
      return this.getOne();
    }
    E result = null;
    for (Element element : elements) {
      if (result == null) {
        result = (E) element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  protected E standardPower(Element element, BigInteger amount) {
    if (amount.signum() < 0) {
      throw new IllegalArgumentException();
    }
    if (amount.signum() == 0) {
      return this.getOne();
    }
    Element result = element;
    for (int i = amount.bitLength() - 2; i >= 0; i--) {
      result = result.selfApply();
      if (amount.testBit(i)) {
        result = result.apply(element);
      }
    }
    return (E) result;
  }

  protected E standardProductOfPowers(final Element[] elements, final BigInteger[] amounts) {
    if (elements.length == 0) {
      return this.getOne();
    }
    Element[] results = new Element[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

  //
  // The following protected abstract method must be implemented in every
  // direct sub-class.
  //
  protected abstract E abstractMultiply(Element element1, Element element2);

  protected abstract E abstractGetOne();

}
