/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundSet;

/**
 *
 * @author rolfhaenni
 */
public class Tuple<CS extends AbstractCompoundSet, S extends Set> extends AbstractCompoundElement<CS, Tuple<CS, S>, S, Element<S>> {

  protected Tuple(CS compoundSet, Element<S>[] elements) {
    super(compoundSet, elements);
  }

  @Override
  protected Tuple<CS, S> abstractRemoveAt(Element[] elements) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
