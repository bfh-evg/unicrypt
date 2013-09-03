/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes.nongeneric;

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
    return ProductMonoid.getTuple(elements);
  }

}
