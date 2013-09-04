package ch.bfh.unicrypt.math.function.classes;

import java.util.Arrays;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class represents the concept of a restricted identity function, which
 * selects a particular element from an arbitrarily complex input tuple element.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class SelectionFunction extends AbstractFunction<ProductSet, Set, Element> {

  private final int[] indices;

  private SelectionFunction(final ProductSet domain, final Set coDomain, int[] indices) {
    super(domain, coDomain);
    this.indices = indices;
  }

  public int[] getIndices() {
    return this.indices;
  }

  @Override
  protected boolean standardEquals(Function function) {
    return Arrays.equals(this.getIndices(), ((SelectionFunction) function).getIndices());
  }

  @Override
  protected int standardHashCode() {
    return this.getIndices().hashCode();
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Element abstractApply(final Element element, final Random random) {
    Tuple tuple = (Tuple) element;
    return tuple.getAt(this.indices);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the general constructor of this class. The resulting function
   * selects and returns in a hierarchy of tuple elements the element that
   * corresponds to a given sequence of indices (e.g., 0,3,2 for the third
   * element in the fourth tuple element of the first tuple element).
   *
   * @param productSet The product group that defines the domain of the function
   * @param indices The given sequence of indices
   * @throws IllegalArgumentException of {@code group} is null
   * @throws IllegalArgumentException if {@code indices} is null or if its
   * length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an
   * out-of-bounds index
   */
  public static SelectionFunction getInstance(final ProductSet productSet, final int... indices) {
    if (productSet == null) {
      throw new IllegalArgumentException();
    }
    return new SelectionFunction(productSet, productSet.getAt(indices), indices);
  }

}
