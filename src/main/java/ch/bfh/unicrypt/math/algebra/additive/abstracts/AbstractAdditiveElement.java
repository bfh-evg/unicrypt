/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAdditiveElement<S extends AdditiveSemiGroup, E extends AdditiveElement<S>> extends AbstractElement<S, E> implements AdditiveElement<S> {

  protected AbstractAdditiveElement(final S semiGroup) {
    super(semiGroup);
  }

  protected AbstractAdditiveElement(final S semiGroup, final BigInteger value) {
    super(semiGroup);
    if (!semiGroup.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final E add(final Element element) {
    return (E) this.getSet().add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final E subtract(final Element element) {
    if (this.getSet() instanceof AdditiveGroup) {
      AdditiveGroup group = ((AdditiveGroup) this.getSet());
      return (E) group.subtract(this, element);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final E times(final BigInteger amount) {
    return (E) this.getSet().times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final E times(final Element amount) {
    return (E) this.getSet().times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final E times(final int amount) {
    return (E) this.getSet().times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final E timesTwo() {
    return (E) this.getSet().timesTwo(this);
  }

}
