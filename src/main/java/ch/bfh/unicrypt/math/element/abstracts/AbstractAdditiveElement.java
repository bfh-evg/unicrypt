/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAdditiveElement<T extends AdditiveElement> extends AbstractElement<T> implements AdditiveElement {

  protected AbstractAdditiveElement(final Set set) {
    super(set);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final T add(final Element element) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final T subtract(final Element element) {
    AdditiveGroup group = this.getAdditiveGroup();
    return (T) group.subtract(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final T times(final BigInteger amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final T times(final Element amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final T times(final int amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final T timesTwo() {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.timesTwo(this);
  }

}
