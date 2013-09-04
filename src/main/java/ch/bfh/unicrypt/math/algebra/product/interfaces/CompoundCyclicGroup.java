/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundCyclicGroup extends CyclicGroup, CompoundGroup {

  @Override
  public CyclicGroup getFirst();

  @Override
  public CyclicGroup getAt(int index);

  @Override
  public CyclicGroup getAt(int... indices);

  @Override
  public CyclicGroup[] getAll();

  @Override
  public CompoundCyclicGroup removeAt(final int index);

}
