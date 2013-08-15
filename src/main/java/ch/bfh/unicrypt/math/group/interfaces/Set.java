package ch.bfh.unicrypt.math.group.interfaces;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;

/**
 * This interface represents the concept a mathematical set of elements.
 * The number of elements in the set is called order. The order may be
 * infinite or unknown. A set is either atomic or a product of multiple other
 * (possibly non-atomic) sets. The arity defines the number of sets such a
 * product is composed of. The arity of an atomic set is 1. It is assumed that
 * each element of a set corresponds to a unique BigInteger value. Therefore,
 * the interface provides methods for converting elements into corresponding
 * BigInteger values and back.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Set extends Serializable {

  /**
   * A constant value that represents an infinite order.
   */
  public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);

  /**
   * A constant value that represents an unknown order.
   */
  public static final BigInteger UNKNOWN_ORDER = BigInteger.ZERO;

  /**
   * Creates and returns the element that corresponds to a given integer (if
   * one exists).
   * @param value The given integer
   * @return The corresponding element
   * @throws IllegalArgumentException if no such element exists
   */
  public Element getElement(int value);

  public Element getElement(int... values);

  /**
   * Creates and returns the group element that corresponds to a given BigInteger value (if one exists).
   * @param value The given BigInteger value
   * @return The corresponding group element
   * @throws IllegalArgumentException if {@code value} is null or if no such element exists in {@code this} group
   */
  public Element getElement(BigInteger value);

  /**
   * Creates and returns the group element that corresponds to a given BigInteger values (if one exists).
   * @param values The given BigInteger values
   * @return The corresponding group element
   * @throws IllegalArgumentException if {@code values} is or contains null or if no such element exists in {@code this} group
   */
  public Element getElement(BigInteger... values);

  /**
   * Creates and returns the group element that corresponds to the integer value of or some other group element (if one exists).
   * @param element The given group element
   * @return The corresponding group element of {@code this} group
   * @throws IllegalArgumentException if {@code element} is null or if no such element exists in {@code this} group
   */
  public Element getElement(Element element);

  /**
   * Creates a new tuple element for that group from a given array of group elements. For this to work,
   * each of the given elements must be exactly one of the corresponding involved group.
   * @param elements The given array of elements
   * @return The resulting tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains null, or if its size does not correspond to the group's arity
   * @throws IllegalArgumentException if an element is not in the corresponding group
   */
  public Element getElement(final Element... elements);

  /**
   * Selects and returns a random group element using the default random generator. For finite order group, it is
   * selected uniformly at random. For groups of infinite or unknown order, the underlying probability distribution
   * is not further specified.
   * @return A random group element
   */
  public Element getRandomElement();

  /**
   * Selects and returns a random group element using a given random generator. If no random generator is specified, i.e., if
   * {@code random} is null, then the system-wide random generator is taken. For finite order group, it is selected uniformly
   * at random. For groups of infinite or unknown order, the underlying probability distribution is not generally specified.
   * @param random Either {@code null} or a given random generator
   * @return A random group element
   */
  public Element getRandomElement(Random random);

  /**
   * Returns the group order. If the group order is unknown, {@link #UNKNOWN_ORDER} is returned.
   * If the group order is infinite, {@link #INFINITE_ORDER} is returned.
   * @see "Handbook of Applied Cryptography, Definition 2.163"
   * @return The group order.
   */
  public BigInteger getOrder();

  /**
   * Checks if the set is of order 0.
   * @return {@code true} if the order is 0, {@code false} otherwise
   */
  public boolean isEmpty();

  /**
   * Checks if the set is of order 1.
   * @return {@code true} if the order is 1, {@code false} otherwise
   */
  public boolean isSingleton();

  /**
   * Returns a lower bound for the group order in case the exact group order is unknown. The least return value is 1.
   * Otherwise, if the exact group order is known (or infinite), the exact group order is returned.
   * @return A lower bound for the group order.
   */
  public BigInteger getMinOrder();

  /**
   * Returns an additive integer group of type {@link ZPlusMod} with the same group order. For this to work, the group
   * order must be finite and known.
   * @return The resulting additive group.
   * @throws UnsupportedOperationException if the group order is infinite or unknown
   */
  public ZPlusMod getOrderGroup();

  /**
   * Returns an additive integer group of type {@link ZPlusMod}. Its order corresponds to {@link #getMinOrder()}.
   * For this to work, the lower bound of the group order must be finite.
   * @return The resulting additive group
   * @throws UnsupportedOperationException if the lower bound of the group order is infinite
   */
  public ZPlusMod getMinOrderGroup();

  /**
   * Checks if {@code this} group contains an element that corresponds to a given integer value.
   * @param value The given integer value
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code value} is null
   */
  public boolean contains(BigInteger value);

  public boolean contains(BigInteger... values);

  /**
   * Checks if a given element belongs to the group.
   * @param element The given element
   * @return {@code true} if {@code element} belongs to the group, {@code false} otherwise
   * @throws IllegalArgumentException if {@code element} is null
   */
  public boolean contains(Element element);

  /**
   * Checks if {@code this} group contains an element that corresponds to the composition of some given elements.
   * @param elements The given elements
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code elements} is null
   */
  public boolean contains(Element... elements);

  /**
   * Checks if two given elements of this group are equal.
   * @param element1 The first element
   * @param element2 The second element
   * @return {@code true} if the elements are equal and belong to the group, {@code false} otherwise
   * @throws IllegalArgumentException if {@code element1} or {@code element2} is null
   */
  public boolean areEqual(Element element1, Element element2);

  /**
   * Returns the group's arity. The default arity of a non-composed non-singleton group is 1. The
   * arity of a singleton group is 0. In case of a product group,
   * the arity is the number of groups it is composed of.
   * @return The group's arity
   */
  public int getArity();

  /**
   * Checks if the arity of the set is 0.
   * @return {@code true} if the arity is 0, {@code false} otherwise
   */
  public boolean isNull();

  /**
   * Checks if the set is atomic, which is equivalent to checking arity <= 1.
   * @return {@code true} if the arity is <= 1, {@code false} otherwise
   */
  public boolean isAtomic();

  /**
   * Checks if the set consists of multiple copies of the same set. Such a product set is called 'power set'.
   * @return {@code true}, if the product group is a power group, {@code false} otherwise
   */
  public abstract boolean isPower();

  /**
   * Returns the group for the given index. The indices are numbered from 0 to the group's arity minus one.
   * @param index The given index
   * @return The corresponding group
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
   */
  public Set getAt(final int index);

  /**
   * Selects and returns in a hierarchy of groups the group that corresponds to a given sequence of indices
   * (e.g., 0,3,2 for the third group in the fourth group of the first group). Returns {@code this} group if {@code indices}
   * is empty.
   * @param indices The given sequence of indices
   * @return The corresponding group
   * @throws IllegalArgumentException if {@code indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an out-of-bounds index
   */
  public Set getAt(int... indices);

  /**
   * Returns the set at index 0.
   * @return The set at index 0
   * @throws UnsupportedOperationException for sets of arity 0
   */
  public Set getFirst();

  /**
   * Creates a new product group which contains one group less than the given product group. If the given product group has
   * arity 1, then a singleton group is returned.
   * @param index The index of the group to remove
   * @return The resulting product group.
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
   */
  public Set removeAt(final int index);

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
