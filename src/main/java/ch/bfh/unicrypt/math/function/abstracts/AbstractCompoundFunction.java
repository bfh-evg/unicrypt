/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Compound;
import java.lang.reflect.Array;

/**
 *
 * @param <D>
 * @param <C>
 * @param <E>
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction<CF extends AbstractCompoundFunction<CF, F, D, C, E>, F extends Function, D extends Set, C extends Set, E extends Element> extends AbstractFunction<D, C, E> implements Compound<CF, F> {

  private final F[] functions;
  private final int arity;
  private final Class<F> functionClass;

  protected AbstractCompoundFunction(D domain, C coDomain, F[] functions, Class<F> functionClass) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
    this.functionClass = functionClass;
  }

  protected AbstractCompoundFunction(D domain, C coDomain, F function, int arity, Class<F> functionClass) {
    super(domain, coDomain);
    this.functions = (F[]) Array.newInstance(functionClass, 1);
    this.functions[0] = function;
    this.arity = arity;
    this.functionClass = functionClass;
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
      return (F) this.functions[0];
    }
    return (F) this.functions[index];
  }

  @Override
  public F getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    F function = (F) this;
    for (final int index : indices) {
      if (function.isCompound()) {
        function = ((Compound<CF, F>) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  @Override
  public F[] getAll() {
    int arity = this.getArity();
    F[] result = (F[]) Array.newInstance(this.functionClass, arity);
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
    final F[] remainingFunction = (F[]) Array.newInstance(this.functionClass, arity - 1);
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingFunction[i] = this.getAt(i);
      } else {
        remainingFunction[i] = this.getAt(i + 1);
      }
    }
    return abstractRemoveAt(remainingFunction);
  }

  protected abstract CF abstractRemoveAt(F function, int arity);

  protected abstract CF abstractRemoveAt(F[] functions);

  @Override
  public Iterator<F> iterator() {
    final Compound<CF, F> compoundFunction = this;
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
