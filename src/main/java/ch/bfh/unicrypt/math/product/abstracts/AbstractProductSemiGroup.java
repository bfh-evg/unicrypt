/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.product.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductSemiGroup<P extends AbstractProductSemiGroup, S extends SemiGroup, T extends Tuple, E extends Element> extends AbstractProductSet<P, S, T, E> implements SemiGroup {

  protected AbstractProductSemiGroup(final SemiGroup... semiGroups) {
    super(semiGroups);
  }

  protected AbstractProductSemiGroup(final SemiGroup semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  @Override
  public final T apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    T tuple1 = (T) element1;
    T tuple2 = (T) element2;
    final E[] results = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = (E) tuple1.getAt(i).apply(tuple2.getAt(i));
    }
    return this.abstractGetElement(results);
  }

  @Override
  public final T apply(Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    T[] tuples = (T[]) elements;
    T result = null;
    for (T tuple : tuples) {
      if (result == null) {
        result = tuple;
      } else {
        result = this.apply(result, tuple);
      }
    }
    return result;
  }

  @Override
  public final T selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    T tuple = (T) element;
    final E[] results = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = (E) tuple.getAt(i).selfApply(amount);
    }
    return abstractGetElement(results);
  }

  @Override
  public final T selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final T selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final T selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final T multiSelfApply(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    T[] results = (T[]) new Tuple[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

}
