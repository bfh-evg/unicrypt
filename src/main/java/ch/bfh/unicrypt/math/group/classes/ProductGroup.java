package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This class represents the concept of a direct product of groups ("product group" for short).
 * The elements are tuples of respective elements of the involved groups, i.e., the set of elements of
 * the product group is the Cartesian product of the respective sets of elements. The involved groups
 * themselves can be product groups, i.e., arbitrary hierarchies of product groups are possible. The binary operation
 * is defined component-wise. Applying the operation to tuple elements yields another
 * tuple element. The group's unique identity element is the tuple of respective identity elements.
 * The inverse element is computed component-wise. The order of the product group is the product of
 * the individual group orders (it may be infinite or unknown). The special case of a product group of arity 0 is
 * isomorphic to the group consisting of a single element (see {@code SingletonGroup}).
 *
 * @see <a href="http://en.wikipedia.org/wiki/Direct_product_of_groups">http://en.wikipedia.org/wiki/Direct_product_of_groups</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ProductGroup extends ProductMonoid implements Group {

  private static final long serialVersionUID = 1L;
  private final Group[] groups;
  private int arity;

//  @Override
//  public Group getAt(final int index);
//
//  @Override
//  public Group getAt(int... indices);
//
//  @Override
//  public Group getFirst();
//
//  @Override
//  public Group removeAt(final int index);
//
  /**
   * This is the general constructor of this class. The resulting product group is the
   * product of the given groups.
   * @param groups The given groups
   * @throws IllegalArgumentException if {@code group} is null or contains null
   */
  private ProductGroup(final Group[] groups) {
    this.groups = groups.clone();
    this.arity = groups.length;
  }

  private ProductGroup(final Group group, final int arity) {
    this.groups = new Group[]{group};
    this.arity = arity;
  }

  private ProductGroup() {
    this.groups = new Group[]{};
    this.arity = 0;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[groups=" + Arrays.toString(this.groups) + ", arity=" + this.getArity() + "]";
  }

  //
  // The following protected methods implement the abstract methods from {@code AbstractGroup}
  //

  @Override
  protected Element abstractGetRandomElement(final Random random) {
    final Element[] randomElements = new Element[this.getArity()];
    for (int i = 0; i < randomElements.length; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return abstractGetElement(randomElements);
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getArity());
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getAt(i).contains(values[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected Element abstractIdentityElement() {
    final Element[] identityElements = new Element[this.getArity()];
    for (int i = 0; i < identityElements.length; i++) {
      identityElements[i] = this.getAt(i).getIdentityElement();
    }
    return abstractGetElement(identityElements);
  }

  @Override
  protected Element abstractApply(final Element element1, final Element element2) {
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element1.getAt(i).apply(element2.getAt(i));
    }
    return abstractGetElement(results);
  }

  @Override
  protected Element abstractSelfApply(final Element element, final BigInteger amount) {
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element.getAt(i).selfApply(amount);
    }
    return abstractGetElement(results);
  }

  @Override
  protected Element abstractInvert(final Element element) {
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element.getAt(i).invert();
    }
    return abstractGetElement(results);
  }

  protected Element abstractGetElement(final Element... elements) {
    return this.abstractGetElement(null, elements);
  }

  protected Element abstractGetElement(final BigInteger value, final Element... elements) {
    return new ProductGroupElement(this, elements);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    if (this.isEmpty()) {
      return BigInteger.ONE;
    }
    if (this.isPower()) {
      BigInteger order = this.getFirst().getOrder();
      if (order.equals(Group.INFINITE_ORDER) || order.equals(Group.UNKNOWN_ORDER)) {
        return order;
      }
      return order.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 0; i < this.getArity(); i++) {
      final BigInteger order = this.getAt(i).getOrder();
      if (order.equals(Group.INFINITE_ORDER)) {
        return Group.INFINITE_ORDER;
      }
      if (order.equals(Group.UNKNOWN_ORDER)) {
        result = UNKNOWN_ORDER;
      } else if (!result.equals(Group.UNKNOWN_ORDER)) {
        result = result.multiply(order);
      }
    }
    return result;
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractGroup}
  //

  @Override
  protected final boolean standardIsAtomicGroup() {
    return false;
  }

  @Override
  protected boolean standardIsPowerGroup() {
    return this.groups.length == 1;
  }

  @Override
  protected Group standardGetSuperGroup() {
    if (isPower()) {
      if (this.getFirst().isSubGroup()) {
        return ProductGroup.getInstance(this.getSuperGroup(),this.getArity());
      }
      return this;
    }
    Group[] superGroups = new Group[this.getArity()];
    boolean isSubGroup = false;
    for (int i=0; i<this.getArity(); i++) {
      superGroups[i] = this.groups[i].getSuperGroup();
      isSubGroup = isSubGroup || this.groups[i].isSubGroup();
    }
    if (isSubGroup) {
      return ProductGroup.getInstance(superGroups);
    }
    return this;
  }

  @Override
  protected BigInteger standardGetMinOrder() {
    if (isPower()) {
      BigInteger minOrder = this.getFirst().getMinOrder();
      if (minOrder.equals(Group.INFINITE_ORDER)) {
        return minOrder;
      }
      return minOrder.pow(this.getArity());
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 0; i < this.getArity(); i++) {
      final BigInteger minOrder = this.getAt(i).getMinOrder();
      if (minOrder.equals(Group.INFINITE_ORDER)) {
        return Group.INFINITE_ORDER;
      }
      result = result.multiply(minOrder);
    }
    return result;
  }

  @Override
  protected int standardGetArity() {
    return this.arity;
  }

  @Override
  protected Group standardGetGroupAt(final int index) {
    if (this.isPower()) {
      return this.groups[0];
    }
    return this.groups[index];
  }

  @Override
  protected Group standardRemoveGroupAt(final int index) {
    int arity = this.getArity();
    if (arity == 0) {
      throw new UnsupportedOperationException();
    }
    if (this.isPower()) {
      return ProductGroup.getInstance(this.getAt(0), arity-1);
    }
    final Group[] remainingGroups = new Group[arity-1];
    for (int i = 0; i < arity-1; i++) {
      if (i < index) {
        remainingGroups[i] = this.getAt(i);
      } else {
        remainingGroups[i] = this.getAt(i+1);
      }
    }
    return ProductGroup.getInstance(remainingGroups);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ProductGroup)) {
      return false;
    }
    final ProductGroup other = (ProductGroup) obj;
    if (this.getArity() != other.getArity()) {
      return false;
    }
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).equals(other.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.getArity();
    for (int i = 0; i < this.getArity(); i++) {
      result = prime * result + this.getAt(i).hashCode();
    }
    return result;
  }

  //
  // LOCAL ELEMENT CLASS
  //

  final private class ProductGroupElement extends Element {

    private static final long serialVersionUID = 1L;
    private Element[] elements;

    protected ProductGroupElement(final Group group, final Element[] elements) {
      super(group);
      this.elements = elements;
    }

    @Override
    protected BigInteger standardGetValue() {
      BigInteger[] values = new BigInteger[this.getArity()];
      for (int i=0; i<this.getArity(); i++) {
        values[i] = this.elements[i].getValue();
      }
      return MathUtil.elegantPair(values);
    }

    @Override
    protected Element standardGetAt(final int index) {
      return this.elements[index];
    }

  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a static factory method to construct a composed group without calling respective
   * constructors. The input groups are given as an array.
   * @param groups The array of input groups
   * @return The corresponding composed group
   * @throws IllegalArgumentException if {@code groups} is null or contains null
   */
  public static Group getInstance(final Group... groups) {
    if (groups == null) {
      throw new IllegalArgumentException();
    }
    if (groups.length == 0) {
      return new ProductGroup();
    }
    boolean isPowerGroup = true;
    Group firstGroup = groups[0];
    for (final Group group: groups) {
      if (group == null) {
        throw new IllegalArgumentException();
      }
      if (!group.equals(firstGroup)) {
        isPowerGroup = false;
      }
    }
    if (isPowerGroup) {
      return new ProductGroup(firstGroup, groups.length);
    }
    return new ProductGroup(groups);
  }

  public static Group getInstance(final Group group, int arity) {
    if ((group == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductGroup();
    }
    return new ProductGroup(group, arity);
  }

  /**
   * This is a static factory method to construct a composed group without calling respective
   * constructors. The input groups are given as a list.
   * @param groups The list of input groups
   * @return The corresponding composed group
   * @throws IllegalArgumentException if {@code groups} is null or contains null
   */
  public static Group getInstance(List<Group> groups){
    if (groups == null) {
      throw new IllegalArgumentException();
    }
    return ProductGroup.getInstance(groups.toArray(new Group[0]));
  }

}