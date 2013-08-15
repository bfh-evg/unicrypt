package ch.bfh.unicrypt.math.group.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;

/**
 * This interface represents the concept a cyclic atomic group. Every element of a cyclic group can be written
 * as a power of some particular element in multiplicative notation, or as a multiple of the element in
 * additive notation. Such an element is called generator of the group. For every positive integer there is exactly
 * one cyclic group (up to isomorphism) with that order, and there is exactly one infinite cyclic group.
 * This interface extends extends {@link Group} with additional methods for dealing with generators. Each implementing
 * class must provide a default generator.
 *
 * @see "Handbook of Applied Cryptography, Definition 2.167"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface CyclicGroup extends Group {

  /**
   * Returns a default generator of this group.
   * @return The default generator
   */
  public Element getDefaultGenerator();

  /**
   * Returns a randomly selected generator of this group using the default random generator.
   * @return The randomly selected generator
   */
  public Element getRandomGenerator();

  /**
   * Returns a randomly selected generator of this group using a given random generator.
   * @param random The given random generator
   * @return The randomly selected generator
   */
  public Element getRandomGenerator(Random random);

  /**
   * Checks if a given element is a generator of the group.
   * @param element The given element
   * @return {@code true} if the element is a generator, {@code false} otherwise
   * @throws IllegalArgumentException if {@code} is null
   */
  public boolean isGenerator(Element element);

  //
  // The following inherited methods are overridden to return cyclic groups
  //

  @Override
  public CyclicGroup getAt(final int index);

  @Override
  public CyclicGroup getAt(int... indices);

  @Override
  public CyclicGroup getFirst();

  @Override
  public CyclicGroup removeAt(final int index);

}