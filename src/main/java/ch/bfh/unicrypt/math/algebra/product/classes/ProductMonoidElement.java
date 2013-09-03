/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoidElement extends AbstractCompoundElement<ProductMonoid, ProductMonoidElement, Monoid, Element> {

  protected ProductMonoidElement(ProductMonoid productMonoid, Element[] elements) {
    super(productMonoid, elements);
  }

  @Override
  protected ProductMonoidElement abstractRemoveAt(Element[] elements) {
    return ProductMonoidElement.getInstance(elements);
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
  public static ProductMonoidElement getInstance(Element... elements) {
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
