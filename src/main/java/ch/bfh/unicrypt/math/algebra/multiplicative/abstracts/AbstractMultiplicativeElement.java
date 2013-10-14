/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractMultiplicativeElement<S extends MultiplicativeSemiGroup, E extends MultiplicativeElement> extends AbstractElement<S, E> implements MultiplicativeElement {

  protected AbstractMultiplicativeElement(final S semiGroup) {
    super(semiGroup);
  }

  protected AbstractMultiplicativeElement(final S semiGroup, final BigInteger value) {
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
  public final E multiply(final Element element) {
    return (E) this.getSet().multiply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final E divide(final Element element) {
    if (this.getSet().isGroup()) {
      MultiplicativeGroup group = ((MultiplicativeGroup) this.getSet());
      return (E) group.divide(this, element);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final E power(final BigInteger amount) {
    return (E) this.getSet().power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final E power(final Element amount) {
    return (E) this.getSet().power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final E power(final int amount) {
    return (E) this.getSet().power(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final E square() {
    return (E) this.getSet().square(this);
  }

  @Override
  public final E oneOver() {
    if (this.getSet().isGroup()) {
      MultiplicativeGroup group = ((MultiplicativeGroup) this.getSet());
      return (E) group.invert(this);
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isOne() {
    if (this.getSet().isMonoid()) {
      MultiplicativeMonoid monoid = ((MultiplicativeMonoid) this.getSet());
      return monoid.isOneElement(this);
    }
    throw new UnsupportedOperationException();
  }

}
