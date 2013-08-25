package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.classes.CompoundElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;

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
 * @version 2.0
 */
public class ProductGroup extends ProductMonoid implements Group {

  protected ProductGroup(final Group[] groups) {
    super(groups);
  }

  protected ProductGroup(final Group group, final int arity) {
    super(group, arity);
  }

  protected ProductGroup() {
    super();
  }

  @Override
  public Group getFirst() {
    return (Group) super.getFirst();
  }

  @Override
  public Group getAt(final int index) {
    return (Group) super.getAt(index);
  }

  @Override
  public Group getAt(int... indices) {
    return (Group) super.getAt(indices);
  }

  @Override
  public Group[] getAll() {
    return (Group[]) super.getAll();
  }

  @Override
  public ProductGroup removeAt(final int index) {
    return (ProductGroup) super.removeAt(index);
  }

  @Override
  public final CompoundElement invert(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CompoundElement compoundElement = (CompoundElement) element;
    final Element[] invertedElements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      invertedElements[i] = compoundElement.getAt(i).invert();
    }
    return standardGetElement(invertedElements);
  }

  @Override
  public final CompoundElement applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
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
  public static ProductGroup getInstance(final Group... groups) {
    if (groups == null) {
      throw new IllegalArgumentException();
    }
    if (groups.length == 0) {
      return new ProductGroup();
    }
    if (ProductSet.areEqual(groups)) {
      return new ProductGroup(groups[0], groups.length);
    }
    return new ProductGroup(groups);
  }

  public static ProductGroup getInstance(final Group group, int arity) {
    if ((group == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductGroup();
    }
    return new ProductGroup(group, arity);
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
    final Group[] groups = new Group[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      groups[i] = elements[i].getGroup();
    }
    return ProductGroup.getInstance(groups).getElement(elements);
  }

}