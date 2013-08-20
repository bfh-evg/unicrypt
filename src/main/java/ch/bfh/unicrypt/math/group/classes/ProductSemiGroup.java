package ch.bfh.unicrypt.math.group.classes;

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
  public SemiGroup getAt(final int index) {
    return (ProductMonoid) this.getAt(index);
  }

  @Override
  public SemiGroup getAt(int... indices) {
    return (ProductMonoid) this.getAt(indices);
  }

  @Override
  public SemiGroup getFirst() {
    return (ProductMonoid) this.getFirst();
  }

  @Override
  public SemiGroup removeAt(final int index) {
    return (ProductMonoid) this.removeAt(index);
  }

  @Override
  public final Element apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element1.getAt(i).apply(element2.getAt(i));
    }
    return this.abstractGetElement(results);
  }

  @Override
  public final Element apply(Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    Element result = null;
    for (Element element : elements) {
      if (result == null) {
        result = element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  @Override
  public final Element selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    final Element[] results = new Element[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      results[i] = element.getAt(i).selfApply(amount);
    }
    return abstractGetElement(results);
  }

  @Override
  public final Element selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final Element selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final Element selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final Element multiSelfApply(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    Element[] results = new Element[elements.length];
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
