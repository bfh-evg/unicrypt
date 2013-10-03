/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAdditiveElement<S extends AdditiveSemiGroup, E extends AdditiveElement> extends AbstractElement<S, E> implements AdditiveElement {

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

  @Override
  public final E add(final Element element) {
    return (E) this.getSet().add(this, element);
  }

  @Override
  public final E subtract(final Element element) {
    if (this.getSet().isGroup()) {
      AdditiveGroup group = ((AdditiveGroup) this.getSet());
      return (E) group.subtract(this, element);
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public final E times(final BigInteger amount) {
    return (E) this.getSet().times(this, amount);
  }

  @Override
  public final E times(final Element amount) {
    return (E) this.getSet().times(this, amount);
  }

  @Override
  public final E times(final int amount) {
    return (E) this.getSet().times(this, amount);
  }

  @Override
  public final E timesTwo() {
    return (E) this.getSet().timesTwo(this);
  }

  @Override
  public final E minus() {
    if (this.getSet().isGroup()) {
      AdditiveGroup group = ((AdditiveGroup) this.getSet());
      return (E) group.invert(this);
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isZero() {
    if (this.getSet().isMonoid()) {
      AdditiveMonoid monoid = ((AdditiveMonoid) this.getSet());
      return monoid.isZero(this);
    }
    throw new UnsupportedOperationException();
  }

}
