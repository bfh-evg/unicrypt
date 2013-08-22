/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundSet extends AbstractSet implements Compound<Set> {

  private final Set[] sets;
  private final int arity;

  protected AbstractCompoundSet(Set[] sets) {
    this.sets = sets.clone();
    this.arity = sets.length;
  }

  protected AbstractCompoundSet(Set set, int arity) {
    this.sets = new Set[]{set};
    this.arity = arity;
  }

  @Override
  public int getArity() {
    return this.arity;
  }

  @Override
  public final boolean isNull() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isUniform() {
    return this.sets.length <= 1;
  }

  @Override
  public Set getFirst() {
    return this.getAt(0);

  }

  @Override
  public Set getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.sets[0];
    }
    return this.sets[index];
  }

  @Override
  public Set getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Set set = this;
    for (final int index : indices) {
      if (set instanceof Compound) {
        set = ((Compound<Set>) set).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return set;
  }

  @Override
  public Set[] getAll() {
    int arity = this.getArity();
    Set[] result = new Set[arity];
    for (int i = 0; i < this.arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Iterator<Set> iterator() {
    final AbstractCompoundSet compoundSet = this;
    return new Iterator<Set>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex >= compoundSet.getArity();
      }

      @Override
      public Set next() {
        if (this.hasNext()) {
          return compoundSet.getAt(this.currentIndex);
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

}
