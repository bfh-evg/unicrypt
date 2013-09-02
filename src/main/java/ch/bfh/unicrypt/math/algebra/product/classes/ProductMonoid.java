package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends AbstractCompoundMonoid<ProductMonoid, Tuple<ProductMonoid, Monoid>, Monoid, Element<Monoid>> {

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
  protected Tuple<ProductMonoid, Monoid> abstractGetElement(final Element[] elements) {
    return new Tuple<ProductMonoid, Monoid>(this, elements) {
      @Override
      protected Tuple<ProductMonoid, Monoid> abstractRemoveAt(Element[] elements) {
        return ProductMonoid.getTuple(elements);
      }
    };
  }

  @Override
  protected ProductMonoid abstractRemoveAt(Set set, int arity) {
    return ProductMonoid.getInstance((Monoid) set, arity);
  }

  @Override
  protected ProductMonoid abstractRemoveAt(Set[] sets) {
    return ProductMonoid.getInstance((Monoid[]) sets);
  }

  /**
   * This is a static factory method to construct a composed monoid without
   * calling respective constructors. The input monids are given as an array.
   *
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
  public static Tuple<ProductMonoid, Monoid> getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Monoid[] monoids = new Monoid[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      monoids[i] = (Monoid) elements[i].getSet();
    }
    return ProductMonoid.getInstance(monoids).getElement(elements);
  }

}
