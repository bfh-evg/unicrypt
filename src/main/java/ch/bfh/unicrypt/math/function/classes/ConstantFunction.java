package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.util.Random;

/**
 * This class represents the concept of a constant function with no input. When the
 * function is called, it returns always the same element as output value.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ConstantFunction extends AbstractFunction {

  private Element element;

  /**
   * This is the general constructor of this class. It creates a function that
   * returns always the same element when called.
   * @param element The constant output value of the function
   * @throws IllegalArgumentException if {@code element} is null
   */
  private ConstantFunction(Group coDomain, Element element) {
    super(ProductGroup.getInstance(), coDomain);
    this.element = element;
  }

  public Element getElement() {
    return this.element;
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(Element element, Random random) {
    return this.element;
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the general constructor of this class. It creates a function that generates random elements
   * from a given group.
   * @param element The given element
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static ConstantFunction getInstance(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return new ConstantFunction(element.getGroup(), element);
  }

  public static ConstantFunction getInstance(final Group group) {
    if (group == null) {
      throw new IllegalArgumentException();
    }
    return new ConstantFunction(group, group.getIdentityElement());
  }

}
