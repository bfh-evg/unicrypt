/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends ProductGroup implements CyclicGroup {

  private Tuple defaultGenerator;

  protected ProductCyclicGroup(final CyclicGroup... cyclicGroups) {
    super(cyclicGroups);
  }

  protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  @Override
  public CyclicGroup getFirst() {
    return (CyclicGroup) super.getFirst();
  }

  @Override
  public CyclicGroup getAt(int index) {
    return (CyclicGroup) super.getAt(index);
  }

  @Override
  public CyclicGroup getAt(int... indices) {
    return (CyclicGroup) super.getAt(indices);
  }

  @Override
  public CyclicGroup[] getAll() {
    return (CyclicGroup[]) super.getAll();
  }

  @Override
  public ProductCyclicGroup removeAt(final int index) {
    return (ProductCyclicGroup) super.removeAt(index);
  }

  @Override
  protected ProductCyclicGroup standardRemoveAt(Set set, int arity) {
    return ProductCyclicGroup.getInstance((CyclicGroup) set, arity);
  }

  @Override
  protected ProductCyclicGroup standardRemoveAt(Set[] sets) {
    return ProductCyclicGroup.getInstance((CyclicGroup[]) sets);
  }

  @Override
  public final Tuple getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      Element[] generators = new Element[this.getArity()];
      for (int i = 0; i < this.getArity(); i++) {
        generators[i] = this.getAt(i).getDefaultGenerator();
      }
      this.defaultGenerator = this.standardGetElement(generators);
    }
    return this.defaultGenerator;
  }

  @Override
  public final Tuple getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final Tuple getRandomGenerator(Random random) {
    int arity = this.getArity();
    Element[] randomElements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return this.standardGetElement(randomElements);
  }

  @Override
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    Tuple tuple = (Tuple) element;
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
        return false;
      }
    }
    return true;
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

  public static Tuple getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final CyclicGroup[] cyclicGroup = new CyclicGroup[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      cyclicGroup[i] = (CyclicGroup) elements[i].getSet();
    }
    return ProductCyclicGroup.getInstance(cyclicGroup).getElement(elements);
  }

}
