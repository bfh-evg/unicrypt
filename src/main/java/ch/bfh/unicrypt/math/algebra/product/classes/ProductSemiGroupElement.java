/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroupElement extends AbstractCompoundElement<ProductSemiGroup, ProductSemiGroupElement, SemiGroup, Element> {

  protected ProductSemiGroupElement(ProductSemiGroup productSemiGroup, Element[] elements) {
    super(productSemiGroup, elements);
  }

  @Override
  protected ProductSemiGroupElement abstractRemoveAt(Element[] elements) {
    return ProductSemiGroupElement.getInstance(elements);
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
  public static ProductSemiGroupElement getInstance(Element... elements) {
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
