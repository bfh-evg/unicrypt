package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This interface represents the the concept of a function f:X->X, which
 * computes the inverse of the given input element.
 *
 * @see Group#invert(Element)
 * @see Element#invert()
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class InvertFunction extends AbstractFunction<Group, Group, Element<Group>> {

  private InvertFunction(final Group domain, Group coDomain) {
    super(domain, coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected Element<Group> abstractApply(final Element element, final Random random) {
    return element.invert();
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the standard constructor for this class. It creates an invert
   * function for a given group.
   *
   * @param group The given Group
   * @throws IllegalArgumentException if the group is null
   */
  public static InvertFunction getInstance(final Group group) {
    return new InvertFunction(group, group);
  }

}
