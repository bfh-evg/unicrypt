package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.math.random.RandomOracle;
import java.util.Random;

/**
 * This interface represents the concept a cyclic atomic group. Every element of
 * a cyclic group can be written as a power of some particular element in
 * multiplicative notation, or as a multiple of the element in additive
 * notation. Such an element is called generator of the group. For every
 * positive integer there is exactly one cyclic group (up to isomorphism) with
 * that order, and there is exactly one infinite cyclic group. This interface
 * extends extends {@link Group} with additional methods for dealing with
 * generators. Each implementing class must provide a default generator.
 *
 * @see "Handbook of Applied Cryptography, Definition 2.167"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface CyclicGroup extends Group {

  /**
   * Returns a default generator of this group.
   *
   * @return The default generator
   */
  public Element getDefaultGenerator();

  /**
   * Returns a randomly selected generator of this group using the default
   * random generator.
   *
   * @return The randomly selected generator
   */
  public Element getRandomGenerator();

  /**
   * Returns a randomly selected generator of this group using a given random
   * generator.
   *
   * @param random The given random generator
   * @return The randomly selected generator
   */
  public Element getRandomGenerator(Random random);

  public Element getIndependentGenerator(long i);

  public Element getIndependentGenerator(long i, RandomOracle randomOracle);

  /**
   * Checks if a given element is a generator of the group.
   *
   * @param element The given element
   * @return {@code true} if the element is a generator, {@code false} otherwise
   * @throws IllegalArgumentException if {@code} is null
   */
  public boolean isGenerator(Element element);

}
