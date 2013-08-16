package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends ProductGroup implements CyclicGroup {

  @Override
  public CyclicGroup getAt(final int index);

  @Override
  public CyclicGroup getAt(int... indices);

  @Override
  public CyclicGroup getFirst();

  @Override
  public CyclicGroup removeAt(final int index);

}
