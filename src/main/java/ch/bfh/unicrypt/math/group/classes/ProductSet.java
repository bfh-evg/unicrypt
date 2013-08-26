package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.abstracts.AbstractCompoundElement;
import ch.bfh.unicrypt.math.element.interfaces.CompoundElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractCompoundSet;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet extends AbstractCompoundSet implements Set {

  protected ProductSet(final Set[] sets) {
    super(sets);
  }

  protected ProductSet(final Set set, final int arity) {
    super(set, arity);
  }

  protected ProductSet() {
    this(new Set[]{});
  }

  /**
   * Checks if {@code this} set contains an element that corresponds to the
   * composition of some given integer values.
   *
   * @param values The given integer values
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code values} is null
   * @throws IllegalArgumentException if the length of {@code values} is different from the set's arity
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
   * @throws IllegalArgumentException if the length of {@code values} is different from the set's arity
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
   * @throws IllegalArgumentException if the length of {@code elements} is different from the set's arity
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

  public final CompoundElement getElement(final int[] values) {
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
  public final CompoundElement getElement(BigInteger[] values) {
    int arity = this.getArity();
    if (values == null || values.length != arity) {
      throw new IllegalArgumentException();
    }
    Element[] elements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      elements[i] = this.getAt(i).getElement(values[i]);
    }
    return this.standardGetElement(elements);
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
  public final CompoundElement getElement(final Element[] elements) {
    int arity = this.getArity();
    if (elements == null || elements.length != arity) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < arity; i++) {
      if (!this.getAt(i).contains(elements[i])) {
        throw new IllegalArgumentException();
      }
    }
    return standardGetElement(elements);
  }

  protected CompoundElement standardGetElement(final Element[] elements) {
      return new AbstractCompoundElement(this, elements) {};
    }

  /**
   * Creates a new product set which contains one set less than the given
   * product set.
   *
   * @param index The index of the set to remove
   * @return The resulting product set.
   * @throws IndexOutOfBoundsException if
   * {@code index<0} or {@code index>arity-1}
   */
  public ProductSet removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return ProductSet.getInstance(this.getFirst(), arity-1);
    }
    final Set[] remainingSets = new Set[arity - 1];
    for (int i=0; i < arity-1; i++) {
      if (i < index) {
        remainingSets[i] = this.getAt(i);
      } else {
        remainingSets[i] = this.getAt(i+1);
      }
    }
    return ProductSet.getInstance(remainingSets);
  }

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
    for (Set set: this) {
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
    for (Set set: this) {
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
  protected CompoundElement abstractGetElement(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.getElement(values);
  }

  @Override
  protected CompoundElement abstractGetRandomElement(Random random) {
    int arity = this.getArity();
    final Element[] randomElements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return standardGetElement(randomElements);
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.contains(values);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a static factory method to construct a composed set without calling
   * respective constructors. The input sets are given as an array.
   * @param sets The array of input sets
   * @return The corresponding product set
   * @throws IllegalArgumentException if {@code sets} is null or contains null
   */
  public static ProductSet getInstance(final Set... sets) {
    if (sets == null) {
      throw new IllegalArgumentException();
    }
    if (sets.length == 0) {
      return new ProductSet();
    }
    if (ProductSet.areEqual(sets)) {
      return new ProductSet(sets[0], sets.length);
    }
    return new ProductSet(sets);
  }

  public static ProductSet getInstance(final Set set, int arity) {
    if ((set == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSet();
    }
    return new ProductSet(set, arity);
  }

  //
  // STATIC HELPER METHODS
  //

  protected static boolean areEqual(Set[] sets) {
    if (sets.length == 0) {
      return true;
    }
    boolean result = true;
    Set firstSet = sets[0];
    for (final Set set: sets) {
      if (set == null) {
        throw new IllegalArgumentException();
      }
      if (!set.equals(firstSet)) {
        return false;
      }
    }
    return true;
  }

  /**
   * This is a static factory method to construct a composed element without the
   * need of constructing the corresponding product or power group beforehand.
   * The input elements are given as an array.
   *
   * @param elements The array of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains
   * null
   */
  public static CompoundElement constructElement(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Set[] sets = new Set[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = elements[i].getSet();
    }
    return ProductSet.getInstance(sets).getElement(elements);
  }

}
