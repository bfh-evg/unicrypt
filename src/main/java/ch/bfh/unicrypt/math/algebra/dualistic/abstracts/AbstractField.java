/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractField<E extends DualisticElement> extends AbstractRing<E> implements Field {

  public DualisticElement divide(Element element1, Element element2) {
    return this.multiply(element1, this.oneOver(element2));
  }

  public DualisticElement oneOver(Element element) {
    if (element.equals(this.getZero())) {
      throw new UnsupportedOperationException();
    }
    return this.abstractOneOver(element);
  }

  protected abstract E abstractOneOver(Element element);

}
