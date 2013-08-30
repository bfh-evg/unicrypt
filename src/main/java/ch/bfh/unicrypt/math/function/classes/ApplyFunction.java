package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.product.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.product.interfaces.Tuple;

/**
 * This interface represents the the concept of a function f:X^n->X, which
 * applies the group operation sequentially to several input elements. For this
 * to work, the input elements is given as a tuple element of a corresponding
 * power group of arity n. For n=0, the function returns the group's identity
 * element. For n=1, the function returns the single element included in the
 * tuple element.
 *
 * @see Group#apply(Element[])
 * @see Element#apply(Element)
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ApplyFunction extends AbstractFunction<ProductSemiGroup, SemiGroup, Element> {

  private ApplyFunction(final ProductSemiGroup domain, final SemiGroup coDomain) {
    super(domain, coDomain);
  }

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    Tuple compoundElement = (Tuple) element;
    return this.getCoDomain().apply(compoundElement.getAll());
  }

  /**
   * This is a special factory method for this class for the particular case of
   * two input elements of a given group.
   *
   * @param semiGroup
   * @param group The group on which this function operates
   * @return The resulting function
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static ApplyFunction getInstance(final SemiGroup semiGroup) {
    return ApplyFunction.getInstance(semiGroup, 2);
  }

  /**
   * This is the general factory method of this class. The first parameter is
   * the group on which it operates, and the second parameter is the number of
   * input elements.
   *
   * @param semiGroup The group on which this function operates
   * @param arity The number of input elements
   * @return The resulting function
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static ApplyFunction getInstance(final SemiGroup semiGroup, final int arity) {
    if (semiGroup == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return new ApplyFunction(ProductSemiGroup.getInstance(semiGroup, arity), semiGroup);
  }

}
