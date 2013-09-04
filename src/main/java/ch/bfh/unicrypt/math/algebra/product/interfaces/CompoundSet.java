/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundSet extends Set, Compound<CompoundSet> {

  /**
   * Checks if {@code this} set contains an element that corresponds to the
   * composition of some given integer values.
   *
   * @param values The given integer values
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code values} is null
   * @throws IllegalArgumentException if the length of {@code values} is
   * different from the set's arity
   */
  public boolean contains(final int[] values);

  /**
   * Checks if {@code this} set contains an element that corresponds to the
   * composition of some given BigInteger values.
   *
   * @param values The given BigInteger values
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code values} is or contains null
   * @throws IllegalArgumentException if the length of {@code values} is
   * different from the set's arity
   */
  public boolean contains(BigInteger... values);

  /**
   * Checks if {@code this} set contains an element that corresponds to the
   * composition of some given elements.
   *
   * @param elements The given elements
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code elements} is or contains null
   * @throws IllegalArgumentException if the length of {@code elements} is
   * different from the set's arity
   */
  public boolean contains(Element... elements);

  public CompoundElement getElement(final int[] values);

  /**
   * Creates and returns the element that corresponds to the given BigInteger
   * values (if one exists).
   *
   * @param values The given BigInteger values
   * @return The corresponding element
   * @throws IllegalArgumentException if {@code values} is or contains null or
   * if no such element exists
   */
  public CompoundElement getElement(BigInteger[] values);

  /**
   * Creates a new tuple element for from a given array of elements. For this to
   * work, each of the given elements must be contained in the corresponding
   * involved set.
   *
   * @param elements The given array of elements
   * @return The resulting tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains
   * null, or if its size does not correspond to the group's arity
   * @throws IllegalArgumentException if an element is not in the corresponding
   * group
   */
  public CompoundElement getElement(final Element[] elements);

  @Override
  public Set getFirst();

  @Override
  public Set getAt(int index);

  @Override
  public Set getAt(int... indices);

  @Override
  public Set[] getAll();

  @Override
  public CompoundSet removeAt(final int index);

}
