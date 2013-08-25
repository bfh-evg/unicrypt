/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.classes;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.abstracts.AbstractAtomicElement;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class AdditiveAtomicElement extends AbstractAtomicElement<AdditiveAtomicElement> implements AdditiveElement {

  protected AdditiveAtomicElement(final Set set) {
    super(set);
  }

  protected AdditiveAtomicElement(final Set set, final BigInteger value) {
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
  public final AdditiveSemiGroup getAdditiveSemiGroup() {
    if (this.getSet() instanceof AdditiveSemiGroup) {
      return (AdditiveSemiGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveMonoid getAdditiveMonoid() {
    if (this.getSet() instanceof AdditiveMonoid) {
      return (AdditiveMonoid) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveGroup getAdditiveGroup() {
    if (this.getSet() instanceof AdditiveGroup) {
      return (AdditiveGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveCyclicGroup getAdditiveCyclicGroup() {
    if (this.getSet() instanceof AdditiveCyclicGroup) {
      return (AdditiveCyclicGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final AdditiveAtomicElement add(final Element element) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AdditiveAtomicElement) semiGroup.add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final AdditiveAtomicElement subtract(final Element element) {
    AdditiveGroup group = this.getAdditiveGroup();
    return (AdditiveAtomicElement) group.subtract(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final AdditiveAtomicElement times(final BigInteger amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AdditiveAtomicElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final AdditiveAtomicElement times(final Element amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AdditiveAtomicElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final AdditiveAtomicElement times(final int amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AdditiveAtomicElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final AdditiveAtomicElement timesTwo() {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AdditiveAtomicElement) semiGroup.timesTwo(this);
  }

}
