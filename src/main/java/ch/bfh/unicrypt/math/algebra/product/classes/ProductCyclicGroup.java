package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundCyclicGroup;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends AbstractCompoundCyclicGroup<ProductCyclicGroup, Tuple<ProductCyclicGroup, CyclicGroup>, CyclicGroup, Element<CyclicGroup>> {

  protected ProductCyclicGroup(final CyclicGroup... cyclicGroups) {
    super(cyclicGroups);
  }

  protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  @Override
  protected Tuple<ProductCyclicGroup, CyclicGroup> abstractGetElement(final Element[] elements) {
    return new Tuple<ProductCyclicGroup, CyclicGroup>(this, elements) {
      @Override
      protected Tuple<ProductCyclicGroup, CyclicGroup> abstractRemoveAt(Element[] elements) {
        return ProductCyclicGroup.getTuple(elements);
      }
    };
  }

  @Override
  protected ProductCyclicGroup abstractRemoveAt(Set set, int arity) {
    return ProductCyclicGroup.getInstance((CyclicGroup) set, arity);
  }

  @Override
  protected ProductCyclicGroup abstractRemoveAt(Set[] sets) {
    return ProductCyclicGroup.getInstance((CyclicGroup[]) sets);
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is a static factory method to construct a composed cyclic group
   * without calling respective constructors. The input groups are given as an
   * array.
   *
   * @param cyclicGroups The array of cyclic groups
   * @return The corresponding composed group
   * @throws IllegalArgumentException if {@code groups} is null or contains null
   */
  public static ProductCyclicGroup getInstance(final CyclicGroup... cyclicGroups) {
    if (cyclicGroups == null) {
      throw new IllegalArgumentException();
    }
    if (cyclicGroups.length == 0) {
      return new ProductCyclicGroup();
    }
    if (ProductCyclicGroup.areRelativelyPrime(cyclicGroups)) {
      return new ProductCyclicGroup(cyclicGroups);
    }
    throw new IllegalArgumentException();
  }

  public static ProductCyclicGroup getInstance(final CyclicGroup group, int arity) {
    if ((group == null) || (arity < 0) || (arity > 1)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductCyclicGroup();
    }
    return new ProductCyclicGroup(group);
  }

  //
  // STATIC HELPER METHODS
  //
  private static boolean areRelativelyPrime(CyclicGroup[] cyclicGroups) {
    BigInteger[] orders = new BigInteger[cyclicGroups.length];
    for (int i = 0; i < cyclicGroups.length; i++) {
      if (cyclicGroups[i] == null) {
        throw new IllegalArgumentException();
      }
      orders[i] = cyclicGroups[i].getOrder();
    }
    return MathUtil.areRelativelyPrime(orders);
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
  public static Tuple<ProductCyclicGroup, CyclicGroup> getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final CyclicGroup[] cyclicGroups = new CyclicGroup[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      cyclicGroups[i] = (CyclicGroup) elements[i].getSet();
    }
    return ProductCyclicGroup.getInstance(cyclicGroups).getElement(elements);
  }

}
