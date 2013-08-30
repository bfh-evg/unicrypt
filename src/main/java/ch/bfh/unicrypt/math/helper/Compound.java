/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper;

/**
 *
 * @param <T>
 * @author rolfhaenni
 */
public interface Compound<T> extends Iterable<T> {

  /**
   * The arity of a function is the number of functions applied in parallel or
   * in series when calling the function.
   *
   * @return The function's arity
   */
  public int getArity();

  public boolean isNull();

  /**
   * Checks if a compound function consists of multiple copies of the same
   * function.
   *
   * @return {@code true}, if the function is a power function, {@code false}
   * otherwise
   */
  public boolean isUniform();

  /**
   * Returns the function at index 0.
   *
   * @return The function at index 0
   * @throws UnsupportedOperationException for functions of arity 0
   */
  public T getFirst();

  /**
   * Returns the function at the given index.
   *
   * @param index The given index
   * @return The corresponding function
   * @throws IndexOutOfBoundsException if {
   * @ode index} is an invalid index
   */
  public T getAt(int index);

  /**
   * Select and returns in a hierarchy of compound functions the function that
   * corresponds to a given sequence of indices. (e.g., 0,3,2 for the third
   * function in the fourth compound function of the first compound function).
   * Returns {@code this} function if {@code indices} is empty.
   *
   * @param indices The given sequence of indices
   * @return The corresponding function
   * @throws IllegalArgumentException if {
   * @ode indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {
   * @ode indices} contains an out-of-bounds index
   */
  public T getAt(int... indices);

  /**
   * Returns an array containing all the functions of which {@code this}
   * function is composed of.
   *
   * @return The corresponding array of functions
   */
  public T[] getAll();

//  /**
//   * Creates a new product set which contains one set less than the given
//   * product set.
//   *
//   * @param index The index of the set to remove
//   * @return The resulting product set.
//   * @throws IndexOutOfBoundsException if
//   * {@code index<0} or {@code index>arity-1}
//   */
//  public Compound<T> removeAt(final int index);
}
