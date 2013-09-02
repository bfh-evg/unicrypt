package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 * This class represents the concept of a direct product of groups ("product
 * group" for short). The elements are tuples of respective elements of the
 * involved groups, i.e., the set of elements of the product group is the
 * Cartesian product of the respective sets of elements. The involved groups
 * themselves can be product groups, i.e., arbitrary hierarchies of product
 * groups are possible. The binary operation is defined component-wise. Applying
 * the operation to tuple elements yields another tuple element. The group's
 * unique identity element is the tuple of respective identity elements. The
 * inverse element is computed component-wise. The order of the product group is
 * the product of the individual group orders (it may be infinite or unknown).
 * The special case of a product group of arity 0 is isomorphic to the group
 * consisting of a single element (see {@code SingletonGroup}).
 *
 * @see <a
 * href="http://en.wikipedia.org/wiki/Direct_product_of_groups">http://en.wikipedia.org/wiki/Direct_product_of_groups</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ProductGroup extends AbstractCompoundGroup<ProductGroup, Tuple<ProductGroup, Group>, Group, Element<Group>> {

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
  protected Tuple<ProductGroup, Group> abstractGetElement(final Element[] elements) {
    return new Tuple<ProductGroup, Group>(this, elements) {
      @Override
      protected Tuple<ProductGroup, Group> abstractRemoveAt(Element[] elements) {
        return ProductGroup.getTuple(elements);
      }
    };
  }

  @Override
  protected ProductGroup abstractRemoveAt(Set set, int arity) {
    return ProductGroup.getInstance((Group) set, arity);
  }

  @Override
  protected ProductGroup abstractRemoveAt(Set[] sets) {
    return ProductGroup.getInstance((Group[]) sets);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is a static factory method to construct a composed group without
   * calling respective constructors. The input groups are given as an array.
   *
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
    Group first = groups[0];
    for (final Group group : groups) {
      if (group == null) {
        throw new IllegalArgumentException();
      }
      if (!group.equals(first)) {
        return new ProductGroup(groups);
      }
    }
    return new ProductGroup(first, groups.length);
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
  public static Tuple<ProductGroup, Group> getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Group[] groups = new Group[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      groups[i] = (Group) elements[i].getSet();
    }
    return ProductGroup.getInstance(groups).getElement(elements);
  }

}
