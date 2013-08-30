package ch.bfh.unicrypt.math.function.classes;

import java.util.Random;

import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 * This classrepresents the concept of a randomized function with no input. When the
 * function is called, it selects an element from the co-domain at random and returns it as
 * output value.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class RandomFunction extends AbstractFunction<SingletonGroup, Set, Element> {

  private RandomFunction(final Set coDomain) {
    super(SingletonGroup.getInstance(), coDomain);
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    return this.getCoDomain().getRandomElement(random);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the general constructor of this class. It creates a function that generates random elements
   * from a given group.
   * @param set The given group
   * @throws IllegalArgumentException if {@code group} is null
   */
  public static RandomFunction getInstance(final Set set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    return new RandomFunction(set);
  }

}
