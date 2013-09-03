/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes.nongeneric;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundSet;

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
    return ProductSet.getTuple(elements);
  }

}
