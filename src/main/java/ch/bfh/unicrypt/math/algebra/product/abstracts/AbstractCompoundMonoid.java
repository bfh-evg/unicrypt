/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundMonoid<CS extends AbstractCompoundMonoid<CS, CE, S, E>, CE extends AbstractCompoundElement<CS, CE, S, E>, S extends Monoid, E extends Element>
        extends AbstractCompoundSemiGroup<CS, CE, S, E> implements Monoid {

  private CE identityElement;

  protected AbstractCompoundMonoid(final S[] monoids) {
    super(monoids);
  }

  protected AbstractCompoundMonoid(final S monoid, final int arity) {
    super(monoid, arity);
  }

  @Override
  public final CE getIdentityElement() {
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
