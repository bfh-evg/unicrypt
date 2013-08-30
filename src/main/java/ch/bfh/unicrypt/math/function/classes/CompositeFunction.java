package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

/**
 * This class represents the concept of a composite function f:X_1->Y_n.
 * It consists of multiple internal functions f_i:X_i->Y_i, which are applied
 * sequentially to the input element in the given order. For this to work,
 * X_i=Y_{i-1} must hold for i=2,...,n, i.e., the co-domain of the
 * first function must correspond to the domain of the second function, the
 * co-domain of the second function must correspond to the domain of the third
 * function, and so on.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public final class CompositeFunction extends AbstractCompoundFunction<Set, Set, Element> {

  private CompositeFunction(final Set domain, final Set coDomain, final Function[] functions) {
    super(domain, coDomain, functions);
  }

  @Override
  protected final Element abstractApply(final Element element, final Random random) {
    Element result = element;
    for (Function function: this) {
      result = function.apply(result, random);
    }
    return result;
  }

  /**
   * This is the general factory method of this class. It takes an array of
   * functions as input and produces the corresponding composite function.
   * @param functions The given array of functions
   * @return The resulting composite function
   * @throws IllegalArgumentException if {@code functions} is null, contains null, or is empty
   * @throws IllegalArgumentException if the domain of a function is different  from the co-domain of the previous function
   */
  public static CompositeFunction getInstance(final Function... functions) {
    if (functions == null || functions.length < 1) {
      throw new IllegalArgumentException();
    }
    for (int i = 1; i < functions.length; i++) {
      if (functions[i] == null || !(functions[i-1].getCoDomain().equals(functions[i].getDomain()))) {
        throw new IllegalArgumentException();
      }
    }
    return new CompositeFunction(functions[0].getDomain(), functions[functions.length-1].getCoDomain(), functions);
  }

}