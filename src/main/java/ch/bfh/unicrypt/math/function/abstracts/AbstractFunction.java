package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.PartiallyAppliedFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

public abstract class AbstractFunction implements Function {

  private final Group domain;
  private final Group coDomain;

  protected AbstractFunction(final Group domain, final Group coDomain) {
    this.coDomain = coDomain;
    this.domain = domain;
  }

  @Override
  public Element apply() {
    return this.apply((Random) null);
  }

  @Override
  public Element apply(Random random) {
    return this.apply(this.getDomain().getIdentityElement(), random);
  }

  @Override
  public final Element apply(final Element element) {
    return this.apply(element, (Random) null);
  }

  @Override
  public final Element apply(final Element element, final Random random) {
    if (!this.getDomain().contains(element)) {
      return this.apply(new Element[]{element}); // this is for increased convenience
    }
    return this.abstractApply(element, random);
  }

  @Override
  public final Element apply(final Element... elements) {
    return this.apply(elements, (Random) null);
  }

  @Override
  public final Element apply(final Element[] elements, final Random random) {
    return this.apply(this.getDomain().getElement(elements), random);
  }

  @Override
  public final int getArity() {
    return this.standardGetArity();
  }

  @Override
  public final int getArityIn() {
    return this.getDomain().getArity();
  }

  @Override
  public final int getArityOut() {
    return this.getCoDomain().getArity();
  }

  @Override
  public final Group getDomain() {
    return this.domain;
  }

  @Override
  public final Group getCoDomain() {
    return this.coDomain;
  }

  @Override
  public final Function getFunction() {
    if (this.isEmptyFunction()) {
      throw new IllegalArgumentException();
    }
    return this.getFunctionAt(0);
  }

  @Override
  public final Function getFunctionAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return this.standardGetFunctionAt(index);
  }

  /**
   * Select and returns in a hierarchy of compound function the function that
   * corresponds to a given sequence of indices. (e.g., 0,3,2 for the third
   * function in the fourth compound function of the first compound function).
   * Returns {@code this} function if {@code indices} is empty.
   *
   * @param indices The given sequence of indices
   * @return The corresponding function
   * @throws IllegalArgumentException if {@ode indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@ode indices} contains an out-of-bounds index
   */
  @Override
  public final Function getFunctionAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Function function = this;
    for (final int index : indices) {
      function = function.getFunctionAt(index);
    }
    return function;
  }

  @Override
  public final boolean isAtomicFunction() {
    return this.standardIsAtomicFunction();
  }

  @Override
  public final boolean isEmptyFunction() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isPowerFunction() {
    if (this.getArity() <= 1) {
      return true;
    }
    return this.standardIsPowerFunction();
  }

  @Override
  public final Function partiallyApply(final Element element, final int index) {
    return PartiallyAppliedFunction.getInstance(this, element, index);
  }

  //
  // The following protected methods are standard implementations for most functions of
  // arity 1. The standard implementation may change in sub-classes for functions of higher arity.
  //

  @SuppressWarnings("static-method")
  protected int standardGetArity() {
    return 1;
  }

  @SuppressWarnings("unused")
  protected Function standardGetFunctionAt(final int index) {
    return this;
  }

  @SuppressWarnings("static-method")
  protected boolean standardIsPowerFunction() {
    return true;
  }

  @SuppressWarnings("static-method")
  protected boolean standardIsAtomicFunction() {
    return true;
  }

  //
  // The following protected abstract method must be implemented in every direct
  // sub-class
  //

  protected abstract Element abstractApply(Element element, Random random);

}
