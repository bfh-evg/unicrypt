package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This class represents the concept of a function, which is derived from another function with
 * a product (or power) group domain by applying a single input element and thus by fixing the
 * corresponding parameter to a constant value. Therefore, the input arity of such a function of is
 * the input arity of the parent function minus 1. Functions of that type are usually constructed by
 * calling the method {@link Function#partiallyApply(Element, int)} for a given function with a product
 * (or power) group domain.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PartiallyAppliedFunction extends AbstractFunction {

  private final Function parentFunction;
  private final Element parameter;
  private final int index;

  /**
   * This is the standard constructor of this class. It derives from a given function a new function, in which one input
   * element is fixed to a given element and thus expects one input element less.
   * @param parentFunction The given function
   * @param parameter The given parameter to fix
   * @param index The index of the parameter to fix
   * @throws IllegalArgumentException if the {@code function} is null or not a ProductGroup
   * @throws IndexOutOfBoundsException if the {@code index} is negative or > the arity of the function's domain
   * @throws IllegalArgumentException if the {@code element} is not an element of the corresponding group
   */
  private PartiallyAppliedFunction(final Group domain, final Group coDomain, final Function parentFunction, final Element parameter, final int index) {
    super(domain, coDomain);
    this.parentFunction = parentFunction;
    this.parameter = parameter;
    this.index = index;
  }

  /**
   * Returns the parent function from which {@code this} function has been derived.
   * @return The parent function
   */
  public Function getParentFunction() {
    return this.parentFunction;
  }

  /**
   * Returns the input element that has been used to derive {@code this} function from the parent function.
   * @return The input element
   */
  public Element getParameter() {
    return this.parameter;
  }

  /**
   * Returns the index of the parameter that has been fixed to derive {@code this} function from the parent function.
   * @return The index of the input element
   */
  public int getIndex() {
    return this.index;
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    final Element[] allElements = new Element[element.getArity() + 1];
    for (int i = 0; i < element.getArity(); i++) {
      if (i < this.getIndex()) {
        allElements[i] = element.getElementAt(i);
      } else {
        allElements[i + 1] = element.getElementAt(i);
      }
      allElements[this.getIndex()] = this.getParameter();
    }
    return this.getFunction().apply(allElements, random);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the standard constructor of this class. It derives from a given function a new function, in which one input
   * element is fixed to a given element and thus expects one input element less.
   * @param parentfunction The given function
   * @param element The given parameter to fix
   * @param index The index of the parameter to fix
   * @throws IllegalArgumentException if the {@code function} is null or not a ProductGroup
   * @throws IndexOutOfBoundsException if the {@code index} is negative or > the arity of the function's domain
   * @throws IllegalArgumentException if the {@code element} is not an element of the corresponding group
   */
  public static PartiallyAppliedFunction getInstance(final Function parentfunction, final Element element, final int index) {
    if (parentfunction == null) {
      throw new IllegalArgumentException();
    }
    if (!parentfunction.getDomain().getGroupAt(index).contains(element)) {
      throw new IllegalArgumentException();
    }
    return new PartiallyAppliedFunction(parentfunction.getDomain().removeGroupAt(index), parentfunction.getCoDomain(), parentfunction, element, index);
  }

}
