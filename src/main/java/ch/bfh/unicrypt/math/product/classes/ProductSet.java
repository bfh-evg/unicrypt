package ch.bfh.unicrypt.math.product.classes;

import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.abstracts.AbstractProductSet;
import ch.bfh.unicrypt.math.general.interfaces.Set;
import ch.bfh.unicrypt.math.product.abstracts.AbstractTuple;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet extends AbstractProductSet<Set, Tuple, Element> {

  protected ProductSet(final Set[] sets) {
    super(sets);
  }

  protected ProductSet(final Set set, final int arity) {
    super(set, arity);
  }

  protected ProductSet() {
    super();
  }

  /**
   * Creates a new product set which contains one set less than the given
   * product set.
   *
   * @param index The index of the set to remove
   * @return The resulting product set.
   * @throws IndexOutOfBoundsException if
   * {@code index<0} or {@code index>arity-1}
   */
  public ProductSet removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return ProductSet.getInstance(this.getFirst(), arity - 1);
    }
    final Set[] remainingSets = new Set[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingSets[i] = this.getAt(i);
      } else {
        remainingSets[i] = this.getAt(i + 1);
      }
    }
    return ProductSet.getInstance(remainingSets);
  }

  @Override
  protected Tuple abstractGetElement(final Element[] elements) {
    return new AbstractTuple<ProductSet, Tuple, Element>(this, elements) {
    };
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
    if (sets.length == 0) {
      return new ProductSet();
    }
    Set first = sets[0];
    for (final Set set : sets) {
      if (set == null) {
        throw new IllegalArgumentException();
      }
      if (!set.equals(first)) {
        return new ProductSet(sets);
      }
    }
    return new ProductSet(first, sets.length);
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

  //
  // STATIC HELPER METHODS
  //
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
    final Set[] sets = new Set[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = elements[i].getSet();
    }
    return ProductSet.getInstance(sets).getElement(elements);
  }

}
