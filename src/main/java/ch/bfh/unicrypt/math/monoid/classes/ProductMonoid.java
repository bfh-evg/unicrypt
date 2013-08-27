package ch.bfh.unicrypt.math.monoid.classes;

import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.monoid.abstracts.AbstractProductMonoid;
import ch.bfh.unicrypt.math.monoid.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends AbstractProductMonoid<Monoid> {

  protected ProductMonoid(final Monoid[] monoids) {
    super(monoids);
  }

  protected ProductMonoid(final Monoid monoid, final int arity) {
    super(monoid, arity);
  }

  protected ProductMonoid() {
    super();
  }

  public ProductMonoid removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return ProductMonoid.getInstance(this.getFirst(), arity-1);
    }
    final Monoid[] remainingMonoids = new Monoid[arity - 1];
    for (int i=0; i < arity-1; i++) {
      if (i < index) {
        remainingMonoids[i] = this.getAt(i);
      } else {
        remainingMonoids[i] = this.getAt(i+1);
      }
    }
    return ProductMonoid.getInstance(remainingMonoids);
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
    Monoid first = monoids[0];
    for (final Monoid monoid : monoids) {
      if (monoid == null) {
        throw new IllegalArgumentException();
      }
      if (!monoid.equals(first)) {
        return new ProductMonoid(monoids);
      }
    }
    return new ProductMonoid(first, monoids.length);
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
  public static Tuple constructElement(Element... elements) {
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
