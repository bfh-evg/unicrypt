/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes.nongeneric;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroupElement extends AbstractCompoundElement<ProductCyclicGroup, ProductCyclicGroupElement, CyclicGroup, Element> {

  protected ProductCyclicGroupElement(ProductCyclicGroup productCyclicGroup, Element[] elements) {
    super(productCyclicGroup, elements);
  }

  @Override
  protected ProductCyclicGroupElement abstractRemoveAt(Element[] elements) {
    return ProductCyclicGroup.getTuple(elements);
  }

}
