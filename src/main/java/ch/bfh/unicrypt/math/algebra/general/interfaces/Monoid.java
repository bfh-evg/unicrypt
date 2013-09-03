package ch.bfh.unicrypt.math.algebra.general.interfaces;


/**
 * This interface represents the mathematical concept a  group. It
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
public interface Monoid extends SemiGroup {

  /**
   * Returns the group's unique identity element.
   * @return The identity element.
   */
  public Element getIdentityElement();

  /**
   * Checks if a given element is the group's identity element.
   * @param element The given element
   * @return {@code true} if {@code element} is the group's identity element, {@code false} otherwise
   * @throws IllegalArgumentException if {@code element} is null
   */
  public boolean isIdentityElement(Element element);

}
