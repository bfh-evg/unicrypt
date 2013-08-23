package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductCyclicGroup;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.util.Random;

/**
 * This class represents the concept of a constant function with no input. When
 * the function is called, it returns always the same element as output value.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ConstantFunction extends AbstractFunction {

  private Element element;

  /**
   * This is the general constructor of this class. It creates a function that
   * returns always the same element when called.
   *
   * @param element The constant output value of the function
   * @throws IllegalArgumentException if {@code element} is null
   */
  private ConstantFunction(Set coDomain, Element element) {
    super(ProductCyclicGroup.getInstance(), coDomain);
    this.element = element;
  }

  /**
   * Returns the constant element returned by this function.
   * @return The constant element
   */
  public Element getElement() {
    return this.element;
  }

  @Override
  public ProductCyclicGroup getDomain() {
    return (ProductCyclicGroup) this.getDomain();
  }

  @Override
  protected boolean standardEquals(Function function) {
    return this.getElement().equals(((ConstantFunction) function).getElement());
  }

 @Override
  protected int standardHashCode() {
    return this.getElement().hashCode();
  }

  @Override
  protected Element abstractApply(Element element, Random random) {
    return this.element;
  }

  /**
   * This is the general factory method of this class. It creates a function that
   * always returns the same element when called.
   *
   * @param element The given element
   * @return The constant function
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static ConstantFunction getInstance(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return new ConstantFunction(element.getSet(), element);
  }

  /**
   * This is a special factory method of this class, which creates a function
   * that always returns the identity element of the given group.
   * @param monoid The given group
   * @return The resulting function
   */
  public static ConstantFunction getInstance(final Monoid monoid) {
    if (monoid == null) {
      throw new IllegalArgumentException();
    }
    return new ConstantFunction(monoid, monoid.getIdentityElement());
  }

}
