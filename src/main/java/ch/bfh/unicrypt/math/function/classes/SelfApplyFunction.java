package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.CompoundElement;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.NPlus;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
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

  private SelfApplyFunction(final ProductSet domain, final SemiGroup coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    CompoundElement compoundElement = (CompoundElement) element;
    return compoundElement.getAt(0).selfApply(compoundElement.getAt(1));
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a special constructor, where the group of the second parameter is selected automatically
   * from the given group.
   *
   * @param semiGroup The underlying group
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static SelfApplyFunction getInstance(final SemiGroup semiGroup) {
    if (semiGroup == null) {
      throw new IllegalArgumentException();
    }
    if (semiGroup.getOrder() == Set.INFINITE_ORDER || semiGroup.getOrder() == Set.UNKNOWN_ORDER) {
      return SelfApplyFunction.getInstance(semiGroup, NPlus.getInstance());
    }
    return SelfApplyFunction.getInstance(semiGroup, semiGroup.getZPlusModOrder());
  }

  /**
   * This is the general constructor of this class. The first parameter is the group on which it operates,
   * and the second parameter is the atomic group, from which an element is needed to determine the number
   * of times the group operation is applied.
   *
   * @param semiGroup The underlying group
   * @param amountSet
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code amountGroup} is negative
   */
  public static SelfApplyFunction getInstance(final SemiGroup semiGroup, final Set amountSet) {
    if (semiGroup == null || amountSet == null) {
      throw new IllegalArgumentException();
    }
    return new SelfApplyFunction(ProductSet.getInstance(semiGroup, amountSet), semiGroup);
  }

}
