/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
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
public abstract class AbstractAdditiveElement extends AbstractElement<AdditiveElement> implements AdditiveElement {

  protected AbstractAdditiveElement(final Set set) {
    super(set);
  }

  protected AbstractAdditiveElement(final Set set, final BigInteger value) {
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
  public final AbstractAdditiveElement add(final Element element) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AbstractAdditiveElement) semiGroup.add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final AbstractAdditiveElement subtract(final Element element) {
    AdditiveGroup group = this.getAdditiveGroup();
    return (AbstractAdditiveElement) group.subtract(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final AbstractAdditiveElement times(final BigInteger amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AbstractAdditiveElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final AbstractAdditiveElement times(final Element amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AbstractAdditiveElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final AbstractAdditiveElement times(final int amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AbstractAdditiveElement) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final AbstractAdditiveElement timesTwo() {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (AbstractAdditiveElement) semiGroup.timesTwo(this);
  }

}
