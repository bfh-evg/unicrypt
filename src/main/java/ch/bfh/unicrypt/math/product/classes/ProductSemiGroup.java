package ch.bfh.unicrypt.math.product.classes;

import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.abstracts.AbstractProductSemiGroup;
import ch.bfh.unicrypt.math.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.product.abstracts.AbstractTuple;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroup extends AbstractProductSemiGroup<SemiGroup, Tuple, Element> {

  protected ProductSemiGroup(final SemiGroup[] semiGroups) {
    super(semiGroups);
  }

  protected ProductSemiGroup(final SemiGroup semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  protected ProductSemiGroup() {
    super();
  }

  public ProductSemiGroup removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return ProductSemiGroup.getInstance(this.getFirst(), arity - 1);
    }
    final SemiGroup[] remainingSemiGroups = new SemiGroup[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingSemiGroups[i] = this.getAt(i);
      } else {
        remainingSemiGroups[i] = this.getAt(i + 1);
      }
    }
    return ProductSemiGroup.getInstance(remainingSemiGroups);
  }

  @Override
  protected Tuple abstractGetElement(final Element[] elements) {
    return new AbstractTuple<ProductSemiGroup, Tuple, Element>(this, elements) {
    };
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
    if (semiGroups.length == 0) {
      return new ProductSemiGroup();
    }
    SemiGroup first = semiGroups[0];
    for (final SemiGroup semiGroup : semiGroups) {
      if (semiGroup == null) {
        throw new IllegalArgumentException();
      }
      if (!semiGroup.equals(first)) {
        return new ProductSemiGroup(semiGroups);
      }
    }
    return new ProductSemiGroup(first, semiGroups.length);
  }

  public static ProductSemiGroup getInstance(final SemiGroup semiGroup, int arity) {
    if ((semiGroup == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSemiGroup();
    }
    return new ProductSemiGroup(semiGroup, arity);
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
  public static Tuple getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final SemiGroup[] semiGroups = new SemiGroup[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      semiGroups[i] = (SemiGroup) elements[i].getSet();
    }
    return ProductSemiGroup.getInstance(semiGroups).getElement(elements);
  }

}
