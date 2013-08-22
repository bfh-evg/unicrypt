package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import java.util.Random;

/**
 * This interface represents the the concept of a function f:X->X, which computes the
 * inverse of the given input element.
 *
 * @see Group#invert(Element)
 * @see Element#invert()
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class InvertFunction extends AbstractFunction {

  private InvertFunction(final Group domain, Group coDomain) {
    super(domain, coDomain);
  }

  @Override
  public Group getDomain() {
    return (Group) this.getDomain();
  }

  @Override
  public Group getCoDomain() {
    return (Group) this.getCoDomain();
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    return element.invert();
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the standard constructor for this class. It creates an invert function for a given group.
   * @param group The given Group
   * @throws IllegalArgumentException if the group is null
   */
  public static InvertFunction getInstance(final Group group) {
    return new InvertFunction(group, group);
  }

}
