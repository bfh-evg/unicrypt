package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.CompoundElement;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup extends ProductGroup implements CyclicGroup {

  private CompoundElement defaultGenerator;

  protected ProductCyclicGroup(final CyclicGroup[] cyclicGroups) {
    super(cyclicGroups);
  }

  protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  protected ProductCyclicGroup() {
    super();
  }

  @Override
  public CyclicGroup getFirst() {
    return (CyclicGroup) super.getFirst();
  }

  @Override
  public CyclicGroup getAt(final int index) {
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
  public final CompoundElement getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      Element[] generators = new Element[this.getArity()];
      for (int i=0; i<this.getArity(); i++) {
        generators[i] = this.getAt(i).getDefaultGenerator();
      }
      this.defaultGenerator = this.standardGetElement(generators);
    }
    return this.defaultGenerator;
  }

  @Override
  public final CompoundElement getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final CompoundElement getRandomGenerator(Random random) {
    int arity = this.getArity();
    Element[] randomElements = new Element[arity];
    for (int i=0; i<arity; i++) {
      randomElements[i] = this.getAt(i).getRandomElement(random);
    }
    return this.standardGetElement(randomElements);
  }

  @Override
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    CompoundElement compoundElement = (CompoundElement) element;
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getAt(i).isGenerator(compoundElement.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a static factory method to construct a composed cyclic group without
   * calling respective constructors. The input groups are given as an array.
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
    for (int i=0; i<cyclicGroups.length; i++) {
      if (cyclicGroups[i] == null) {
        throw new IllegalArgumentException();
      }
      orders[i] = cyclicGroups[i].getOrder();
    }
    return MathUtil.areRelativelyPrime(orders);
  }

}
