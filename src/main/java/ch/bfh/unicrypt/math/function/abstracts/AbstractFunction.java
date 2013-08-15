package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.PartiallyAppliedFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.util.Random;

/**
 * This abstract class contains standard implementations for most methods of
 * type {@link Function}. For most classes implementing {@link Function}, it is
 * sufficient to inherit from {@link AbstractFunction} and to implement the
 * single abstract method {@link abstractApply(Element element, Random random)}.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractFunction implements Function {

  private final Set domain;
  private final Set coDomain;

  protected AbstractFunction(final Set domain, final Set coDomain) {
    this.coDomain = coDomain;
    this.domain = domain;
  }

  @Override
  public final Element apply(final Element element) {
    return this.apply(element, (Random) null);
  }

  @Override
  public final Element apply(final Element element, final Random random) {
    if (!this.getDomain().contains(element)) {
      return this.apply(new Element[]{element});
      // This is for increased convenience when applying a composed function of
      // arity 1.
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
  public final Set getDomain() {
    return this.domain;
  }

  @Override
  public final Set getCoDomain() {
    return this.coDomain;
  }

  @Override
  public final Function getFunction() {
    if (this.isEmptyFunction()) {
      throw new UnsupportedOperationException();
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
  // The following protected methods are standard implementations for most atomic
  // functions of arity 1. The standard implementation may change in sub-classes
  // for non-atomic functions.
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
  /**
   * This abstract method is the main method to implement in each sub-class of
   * {@link AbstractFunction}. The validity of the two parameters has already
   * been tested.
   *
   * @see apply(Element, Random)
   * @see Group#apply(Element[])
   * @see Element#apply(Element)
   *
   * @param element The given input element
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   */
  protected abstract Element abstractApply(Element element, Random random);
}
