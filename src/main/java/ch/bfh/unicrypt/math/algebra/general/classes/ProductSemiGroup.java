/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ProductSemiGroup extends ProductSet implements SemiGroup {

  protected ProductSemiGroup(SemiGroup[] semiGroups) {
    super(semiGroups);
  }

  protected ProductSemiGroup(SemiGroup semiGroup, int arity) {
    super(semiGroup, arity);
  }

  @Override
  public SemiGroup getFirst() {
    return (SemiGroup) super.getFirst();
  }

  @Override
  public SemiGroup getAt(int index) {
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
  protected ProductSemiGroup abstractRemoveAt(Set set, int arity) {
    return ProductSemiGroup.getInstance((SemiGroup) set, arity);
  }

  @Override
  protected ProductSemiGroup abstractRemoveAt(Set[] sets) {
    return ProductSemiGroup.getInstance((SemiGroup[]) sets);
  }

  @Override
  public final Tuple apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    Tuple tuple1 = (Tuple) element1;
    Tuple tuple2 = (Tuple) element2;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = tuple1.getAt(i).apply(tuple2.getAt(i));
    }
    return this.standardGetElement(results);
  }

  @Override
  public final Tuple apply(final Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    return this.standardApply(elements);
  }

  @Override
  public final Tuple selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    Tuple tuple = (Tuple) element;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = tuple.getAt(i).selfApply(amount);
    }
    return this.standardGetElement(results);
  }

  @Override
  public final Tuple selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final Tuple selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final Tuple selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final Tuple multiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length)) {
      throw new IllegalArgumentException();
    }
    return this.standardMultiSelfApply(elements, amounts);
  }

  protected Tuple standardApply(final Element... elements) {
    if (elements.length == 0) {
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
    return (Tuple) result;
  }

  protected Tuple standardMultiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if (elements.length == 0) {
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
   * calling respective constructors. The input semigroups are given as an
   * array.
   *
   * @param semiGroups The array of input semigroups
   * @return The corresponding product semigroup
   * @throws IllegalArgumentException if {@code semigroups} is null or contains
   * null
   */
  public static ProductSemiGroup getInstance(final SemiGroup... semiGroups) {
    if (semiGroups == null) {
      throw new IllegalArgumentException();
    }
    if (semiGroups.length > 0) {
      boolean uniform = true;
      SemiGroup first = semiGroups[0];
      for (final SemiGroup semiGroup : semiGroups) {
        if (semiGroup == null) {
          throw new IllegalArgumentException();
        }
        if (!semiGroup.equals(first)) {
          uniform = false;
        }
      }
      if (uniform) {
        return ProductSemiGroup.getInstance(first, semiGroups.length);
      }
    }
    return new ProductSemiGroup(semiGroups);
  }

  public static ProductSemiGroup getInstance(final SemiGroup semiGroup, int arity) {
    if ((semiGroup == null) || (arity < 0)) {
      throw new IllegalArgumentException();
    }
    if (arity == 0) {
      return new ProductSemiGroup(new SemiGroup[]{});
    }
    return new ProductSemiGroup(semiGroup, arity);
  }

  public static Tuple getTuple(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final SemiGroup[] semiGroup = new SemiGroup[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      semiGroup[i] = (SemiGroup) elements[i].getSet();
    }
    return ProductSemiGroup.getInstance(semiGroup).getElement(elements);
  }

}
