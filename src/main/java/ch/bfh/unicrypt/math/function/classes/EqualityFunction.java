package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.BooleanGroup;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This interface represents the concept of a function, which tests the given input elements
 * for equality. For this to work, its domain is a power group and its co-domain the Boolean
 * group. If all all input elements are equal, the function outputs 1, and 0 otherwise.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class EqualityFunction extends AbstractFunction {

  private EqualityFunction(final Group domain, final Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  public Element abstractApply(final Element element, final Random random) {
    if (element.getArity() > 1) {
      final Element firstElement = element.getElementAt(0);
      for (int i = 1; i < element.getArity(); i++) {
        if (!firstElement.equals(element.getElementAt(i))) {
          return BooleanGroup.FALSE;
        }
      }
    }
    return BooleanGroup.TRUE;
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a special constructor for this class for the particular case of two input elements.
   * @param group The group on which this function operates
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static EqualityFunction getInstance(final Group group) {
    return EqualityFunction.getInstance(group, 2);
  }

  /**
   * This is the general constructor of this class. The first parameter is the group on which it operates,
   * and the second parameter is the number of input elements to compare.
   * @param group The group on which this function operates
   * @param arity The number of input elements to compare
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static EqualityFunction getInstance(final Group group, final int arity) {
    return new EqualityFunction(ProductGroup.getInstance(group, arity), BooleanGroup.getInstance());
  }

}
