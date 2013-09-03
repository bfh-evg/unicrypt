package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundSemiGroup;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroup extends AbstractCompoundSemiGroup<ProductSemiGroup, ProductSemiGroupElement, SemiGroup, Element> {

  protected ProductSemiGroup(final SemiGroup[] semiGroups) {
    super(semiGroups);
  }

  protected ProductSemiGroup(final SemiGroup semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  @Override
  protected ProductSemiGroupElement abstractGetElement(final Element[] elements) {
    return new ProductSemiGroupElement(this, elements) {
    };
  }

  @Override
  protected ProductSemiGroup abstractRemoveAt(SemiGroup semiGroup, int arity) {
    return ProductSemiGroup.getInstance(semiGroup, arity);
  }

  @Override
  protected ProductSemiGroup abstractRemoveAt(SemiGroup[] semiGroups) {
    return ProductSemiGroup.getInstance(semiGroups);
  }

  /**
   * This is a static factory method to construct a composed semigroup without
   * calling respective constructors. The input semigroups are given as an
   * array.
   *
   * @param semiGroups The array of input semigroups
   * @return The corresponding product semigroup
   * @throws IllegalArgumentException if {@code semigroups} is null or contains
   * null
   */
  public static ProductSemiGroup getInstance(final SemiGroup... semiGroups) {
    if (semiGroups == null) {
      throw new IllegalArgumentException();
    }
    if (semiGroups.length > 0) {
      boolean uniform = true;
      SemiGroup first = semiGroups[0];
      for (final SemiGroup semiGroup : semiGroups) {
        if (semiGroup == null) {
          throw new IllegalArgumentException();
        }
        if (!semiGroup.equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return ProductSemiGroup.getInstance(first, semiGroups.length);
      }
    }
    return new ProductSemiGroup(semiGroups);
  }

  public static ProductSemiGroup getInstance(final SemiGroup semiGroup, int arity) {
    if ((semiGroup == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSemiGroup(new SemiGroup[]{});
    }
    return new ProductSemiGroup(semiGroup, arity);
  }

}
