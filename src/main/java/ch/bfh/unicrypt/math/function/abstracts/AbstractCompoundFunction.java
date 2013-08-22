/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.function.interfaces.CompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction extends AbstractFunction implements CompoundFunction {

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
  public final boolean isPower() {
    return this.functions.length <= 1;
  }

  @Override
  public Function getFirst() {
    return this.getAt(0);

  }

  @Override
  public Function getAt(int index) {
    if (index < 0 || index > this.getArity() - 1) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isPower()) {
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
      if (function instanceof CompoundFunction) {
        function = ((CompoundFunction) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  @Override
  public Function[] getAll() {
    Function[] result = new Function[this.getArity()];
    for (int index = 0; index < this.getArity(); index++) {
      result[index] = this.getAt(index);
    }
    return result;
  }

}
