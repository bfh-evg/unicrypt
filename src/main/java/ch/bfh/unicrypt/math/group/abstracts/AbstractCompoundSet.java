/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.CompoundElement;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundSet extends AbstractSet<CompoundElement> implements Compound<Set> {

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

  public AbstractCompoundSet removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return ProductSet.getInstance(this.getFirst(), arity-1);
    }
    final Set[] remainingSets = new Set[arity - 1];
    for (int i=0; i < arity-1; i++) {
      if (i < index) {
        remainingSets[i] = this.getAt(i);
      } else {
        remainingSets[i] = this.getAt(i+1);
      }
    }
    return ProductSet.getInstance(remainingSets);
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

  @Override
  protected boolean standardEquals(Set set) {
    AbstractCompoundSet other = (AbstractCompoundSet) set;
    int arity = this.getArity();
    if (arity != other.getArity()) {
      return false;
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).equals(other.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected int standardHashCode() {
    final int prime = 31;
    int result = 1;
    for (Set set : this) {
      result = prime * result + set.hashCode();
    }
    result = prime * result + this.getArity();
    return result;
  }

  @Override
  protected String standardToString() {
    if (this.isNull()) {
      return "";
    }
    if (this.isUniform()) {
      return this.getFirst().toString() + "^" + this.getArity();
    }
    String result = "";
    String separator = "";
    for (Set set: this) {
      result = result + separator + set.toString();
      separator = ",";
    }
    return result;
  }

}


