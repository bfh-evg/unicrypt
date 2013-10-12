package ch.bfh.unicrypt.math.algebra.general.interfaces;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;

/**
 * This interface represents the concept a mathematical set of elements. The
 * number of elements in the set is called order. The order may be infinite or
 * unknown. A set is either atomic or a product of multiple other (possibly
 * non-atomic) sets. The arity defines the number of sets such a product is
 * composed of. The arity of an atomic set is 1. It is assumed that each element
 * of a set corresponds to a unique BigInteger value. Therefore, the interface
 * provides methods for converting elements into corresponding BigInteger values
 * and back.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Set {

  /**
   * A constant value that represents an infinite order.
   */
  public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);
  /**
   * A constant value that represents an unknown order.
   */
  public static final BigInteger UNKNOWN_ORDER = BigInteger.ZERO;

  public boolean isSemiGroup();

  public boolean isMonoid();

  public boolean isGroup();

  public boolean isSemiRing();

  public boolean isRing();

  public boolean isField();

  public boolean isCyclic();

  public boolean isAdditive();

  public boolean isMultiplicative();

  public boolean isConcatenative();

  public boolean isProduct();

  public boolean isFinite();

  public boolean hasKnownOrder();

  /**
   * Returns the group order. If the group order is unknown,
   * {@link #UNKNOWN_ORDER} is returned. If the group order is infinite,
   * {@link #INFINITE_ORDER} is returned.
   *
   * @see "Handbook of Applied Cryptography, Definition 2.163"
   * @return The group order.
   */
  public BigInteger getOrder();

  /**
   * Returns a lower bound for the group order in case the exact group order is
   * unknown. The least return value is 1. Otherwise, if the exact group order
   * is known (or infinite), the exact group order is returned.
   *
   * @return A lower bound for the group order.
   */
  public BigInteger getMinOrder();

  public BigInteger getMaxOrder();

  /**
   * Checks if the set is of order 0.
   *
   * @return {@code true} if the order is 0, {@code false} otherwise
   */
  public boolean isEmpty();

  /**
   * Checks if the set is of order 1.
   *
   * @return {@code true} if the order is 1, {@code false} otherwise
   */
  public boolean isSingleton();

  /**
   * Returns an additive integer group of type {@link ZPlusMod} with the same
   * group order. For this to work, the group order must be finite and known.
   *
   * @return The resulting additive group.
   * @throws UnsupportedOperationException if the group order is infinite or
   * unknown
   */
  public ZMod getZModOrder();

  /**
   * Returns an multiplicative integer group of type {@link ZTimesMod} with the
   * same group order. For this to work, the group order must be finite and
   * known.
   *
   * @return The resulting multiplicative group.
   * @throws UnsupportedOperationException if the group order is infinite or
   * unknown
   */
  public ZStarMod getZStarModOrder();

  /**
   * Checks if {@code this} set contains an element that corresponds to a given
   * integer value.
   *
   * @param value The given integer value
   * @return {@code true} if such an element exists, {@code false} otherwise
   */
  public boolean contains(int value);

  /**
   * Checks if {@code this} set contains an element that corresponds to a given
   * BigInteger value.
   *
   * @param value The given BigInteger value
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code value} is null
   */
  public boolean contains(BigInteger value);

  /**
   * Checks if a given element belongs to the group.
   *
   * @param element The given element
   * @return {@code true} if {@code element} belongs to the group, {@code false}
   * otherwise
   * @throws IllegalArgumentException if {@code element} is null
   */
  public boolean contains(Element element);

  /**
   * Creates and returns the element that corresponds to a given integer (if one
   * exists).
   *
   * @param value The given integer
   * @return The corresponding element
   * @throws IllegalArgumentException if no such element exists
   */
  public Element getElement(int value);

  /**
   * Creates and returns the group element that corresponds to a given
   * BigInteger value (if one exists).
   *
   * @param value The given BigInteger value
   * @return The corresponding group element
   * @throws IllegalArgumentException if {@code value} is null or if no such
   * element exists in {@code this} group
   */
  public Element getElement(BigInteger value);

  /**
   * Creates and returns the group element that corresponds to the integer value
   * of or some other group element (if one exists).
   *
   * @param element The given group element
   * @return The corresponding group element of {@code this} group
   * @throws IllegalArgumentException if {@code element} is null or if no such
   * element exists in {@code this} group
   */
  public Element getElement(Element element);

  /**
   * Selects and returns a random group element using the default random
   * generator. For finite order group, it is selected uniformly at random. For
   * groups of infinite or unknown order, the underlying probability
   * distribution is not further specified.
   *
   * @return A random group element
   */
  public Element getRandomElement();

  /**
   * Selects and returns a random group element using a given random generator.
   * If no random generator is specified, i.e., if {@code random} is null, then
   * the system-wide random generator is taken. For finite order group, it is
   * selected uniformly at random. For groups of infinite or unknown order, the
   * underlying probability distribution is not generally specified.
   *
   * @param random Either {@code null} or a given random generator
   * @return A random group element
   */
  public Element getRandomElement(Random random);

  /**
   * Checks if two given elements of this group are equal.
   *
   * @param element1 The first element
   * @param element2 The second element
   * @return {@code true} if the elements are equal and belong to the group,
   * {@code false} otherwise
   * @throws IllegalArgumentException if {@code element1} or {@code element2} is
   * null
   */
  public boolean areEqual(Element element1, Element element2);

  public boolean isCompatible(Set set);

  //
  // The standard implementations of the following three inherited methods are
  // insufficient for sets
  //
  @Override
  public boolean equals(Object obj);

  @Override
  public int hashCode();

  @Override
  public String toString();

}
