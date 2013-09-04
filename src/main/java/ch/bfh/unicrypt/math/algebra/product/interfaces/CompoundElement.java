/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.Compound;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundElement extends Element, Compound {

  @Override
  public Element getFirst();

  @Override
  public Element getAt(int index);

  @Override
  public Element getAt(int... indices);

  @Override
  public Element[] getAll();

  @Override
  public CompoundElement removeAt(final int index);

}
