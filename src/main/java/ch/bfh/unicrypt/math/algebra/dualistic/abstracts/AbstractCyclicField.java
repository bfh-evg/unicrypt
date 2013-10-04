/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.CyclicField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCyclicField<E extends DualisticElement> extends AbstractCyclicRing<E> implements CyclicField {

  @Override
  public DualisticElement divide(Element element1, Element element2) {
    return this.multiply(element1, this.oneOver(element2));
  }

  @Override
  public DualisticElement oneOver(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    if (element.equals(this.getZero())) {
      throw new UnsupportedOperationException();
    }
    return this.abstractOneOver(element);
  }

  protected abstract E abstractOneOver(Element element);

}
