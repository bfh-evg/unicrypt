/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.Compound;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <D>
 * @param <C>
 * @param <E>
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction<D extends Set, C extends Set, E extends Element> extends AbstractFunction<D, C, E> implements Compound<Function> {

  private final Function[] functions;
  private final int arity;

  protected AbstractCompoundFunction(Set domain, Set coDomain, Function[] functions) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
  }

  protected AbstractCompoundFunction(Set domain, Set coDomain, Function function, int arity) {
    super(domain, coDomain);
    this.functions = new Function[]{function};
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
    return this.functions.length <= 1;
  }

  @Override
  public Function getFirst() {
    return this.getAt(0);

  }

  @Override
  public Function getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.functions[0];
    }
    return this.functions[index];
  }

  @Override
  public Function getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Function function = this;
    for (final int index : indices) {
      if (function.isCompound()) {
        function = ((Compound<Function>) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  @Override
  public Function[] getAll() {
    int arity = this.getArity();
    Function[] result = new Function[arity];
    for (int i = 0; i < arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Iterator<Function> iterator() {
    final Compound<Function> compoundFunction = this;
    return new Iterator<Function>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < compoundFunction.getArity();
      }

      @Override
      public Function next() {
        if (this.hasNext()) {
          return compoundFunction.getAt(this.currentIndex++);
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
  protected boolean standardIsCompound() {
    return true;
  }

  @Override
  protected boolean standardEquals(Function function) {
    AbstractCompoundFunction<D, C, E> other = (AbstractCompoundFunction<D, C, E>) function;
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
    for (Function function : this) {
      result = prime * result + function.hashCode();
    }
    result = prime * result + this.getArity();
    return result;
  }

}
