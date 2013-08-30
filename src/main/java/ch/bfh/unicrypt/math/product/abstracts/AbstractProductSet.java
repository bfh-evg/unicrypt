/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.product.abstracts;

import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.Compound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.abstracts.AbstractSet;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductSet<S extends Set, T extends Tuple, E extends Element> extends AbstractSet<T> implements Compound<S> {

  private final S[] sets;
  private final int arity;

  protected AbstractProductSet(Set[] sets) {
    this.sets = (S[]) sets.clone();
    this.arity = sets.length;
  }

  protected AbstractProductSet(Set set, int arity) {
    this.sets = (S[]) new Set[]{set};
    this.arity = arity;
  }

  protected AbstractProductSet() {
    this(new Set[]{});
  }

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
  public final boolean contains(final int[] values) {
    return this.contains(MathUtil.intToBigIntegerArray(values));
  }

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
  public final boolean contains(BigInteger... values) {
    int arity = this.getArity();
    if (values == null || values.length != arity) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).contains(values[i])) {
        return false;
      }
    }
    return true;
  }

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
  public final boolean contains(Element... elements) {
    int arity = this.getArity();
    if (elements == null || elements.length != arity) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).contains(elements[i])) {
        return false;
      }
    }
    return true;
  }

  public final T getElement(final int[] values) {
    return this.getElement(MathUtil.intToBigIntegerArray(values));
  }

  /**
   * Creates and returns the element that corresponds to the given BigInteger
   * values (if one exists).
   *
   * @param values The given BigInteger values
   * @return The corresponding element
   * @throws IllegalArgumentException if {@code values} is or contains null or
   * if no such element exists
   */
  public final T getElement(BigInteger[] values) {
    int arity = this.getArity();
    if (values == null || values.length != arity) {
      throw new IllegalArgumentException();
    }
    E[] elements = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      elements[i] = (E) this.getAt(i).getElement(values[i]);
    }
    return this.abstractGetElement(elements);
  }

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
  public final T getElement(final Element[] elements) {
    int arity = this.getArity();
    if (elements == null || elements.length != arity) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).contains(elements[i])) {
        throw new IllegalArgumentException();
      }
    }
    return this.abstractGetElement(elements);
  }

  protected abstract T abstractGetElement(final Element[] elements);

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected BigInteger standardGetMinOrder() {
    if (this.isUniform()) {
      BigInteger minOrder = this.getFirst().getMinOrder();
      return minOrder.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (Set set : this) {
      final BigInteger minOrder = set.getMinOrder();
      result = result.multiply(minOrder);
    }
    return result;
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected BigInteger abstractGetOrder() {
    if (this.isNull()) {
      return BigInteger.ONE;
    }
    if (this.isUniform()) {
      BigInteger order = this.getFirst().getOrder();
      if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
        return order;
      }
      return order.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (Set set : this) {
      final BigInteger order = set.getOrder();
      if (order.equals(BigInteger.ZERO)) {
        return BigInteger.ZERO;
      }
      if (order.equals(Set.INFINITE_ORDER) || result.equals(Set.INFINITE_ORDER)) {
        result = Set.INFINITE_ORDER;
      } else {
        if (order.equals(Set.UNKNOWN_ORDER) || result.equals(Set.UNKNOWN_ORDER)) {
          result = Set.UNKNOWN_ORDER;
        } else {
          result = result.multiply(order);
        }
      }
    }
    return result;
  }

  @Override
  protected T abstractGetElement(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.getElement(values);
  }

  @Override
  protected T abstractGetRandomElement(Random random) {
    int arity = this.getArity();
    final Element[] randomElements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return this.abstractGetElement(randomElements);
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.contains(values);
  }

  @Override
  public int getArity() {
    return this.arity;
  }

  @Override
  public final boolean isNull() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isUniform() {
    return this.sets.length <= 1;
  }

  @Override
  public S getFirst() {
    return this.getAt(0);

  }

  @Override
  public S getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.sets[0];
    }
    return this.sets[index];
  }

  @Override
  public S getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    S set = (S) this;
    for (final int index : indices) {
      if (set.isCompound()) {
        set = ((Compound<S>) set).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return set;
  }

  @Override
  public S[] getAll() {
    int arity = this.getArity();
    S[] result = (S[]) new Set[arity];
    for (int i = 0; i < this.arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Iterator<S> iterator() {
    final Compound<S> compoundSet = this;
    return new Iterator<S>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < compoundSet.getArity();
      }

      @Override
      public S next() {
        if (this.hasNext()) {
          return (S) compoundSet.getAt(this.currentIndex++);
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

  @Override
  protected boolean standardIsCompound() {
    return true;
  }

  @Override
  protected boolean standardEquals(Set set) {
    AbstractProductSet other = (AbstractProductSet) set;
    int arity = this.getArity();
    if (arity != other.getArity()) {
      return false;
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).equals(other.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof AbstractProductSet);
  }

  @Override
  protected int standardHashCode() {
    final int prime = 31;
    int result = 1;
    for (Set set : this) {
      result = prime * result + set.hashCode();
    }
    result = prime * result + this.getArity();
    return result;
  }

  @Override
  protected String standardToString() {
    if (this.isNull()) {
      return "";
    }
    if (this.isUniform()) {
      return this.getFirst().toString() + "^" + this.getArity();
    }
    String result = "";
    String separator = "";
    for (Set set : this) {
      result = result + separator + set.toString();
      separator = ",";
    }
    return result;
  }

}
