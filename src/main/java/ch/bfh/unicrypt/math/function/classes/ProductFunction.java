package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

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
public final class ProductFunction extends AbstractCompoundFunction<ProductFunction, Function, ProductSet, ProductSet, Tuple> {

  /**
   * This is the general constructor of this class. It takes a list of functions
   * as input and produces the corresponding product function.
   *
   * @param functions
   * @throws IllegalArgumentException if {@code functions} is null or contains
   * null
   */
  protected ProductFunction(final ProductSet domain, ProductSet coDomain, Function... functions) {
    super(domain, coDomain, functions);
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
  protected ProductFunction(ProductSet domain, ProductSet coDomain, Function function, int arity) {
    super(domain, coDomain, function, arity);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Tuple abstractApply(final Element element, final Random random) {
    int arity = this.getArity();
    Tuple tuple = (Tuple) element;
    final Element[] elements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      elements[i] = this.getAt(i).apply(tuple.getAt(i), random);
    }
    return this.getCoDomain().getElement(elements);
  }

  @Override
  protected ProductFunction abstractRemoveAt(Function function, int arity) {
    return ProductFunction.getInstance(function, arity);
  }

  @Override
  protected ProductFunction abstractRemoveAt(Function[] functions) {
    return ProductFunction.getInstance(functions);
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
    if (functions.length > 0) {
      boolean uniform = true;
      Function first = functions[0];
      for (int i = 0; i < arity; i++) {
        if (functions[i] == null) {
          throw new IllegalArgumentException();
        }
        domains[i] = functions[i].getDomain();
        coDomains[i] = functions[i].getCoDomain();
        if (!functions[i].equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return ProductFunction.getInstance(first, arity);
      }
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
    if (arity == 0) {
      return new ProductFunction(ProductSet.getInstance(), ProductSet.getInstance());
    }
    return new ProductFunction(ProductSet.getInstance(function.getDomain(), arity), ProductSet.getInstance(function.getCoDomain(), arity), function, arity);
  }

}
