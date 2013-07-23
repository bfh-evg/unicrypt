package ch.bfh.unicrypt.math.function.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;

/**
 * This interface represents the concept a unary mathematical function f:X->Y. It takes an input 
 * element from one group (the domain X) and maps it into an output element from another group 
 * (the co-domain Y). Although such functions always operate with single input and output elements, 
 * it is possible that these values are tuple elements of product or power groups. In such cases, 
 * the respective arities of these groups define thus the input and output arities of the 
 * function. Most functions will be proper deterministic mathematical functions, but some 
 * functions may be randomized and thus output different values when called multiple times.
 * 
 * @see Group
 * @see ProductGroup
 * @see <a href="http://en.wikipedia.org/wiki/Function_(mathematics)">http://en.wikipedia.org/wiki/Function_(mathematics)</a>
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface Function {

  /**
   * Returns the domain of this function.
   * @return The domain
   */
  public Group getDomain();
  
  /**
   * Returns the co-domain of this function.
   * @return The co-domain
   */
  public Group getCoDomain();

  /**
   * Returns the arity of the domain and the input elements.
   * @return The input arity
   */
  public int getArityIn();

  /**
   * Returns the arity of the co-domain and the output elements.
   * @return The output arity
   */
  public int getArityOut();

  /**
   * Returns the result of applying the function to the identity element.
   * @return The resulting output element
   */
  public Element apply();
  
  /**
   * Returns the result of applying the function to the identity element. In case of a randomized function, a random 
   * generator can be given as a second parameter. If no random generator is specified, i.e., if {@code random} 
   * is null, then the system-wide random generator is taken. If the function is deterministic, then {@code random} 
   * is ignored. 
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   */
  public Element apply(Random random);

  /**
   * Applies the function to an input element from the domain and returns the resulting output 
   * element from the co-domain.
   * @param element The given input element
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code element} is null or not contained in the domain
   */
  public Element apply(Element element);

  /**
   * Applies the function to an input element from the domain and returns the resulting output 
   * element from the co-domain. In case of a randomized function, a random generator can be given 
   * as a second parameter. If no random generator is specified, i.e., if {@code random} is null, then 
   * the system-wide random generator is taken. If the function is deterministic, then {@code random} 
   * is ignored. This is the main the method to implement for any type of function.
   * @param element The given input element
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   * @throws IllegalArgumentException if {@code element} is null or not contained in the domain
   */
  public Element apply(Element element, Random random);

  /**
   * This method provides a shortcut for applying a function with a product (or power group) domain. The
   * specified elements are used to create a corresponding tuple element first, which is then used 
   * to call the ordinary method {@code apply(Element element)}.
   * @param elements The given input elements
   * @return The resulting output element
   * @throws UnsupportedOperationException if the domain is not an instance a power group
   * @throws IllegalArgumentException if {@code elements} is or contains null
   * @throws IllegalArgumentException if the elements in {@code elements} are not contained in the corresponding sub-domains
   * @throws IllegalArgumentException if the the length of {@code elements} is different from {@code getArityIn()}
   */
  public Element apply(Element... elements);

  /**
   * This method provides a shortcut for applying a function with a product (or power group) domain. The
   * specified elements are used to create a corresponding tuple element first, which is then used 
   * to call the ordinary method {@code apply(Element element)}. In case of a randomized function, 
   * a random generator can be given as an additional parameter. If no random generator is specified, 
   * i.e., if {@code random} is null, then the system-wide random generator is taken. If the function 
   * is deterministic, then {@code random} is ignored.
   * @param elements The given input elements
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   * @throws UnsupportedOperationException if the domain is not a power group
   * @throws IllegalArgumentException if {@code elements} is or contains null
   * @throws IllegalArgumentException if the elements in {@code elements} are not contained in the corresponding sub-domains
   * @throws IllegalArgumentException if the the length of {@code elements} is different from {@code getArityIn()}
   */
  public Element apply(Element[] elements, Random random);

  /**
   * This method applies a single input value to a given function with a product (or power group) domain. The result is
   * a new function with an input arity decreased by 1. If the resulting input arity is 1, then the product (or power 
   * group) domain is replaced by the single remaining group. 
   * @param element The given input value
   * @param index The index of the corresponding group in the product (or power group) domain
   * @return The resulting partially applied function
   * @throws UnsupportedOperationException if the domain is not a power group 
   * @throws IllegalArgumentException if {@code element} is null or not an element of the corresponding sub-domain group
   * @throws IndexOutOfBoundsException if {@code index} is an invalid index 
   */
  public Function partiallyApply(Element element, int index);

  /**
   * Returns the function at the given index.
   * @param index The given index
   * @return The corresponding function
   * @throws IndexOutOfBoundsException if {@ode index} is an invalid index
   */
  public Function getFunctionAt(int index);

  /**
   * Select and returns in a hierarchy of compound function the function that corresponds to a given sequence of indices.
   * (e.g., 0,3,2 for the third function in the fourth compound function of the first compound function). Returns 
   * {@code this} function if {@code indices} is empty.
   * @param indices The given sequence of indices
   * @return The corresponding function
   * @throws IllegalArgumentException if {@ode indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@ode indices} contains an out-of-bounds index 
   */
  public Function getFunctionAt(int... indices);
  
  public Function getFunction();

  public int getArity();

  public boolean isAtomicFunction();

  public boolean isEmptyFunction();
  
  public boolean isPowerFunction();

}
