/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundGroup extends Group, CompoundMonoid {

  @Override
  public Group getFirst();

  @Override
  public Group getAt(int index);

  @Override
  public Group getAt(int... indices);

  @Override
  public Group[] getAll();

  @Override
  public CompoundGroup removeAt(final int index);

}
