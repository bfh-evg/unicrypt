package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundSet;

/**
 *
 * @author rolfhaenni
 */
public class Product<S extends Set> extends AbstractCompoundSet<Product<S>, Tuple<S>, S, Element> {

  protected Product(final S... sets) {
    super(sets);
  }

  protected Product(final S set, final int arity) {
    super(set, arity);
  }

  @Override
  protected Tuple<S> abstractGetElement(final Element[] elements) {
    return new Tuple<S>(this, elements) {
    };
  }

  @Override
  protected Product<S> abstractRemoveAt(S set, int arity) {
    return Product.<S>getInstance(set, arity);
  }

  @Override
  protected Product<S> abstractRemoveAt(S[] sets) {
    return Product.<S>getInstance(sets);
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
  public static <S extends Set> Product<S> getInstance(final S... sets) {
    if (sets == null) {
      throw new IllegalArgumentException();
    }
    if (sets.length > 0) {
      boolean uniform = true;
      S first = sets[0];
      for (final S set : sets) {
        if (set == null) {
          throw new IllegalArgumentException();
        }
        if (!set.equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return Product.<S>getInstance(first, sets.length);
      }
    }
    return new Product<S>(sets);
  }

  public static <S extends Set> Product<S> getInstance(final S set, int arity) {
    if ((set == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new Product<S>();
    }
    return new Product<S>(set, arity);
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
  public static <S extends Set> Tuple<S> getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final S[] sets = (S[]) new Set[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = (S) elements[i].getSet();
    }
    return Product.<S>getInstance(sets).getElement(elements);
  }

}
