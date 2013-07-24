package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This interface represents the the concept of a function f:X^n->X, which applies the
 * group operation sequentially to several input elements. For this to work, the input
 * elements is given as a tuple element of a corresponding power group of arity n. If
 * n=0, the function returns the group's identity element. If n=1, the function returns
 * the single element included in the tuple element.
 *
 * @see Group#apply(Element[])
 * @see Element#apply(Element)
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ApplyFunction extends AbstractFunction {

  private ApplyFunction(final Group domain, final Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    return this.getCoDomain().apply(element.getElements());
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a special constructor for this class for the particular case of two input elements.
   * @param group The group on which this function operates
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static ApplyFunction getInstance(final Group group) {
    return ApplyFunction.getInstance(group, 2);
  }

  /**
   * This is the general constructor of this class. The first parameter is the group on which it operates,
   * and the second parameter is the number of input elements.
   * @param group The group on which this function operates
   * @param arity The number of input elements
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static ApplyFunction getInstance(final Group group, final int arity) {
    if (group == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return new ApplyFunction(ProductGroup.getInstance(group, arity), group);
  }

}
