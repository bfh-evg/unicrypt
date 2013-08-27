/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.monoid.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.monoid.classes.ProductMonoid;
import ch.bfh.unicrypt.math.monoid.interfaces.Monoid;
import ch.bfh.unicrypt.math.semigroup.abstracts.AbstractProductSemiGroup;
import ch.bfh.unicrypt.math.semigroup.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.semigroup.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductMonoid<S extends Monoid> extends AbstractProductSemiGroup<S> implements Monoid {

  private Tuple identityElement;

  protected AbstractProductMonoid(final Monoid[] monoids) {
    super(monoids);
  }

  protected AbstractProductMonoid(final Monoid monoid, final int arity) {
    super(monoid, arity);
  }

  protected AbstractProductMonoid() {
    super();
  }
  
  @Override
  public final Tuple getIdentityElement() {
    if (this.identityElement == null) {
      final Element[] identityElements = new Element[this.getArity()];
      for (int i=0; i<identityElements.length; i++) {
        identityElements[i] = this.getAt(i).getIdentityElement();
      }
      this.identityElement = this.standardGetElement(identityElements);
    }
    return this.identityElement;
  }

  @Override
  public final boolean isIdentityElement(Element element) {
    return this.areEqual(element, getIdentityElement());
  }

}
