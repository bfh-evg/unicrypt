package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.interfaces.Group;

/**
 * This interface represents the the concept of a function f:X->Y, which outputs the element
 * of Y that corresponds to the integer value of the input element..
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ConvertFunction extends AbstractFunction {

  private ConvertFunction(final Group domain, final Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    return this.getCoDomain().getElement(element);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the standard constructor for this class. It creates an invert function for a given group.
   * @param domain The given domain
   * @param coDomain The given co-domain
   * @throws IllegalArgumentException if the domain or coDomain is null
   */
  public static ConvertFunction getInstance(final Group domain, final Group coDomain) {
    return new ConvertFunction(domain, coDomain);
  }
  
}
