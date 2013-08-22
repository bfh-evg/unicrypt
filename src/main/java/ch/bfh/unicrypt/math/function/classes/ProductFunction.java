package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.util.Random;

/**
 * This class represents the concept of a product function
 * f:(X_1x...xX_n)->(Y_1x...xY_n). It consists of multiple individual functions
 * f_i:X_i->Y_i, which are applied in parallel to respective input elements. To
 * be compatible with {@link Function}, these input elements must be given as a
 * tuple element of the product domain X_1x...xX_n. In the same way, the output
 * elements are returned as a tuple element of the product domain Y_1x...xY_n.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public final class ProductFunction extends AbstractFunction {

  private final Function[] functions;
  private final int arity;

  /**
   * This is the general constructor of this class. It takes a list of functions
   * as input and produces the corresponding product function.
   *
   * @param functions
   * @throws IllegalArgumentException if {@code functions} is null or contains
   * null
   */
  private ProductFunction(final ProductSet domain, ProductSet coDomain, Function[] functions) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
  }

  /**
   * This is the general constructor of this class. The first parameter
   * specifies the function to be applied multiple times in parallel, and the
   * second parameter defines the number of times it is applied in parallel.
   *
   * @param function The given function
   * @param arity The number of times the function is applied in parallel
   * @throws IllegalArgumentException if {@code function} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  private ProductFunction(ProductSet domain, ProductSet coDomain, Function function, int arity) {
    super(domain, coDomain);
    this.functions = new Function[]{function};
    this.arity = arity;
  }

  /**
   * The arity of a function is the number of functions applied in parallel when
   * calling the function. For arity<>1, the arity corresponds to the arity of
   * both the domain and the co-domain of the function.
   *
   * @return The function's arity
   */
  protected int getArity() {
    return this.arity;
  }

  public final boolean isNull() {
    return this.getArity() == 0;
  }

  /**
   * Checks if the set consists of multiple copies of the same function. Such a
   * product set is called 'power function'.
   *
   * @return {@code true}, if the product function is a power function,
   * {@code false} otherwise
   */
  public final boolean isPower() {
    return this.functions.length <= 1;
  }

  /**
   * Returns the function at index 0.
   *
   * @return The function at index 0
   * @throws UnsupportedOperationException for functions of arity 0
   */
  public Function getFirst() {
    return this.getAt(0);

  }

  /**
   * Returns the function at the given index.
   *
   * @param index The given index
   * @return The corresponding function
   * @throws IndexOutOfBoundsException if {
   * @ode index} is an invalid index
   */
  public Function getAt(int index) {
    if (index < 0 || index > this.getArity() - 1) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isPower()) {
      return this.functions[0];
    }
    return this.functions[index];
  }

  /**
   * Select and returns in a hierarchy of composed function the function that
   * corresponds to a given sequence of indices. (e.g., 0,3,2 for the third
   * function in the fourth composed function of the first composed function).
   * Returns {@code this} function if {@code indices} is empty.
   *
   * @param indices The given sequence of indices
   * @return The corresponding function
   * @throws IllegalArgumentException if {
   * @ode indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {
   * @ode indices} contains an out-of-bounds index
   */
  public Function getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Function function = this;
    for (final int index : indices) {
      if (function instanceof ProductSet) {
        function = ((ProductFunction) function).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return function;
  }

  /**
   * Returns an array of length {@code this.getArity()} containing all the
   * functions of which {@code this} function is composed of.
   */
  public Function[] getAll() {
    Function[] result = new Function[this.getArity()];
    for (int index = 0; index < this.getArity(); index++) {
      result[index] = this.getAt(index);
    }
    return result;
  }

  @Override
  public ProductSet getDomain() {
    return (ProductSet) this.getDomain();
  }

  @Override
  public ProductSet getCoDomain() {
    return (ProductSet) this.getCoDomain();
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractFunction}
  //
  @Override
  protected final boolean standardIsAtomic() {
    return false;
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Element abstractApply(final Element element, final Random random) {
    final Element[] elements = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      elements[i] = this.getAt(i).apply(element.getAt(i), random);
    }
    return this.getCoDomain().getElement(elements);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the general constructor of this class. It takes a list of functions
   * as input and produces the corresponding product function.
   *
   * @param functions
   * @throws IllegalArgumentException if {@code functions} is null or contains
   * null
   */
  public static ProductFunction getInstance(final Function... functions) {
    if (functions == null) {
      throw new IllegalArgumentException();
    }
    int arity = functions.length;
    final Set[] domains = new Set[arity];
    final Set[] coDomains = new Set[arity];
    for (int i = 0; i < arity; i++) {
      if (functions[i] == null) {
        throw new IllegalArgumentException();
      }
      domains[i] = functions[i].getDomain();
      coDomains[i] = functions[i].getCoDomain();
    }
    return new ProductFunction(ProductSet.getInstance(domains), ProductSet.getInstance(coDomains), functions);
  }

  /**
   * This is the general constructor of this class. The first parameter
   * specifies the function to be applied multiple times in parallel, and the
   * second parameter defines the number of times it is applied in parallel.
   *
   * @param function The given function
   * @param arity The number of times the function is applied in parallel
   * @throws IllegalArgumentException if {@code function} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  public static ProductFunction getInstance(final Function function, final int arity) {
    if (function == null || arity < 0) {
      throw new IllegalArgumentException();
    }
    return new ProductFunction(ProductGroup.getInstance(function.getDomain(), arity), ProductGroup.getInstance(function.getCoDomain(), arity), function, arity);
  }

}
