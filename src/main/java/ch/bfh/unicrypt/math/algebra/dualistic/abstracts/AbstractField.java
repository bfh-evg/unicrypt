/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractField<E extends DualisticElement, M extends MultiplicativeGroup> extends AbstractRing<E> implements Field {

  private M multiplicativeGroup;

  @Override
  public M getMultiplicativeGroup() {
    if (this.multiplicativeGroup == null) {
      this.multiplicativeGroup = this.abstractGetMultiplicativeGroup();
    }
    return this.multiplicativeGroup;
  }

  @Override
  public final E divide(Element element1, Element element2) {
    return this.multiply(element1, this.oneOver(element2));
  }

  @Override
  public final E oneOver(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    if (element.isEqual(this.getZeroElement())) {
      throw new UnsupportedOperationException();
    }
    return this.abstractOneOver(element);
  }

  protected abstract E abstractOneOver(Element element);

  protected abstract M abstractGetMultiplicativeGroup();

}
