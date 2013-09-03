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
public class Tuple<S extends Set> extends AbstractCompoundElement<Product<S>, Tuple<S>, S, Element> {

  protected Tuple(Product<S> product, Element[] elements) {
    super(product, elements);
  }

  @Override
  protected Tuple<S> abstractRemoveAt(Element[] elements) {
    return Product.getTuple(elements);
  }

}
