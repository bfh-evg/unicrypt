/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundMonoid extends Monoid, CompoundSemiGroup {

  @Override
  public Monoid getFirst();

  @Override
  public Monoid getAt(int index);

  @Override
  public Monoid getAt(int... indices);

  @Override
  public Monoid[] getAll();

  @Override
  public CompoundMonoid removeAt(final int index);

}
