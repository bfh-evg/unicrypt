package ch.bfh.unicrypt.math.algebra.product.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundCyclicGroup;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends AbstractCompoundCyclicGroup<ProductCyclicGroup, ProductCyclicGroupElement, CyclicGroup, Element> {

  protected ProductCyclicGroup(final CyclicGroup... cyclicGroups) {
    super(cyclicGroups);
  }

  protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  @Override
  protected ProductCyclicGroupElement abstractGetElement(final Element[] elements) {
    return new ProductCyclicGroupElement(this, elements) {
    };
  }

  @Override
  protected ProductCyclicGroup abstractRemoveAt(CyclicGroup cyclicGroup, int arity) {
    return ProductCyclicGroup.getInstance(cyclicGroup, arity);
  }

  @Override
  protected ProductCyclicGroup abstractRemoveAt(CyclicGroup[] cyclicGroups) {
    return ProductCyclicGroup.getInstance(cyclicGroups);
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

}
