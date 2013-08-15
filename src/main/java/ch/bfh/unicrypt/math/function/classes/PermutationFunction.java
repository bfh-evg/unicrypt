package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.PermutationGroup;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.helper.Permutation;
import java.util.Random;

/**
 * This interface represents the concept of a function f:X^n x Z->X^n, where Z is a permutation group of
 * size n. Calling the function permutes the given input tuple element of X^n according to the permutation
 * element given as a second argument. The output of the function is the permuted tuple element.
 *
 * @see PermutationGroup
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PermutationFunction extends AbstractFunction {

  private PermutationFunction(final Group domain, final Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    if (!this.getDomain().contains(element)) {
      throw new IllegalArgumentException();
    }
    final Element tuple = element.getAt(0);
    final Permutation permutation = ((PermutationGroup) this.getDomain().getAt(1)).getPermutation(element.getAt(1));
    final Element[] result = new Element[tuple.getArity()];
    for (int i = 0; i < tuple.getArity(); i++) {
      result[i] = tuple.getAt(permutation.permute(i));
    }
    return this.getCoDomain().getElement(result);
  }

  /**
   * Returns the permutation group of size n, which is need to conduct the actual permutation.
   * @return The permutation group
   */
  public PermutationGroup getPermutationGroup() {
    return (PermutationGroup) this.getDomain().getAt(1);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the general constructor of this class, which construct a permutation function from a
   * given group and for the specified arity.
   * @param group The given group
   * @param arity The arity of the tuple elements to permute
   * @throws IllegalArgumentException if {@code group} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static PermutationFunction getInstance(final Group group, final int arity) {
    if (group == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return PermutationFunction.getInstance(ProductGroup.getInstance(group, arity));
  }

  /**
   * This is a special constructor of this class, which deals with the particular case, where a product
   * group is given from the beginning.
   * @param group The given power group
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static PermutationFunction getInstance(final Group group) {
    if (group == null || !group.isPower()) {
      throw new IllegalArgumentException();
    }
    return new PermutationFunction(ProductGroup.getInstance(group, PermutationGroup.getInstance(group.getArity())), group);
  }

}
