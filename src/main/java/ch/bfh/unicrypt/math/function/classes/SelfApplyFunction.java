package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This class represents the the concept of a function f:XxZ->Y, where Z is
 * an atomic group. The second input element can thus be transformed into an
 * integer value z, which determines the number of times the group operation is
 * applied to the first input element.
 *
 * @see Group#selfApply(Element, Element)
 * @see Element#selfApply(Element)
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class SelfApplyFunction extends AbstractFunction {

  private SelfApplyFunction(final Group domain, final Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    return element.getAt(0).selfApply(element.getAt(1));
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a special constructor, where the group of the second parameter is selected automatically
   * from the given group.
   *
   * @param group The underlying group
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static SelfApplyFunction getInstance(final Group group) {
    if (group == null) {
      throw new IllegalArgumentException();
    }
    if (group.getOrder() == Group.INFINITE_ORDER || group.getOrder() == Group.UNKNOWN_ORDER) {
      return SelfApplyFunction.getInstance(group, ZPlus.getInstance());
    }
    return SelfApplyFunction.getInstance(group, group.getOrderGroup());
  }

  /**
   * This is the general constructor of this class. The first parameter is the group on which it operates,
   * and the second parameter is the atomic group, from which an element is needed to determine the number
   * of times the group operation is applied.
   *
   * @param group The underlying group
   * @param amountGroup
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code amountGroup} is negative
   */
  public static SelfApplyFunction getInstance(final Group group, final Group amountGroup) {
    if (group == null || amountGroup == null) {
      throw new IllegalArgumentException();
    }
    return new SelfApplyFunction(ProductGroup.getInstance(group, amountGroup), group);
  }

}
