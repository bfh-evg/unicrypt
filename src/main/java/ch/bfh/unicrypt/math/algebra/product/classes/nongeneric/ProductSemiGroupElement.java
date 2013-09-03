/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes.nongeneric;

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
    return ProductSemiGroup.getTuple(elements);
  }

}
