/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductSetElement extends AbstractCompoundElement<ProductSet, ProductSetElement, Set, Element> {

  protected ProductSetElement(ProductSet productSet, Element[] elements) {
    super(productSet, elements);
  }

  @Override
  protected ProductSetElement abstractRemoveAt(Element[] elements) {
    return ProductSetElement.getInstance(elements);
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
  public static ProductSetElement getInstance(Element... elements) {
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
