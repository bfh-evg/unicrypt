package ch.bfh.unicrypt.math.product.classes;

import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.product.abstracts.AbstractProductCyclicGroup;
import ch.bfh.unicrypt.math.product.abstracts.AbstractTuple;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends AbstractProductCyclicGroup<CyclicGroup, Tuple, Element> {

  protected ProductCyclicGroup(final CyclicGroup[] cyclicGroups) {
    super(cyclicGroups);
  }

  protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  protected ProductCyclicGroup() {
    super();
  }

  public ProductCyclicGroup removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    final CyclicGroup[] remainingGroups = new CyclicGroup[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingGroups[i] = this.getAt(i);
      } else {
        remainingGroups[i] = this.getAt(i + 1);
      }
    }
    return ProductCyclicGroup.getInstance(remainingGroups);
  }

  @Override
  protected Tuple abstractGetElement(final Element[] elements) {
    return new AbstractTuple<ProductCyclicGroup, Tuple, Element>(this, elements) {
    };
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
  public static Tuple getTuple(Element... elements) {
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
