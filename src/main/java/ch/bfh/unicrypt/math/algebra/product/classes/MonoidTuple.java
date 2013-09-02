/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public class MonoidTuple extends Tuple<ProductMonoid, Monoid> {

  protected MonoidTuple(ProductMonoid productMonoid, Element<Monoid>[] elements) {
    super(productMonoid, elements);
  }

}
