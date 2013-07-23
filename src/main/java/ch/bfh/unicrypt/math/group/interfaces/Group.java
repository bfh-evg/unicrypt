package ch.bfh.unicrypt.math.group.interfaces;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;

/**
 * This interface represents the concept a mathematical group. It
 * defines a set of elements and an associative (but not necessarily
 * commutative) binary operation. Applying the operation to group elements yields another
 * group element. Every group element has an inverse element, and the group
 * itself has a unique identity element. The number of elements in the group is
 * called group order (it may be infinite or unknown). 
 * We assume that each element of a group corresponds to a unique integer value. Therefore,
 * this interface provides methods for converting group 
 * elements into corresponding integer values and back. 

 * 
 * @see "Handbook of Applied Cryptography, Definition 2.162"
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface Group extends Serializable {

  /**
   * A constant value that represents an infinite group order.
   */
  public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);

  /**
   * A constant value that represents an unknown group order.
   */
  public static final BigInteger UNKNOWN_ORDER = BigInteger.ZERO;

  /**
   * Checks if the group is a strict sub-group of another group.
   * @return {@code true} if it is a strict sub-group, {@code false} otherwise
   */
  public boolean isSubGroup();

  /**
   * Returns the group of maximal order which contains {@code this} group as a sub-group. The 
   * resulting super-group can be {@code this} group itself. 
   * @return The super-group of maximal order
   */
  public Group getSuperGroup();

  /**
   * Creates and returns the group element that corresponds to a given integer (if one exists).
   * @param value The given integer
   * @return The corresponding group element
   * @throws IllegalArgumentException if no such element exists in {@code this} group
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
   * Returns the group's unique identity element.
   * @return The identity element.
   */
  public Element getIdentityElement();

  /**
   * Applies the binary group operation to two group elements (in the given order).
   * @param element1 The first group element
   * @param element2 The second group element
   * @return The result of applying the group operation to the two input elements
   * @throws IllegalArgumentException if {@code element1} or {@code element2} is null 
   * @throws IllegalArgumentException if {@code element1} or {@code element2} does not belong to the group
   **/
  public Element apply(Element element1, Element element2);

  /**
   * Applies the binary group operation to the first and the inverse of the second given group element.
   * @param element1 The first group element
   * @param element2 The second group element
   * @return The result of applying the group operation to the two input elements
   * @throws IllegalArgumentException if {@code element1} or {@code element2} is null 
   * @throws IllegalArgumentException if {@code element1} or {@code element2} does not belong to the group
   **/
  public Element applyInverse(Element element1, Element element2);

  /**
   * Applies the binary group operation pair-wise sequentially to multiple group elements (in the given order). 
   * Returns the identity element, if the given list of group elements is empty.
   * @param elements A given array of group elements
   * @return The result of applying the group operation to the input elements
   * @throws IllegalArgumentException if {@code elements} or one of its elements is null 
   * @throws IllegalArgumentException if one of the elements in {@code elements} does not belong to the group
   */
  public Element apply(Element... elements);

  /**
   * Applies the binary group operation repeatedly to {@code amount} many instances of a given group element. 
   * If {@code amount} equals 0, then the identity element is returned. If {@code amount} is negative, then 
   * the corresponding positive value is applied to the inverse of the given element. If the group is finite 
   * and if its order is known to be {@code q}, then {@amount} can be replaced by {@code amount mod q}.
   * @param element A given group element
   * @param amount The number of instances of the input element
   * @return The result of applying the group operation multiple times to the input element
   * @throws IllegalArgumentException if {@code element} or {@code amount} is null
   * @throws IllegalArgumentException if {@code element} does not belong to the group
   */
  public Element selfApply(Element element, BigInteger amount);

  /**
   * Same as {@link #Group.selfApply(Element, BigInteger)}, except that the amount is given as an {@link Element} 
   * object, which can always be converted into a BigInteger value.
   * @param element A given group element
   * @param amount The number of instances of the input element given as an {@link Element} object
   * @return The result of applying the group operation multiple times to the input element
   * @throws IllegalArgumentException if {@code element} or {@code amount} is null
   * @throws IllegalArgumentException if {@code element} does not belong to the group
   */
  public Element selfApply(Element element, Element amount);

  /**
   * Same as {@link #Group.selfApply(Element, BigInteger)}, except that the amount is given as an {@code int} value.
   * @param element A given group element
   * @param amount The number of instances of the input element
   * @return The result of applying the operation multiple times to the input element
   * @throws IllegalArgumentException if {@code element} is null or does not belong to the group
   */
  public Element selfApply(Element element, int amount);

  /**
   * Applies the group operation to two instances of a given group element. This is equivalent to {@code selfApply(element, 2)}.
   * @param element A given group element
   * @return The result of applying the group operation to the input element
   * @throws IllegalArgumentException if {@code element} is null or does not belong to the group
   */
  public Element selfApply(Element element);

  /**
   * Applies the group binary operation pair-wise sequentially to the results of computing {@link #selfApply(Element, BigInteger)} 
   * multiple times. In an additive group, this operation is sometimes called 'weighed sum', and 'product-of-powers' in 
   * a multiplicative group.
   * @param elements A given array of group elements
   * @param amounts Corresponding amounts
   * @return The result of this operation
   * @throws IllegalArgumentException if {@code elements} or one of its elements is null 
   * @throws IllegalArgumentException if {@code amounts} or one of its values is null 
   * @throws IllegalArgumentException if one of the elements of {@code elements} does not belong to the group 
   * @throws IllegalArgumentException if {@code elements} and {@code amounts} have different lengths 
   */
  public Element multiSelfApply(Element[] elements, BigInteger[] amounts);

  /**
   * Computes and returns the inverse of a given group element.
   * @param element A given group element
   * @return The inverse element of the input element
   * @throws IllegalArgumentException if {@code element} is null or does belong to the group
   */
  public Element invert(Element element);

  /**
   * Returns the group order. If the group order is unknown, {@link #UNKNOWN_ORDER} is returned.
   * If the group order is infinite, {@link #INFINITE_ORDER} is returned. 
   * @see "Handbook of Applied Cryptography, Definition 2.163"
   * @return The group order.
   */
  public BigInteger getOrder();

  /**
   * Returns a lower bound for the group order in case the exact group order is unknown. The least return value is 1. 
   * Otherwise, if the exact group order is known (or infinite), the exact group order is returned.  
   * @return A lower bound for the group order.
   */
  public BigInteger getMinOrder();

  /**
   * Checks if {@code this} group has the same order as another given group. Returns {@code false} if the order of both groups 
   * is unknown.
   * @param group The other group
   * @return {@code true} if the order is the same, {@code false} otherwise
   * @throws IllegalArgumentException if {@code group} is null
   */
  public boolean hasSameOrder(Group group);

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
   * Checks if a given element is the group's identity element.
   * @param element The given element
   * @return {@code true} if {@code element} is the group's identity element, {@code false} otherwise
   * @throws IllegalArgumentException if {@code element} is null
   */
  public boolean isIdentity(Element element);

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
   * Checks if the group is atomic, which is equivalent to checking arity<=1.
   * @return {@code true} if the arity is 1, {@code false} otherwise
   */
  public boolean isAtomicGroup();

  public boolean isEmptyGroup();

  /**
   * Checks if the group is of order 1.
   * @return {@code true} if the order is 1, {@code false} otherwise
   */
  public boolean isSingletonGroup();

  /**
   * Returns the group for the given index. The indices are numbered from 0 to the group's arity minus one.
   * @param index The given index
   * @return The corresponding group
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}  
   */
  public Group getGroupAt(final int index);

  /**
   * Selects and returns in a hierarchy of groups the group that corresponds to a given sequence of indices
   * (e.g., 0,3,2 for the third group in the fourth group of the first group). Returns {@code this} group if {@code indices}
   * is empty.
   * @param indices The given sequence of indices
   * @return The corresponding group
   * @throws IllegalArgumentException if {@code indices} is null or if its length exceeds the hierarchy's depth  
   * @throws IndexOutOfBoundsException if {@code indices} contains an out-of-bounds index   
   */  
  public Group getGroupAt(int... indices);

  /**
   * Returns the group at index 0.
   * @return The group at index;
   */
  public Group getGroup();

  /**
   * Creates a new product group which contains one group less than the given product group. If the given product group has 
   * arity 1, then a singleton group is returned.
   * @param index The index of the group to remove
   * @return The resulting product group.
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}  
   */
  public Group removeGroupAt(final int index);

  /**
   * Checks if the product group consists of multiple copies of the same group. Such a product group is called 'power group'.
   * @return {@code true}, if the product group is a power group, {@code false} otherwise
   */
  public abstract boolean isPowerGroup();

  //
  // The standard implementations of the following three inherited methods are insufficient for groups
  //

  @Override
  public boolean equals(Object obj);

  @Override
  public int hashCode();

  @Override
  public String toString();

}
