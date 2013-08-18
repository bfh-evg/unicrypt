package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet implements Set {

  /**
   * Returns the group's arity. The default arity of a non-composed non-singleton group is 1. The
   * arity of a singleton group is 0. In case of a product group,
   * the arity is the number of groups it is composed of.
   * @return The group's arity
   */
  public int getArity() {
    return 0;
  }

  public final Element getElement() {
    return this.getElement(new BigInteger[]{});
  }

  @Override
  public final Element getElement(BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomic()) {
      if (!this.contains(value)) {
        throw new IllegalArgumentException();
      }
      return this.abstractGetElement(value);
    }
    return this.getElement(MathUtil.elegantUnpair(value, this.getArity()));
  }

  @Override
  public final Element getElement(final int... values) {
    return this.getElement(MathUtil.intToBigIntegerArray(values));
  }

  /**
   * Creates and returns the group element that corresponds to a given BigInteger values (if one exists).
   * @param values The given BigInteger values
   * @return The corresponding group element
   * @throws IllegalArgumentException if {@code values} is or contains null or if no such element exists in {@code this} group
   */
  public final Element getElement(BigInteger... values) {
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomic()) {
      return this.getElement(values[0]);
    }
    Element[] elements = new Element[this.getArity()];
    for (int i=0; i<this.getArity(); i++) {
      elements[i] = this.getAt(i).getElement(values[i]);
    }
    return this.abstractGetElement(null, elements);
  }

    /**
   * Creates a new tuple element for that group from a given array of group elements. For this to work,
   * each of the given elements must be exactly one of the corresponding involved group.
   * @param elements The given array of elements
   * @return The resulting tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains null, or if its size does not correspond to the group's arity
   * @throws IllegalArgumentException if an element is not in the corresponding group
   */
  public final Element getElement(final Element... elements) {
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomic()) {
      return this.getElement(elements[0]);
    }
    Element[] newElements = new Element[this.getArity()];
    for (int i=0; i<this.getArity(); i++) {
      newElements[i] = this.getAt(i).getElement(elements[i]);
    }
    return abstractGetElement(null, newElements);
  }


    /**
   * Checks if the arity of the set is 0.
   * @return {@code true} if the arity is 0, {@code false} otherwise
   */
  public boolean isNull() {
    return this.getArity() == 0;
  }



  /**
   * Checks if the set consists of multiple copies of the same set. Such a product set is called 'power set'.
   * @return {@code true}, if the product group is a power group, {@code false} otherwise
   */
  public final boolean isPower() {
    return true;
  }

  /**
   * Returns the set at index 0.
   * @return The set at index 0
   * @throws UnsupportedOperationException for sets of arity 0
   */
  public final Set getFirst() {
    return this;
  }

  /**
   * Returns the group for the given index. The indices are numbered from 0 to the group's arity minus one.
   * @param index The given index
   * @return The corresponding group
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
   */
  public final Set getAt(final int index) {
    if (index != 0) {
      throw new IndexOutOfBoundsException();
    }
    return this;
  }

  /**
   * Selects and returns in a hierarchy of groups the group that corresponds to a given sequence of indices
   * (e.g., 0,3,2 for the third group in the fourth group of the first group). Returns {@code this} group if {@code indices}
   * is empty.
   * @param indices The given sequence of indices
   * @return The corresponding group
   * @throws IllegalArgumentException if {@code indices} is null or if its length exceeds the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an out-of-bounds index
   */
  public final Set getAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Set set = this;
    for (final int index : indices) {
      set = set.getAt(index);
    }
    return set;
  }

  /**
   * Creates a new product group which contains one group less than the given product group. If the given product group has
   * arity 1, then a singleton group is returned.
   * @param index The index of the group to remove
   * @return The resulting product group.
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
   */
  public final Set removeAt(int index) {
    if (this.getArity() == 0) {
      throw new UnsupportedOperationException();
    }
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardRemoveGroupAt(index);
  }

  @Override
  public final boolean contains(BigInteger... values) {
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getAt(i).contains(values[i])) {
        return false;
      }
    }
    return true;
  }

    /**
   * Checks if {@code this} group contains an element that corresponds to the composition of some given elements.
   * @param elements The given elements
   * @return {@code true} if such an element exists, {@code false} otherwise
   * @throws IllegalArgumentException if {@code elements} is null
   */
  public final boolean contains(Element... elements) {
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getAt(i).contains(elements[i])) {
        return false;
      }
    }
    return true;
  }

  //
  // STATIC FACTORY METHODS
  //

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
  public static Element getInstance(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    if (arity == 1) {
      return elements[0];
    }
    final Set[] sets = new Set[arity];
    int i = 0;
    for (final Element element : elements) {
      if (element == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = element.getSet();
      i++;
    }
    ProductSet productSet = ProductSet.getInstance(sets);
    return productSet.getElement(elements);
  }

  /**
   * This is a static factory method to construct a composed element without the
   * need of constructing the corresponding product or power group beforehand.
   * The input elements are given as a list.
   *
   * @param elements The list of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains
   * null
   */
  public static Element getInstance(List<Element> elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    return Element.getInstance(elements.toArray(new Element[0]));
  }

}
