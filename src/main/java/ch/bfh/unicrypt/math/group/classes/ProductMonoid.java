package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.interfaces.CompoundElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends ProductSemiGroup implements Monoid {

  private CompoundElement identityElement;

  protected ProductMonoid(final Monoid[] monoids) {
    super(monoids);
  }

  protected ProductMonoid(final Monoid monoid, final int arity) {
    super(monoid, arity);
  }

  protected ProductMonoid() {
    super();
  }

  @Override
  public Monoid getAt(final int index) {
    return (Monoid) super.getAt(index);
  }

  @Override
  public Monoid getAt(int... indices) {
    return (Monoid) super.getAt(indices);
  }

  @Override
  public Monoid getFirst() {
    return (Monoid) super.getFirst();
  }

  @Override
  public Monoid[] getAll() {
    return (Monoid[]) super.getAll();
  }

  @Override
  public ProductMonoid removeAt(final int index) {
    return (ProductMonoid) super.removeAt(index);
  }

  @Override
  public final CompoundElement getIdentityElement() {
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

  /**
   * This is a static factory method to construct a composed monoid without calling
   * respective constructors. The input monids are given as an array.
   * @param monoids The array of input monoids
   * @return The corresponding product monoids
   * @throws IllegalArgumentException if {@code monids} is null or contains null
   */
  public static ProductMonoid getInstance(final Monoid... monoids) {
    if (monoids == null) {
      throw new IllegalArgumentException();
    }
    if (monoids.length == 0) {
      return new ProductMonoid();
    }
    if (ProductSet.areEqual(monoids)) {
      return new ProductMonoid(monoids[0], monoids.length);
    }
    return new ProductMonoid(monoids);
  }

  public static ProductMonoid getInstance(final Monoid monoid, int arity) {
    if ((monoid == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductMonoid();
    }
    return new ProductMonoid(monoid, arity);
  }

  /**
   * This is a static factory method to construct a composed element without the
   * need of constructing the corresponding product or power group beforehand.
   * The input elements are given as an array.
   *
   * @param elements The array of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains
   * null
   */
  public static CompoundElement constructElement(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Monoid[] monoids = new Monoid[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      monoids[i] = elements[i].getMonoid();
    }
    return ProductMonoid.getInstance(monoids).getElement(elements);
  }

}
