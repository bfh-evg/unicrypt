package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import static ch.bfh.unicrypt.math.group.interfaces.Set.UNKNOWN_ORDER;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet extends AbstractSet implements Set {

  private final Set[] sets;
  private int arity;

  protected ProductSet(final Set[] sets) {
    this.sets = sets;
    this.arity = sets.length;
  }

  protected ProductSet(final Set set, final int arity) {
    this.sets = new Set[]{set};
    this.arity = arity;
  }

  protected ProductSet() {
    this.sets = new Set[]{};
    this.arity = 0;
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
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < this.getArity(); i++) {
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
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).contains(elements[i])) {
        return false;
      }
    }
    return true;
  }

  public final Element getElement(final int[] values) {
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
  public final Element getElement(BigInteger[] values) {
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    Element[] elements = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      elements[i] = this.getAt(i).getElement(values[i]);
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
  public final Element getElement(final Element[] elements) {
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).contains(elements[i])) {
        throw new IllegalArgumentException();
      }
    }
    return abstractGetElement(elements);
  }

  protected Element abstractGetElement(final Element[] elements) {
    return new TupleElement(this, elements);
  }

  /**
   * Checks if the arity of the set is 0.
   *
   * @return {@code true} if the arity is 0, {@code false} otherwise
   */
  public final int getArity() {
    return this.arity;
  }

  public final boolean isNull() {
    return this.getArity() == 0;
  }

  /**
   * Checks if the set consists of multiple copies of the same set. Such a
   * product set is called 'power set'.
   *
   * @return {@code true}, if the product group is a power group, {@code false}
   * otherwise
   */
  public final boolean isPower() {
    return this.sets.length <= 1;
  }

  /**
   * Returns the set for the given index. The indices are numbered from 0 to the
   * set's arity minus one.
   *
   * @param index The given index
   * @return The corresponding set
   * @throws IndexOutOfBoundsException if
   * {@code index<0} or {@code index>arity-1}
   */
  public Set getAt(final int index) {
    if (index < 0 || index > this.getArity() - 1) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isPower()) {
      return this.sets[0];
    }
    return this.sets[index];
  }

  /**
   * Selects and returns in a hierarchy of sets the set that corresponds to a
   * given sequence of indices (e.g., 0,3,2 for the third set in the fourth set
   * of the first set). Returns {@code this} set if {@code indices} is empty.
   *
   * @param indices The given sequence of indices
   * @return The corresponding set
   * @throws IllegalArgumentException if {@code indices} is null or if its
   * length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an
   * out-of-bounds index
   */
  public Set getAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Set set = this;
    for (final int index : indices) {
      if (set instanceof ProductSet) {
        set = ((ProductSet) set).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return set;
  }

  public Set getFirst() {
    return this.getAt(0);
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
  public Set removeAt(final int index) {
    if (index < 0 || index > this.getArity() - 1) {
      throw new IndexOutOfBoundsException();
    }
    int arity = this.getArity();
    if (this.isPower()) {
      return ProductSet.getInstance(this.getFirst(), arity - 1);
    }
    final Set[] remainingSets = new Set[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingSets[i] = this.getAt(i);
      } else {
        remainingSets[i] = this.getAt(i + 1);
      }
    }
    return ProductSet.getInstance(remainingSets);
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  protected boolean standardIsAtomic() {
    return false;
  }

  @Override
  protected BigInteger standardGetMinOrder() {
    if (this.isPower()) {
      BigInteger minOrder = this.getFirst().getMinOrder();
      return minOrder.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 0; i < this.getArity(); i++) {
      final BigInteger minOrder = this.getAt(i).getMinOrder();
      result = result.multiply(minOrder);
    }
    return result;
  }

  @Override
  protected int standardHashCode() {
    final int prime = 31;
    int result = 1;
    for (Set set : this.sets) {
      result = prime * result + set.hashCode();
    }
    result = prime * result + this.getArity();
    return result;
  }

  @Override
  protected String standardToString() {
    if (this.isEmpty()) {
      return "";
    }
    if (this.isPower()) {
      return this.getFirst().toString() + "^" + this.getArity();
    }
    String result = null;
    for (Set set : this.sets) {
      if (result == null) {
        result = set.toString();
      } else {
        result = result + "," + set.toString();
      }
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
    if (this.isPower()) {
      BigInteger order = this.getFirst().getOrder();
      if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
        return order;
      }
      return order.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 0; i < this.getArity(); i++) {
      final BigInteger order = this.getAt(i).getOrder();
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
  protected Element abstractGetElement(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.getElement(values);
  }

  @Override
  protected Element abstractGetRandomElement(Random random) {
    final Element[] randomElements = new Element[this.getArity()];
    for (int i = 0; i < randomElements.length; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return abstractGetElement(randomElements);
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    return this.contains(values);
  }

  @Override
  protected boolean abstractEquals(Set set) {
    ProductSet productSet = (ProductSet) set;
    if (this.getArity() != productSet.getArity()) {
      return false;
    }
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).equals(productSet.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  //
  // LOCAL ELEMENT CLASS
  //

  final private class TupleElement extends Element {

    private static final long serialVersionUID = 1L;
    private Element[] elements;

    protected TupleElement(final Set set, final Element[] elements) {
      super(set);
      this.elements = elements;
    }

    @Override
    protected BigInteger standardGetValue() {
      BigInteger[] values = new BigInteger[this.getArity()];
      for (int i = 0; i < this.getArity(); i++) {
        values[i] = this.elements[i].getValue();
      }
      return MathUtil.elegantPair(values);
    }

    @Override
    protected Element standardGetAt(final int index) {
      return this.elements[index];
    }

    @Override
    protected boolean standardEquals(Element element) {
      for (int i=0; i<this.getArity(); i++) {
        if (!this.getAt(i).equals(element.getAt(i))) {
          return false;
        }
      }
      return true;
    }

    @Override
    protected int standardHashCode() {
      final int prime = 31;
      int result = 1;
      for (Element element : this.elements) {
        result = prime * result + element.hashCode();
      }
      result = prime * result + this.getArity();
      return result;
    }

    @Override
    protected String standardToString() {
      String result = null;
      for (Element element : this.elements) {
        if (result == null) {
          result = element.toString();
        } else {
          result = result + "," + element.toString();
        }
      }
      return result;
    }

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

//  /**
//   * This is a static factory method to construct a composed element without the
//   * need of constructing the corresponding product or power group beforehand.
//   * The input elements are given as an array.
//   *
//   * @param elements The array of input elements
//   * @return The corresponding tuple element
//   * @throws IllegalArgumentException if {@code elements} is null or contains
//   * null
//   */
//  public static Element getInstance(Element... elements) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    int arity = elements.length;
//    if (arity == 1) {
//      return elements[0];
//    }
//    final Set[] sets = new Set[arity];
//    int i = 0;
//    for (final Element element : elements) {
//      if (element == null) {
//        throw new IllegalArgumentException();
//      }
//      sets[i] = element.getSet();
//      i++;
//    }
//    ProductSet productSet = ProductSet.getInstance(sets);
//    return productSet.getElement(elements);
//  }
//
//  /**
//   * This is a static factory method to construct a composed element without the
//   * need of constructing the corresponding product or power group beforehand.
//   * The input elements are given as a list.
//   *
//   * @param elements The list of input elements
//   * @return The corresponding tuple element
//   * @throws IllegalArgumentException if {@code elements} is null or contains
//   * null
//   */
//  public static Element getInstance(List<Element> elements) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    return Element.getInstance(elements.toArray(new Element[0]));
//  }
}
