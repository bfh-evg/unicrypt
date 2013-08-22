/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.interfaces;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundFunction extends Function {

  /**
   * The arity of a function is the number of functions applied in parallel when
   * calling the function. For arity<>1, the arity corresponds to the arity of
   * both the domain and the co-domain of the function.
   *
   * @return The function's arity
   */
  public int getArity();

  public boolean isNull();

  /**
   * Checks if the set consists of multiple copies of the same function. Such a
   * product set is called 'power function'.
   *
   * @return {@code true}, if the product function is a power function,
   * {@code false} otherwise
   */
  public boolean isPower();

  /**
   * Returns the function at index 0.
   *
   * @return The function at index 0
   * @throws UnsupportedOperationException for functions of arity 0
   */
  public Function getFirst();

  /**
   * Returns the function at the given index.
   *
   * @param index The given index
   * @return The corresponding function
   * @throws IndexOutOfBoundsException if {
   * @ode index} is an invalid index
   */
  public Function getAt(int index);

  /**
   * Select and returns in a hierarchy of composed function the function that
   * corresponds to a given sequence of indices. (e.g., 0,3,2 for the third
   * function in the fourth composed function of the first composed function).
   * Returns {@code this} function if {@code indices} is empty.
   *
   * @param indices The given sequence of indices
   * @return The corresponding function
   * @throws IllegalArgumentException if {
   * @ode indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {
   * @ode indices} contains an out-of-bounds index
   */
  public Function getAt(int... indices);

  /**
   * Returns an array of length {@code this.getArity()} containing all the
   * functions of which {@code this} function is composed of.
   */
  public Function[] getAll();

}
