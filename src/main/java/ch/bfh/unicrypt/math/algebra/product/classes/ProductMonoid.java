package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundMonoid;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends AbstractCompoundMonoid<ProductMonoid, ProductMonoidElement, Monoid, Element> {

  protected ProductMonoid(final Monoid[] monoids) {
    super(monoids);
  }

  protected ProductMonoid(final Monoid monoid, final int arity) {
    super(monoid, arity);
  }

  @Override
  protected ProductMonoidElement abstractGetElement(final Element[] elements) {
    return new ProductMonoidElement(this, elements) {
    };
  }

  @Override
  protected ProductMonoid abstractRemoveAt(Monoid monoid, int arity) {
    return ProductMonoid.getInstance(monoid, arity);
  }

  @Override
  protected ProductMonoid abstractRemoveAt(Monoid[] monoids) {
    return ProductMonoid.getInstance(monoids);
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
    if (monoids.length > 0) {
      boolean uniform = true;
      Monoid first = monoids[0];
      for (final Monoid monoid : monoids) {
        if (monoid == null) {
          throw new IllegalArgumentException();
        }
        if (!monoid.equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return ProductMonoid.getInstance(first, monoids.length);
      }
    }
    return new ProductMonoid(monoids);
  }

  public static ProductMonoid getInstance(final Monoid monoid, int arity) {
    if ((monoid == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductMonoid(new Monoid[]{});
    }
    return new ProductMonoid(monoid, arity);
  }

}
