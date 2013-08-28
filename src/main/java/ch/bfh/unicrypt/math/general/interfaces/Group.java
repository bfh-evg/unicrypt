package ch.bfh.unicrypt.math.general.interfaces;

import ch.bfh.unicrypt.math.monoid.interfaces.Monoid;

/**
 * This interface represents the mathematical concept a group. It
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
 * @version 2.0
 */
public interface Group extends Monoid {

  /**
   * Computes and returns the inverse of a given group element.
   * @param element A given group element
   * @return The inverse element of the input element
   * @throws IllegalArgumentException if {@code element} is null or does belong to the group
   */
  public Element invert(Element element);

  /**
   * Applies the binary group operation to the first and the inverse of the second given group element.
   * @param element1 The first group element
   * @param element2 The second group element
   * @return The result of applying the group operation to the two input elements
   * @throws IllegalArgumentException if {@code element1} or {@code element2} is null
   * @throws IllegalArgumentException if {@code element1} or {@code element2} does not belong to the group
   **/
  public Element applyInverse(Element element1, Element element2);

}
