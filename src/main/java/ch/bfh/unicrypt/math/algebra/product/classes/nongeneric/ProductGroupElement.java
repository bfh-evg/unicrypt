/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes.nongeneric;

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
    return ProductGroup.getTuple(elements);
  }

}
