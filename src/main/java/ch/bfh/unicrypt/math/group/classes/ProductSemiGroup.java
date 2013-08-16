package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroup extends ProductSet implements SemiGroup {

@Override
  public final Set getAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Set set = this;
    for (final int index : indices) {
      set = set.getAt(index);
    }
    return set;
  }

  @Override
  public final Set removeAt(int index) {
    if (this.getArity() == 0) {
      throw new UnsupportedOperationException();
    }
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardRemoveGroupAt(index);
  }

  @Override
  public SemiGroup getAt(final int index);

  @Override
  public SemiGroup getAt(int... indices);

  @Override
  public SemiGroup getFirst();

  @Override
  public SemiGroup removeAt(final int index);

}
