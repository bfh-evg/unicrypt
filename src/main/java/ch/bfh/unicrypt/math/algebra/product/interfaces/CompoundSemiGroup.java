/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundSemiGroup extends SemiGroup, CompoundSet {

  @Override
  public SemiGroup getFirst();

  @Override
  public SemiGroup getAt(int index);

  @Override
  public SemiGroup getAt(int... indices);

  @Override
  public SemiGroup[] getAll();

  @Override
  public CompoundSemiGroup removeAt(final int index);

}
