/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.classes;

import ch.bfh.unicrypt.math.element.abstracts.AbstractAtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class MultiplicativeAtomicElement extends AbstractAtomicElement<MultiplicativeAtomicElement> implements MultiplicativeElement {

  protected MultiplicativeAtomicElement(final Set set) {
    super(set);
  }

  protected MultiplicativeAtomicElement(final Set set, final BigInteger value) {
    super(set);
    if (!set.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeSemiGroup getMultiplicativeSemiGroup() {
    if (this.getSet() instanceof MultiplicativeSemiGroup) {
      return (MultiplicativeSemiGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeMonoid getMultiplicativeMonoid() {
    if (this.getSet() instanceof MultiplicativeMonoid) {
      return (MultiplicativeMonoid) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeGroup getMultiplicativeGroup() {
    if (this.getSet() instanceof MultiplicativeGroup) {
      return (MultiplicativeGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeCyclicGroup getMultiplicativeCyclicGroup() {
    if (this.getSet() instanceof MultiplicativeCyclicGroup) {
      return (MultiplicativeCyclicGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final MultiplicativeAtomicElement multiply(final Element element) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (MultiplicativeAtomicElement) semiGroup.multiply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final MultiplicativeAtomicElement divide(final Element element) {
    MultiplicativeGroup group = this.getMultiplicativeGroup();
    return (MultiplicativeAtomicElement) group.divide(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final MultiplicativeAtomicElement power(final BigInteger amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (MultiplicativeAtomicElement) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final MultiplicativeAtomicElement power(final Element amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (MultiplicativeAtomicElement) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final MultiplicativeAtomicElement power(final int amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (MultiplicativeAtomicElement) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final MultiplicativeAtomicElement square() {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (MultiplicativeAtomicElement) semiGroup.square(this);
  }

}
