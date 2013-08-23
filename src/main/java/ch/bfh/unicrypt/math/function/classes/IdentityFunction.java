package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.util.Arrays;
import java.util.Random;

/**
 * This class represents the concept of a generalized identity function f:X->X^n
 * with f(x)=(x,...,x) for all elements x in X.
 * This class represents the concept of an identity function f:X->X with f(x)=x for
 * all elements x in X.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class IdentityFunction extends AbstractFunction {

  private IdentityFunction(final Set domain, final ProductSet coDomain) {
    super(domain, coDomain);
  }

  @Override
  public ProductSet getCoDomain() {
    return (ProductSet) this.getCoDomain();
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    final Element[] elements = new Element[this.getCoDomain().getArity()];
    Arrays.fill(elements, element);
    return this.getCoDomain().getElement(elements);
  }

  //
  // STATIC FACTORY METHODS
  //

  public static IdentityFunction getInstance(final Group group) {
    return IdentityFunction.getInstance(group, 1);
  }

  /**
   * This is the standard constructor for this class. It creates a generalized identity function for a given group,
   * which reproduces the input value multiple time.
   * @param set The given set
   * @param arity The arity of the output element
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static IdentityFunction getInstance(final Set set, final int arity) {
    if (set == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return new IdentityFunction(set, ProductSet.getInstance(set, arity));
  }

}
