package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid extends ProductSemiGroup implements Monoid {

  @Override
  public Monoid getAt(final int index);

  @Override
  public Monoid getAt(int... indices);

  @Override
  public Monoid getFirst();

  @Override
  public Monoid removeAt(final int index);

}
