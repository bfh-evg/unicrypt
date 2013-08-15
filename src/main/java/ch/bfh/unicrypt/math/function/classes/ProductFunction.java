package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This class represents the concept of a product function f:(X_1x...xX_n)->(Y_1x...xY_n). It consists
 * of multiple individual functions f_i:X_i->Y_i, which are applied in parallel to respective input
 * elements. To be compatible with {@link Function}, these input elements must be given as a tuple element
 * of the product domain X_1x...xX_n. In the same way, the output elements are returned as a tuple element
 * of the product domain Y_1x...xY_n.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public final class ProductFunction extends AbstractFunction {

  private final Function[] functions;
  private final int arity;

  /**
   * This is the general constructor of this class. It takes a list of functions as input and produces
   * the corresponding product function.
   * @param functions
   * @throws IllegalArgumentException if {@code functions} is null or contains null
   */
  private ProductFunction(final Group domain, Group coDomain, Function[] functions) {
    super(domain, coDomain);
    this.functions = functions.clone();
    this.arity = functions.length;
  }

  /**
   * This is the general constructor of this class. The first parameter specifies the function to be
   * applied multiple times in parallel, and the second parameter defines the number of times it
   * is applied in parallel.
   * @param function The given function
   * @param arity The number of times the function is applied in parallel
   * @throws IllegalArgumentException if {@code function} is null
   * @throws IllegalArgumentException if {@code arity} is negative
   */
  private ProductFunction(Group domain, Group coDomain, Function function, int arity) {
    super(domain, coDomain);
    this.functions = new Function[]{function};
    this.arity = arity;
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractFunction}
  //

  @Override
  protected final boolean standardIsAtomicFunction() {
    return false;
  }

  @Override
  protected boolean standardIsPowerFunction() {
    return (this.functions.length == 1) ;
  }

  @Override
  protected int standardGetArity() {
    return this.arity;
  }

  @Override
  protected Function standardGetFunctionAt(final int index) {
    if (this.isPowerFunction()) {
      return this.functions[0];
    }
    return this.functions[index];
  }


  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    final Element[] elements = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      elements[i] = this.getFunctionAt(i).apply(element.getAt(i), random);
    }
    return this.getCoDomain().getElement(elements);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the general constructor of this class. It takes a list of functions as input and produces
   * the corresponding product function.
   * @param functions
   * @throws IllegalArgumentException if {@code functions} is null or contains null
   */
  public static ProductFunction getInstance(final Function... functions) {
    if (functions == null) {
      throw new IllegalArgumentException();
    }
    final Group[] domainGroups = new Group[functions.length];
    final Group[] coDomainGroups = new Group[functions.length];
    for (int i = 0; i < functions.length; i++) {
      if (functions[i] == null) {
        throw new IllegalArgumentException();
      }
      domainGroups[i] = functions[i].getDomain();
      coDomainGroups[i] = functions[i].getCoDomain();
    }
    return new ProductFunction(ProductGroup.getInstance(domainGroups), ProductGroup.getInstance(coDomainGroups), functions);
  }

  /**
   * This is the general constructor of this class. The first parameter specifies the function to be
   * applied multiple times in parallel, and the second parameter defines the number of times it
   * is applied in parallel.
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
