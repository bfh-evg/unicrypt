/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductGroupElement extends AbstractCompoundElement<ProductGroup, ProductGroupElement, Group, Element> {

  protected ProductGroupElement(ProductGroup productGroup, Element[] elements) {
    super(productGroup, elements);
  }

  @Override
  protected ProductGroupElement abstractRemoveAt(Element[] elements) {
    return ProductGroupElement.getInstance(elements);
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
  public static ProductGroupElement getInstance(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Group[] groups = new Group[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      groups[i] = (Group) elements[i].getSet();
    }
    return ProductGroup.getInstance(groups).getElement(elements);
  }

}
