package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.helper.Permutation;
import java.util.Random;

/**
 * This interface represents the concept of a function f:X^n x Z->X^n, where Z
 * is a permutation group of size n. Calling the function permutes the given
 * input tuple element of X^n according to the permutation element given as a
 * second argument. The output of the function is the permuted tuple element.
 * <p>
 * @see PermutationGroup
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PermutationFunction
       extends AbstractFunction<ProductSet, ProductSet, Tuple> {

  private PermutationFunction(final ProductSet domain, final ProductSet coDomain) {
    super(domain, coDomain);
  }

  /**
   * Returns the permutation group of size n, which is need to conduct the
   * actual permutation.
   * <p>
   * @return The permutation group
   */
  public PermutationGroup getPermutationGroup() {
    return (PermutationGroup) this.getDomain().getAt(1);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Tuple abstractApply(final Element element, final Random random) {
    final Tuple tuple = (Tuple) element;
    final Tuple elements = (Tuple) tuple.getAt(0);
    final Permutation permutation = ((PermutationElement) tuple.getAt(1)).getPermutation();
    final Element[] result = new Element[elements.getArity()];
    for (int i = 0; i < elements.getArity(); i++) {
      result[i] = elements.getAt(permutation.permute(i));
    }
    return this.getCoDomain().getElement(result);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the general constructor of this class, which construct a
   * permutation function from a given group and for the specified arity.
   * <p>
   * @param set   The given group
   * @param arity The arity of the tuple elements to permute
   * @throws IllegalArgumentException if {@literal group} is null
   * @throws IllegalArgumentException if {@literal arity} is negative
   */
  public static PermutationFunction getInstance(final Set set, final int arity) {
    if (set == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return PermutationFunction.getInstance(ProductSet.getInstance(set, arity));
  }

  /**
   * This is a special constructor of this class, which deals with the
   * particular case, where a product group is given from the beginning.
   * <p>
   * @param productSet The given power group
   * @throws IllegalArgumentException if {@literal group} is null
   */
  public static PermutationFunction getInstance(final ProductSet productSet) {
    if (productSet == null || !productSet.isUniform()) {
      throw new IllegalArgumentException();
    }
    return new PermutationFunction(ProductSet.getInstance(productSet, PermutationGroup.getInstance(productSet.getArity())), productSet);
  }

}
