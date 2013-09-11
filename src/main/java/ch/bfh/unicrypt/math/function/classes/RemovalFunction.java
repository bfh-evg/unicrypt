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
public class RemovalFunction extends AbstractFunction<ProductSet, ProductSet, Tuple> {

  private final int index;

  private RemovalFunction(final ProductSet domain, final Set coDomain, int index) {
    super(domain, coDomain);
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }

  @Override
  protected boolean standardEquals(Function function) {
    return this.getIndex() == ((RemovalFunction) function).getIndex();
  }

  @Override
  protected int standardHashCode() {
    return this.getIndex();
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Tuple abstractApply(final Element element, final Random random) {
    Tuple tuple = (Tuple) element;
    return tuple.removeAt(this.getIndex());
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
   * @param index The given sequence of indices
   * @throws IllegalArgumentException of {@code group} is null
   * @throws IllegalArgumentException if {@code indices} is null or if its
   * length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an
   * out-of-bounds index
   */
  public static RemovalFunction getInstance(final ProductSet productSet, final int index) {
    if (productSet == null) {
      throw new IllegalArgumentException();
    }
    return new RemovalFunction(productSet, productSet.removeAt(index), index);
  }

}
