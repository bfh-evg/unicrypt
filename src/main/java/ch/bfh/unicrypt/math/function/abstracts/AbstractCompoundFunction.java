/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.CompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @param <D>
 * @param <C>
 * @param <E>
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction<CF extends CompoundFunction<CF, F>, F extends Function, D extends Set, C extends Set, E extends Element> extends AbstractFunction<D, C, E> implements CompoundFunction<CF, F> {

  private final F[] functions;
  private final int arity;

  protected AbstractCompoundFunction(D domain, C coDomain, F[] functions) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
  }

  protected AbstractCompoundFunction(D domain, C coDomain, F function, int arity) {
    super(domain, coDomain);
    this.functions = (F[]) new Function[]{function};
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
  public F getFirst() {
    return this.getAt(0);

  }

  @Override
  public F getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.functions[0];
    }
    return this.functions[index];
  }

  @Override
  public F getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    F function = (F) this;
    for (final int index : indices) {
      if (function.isCompound()) {
        function = ((CompoundFunction<CF, F>) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  @Override
  public F[] getAll() {
    int arity = this.getArity();
    F[] result = (F[]) new Function[arity];
    for (int i = 0; i < arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public CF removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.abstractRemoveAt(this.getFirst(), arity - 1);
    }
    final F[] remainingFunction = (F[]) new Function[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingFunction[i] = this.getAt(i);
      } else {
        remainingFunction[i] = this.getAt(i + 1);
      }
    }
    return abstractRemoveAt(remainingFunction);
  }

  protected abstract CF abstractRemoveAt(Function function, int arity);

  protected abstract CF abstractRemoveAt(Function[] functions);

  @Override
  public Iterator<F> iterator() {
    final CompoundFunction<CF, F> compoundFunction = this;
    return new Iterator<F>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < compoundFunction.getArity();
      }

      @Override
      public F next() {
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
    CF other = (CF) function;
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
    for (F function : this) {
      result = prime * result + function.hashCode();
    }
    result = prime * result + this.getArity();
    return result;
  }

}
