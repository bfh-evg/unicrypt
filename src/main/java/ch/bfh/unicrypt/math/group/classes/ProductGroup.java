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


  @Override
  protected Element abstractIdentityElement() {
    final Element[] identityElements = new Element[this.getArity()];
    for (int i = 0; i < identityElements.length; i++) {
      identityElements[i] = this.getAt(i).getIdentityElement();
    }
    return abstractGetElement(identityElements);
  }


  @Override
  protected Element abstractInvert(final Element element) {
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element.getAt(i).invert();
    }
    return abstractGetElement(results);
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