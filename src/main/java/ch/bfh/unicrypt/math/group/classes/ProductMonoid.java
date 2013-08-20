package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends ProductSemiGroup implements Monoid {

  private Element identityElement;

  protected ProductMonoid(final ProductMonoid[] monoids) {
    super(monoids);
  }

  protected ProductMonoid(final ProductMonoid monoid, final int arity) {
    super(monoid, arity);
  }

  protected ProductMonoid() {
    super();
  }

  @Override
  public Monoid getAt(final int index) {
    return (Monoid) this.getAt(index);
  }

  @Override
  public Monoid getAt(int... indices) {
    return (Monoid) this.getAt(indices);
  }

  @Override
  public Monoid getFirst() {
    return (Monoid) this.getFirst();
  }

  @Override
  public Monoid removeAt(final int index) {
    return (Monoid) this.removeAt(index);
  }

  @Override
  public Element getIdentityElement() {
    final Element[] identityElements = new Element[this.getArity()];
    for (int i = 0; i < identityElements.length; i++) {
      identityElements[i] = this.getAt(i).getIdentityElement();
    }
    return abstractGetElement(identityElements);
  }

  @Override
  public boolean isIdentityElement(Element element) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


}
