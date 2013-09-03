/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public class SemiGroupTuple extends Tuple<ProductSemiGroup, SemiGroup> {

  protected SemiGroupTuple(ProductSemiGroup productSemiGroup, Element<SemiGroup>[] elements) {
    super(productSemiGroup, elements);
  }

}
