package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.product.classes.Tuple;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This class represents the concept of a function, which tests the given input
 * elements for equality. For this to work, its domain is a power group and its
 * co-domain the Boolean group. If all all input elements are equal, the
 * function outputs {@link BooleanGroup#TRUE}, and {@link BooleanGroup#FALSE}
 * otherwise.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class EqualityFunction extends AbstractFunction<ProductSet, BooleanSet, BooleanElement> {

  private EqualityFunction(final ProductSet domain, final BooleanSet coDomain) {
    super(domain, coDomain);
  }

  @Override
  public BooleanElement abstractApply(final Element element, final Random random) {
    Tuple tuple = (Tuple) element;
    int arity = tuple.getArity();
    if (arity > 1) {
      final Element firstElement = tuple.getFirst();
      for (Element currentElement : tuple) {
        if (!firstElement.equals(currentElement)) {
          return BooleanSet.FALSE;
        }
      }
    }
    return BooleanSet.TRUE;
  }

  /**
   * This is a special factory method for this class for the particular case of
   * two input elements.
   *
   * @param set The group on which this function operates
   * @return The resulting equality function
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static EqualityFunction getInstance(final Set set) {
    return EqualityFunction.getInstance(set, 2);
  }

  /**
   * This is the general factory method of this class. The first parameter is
   * the group on which it operates, and the second parameter is the number of
   * input elements to compare.
   *
   * @param set The group on which this function operates
   * @param arity The number of input elements to compare
   * @return The resulting equality function
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static EqualityFunction getInstance(final Set set, final int arity) {
    return new EqualityFunction(ProductSet.getInstance(set, arity), BooleanSet.getInstance());
  }

}
