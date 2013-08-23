package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.CompoundElement;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroup extends ProductSet implements SemiGroup {

  protected ProductSemiGroup(final SemiGroup[] semiGroups) {
    super(semiGroups);
  }

  protected ProductSemiGroup(final SemiGroup semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  protected ProductSemiGroup() {
    super();
  }

  @Override
  public SemiGroup getFirst() {
    return (SemiGroup) super.getFirst();
  }

  @Override
  public SemiGroup getAt(final int index) {
    return (SemiGroup) super.getAt(index);
  }

  @Override
  public SemiGroup getAt(int... indices) {
    return (SemiGroup) super.getAt(indices);
  }

  @Override
  public SemiGroup[] getAll() {
    return (SemiGroup[]) super.getAll();
  }

  @Override
  public ProductSemiGroup removeAt(final int index) {
    return (ProductSemiGroup) super.removeAt(index);
  }

  @Override
  public final CompoundElement apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CompoundElement compoundElement1 = (CompoundElement) element1;
    CompoundElement compoundElement2 = (CompoundElement) element2;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = compoundElement1.getAt(i).apply(compoundElement1.getAt(i));
    }
    return this.standardGetElement(results);
  }

  @Override
  public final CompoundElement apply(Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    CompoundElement[] compoundElements = (CompoundElement[]) elements;
    CompoundElement result = null;
    for (CompoundElement element : compoundElements) {
      if (result == null) {
        result = element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  @Override
  public final CompoundElement selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CompoundElement compoundElement = (CompoundElement) element;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = compoundElement.getAt(i).selfApply(amount);
    }
    return standardGetElement(results);
  }

  @Override
  public final CompoundElement selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final CompoundElement selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final CompoundElement selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final CompoundElement multiSelfApply(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    CompoundElement[] results = new CompoundElement[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

  /**
   * This is a static factory method to construct a composed semigroup without
   * calling respective constructors. The input semigroups are given as an array.
   * @param semiGroups The array of input semigroups
   * @return The corresponding product semigroup
   * @throws IllegalArgumentException if {@code semigroups} is null or contains null
   */
  public static ProductSemiGroup getInstance(final SemiGroup... semiGroups) {
    if (semiGroups == null) {
      throw new IllegalArgumentException();
    }
    if (semiGroups.length == 0) {
      return new ProductSemiGroup();
    }
    if (ProductSet.areEqual(semiGroups)) {
      return new ProductSemiGroup(semiGroups[0], semiGroups.length);
    }
    return new ProductSemiGroup(semiGroups);
  }

  public static ProductSemiGroup getInstance(final SemiGroup semiGroup, int arity) {
    if ((semiGroup == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSemiGroup();
    }
    return new ProductSemiGroup(semiGroup, arity);
  }

}
