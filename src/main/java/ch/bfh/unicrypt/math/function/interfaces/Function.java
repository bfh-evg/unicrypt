package ch.bfh.unicrypt.math.function.interfaces;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.set.interfaces.Set;
import java.util.Random;

/**
 * This interface represents the concept a unary mathematical function f:X->Y.
 * It takes an input element from one group (the domain X) and maps it into an
 * output element from another group (the co-domain Y). Although such functions
 * always operate with single input and output elements, it is possible that
 * these values are tuple elements of corresponding product groups. In such
 * cases, the respective arities of these groups define thus the input and
 * output arities of the function. Most functions will be proper deterministic
 * mathematical functions, but some functions may be randomized and thus output
 * different values when called multiple times.
 *
 * @see Group
 * @see ProductGroup
 * @see <a
 * href="http://en.wikipedia.org/wiki/Function_(mathematics)">http://en.wikipedia.org/wiki/Function_(mathematics)</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Function {

  /**
   * Applies the function to an input element from the domain and returns the
   * resulting output element from the co-domain.
   *
   * @param element The given input element
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code element} is null or not
   * contained in the domain
   */
  public Element apply(Element element);

  /**
   * Applies the function to an input element from the domain and returns the
   * resulting output element from the co-domain. In case of a randomized
   * function, a random generator can be given as a second parameter. If no
   * random generator is specified, i.e., if {@code random} is null, then the
   * system-wide random generator is taken. If the function is deterministic,
   * then {@code random} is ignored. This is the main the method to implement
   * for any type of function.
   *
   * @param element The given input element
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code element} is null or not
   * contained in the domain
   */
  public Element apply(Element element, Random random);

  /**
   * This method provides a shortcut for applying a function with multiple input
   * values. The specified elements are used to create a corresponding tuple
   * element first, which is then used to call the ordinary method
   * {@code apply(Element element)}.
   *
   * @param elements The given input elements
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code elements} is or contains null
   * @throws IllegalArgumentException if the elements in {@code elements} are
   * not contained in the corresponding sub-domains
   * @throws IllegalArgumentException if the the length of {@code elements} is
   * different from {@code getArityIn()}
   */
  public Element apply(Element... elements);

  /**
   * This method provides a shortcut for applying a function with multiple input
   * values. The specified elements are used to create a corresponding tuple
   * element first, which is then used to call the ordinary method
   * {@code apply(Element element, Random random)}. In case of a randomized
   * function, a random generator can be given as an additional parameter. If no
   * random generator is specified, i.e., if {@code random} is null, then the
   * system-wide random generator is taken. If the function is deterministic,
   * then {@code random} is ignored.
   *
   * @param elements The given input elements
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code elements} is or contains null
   * @throws IllegalArgumentException if the elements in {@code elements} are
   * not contained in the corresponding sub-domains
   * @throws IllegalArgumentException if the the length of {@code elements} is
   * different from {@code getArityIn()}
   */
  public Element apply(Element[] elements, Random random);

  /**
   * Returns the domain of this function.
   *
   * @return The domain
   */
  public Set getDomain();

  /**
   * Returns the co-domain of this function.
   *
   * @return The co-domain
   */
  public Set getCoDomain();

  /**
   * This method applies a single input value to a given function. The result is
   * a new function with an input arity decreased by 1.
   *
   * @param element The given input value
   * @param index The index of the corresponding group in the product (or power
   * group) domain
   * @return The resulting partially applied function
   * @throws IllegalArgumentException if {@code element} is null or not an
   * element of the corresponding sub-domain group
   * @throws IndexOutOfBoundsException if {@code index} is an invalid index
   */
  public Function partiallyApply(Element element, int index);

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