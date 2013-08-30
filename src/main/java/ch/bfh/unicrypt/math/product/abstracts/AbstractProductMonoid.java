/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.product.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductMonoid<P extends AbstractProductMonoid, S extends Monoid, T extends Tuple, E extends Element> extends AbstractProductSemiGroup<P, S, T, E> implements Monoid {

  private T identityElement;

  protected AbstractProductMonoid(final Monoid... monoids) {
    super(monoids);
  }

  protected AbstractProductMonoid(final Monoid monoid, final int arity) {
    super(monoid, arity);
  }

  @Override
  public final T getIdentityElement() {
    if (this.identityElement == null) {
      final E[] identityElements = (E[]) new Element[this.getArity()];
      for (int i = 0; i < identityElements.length; i++) {
        identityElements[i] = (E) this.getAt(i).getIdentityElement();
      }
      this.identityElement = this.abstractGetElement(identityElements);
    }
    return this.identityElement;
  }

  @Override
  public final boolean isIdentityElement(Element element) {
    return this.areEqual(element, getIdentityElement());
  }

}
