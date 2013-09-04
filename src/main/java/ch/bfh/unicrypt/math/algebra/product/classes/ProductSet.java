package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundSet;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet extends AbstractCompoundSet<ProductSet, ProductSetElement, Set, Element> {

  protected ProductSet(final Set... sets) {
    super(sets);
  }

  protected ProductSet(final Set set, final int arity) {
    super(set, arity);
  }

  @Override
  protected ProductSetElement abstractGetElement(final Element[] elements) {
    return new ProductSetElement(this, elements) {
    };
  }

  @Override
  protected ProductSet abstractRemoveAt(Set set, int arity) {
    return ProductSet.getInstance(set, arity);
  }

  @Override
  protected ProductSet abstractRemoveAt(Set[] sets) {
    return ProductSet.getInstance(sets);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is a static factory method to construct a composed set without calling
   * respective constructors. The input sets are given as an array.
   *
   * @param sets The array of input sets
   * @return The corresponding product set
   * @throws IllegalArgumentException if {@code sets} is null or contains null
   */
  public static ProductSet getInstance(final Set... sets) {
    if (sets == null) {
      throw new IllegalArgumentException();
    }
    if (sets.length > 0) {
      boolean uniform = true;
      Set first = sets[0];
      for (final Set set : sets) {
        if (set == null) {
          throw new IllegalArgumentException();
        }
        if (!set.equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return ProductSet.getInstance(first, sets.length);
      }
    }
    return new ProductSet(sets);
  }

  public static ProductSet getInstance(final Set set, int arity) {
    if ((set == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSet();
    }
    return new ProductSet(set, arity);
  }

}
